package com.kisdy.sdt13411.blue_booth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
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

//服务器
public class ServerActivity extends AppCompatActivity {

    private static final String NAME ="KISDY" ;
    private static final String TAG ="ServerActivity" ;

    private BluetoothAdapter mBluetoothAdapter;

    private final int MSG_ID_SERVER_OK=0x01;//服务器已经启动
    private final int MSG_ID_CLIENT_CONNED=0x02;//服务器已经启动
    private static final int MESSAGE_READ =0x03 ;//读取到客户端发来的数据
    private static final int MESSAGE_WRITE=0x04 ;

    private  AcceptThread accepThread;
    private ConnectedThread connThread;

    private TextView tvConnectInfo;
    private TextView tvMessage;
    private EditText edtContent;
    private Button btnSend;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_ID_SERVER_OK:
                    setConnInfo("服务器已经启动,等待客户端的连接...");
                    break;
                case MSG_ID_CLIENT_CONNED:
                    setConnInfo("客户端已经连接上...");
                    break;
                case MESSAGE_READ:
                    byte[] data=(byte[])msg.obj;
                    refreshMsg("client:"+new String(data));
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
        setContentView(R.layout.activity_server);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        initUI();
        initConnect();
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtContent.getText().length()>0) {
                    connThread.write(edtContent.getText().toString().getBytes());
                    refreshMsg("server:" + edtContent.getText().toString());
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
        accepThread=new AcceptThread();
        accepThread.start();


    }


    private class AcceptThread extends Thread {
        private BluetoothServerSocket mmServerSocket;
        private AcceptThread() {
            BluetoothServerSocket tmp = null;
            try{
                tmp= mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(NAME,Constants.MY_UUID);
                Log.d(TAG, "服务器已经启动,等待客户端的连接");
                mHandler.sendEmptyMessage(MSG_ID_SERVER_OK);
            }catch (Exception ex){
            }
            mmServerSocket=tmp;
        }

        @Override
        public void run(){
            BluetoothSocket socket = null;

            while (true) {
                try {
                    Log.d(TAG, "等待客户端连接中...");
                    socket = mmServerSocket.accept();
                    if (socket != null) {
                        mHandler.sendEmptyMessage(MSG_ID_CLIENT_CONNED);
                        BluetoothDevice d= socket.getRemoteDevice();
                        Log.d(TAG, "客户端:" + d.getName() + "连接上了!");
                        // Do work to manage the connection (in a separate thread)
                        manageConnectedSocket(socket);
                        mmServerSocket.close();
                        break;
                    }

                }catch (Exception ex){
                    break;
                }
            }

        }

        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) { }
        }
    }

    //逻辑处理
    private void manageConnectedSocket(BluetoothSocket socket) {
        connThread=new ConnectedThread(socket);
        connThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
                    bytes = mmInStream.read(buffer);         // Read from the InputStream

                    mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget(); // Send the obtained bytes to the UI Activity
                    Log.d(TAG, "客户端发来数据,长度="+bytes);

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
