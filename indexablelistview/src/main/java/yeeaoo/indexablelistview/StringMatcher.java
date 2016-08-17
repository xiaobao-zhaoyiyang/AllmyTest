package yeeaoo.indexablelistview;

/**
 * Created by yo on 2016/4/6.
 */
public class StringMatcher {
    // value :item的文本
    // keyword：索引列表的字符
    public static boolean match(String value, String keyword){
        // value和keyword参数值都不能为空
        if (value == null || keyword == null){
            return false;
        }
        if (keyword.length() > value.length()){
            return false;
        }
        /**
         * i是value的指针，j是keyword的指针
         */
        int i = 0, j = 0;
        do {
            if (keyword.charAt(j) == value.charAt(i)){
                i++;
                j++;
            }else if (j > 0){
                break;
            }else {
                i++;
            }
        }while (i < value.length() && j < keyword.length());
        return j == keyword.length() ? true : false;
    }
}
