package yeeaoo.imooc_image;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void btnPrimaryColor(View view){
        startActivity(new Intent(this, PrimaryColor.class));
    }
    public void btnColorMatrix(View view){
        startActivity(new Intent(this, ColorMatrix.class));
    }
    public void btnPixelEffect(View view){
        startActivity(new Intent(this, PixelEffect.class));
    }
    public void btnMatrix(View view){
        startActivity(new Intent(this, ImageMatrixTest.class));
    }
    public void btnXformode(View v){
        startActivity(new Intent(this, RoundRectXformodeActivity.class));
    }
    public void btnShader(View view){
        startActivity(new Intent(this, BitmapShaderTest.class));
    }
    public void btnReflect(View view){
        startActivity(new Intent(this, ReflectViewTest.class));
    }
    public void btnMesh(View view){
        startActivity(new Intent(this, MeshViewTest.class));
    }
}
