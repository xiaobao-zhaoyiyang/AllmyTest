package com.imooc_guaguaka;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import view.GuaGuaKa;

public class MainActivity extends AppCompatActivity {

    private GuaGuaKa mGuaguaka;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGuaguaka = (GuaGuaKa) findViewById(R.id.id_guaguaka);
        mGuaguaka.setText("技能GET");
        mGuaguaka.setOnGuaGuaKaCompleteListener(new GuaGuaKa.OnGuaGuaKaCompleteListener() {
            @Override
            public void complete() {
                Toast.makeText(MainActivity.this, "用户已经刮完", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
