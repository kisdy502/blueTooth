package com.kisdy.sdt13411.blue_booth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//客户端
public class ClientActivity extends AppCompatActivity {


    private static final String TAG ="ClientActivity" ;
    private final int MSG_ID_CONN_SERVER_SUCCESS=0x01;//服务器已经启动
    private final int MSG_ID_CLIENT_CONNED=0x02;//服务器已经启动
    private static final int MESSAGE_READ =0x03 ;//读取到客户端发来的数据
    private static final int MESSAGE_WRITE=0x04 ;//发送数据

    private BluetoothAdapter mBluetoothAdapter;

    BluetoothDevice mDevice=null;

    private TextView tvConnectInfo;
    private TextView tvMessage;
    private EditText edtContent;
    private Button btnSend;

    private ConnectedThread connThread;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_ID_CONN_SERVER_SUCCESS:
                    setConnInfo("已经连接到服务器...");
                    break;
                case MESSAGE_READ:
                    byte[] data=(byte[])msg.obj;
                    refreshMsg("server:"+new String(data));
                    break;
                case MESSAGE_WRITE:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void setConnInfo(String info){
        tvConnectInfo.setText(info);
    }

    private void refreshMsg(String info){
        StringBuffer sb=new StringBuffer(tvMessage.getText());
        sb.append(info);
        sb.append("\n");
        tvMessage.setText(sb.toString());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        mDevice = (BluetoothDevice) getIntent().getExtras().get("device");

        initUI();

        initConnect();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtContent.getText().length()>0) {
                    connThread.write(edtContent.getText().toString().getBytes());
                    refreshMsg("client:" + edtContent.getText().toString());
                    edtContent.setText("");
                }
            }
        });
    }

    private void  initUI(){
        tvConnectInfo= (TextView) findViewById(R.id.tvConnInfo);
        tvMessage= (TextView) findViewById(R.id.tvMsgList);
        edtContent= (EditText) findViewById(R.id.id_edt_text);
        btnSend= (Button) findViewById(R.id.id_btn_send);
    }

    private void initConnect() {
        new ClientThread(mDevice).start();
    }


    private class ClientThread extends Thread {
        private  BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        private ClientThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mmDevice = device;
            try {
                tmp = device.createRfcommSocketToServiceRecord(Constants.MY_UUID);
                Log.d(TAG,"尝试连接服务器!");

            }catch (Exception ex){
            }
            mmSocket=tmp;
        }
        @Override
        public void run() {
            mBluetoothAdapter.cancelDiscovery();
            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
                BluetoothDevice d=  mmSocket.getRemoteDevice();
                Log.d(TAG, "连接到服务器成功,服务器名称:"+d.getName());
                mHandler.sendEmptyMessage(MSG_ID_CONN_SERVER_SUCCESS);
            } catch (IOException connectException) {
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }

            // Do work to manage the connection (in a separate thread)
            manageConnectedSocket(mmSocket);
        }

        /** Will cancel an in-progress connection, and close the socket **/
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    private void manageConnectedSocket(BluetoothSocket mmSocket) {
        connThread=new ConnectedThread(mmSocket);
        connThread.start();
    }


    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI Activity
                    mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                    Log.d(TAG, "接收到服务器发过来的数据!");
                } catch (IOException e) {
                    break;
                }
            }
        }

        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
                mmOutStream.flush();
            } catch (IOException e) { }
        }


        /* Call this from the main Activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }
}
