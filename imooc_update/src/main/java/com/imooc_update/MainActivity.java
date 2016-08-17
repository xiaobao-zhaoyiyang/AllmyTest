package com.imooc_update;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.imooc_update.update.UpdateService;

public class MainActivity extends AppCompatActivity {
    private Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = (Button) findViewById(R.id.id_but_view);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkVersion();
            }
        });
        checkVersion();
    }

    private void checkVersion() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("有新版本是否更新");
        dialog.setPositiveButton("安装", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.i("UpdateService", "AlertDialog");
                Intent intent = new Intent(MainActivity.this, UpdateService.class);
                intent.putExtra("apkUrl", "http://wap.apk.anzhi.com/data2/apk/201607/27/8e6b9a38b05bf732d142f45cd99612f8_20397900.apk");
                startService(intent);
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.create();
        dialog.show();
    }
}
