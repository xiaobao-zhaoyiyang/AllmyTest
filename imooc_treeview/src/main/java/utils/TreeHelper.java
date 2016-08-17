package utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import annotation.TreeNodeId;
import annotation.TreeNodeLabel;
import annotation.TreeNodePid;
import yeeaoo.imooc_treeview.R;

/**
 * Created by yo on 2016/4/26.
 */
public class TreeHelper {
    /**
     * 将用户的数据转化为树形数据
     * @param datas
     * @param <T>
     * @return
     */
    public static <T> List<Node> convertDatas2Nodes(List<T> datas) throws IllegalAccessException {
        List<Node> nodes = new ArrayList<>();
        Node node =  null;
        for (T t:datas) {
            int id = -1;
            int pid = -1;
            String label = null;
            node = new Node();
            Class clazz = t.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field:fields) {
                if(field.getAnnotation(TreeNodeId.class) != null){
                    field.setAccessible(true);
                    id = field.getInt(t);
                }
                if(field.getAnnotation(TreeNodePid.class) != null){
                    field.setAccessible(true);
                    pid = field.getInt(t);
                }
                if(field.getAnnotation(TreeNodeLabel.class) != null){
                    field.setAccessible(true);
                    label = (String) field.get(t);
                }
            }
            node = new Node(id, pid, label);
            nodes.add(node);
        }
        /**
         * 设置Node间的节点关系
         */
        for (int i = 0; i < nodes.size(); i ++){
            Node n = nodes.get(i);
            for (int j = i + 1; j < nodes.size(); j ++){
                Node m = nodes.get(j);
                if (m.getpId() == n.getId()){
                    n.getChildren().add(m);
                    m.setParent(n);
                }else if (m.getId() == n.getpId()){
                    m.getChildren().add(n);
                    n.setParent(m);
                }
            }
        }

        for (Node n : nodes){
            setNodeIcon(n);
        }
        return nodes;
    }

    public static <T> List<Node> getSortedNodes(List<T> datas, int defaultExpandLevel) throws IllegalAccessException {
        List<Node> result = new ArrayList<>();
        List<Node> nodes = convertDatas2Nodes(datas);
        // 获得树的根节点
        List<Node> rootNodes = getRootNodes(nodes);
        for (Node node : rootNodes){
            addNode(result, node, defaultExpandLevel, 1);
        }
        return result;
    }

    /**
     * 把一个节点的所有孩子节点都放入result
     * @param result
     * @param node
     * @param defaultExpandLevel
     * @param currentLevel
     */
    private static void addNode(List<Node> result, Node node, int defaultExpandLevel, int currentLevel) {
        result.add(node);
        if (defaultExpandLevel >= currentLevel){
            node.setExpand(true);
        }
        if (node.isLeaf())return;
        for (int i = 0; i < node.getChildren().size(); i ++){
            addNode(result, node.getChildren().get(i), defaultExpandLevel, currentLevel + 1);
        }
    }

    /**
     * 过滤出需要显示的
     * @param nodes
     * @return
     */
    public static List<Node> filterVisibleNodes(List<Node> nodes){
        List<Node> result = new ArrayList<>();
        for (Node node : nodes){
            if (node.isRoot() || node.isParentExpand()){
                setNodeIcon(node);
                result.add(node);
            }
        }
        return result;
    }

    /**
     * 从所有节点中过滤分节点
     * @param nodes
     * @return
     */
    private static List<Node> getRootNodes(List<Node> nodes) {
        List<Node> root = new ArrayList<>();
        for (Node node : nodes){
            if (node.isRoot()){
                root.add(node);
            }
        }
        return root;
    }

    /**
     * 为Node设置图标
     * @param n
     */
    private static void setNodeIcon(Node n) {
        if (n.getChildren().size() > 0 && n.isExpand()){
            n.setIcon(R.mipmap.tree_ex);
        }else if (n.getChildren().size() > 0 && !n.isExpand()){
            n.setIcon(R.mipmap.tree_ec);
        }else{
            n.setIcon(-1);
        }
    }
}
