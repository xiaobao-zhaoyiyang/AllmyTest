package yeeaoo.gellerydemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ViewSwitcher;

import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        ViewSwitcher.ViewFactory, CompoundButton.OnCheckedChangeListener {
    private int[] res = {R.mipmap.item1, R.mipmap.item2, R.mipmap.item3, R.mipmap.item4,
            R.mipmap.item5, R.mipmap.item6, R.mipmap.item7,
            R.mipmap.item8, R.mipmap.item9, R.mipmap.item10,
            R.mipmap.item11, R.mipmap.item12};
    private Gallery gallery; // 官方建议横向的滚动或是viewpager代替
    private ImageAdapter adapter;
    private ImageSwitcher switcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gallery = (Gallery) findViewById(R.id.gallery);
        switcher = (ImageSwitcher) findViewById(R.id.switcher);
        adapter = new ImageAdapter(this, res);
        gallery.setAdapter(adapter);
        gallery.setOnItemSelectedListener(this);
        switcher.setFactory(this);
        switcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        switcher.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
        LayoutInflater inflater = this.getLayoutInflater();
        ToggleButton tb = (ToggleButton) findViewById(R.id.toggleButton);
//        tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked){
//                    Toast.makeText(MainActivity.this, "isChecked:打开", Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(MainActivity.this, "isChecked:关闭", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switcher.setBackgroundResource(res[position % res.length]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public View makeView() {
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        return imageView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            Toast.makeText(MainActivity.this, "isChecked:打开", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "isChecked:关闭", Toast.LENGTH_SHORT).show();
        }
    }
}
