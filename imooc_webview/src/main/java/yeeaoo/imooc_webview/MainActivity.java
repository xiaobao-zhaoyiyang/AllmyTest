package yeeaoo.imooc_webview;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private String url = "http://2014.qq.com/";
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Uri uri = Uri.parse(url);
//        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//        startActivity(intent);
        init();
    }

    private void init() {
        webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl(url);
        // 覆盖WebView默认通过第三方或系统浏览器打开网页的行为， 是的网页在WebView中打开
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 返回值是true，控制网页在WebView中打开。false则其他浏览器打开
                view.loadUrl(url);
                return true;
            }
            // WebviewClient帮助Webview去处理一些页面控制和请求通知
        });
        // 启动支持JavaScript
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        // WebView加载页面优先使用缓存
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    // 网页加载完毕， 关闭dialog
                    closeDialog();
                } else {
                    // 网页正在加载, 打开Dialog
                    openDialog(newProgress);
                }
            }
        });
    }

    private void closeDialog() {
        if (dialog != null && dialog.isShowing()){
            dialog.dismiss();
            dialog = null;
        }
    }

    private void openDialog(int newProgress) {
        if (dialog == null){
            dialog = new ProgressDialog(this);
            dialog.setTitle("正在加载");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setProgress(newProgress);
            dialog.show();
        }else {
            dialog.setProgress(newProgress);
        }
    }

    // 改写手机物理按键返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Toast.makeText(this, webView.getUrl(), Toast.LENGTH_SHORT).show();
            if (webView.canGoBack()){
                webView.goBack(); // 返回上一页面
                return true;
            }else{
                System.exit(0);// 退出程序
            }
        }

        return super.onKeyDown(keyCode, event);
    }
}
