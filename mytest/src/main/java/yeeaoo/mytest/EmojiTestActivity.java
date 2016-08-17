package yeeaoo.mytest;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import tools.EmojiData;

/**
 * Created by yo on 2016/6/20.
 */
public class EmojiTestActivity extends AppCompatActivity{
    private EditText et;
    private GridView gridView;
    private ArrayAdapter<String> adapter;
    private Typeface typeface;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        typeface = Typeface.createFromAsset(getAssets(), "styleType.TTF");
        LayoutInflaterCompat.setFactory(LayoutInflater.from(this), new LayoutInflaterFactory() {
            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                AppCompatDelegate delegate = getDelegate();
                View view = delegate.createView(parent, name, context, attrs);
                if (view != null && (view instanceof TextView)){
                    ((TextView)view).setTypeface(typeface);
                }
                return view;
            }
        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emoji);

        Person person = getIntent().getParcelableExtra("person");

        gridView = (GridView) findViewById(R.id.emoji_gridView);
        et = (EditText) findViewById(R.id.emoji_edittext);

        et.append(person.getName() + "," + person.getAge());

        final ArrayList list = EmojiData.initEmojiString();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                et.append(list.get(position).toString());
                et.setSelection(et.getText().toString().length());
            }
        });

    }
}
