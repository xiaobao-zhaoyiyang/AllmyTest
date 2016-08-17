package yeeaoo.imooc_topbar;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TopBar topBar = (TopBar) findViewById(R.id.topbar);
        topBar.setBackgroundColor(Color.parseColor("#398CD9"));
        topBar.setOnTopBarClickListener(new TopBar.topBarClickListener() {
            @Override
            public void leftClick() {
                Toast.makeText(MainActivity.this, "LeftButton", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void rightClick() {
                Toast.makeText(MainActivity.this, "RightButton", Toast.LENGTH_SHORT).show();
            }
        });

        topBar.setLeftIsVisible(true);
        topBar.setRightIsVisible(false);
    }
}
