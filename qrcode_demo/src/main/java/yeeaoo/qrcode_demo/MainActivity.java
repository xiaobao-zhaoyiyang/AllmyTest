package yeeaoo.qrcode_demo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CaptureRequest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

public class MainActivity extends AppCompatActivity {
private TextView mTvResult;
    private EditText mEditText;
    private ImageView mImageView;
    private CheckBox mCheckBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvResult = (TextView) findViewById(R.id.tv_result);
        mEditText = (EditText) findViewById(R.id.et_text);
        mImageView = (ImageView) findViewById(R.id.image);
        mCheckBox = (CheckBox) findViewById(R.id.cb_logo);
    }
    public void scan(View view){
        startActivityForResult(new Intent(MainActivity.this, CaptureActivity.class), 0);
    }
    // 生成二维码
    public void make(View view){
        String input = mEditText.getText().toString();
        if (input.equals("")){
            Toast.makeText(MainActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
        }else {
            Bitmap bitmap = EncodingUtils
                    .createQRCode(input, 500, 500, mCheckBox.isChecked()?
                            BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher) : null);
            mImageView.setImageBitmap(bitmap);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Bundle bundle = data.getExtras();
            String result = bundle.getString("result");
            mTvResult.setText(result);
        }
    }
}
