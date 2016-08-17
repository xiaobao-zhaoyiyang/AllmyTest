package yeeaoo.imooc_treeview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import utils.Node;
import utils.TreeHelper;
import utils.TreeListViewAdapter;

/**
 * Created by yo on 2016/4/26.
 */
public class SimpleTreeListViewAdapter<T> extends TreeListViewAdapter<T> {
    public SimpleTreeListViewAdapter(ListView tree, Context context, List<T> datas, int defaultExpandleLevel) throws IllegalAccessException {
        super(tree, context, datas, defaultExpandleLevel);
    }

    @Override
    public View getConvertView(Node node, int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.mIcon = (ImageView) convertView.findViewById(R.id.id_item_icon);
            holder.mTextView = (TextView) convertView.findViewById(R.id.id_item_text);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        if (node.getIcon() == -1){
            holder.mIcon.setVisibility(View.INVISIBLE);
        }else {
            holder
                    .mIcon
                    .setVisibility(View.VISIBLE);
            holder.mIcon.setImageResource(node.getIcon());
        }
        holder.mTextView.setText(node.getName());
        return convertView;
    }

    public void addExtraNode(int position, String string) {
        Node node = mVisibleNodes.get(position);
        int indexOf = mAllNodes.indexOf(node);

        Node extraNode = new Node(-1, node.getId(), string);
        extraNode.setParent(node);
        node.getChildren().add(extraNode);
        mAllNodes.add(indexOf + 1, extraNode);
        mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes);
        notifyDataSetChanged();
    }

    private class ViewHolder{
        ImageView mIcon;
        TextView mTextView;
    }
}
