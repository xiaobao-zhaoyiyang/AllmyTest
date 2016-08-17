package yeeaoo.mytest;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by yo on 2016/6/30.
 */
public class SlidingTestActivity extends AppCompatActivity {
    private EditText et;
    private boolean isShow;
    private Drawable drawable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slidingmenutest);
        et = (EditText) findViewById(R.id.id_editTest);
        try {
            if (isShow) {
                et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                drawable = Drawable.createFromStream(getAssets().open("compass.png"), "");
            } else {
                et.setTransformationMethod(PasswordTransformationMethod.getInstance());
                drawable = Drawable.createFromStream(getAssets().open("ic_launcher.png"), "");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        isShow = !isShow;
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        //对文本框设置一个图片资源。4个参数分别代表：左/上/右/下
        // (@Nullable Drawable left, @Nullable Drawable top, @Nullable Drawable right,
        // @Nullable Drawable bottom)
        et.setCompoundDrawables(null, null, drawable, null);
        et.setSelection(et.getText().length());//设置选中

        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.i("TAG", actionId + "");
                return false;
            }
        });
    }
}
