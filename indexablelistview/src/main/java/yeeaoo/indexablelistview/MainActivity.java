package yeeaoo.indexablelistview;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> mItems;
    private IndexableListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         * 1.初始化items
         * 2.根据section获取position
         */
        mItems = new ArrayList<>();
        mItems.add("123456");
        mItems.add("A:123456");
        mItems.add("B:123456");
        mItems.add("C:123456");
        mItems.add("D:123456");
        mItems.add("E:123456");
        mItems.add("F:123456");
        mItems.add("G:123456");
        mItems.add("H:123456");
        mItems.add("I:123456");
        mItems.add("G:123456");
        mItems.add("K:123456");
        mItems.add("L:123456");
        mItems.add("M:123456");
        mItems.add("N:123456");
        mItems.add("O:123456");
        mItems.add("P:123456");
        mItems.add("Q:123456");
        mItems.add("R:123456");
        mItems.add("S:123456");
        mItems.add("T:123456");
        // 对集合进行排序
        Collections.sort(mItems);

        ContentAdapter adapter = new ContentAdapter(this, android.R.layout.simple_list_item_1, mItems);
        mListView = (IndexableListView) findViewById(R.id.listview);
        mListView.setAdapter(adapter);
        mListView.setFastScrollEnabled(true);
    }

    private class ContentAdapter extends ArrayAdapter<String> implements SectionIndexer{

        private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        public ContentAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
        }

        @Override
        public Object[] getSections() {
            String [] sections = new String[mSections.length()];
            for (int i = 0; i < mSections.length() ; i++) {
                sections[i] = String.valueOf(mSections.charAt(i));
            }
            return sections;
        }

        @Override
        public int getPositionForSection(int sectionIndex) {
            // 从当前的Section往前查，直到遇到第一个有对应item为止，否则不进行定位
            for (int i = sectionIndex; i >= 0 ; i--) {
                for (int j = 0; j < getCount(); j++) {
                    if (i == 0){// 查询数字
                        for (int k = 0; k <= 9 ; k++) {
                            if (StringMatcher.match(String.valueOf(getItem(j).charAt(0)), String.valueOf(k))){
                                return j;
                            }
                        }
                    }else{ // 查询字母
                        if (StringMatcher.match(String.valueOf(getItem(j).charAt(0)), String.valueOf(mSections.charAt(i)))){
                            return j;
                        }
                    }
                }
            }
            return 0;
        }

        @Override
        public int getSectionForPosition(int position) {
            return 0;
        }
    }
}
