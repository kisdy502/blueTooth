package com.kisdy.sdt13411.blue_booth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.opengl.Visibility;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_ENABLE_BT = 1;

    private static final String TAG = "MainActivity";

    private Button btn_open;

    private Button btn_close;

    private Button btn_scan;

    private Button btn_create_game;

    private Button btn_add_game;

    private View loading;

    private View view;


    private List<BluetoothDevice> pairedDevices;

    private BluetoothAdapter mBluetoothAdapter;

    private PopupWindow popupWindow;


    ArrayAdapter<BluetoothDevice> mAdapter;

    private LayoutInflater mInflater;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            loading.setVisibility(View.GONE);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();

        pairedDevices = new ArrayList<BluetoothDevice>();
        mInflater = LayoutInflater.from(this);
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
    }

    private void initUI() {
        btn_open = (Button) findViewById(R.id.id_btn_open);
        btn_close = (Button) findViewById(R.id.id_btn_close);
        btn_scan = (Button) findViewById(R.id.id_btn_scan);
        btn_create_game = (Button) findViewById(R.id.id_btn_create_game);
        btn_add_game = (Button) findViewById(R.id.id_btn_add_game);

        loading = findViewById(R.id.loading_layout);

        btn_open.setOnClickListener(this);
        btn_close.setOnClickListener(this);
        btn_scan.setOnClickListener(this);
        btn_create_game.setOnClickListener(this);
        btn_add_game.setOnClickListener(this);
    }

    //打开蓝牙
    private void openBlueTooch() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            Toast.makeText(MainActivity.this, "打开蓝牙失败，请检查你的手机是否支持蓝牙！", Toast.LENGTH_SHORT).show();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    //关闭蓝牙
    private void closeBlueTooch() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.disable();//禁用
            Toast.makeText(MainActivity.this, "蓝牙已经关闭！", Toast.LENGTH_SHORT).show();
        }
    }

    //扫描蓝牙
    private void scanBlueTooch() {
        pairedDevices.clear();
        loading.setVisibility(View.VISIBLE);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter.isEnabled()) {
            if (mBluetoothAdapter.startDiscovery()) {
                mBluetoothAdapter.cancelDiscovery();
            }
            mBluetoothAdapter.startDiscovery();
            mHandler.sendEmptyMessageDelayed(0, 12000);
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (!pairedDevices.contains(device))
                    pairedDevices.add(device);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_ENABLE_BT == requestCode) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(MainActivity.this, "蓝牙已经打开！", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.id_btn_open:
                openBlueTooch();
                break;
            case R.id.id_btn_close:
                closeBlueTooch();
                break;
            case R.id.id_btn_scan:
                scanBlueTooch();
                break;
            case R.id.id_btn_create_game:
                //创建服务器
                if (mBluetoothAdapter.isEnabled()) {
                    intent = new Intent(MainActivity.this, ServerActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, "请先打开蓝牙!", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            case R.id.id_btn_add_game:
                if (mBluetoothAdapter.isEnabled()) {
                    showPop();
                }else{
                    Toast.makeText(MainActivity.this, "请先打开蓝牙!", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
        }
    }



    //这里可以单独抽取为一个PopWindow的子类,代码比较少就写在一起了
    private void showPop() {
        if (popupWindow == null) {

            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.pop_layout, null);
            ListView lv_group = (ListView) view.findViewById(R.id.lv_devices);
            lv_group.setAdapter(mAdapter = new ArrayAdapter<BluetoothDevice>(MainActivity.this, -1, pairedDevices) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    ViewHolder viewHolder = null;
                    if (convertView == null) {
                        convertView = mInflater.inflate(R.layout.pop_item, parent, false);
                        viewHolder = new ViewHolder();
                        viewHolder.tvDevName = (TextView) convertView.findViewById(R.id.id_tv_devName);
                        viewHolder.ImgSelected = (ImageView) convertView.findViewById(R.id.id_img_selected);
                        convertView.setTag(viewHolder);
                    } else {
                        viewHolder = (ViewHolder) convertView.getTag();
                    }

                    BluetoothDevice device = getItem(position);
                    String name = getItem(position).getName();
                    Log.d(TAG, name);
                    viewHolder.tvDevName.setText(name);
                    return convertView;
                }
            });


            lv_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    BluetoothDevice device = pairedDevices.get(position);
                    Toast.makeText(MainActivity.this, "position=" + position, Toast.LENGTH_SHORT).show();

                    //搜索服务器并连接到服务器
                    Intent intent = new Intent(MainActivity.this, ClientActivity.class);
                    intent.putExtra("device", device);
                    startActivity(intent);

                    if (popupWindow != null) {
                        popupWindow.dismiss();
                    }
                }
            });

        }
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        popupWindow = new PopupWindow(view, windowManager.getDefaultDisplay().getWidth(), (int) (windowManager.getDefaultDisplay().getHeight() * 0.5));
        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        // 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
        int xPos = windowManager.getDefaultDisplay().getWidth() / 2
                - popupWindow.getWidth();
        //底部弹出
        popupWindow.showAtLocation(MainActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
    }

    class ViewHolder {
        TextView tvDevName;
        ImageView ImgSelected;
    }
}
