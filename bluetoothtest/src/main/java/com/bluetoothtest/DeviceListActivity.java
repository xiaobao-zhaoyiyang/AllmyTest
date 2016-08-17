package com.bluetoothtest;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Set;

/**
 * Created by yo on 2016/7/18.
 */
public class DeviceListActivity extends AppCompatActivity {
    private Button stopDevice;
    private ListView listView;
    private DeviceItemAdapter mAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            //找到设备
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                //避免重复添加已经绑定过的设备
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    DeviceItemAdapter adapter = (DeviceItemAdapter) listView.getAdapter();
                    adapter.add(device);
                    adapter.notifyDataSetChanged();
                }

            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devicelist);
        listView = (ListView) findViewById(R.id.device_listView);
        mAdapter = new DeviceItemAdapter(this, R.layout.item_device);
        listView.setAdapter(mAdapter);

        registerBluetooth();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //开始搜索
        mBluetoothAdapter.startDiscovery();

        //获取已经配对过的设备
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        DeviceItemAdapter adapter = (DeviceItemAdapter) listView.getAdapter();
        adapter.clear();

        //将其添加到设备列表中
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                adapter.add(device);
            }
        }

        stopDevice = (Button) findViewById(R.id.stop_device);
        stopDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothAdapter.isDiscovering()){
                    mBluetoothAdapter.cancelDiscovery();
                    stopDevice.setVisibility(View.GONE);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //取消可能正在进行的搜索
                if (mBluetoothAdapter.isDiscovering()) {
                    mBluetoothAdapter.cancelDiscovery();
                }

                ArrayAdapter adapter = (ArrayAdapter) listView.getAdapter();
                BluetoothDevice device = (BluetoothDevice) adapter.getItem(position);

                Log.i("TAG", "获取到的地址： " + device.getAddress());

                Intent i = new Intent(DeviceListActivity.this, ChattingActivity.class);
                //将设备地址存储到Intent当中
                i.putExtra("DEVICE_ADDR", device.getAddress());
                startActivity(i);
            }
        });
    }

    private void registerBluetooth() {
        //注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filter);
    }

    public class DeviceItemAdapter extends ArrayAdapter<BluetoothDevice> {

        private final LayoutInflater mInflater;
        private int mResource;

        public DeviceItemAdapter(Context context, int resource) {
            super(context, resource);
            mInflater = LayoutInflater.from(context);
            mResource = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = mInflater.inflate(mResource, parent, false);
            }

            TextView name = (TextView) convertView.findViewById(R.id.device_name);
            TextView info = (TextView) convertView.findViewById(R.id.device_info);
            BluetoothDevice device = getItem(position);
            name.setText(device.getName());
            info.setText(device.getAddress());

            return convertView;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //取消搜索
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }

        //注销BroadcastReceiver，防止资源泄露
        unregisterReceiver(mReceiver);
    }
}
