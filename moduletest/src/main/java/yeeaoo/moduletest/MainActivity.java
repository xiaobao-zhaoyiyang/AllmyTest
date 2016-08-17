package yeeaoo.moduletest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {

    private Socket mSocket;

    private String url = "http://chat.socket.io";
    private String url1 = "http://node.livechat.yeeaoo.com";

    private EditText et;

    {
        try {
            Log.i("MainActivity", "Socket初始化开始");
            mSocket = IO.socket(url1);
        } catch (URISyntaxException e) {
            Log.i("MainActivity", "Socket初始化失败");
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSocket.connect();
        Log.i("MainActivity", "Socket成功:" + mSocket.connected());
        if (mSocket.connected()) {
            Log.i("MainActivity", "Socket链接成功");
            Toast.makeText(MainActivity.this, "Socket链接成功", Toast.LENGTH_SHORT).show();
        }else{
            Log.i("MainActivity", "Socket链接不成功");
        }

//        ImageView iv1 = (ImageView) findViewById(R.id.roundimage1);
//        ImageView iv2 = (ImageView) findViewById(R.id.roundimage2);
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
//                R.mipmap.psb);
//        iv1.setImageDrawable(new RoundImageDrawable(bitmap));
//        iv2.setImageDrawable(new CircleImageDrawable(bitmap));

        Button button = (Button) findViewById(R.id.sendmsg);
        et = (EditText) findViewById(R.id.edittext);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 发消息
                mSocket.emit("pushmsgtoroom",et.getText().toString().trim());
            }
        });

        // 接收消息
        mSocket.on("pushmsgtoroom", onNewMessage);
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    Log.i("data_onNewMessage", data.toString());
                    String username;
                    String message;
                    try {
                        username = data.getString("username");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        return;
                    }

                    // add the message to view
//	                addMessage(username, message);
                }
            });
        }
    };
}
