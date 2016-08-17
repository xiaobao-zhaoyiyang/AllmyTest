package yeeaoo.mytest;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by yo on 2016/6/14.
 */
public class LocationTestActivity extends AppCompatActivity {
    private TextView positionTextView;
    private LocationManager locationManager;
    private String provider;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String currentPosition = (String) msg.obj;
            positionTextView.setText(currentPosition);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locationtest);
        positionTextView = (TextView) findViewById(R.id.position_text_view);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // 获取所有可用的位置提供器
        List<String> providerList = locationManager.getProviders(true);
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            // 当前没有可用的位置提供器时，弹出Toast提示用户
            Toast.makeText(LocationTestActivity.this, "No location provider to use", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("locationTestActivity", "no user_permission1");
            return;
        }
        provider = LocationManager.NETWORK_PROVIDER;
        Log.d("locationTestActivity", "provider: " + provider);
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            // 显示当前设备的位置信息
            showLocation(location);
        }else{
            Log.d("locationTestActivity", "no location");
        }
        locationManager.requestLocationUpdates(provider, 5000, 1, locationListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Log.d("locationTestActivity", "no user_permission2");
                return;
            }
            locationManager.removeUpdates(locationListener);
        }
    }

    private void showLocation(Location location) {
//        String currentPosition = "latitude is " + location.getLatitude() +
//                "\n" + "longitude is " + location.getLongitude();
//        Log.d("locationTestActivity", currentPosition);
//        positionTextView.setText(currentPosition);
        parseToLocation(location);
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private void parseToLocation(final Location location){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection;
                String path = "http://maps.googleapis.com/maps/api/geocode/json?" +
                        "latlng=" + location.getLatitude() + ","
                        + location.getLongitude() + "&sensor=false";
                Log.d("locationTestActivity", "path=" + path);
                try {
                    URL url = new URL(path);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(8000);
                    connection.setConnectTimeout(8000);
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    if (connection.getResponseCode() == 200) {
                        InputStream is = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }
                        JSONObject object = new JSONObject(sb.toString());
                        JSONArray array = object.getJSONArray("result");
                        if (array.length() > 0) {
                            JSONObject subObject = array.getJSONObject(0);
                            String address = subObject.getString("formatted_address");
                            Message message = new Message();
                            message.obj = address;
                            mHandler.sendMessage(message);
                        }
                    }else{
                        Log.d("locationTestActivity", "code=" + connection.getResponseCode());
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



}
