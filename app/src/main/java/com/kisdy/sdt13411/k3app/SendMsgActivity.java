package com.kisdy.sdt13411.k3app;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.kisdy.sdt13411.bean.FestivalBean;
import com.kisdy.sdt13411.bean.FestivalLab;
import com.kisdy.sdt13411.bean.Msg;
import com.kisdy.sdt13411.bean.SendedMsg;
import com.kisdy.sdt13411.util.SmsUtil;
import com.kisdy.sdt13411.view.FlowLayout;

import java.util.Date;
import java.util.HashSet;

public class SendMsgActivity extends AppCompatActivity {

    private static final String TAG = "SendMsgActivity";
    private static final String ACTION_SEND_MSG = "Action.send.msg";
    private static final String ACTION_DELIVER_MSG = "Action.deliver.msg";

    private static final int REQUEST_CANTACT = 1;

    private int festivalId;
    private int msgId;
    private String smsContent;

    FestivalBean festivalBean;
    Msg msg;

    EditText edtContent;
    Button btnAdd;
    FloatingActionButton faBSend;
    FlowLayout flowView;
    View loadingView;

    HashSet<String> mContactNumbers = new HashSet<String>(); //联系人姓名
    HashSet<String> mContactNames = new HashSet<String>();   //联系人手机号码

    PendingIntent sendPni;
    PendingIntent deliverPni;

    BroadcastReceiver msendMsgReceiver;
    BroadcastReceiver mdeliverMsgReceiver;

    LayoutInflater inflater;

    int totalSendCount;
    int sendSuccesCount;

    public static void toActivity(Context context, int msgid, int festivalId) {
        Intent intent = new Intent(context, SendMsgActivity.class);
        intent.putExtra(Constants.MSG_ID, msgid);
        intent.putExtra(Constants.FESTIVAL_ID, festivalId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_msg);
        inflater = LayoutInflater.from(this);
        initData();
        initView();
        initEvents();
        initReciver();
    }

    private void initData() {
        msgId = getIntent().getIntExtra(Constants.MSG_ID, -1);
        festivalId = getIntent().getIntExtra(Constants.FESTIVAL_ID, -1);
        festivalBean = FestivalLab.getInstance().getFestivalByFestId(festivalId);
        msg = FestivalLab.getInstance().getMsgByMsgId(msgId);
    }

    private void initView() {
        edtContent = (EditText) findViewById(R.id.id_edtContent);
        btnAdd = (Button) findViewById(R.id.id_btnAdd);
        faBSend = (FloatingActionButton) findViewById(R.id.id_fabSendMsg);
        flowView = (FlowLayout) findViewById(R.id.id_flowLayout);
        loadingView = findViewById(R.id.id_loading_layout);

        if (msg != null)
            edtContent.setText(msg.getContent());
    }


    private void initEvents() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, REQUEST_CANTACT);
            }
        });

        faBSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edtContent.getText())) {
                    Toast.makeText(SendMsgActivity.this, "短信内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (flowView.getChildCount() == 0) {
                    Toast.makeText(SendMsgActivity.this, "请至少选择一个联系人", Toast.LENGTH_SHORT).show();
                    return;
                }

                totalSendCount = SmsUtil.sendSms(SendMsgActivity.this,mContactNumbers, buildMsg(edtContent.getText().toString()), sendPni, deliverPni);
                if (totalSendCount == 0) {

                }
            }
        });
    }

    public SendedMsg buildMsg(String content) {
        SendedMsg msg = new SendedMsg();
        msg.setSmsContent(content);
        msg.setFestivalName(festivalBean.getName());
        StringBuffer sb = new StringBuffer();
        for (String contactName : mContactNames) {
            sb.append(contactName+";");
        }
        msg.setNames(sb.substring(0,sb.length() - 1));

        StringBuffer sb1 = new StringBuffer();
        for (String contactName : mContactNumbers) {
            sb1.append(contactName+";");
        }
        msg.setNumber(sb1.substring(0,sb1.length() - 1));
        msg.setDate(new Date());

        return msg;
    }

    private void initReciver() {
        Intent sendIntent = new Intent(ACTION_SEND_MSG);
        sendPni = PendingIntent.getBroadcast(this, 0, sendIntent, 0);

        Intent deliverIntent = new Intent(ACTION_DELIVER_MSG);
        deliverPni = PendingIntent.getBroadcast(this, 0, deliverIntent, 0);


        registerReceiver(msendMsgReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (getResultCode() == RESULT_OK) {
                    sendSuccesCount++;
                    Toast.makeText(SendMsgActivity.this, "短信发送成功！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SendMsgActivity.this, "短信发送失败！", Toast.LENGTH_SHORT).show();
                }

                if (sendSuccesCount == totalSendCount) {
                    finish();
                }
            }
        }, new IntentFilter(ACTION_SEND_MSG));


        registerReceiver(mdeliverMsgReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(SendMsgActivity.this, "对方已经成功接收你的短信！", Toast.LENGTH_SHORT).show();
            }
        }, new IntentFilter(ACTION_DELIVER_MSG));

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CANTACT) {
            if (resultCode == RESULT_OK) {
                Uri contactUri = data.getData();
                Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);

                cursor.moveToFirst();
                String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                int result = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                if (result > 0) {
                    int conactID = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                    Cursor phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + conactID, null, null);
                    phoneCursor.moveToFirst();
                    String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    phoneCursor.close();

                    if (!TextUtils.isEmpty(phoneNumber) && !mContactNumbers.contains(phoneNumber)) {
                        mContactNames.add(contactName);//存储联系人姓名
                        mContactNumbers.add(phoneNumber);//联系人手机号码存起来
                        Toast.makeText(SendMsgActivity.this, "contactName=" + contactName + "----phoneNumber=" + phoneNumber, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "contactName=" + contactName + "----phoneNumber=" + phoneNumber);

                        addTag(contactName);
                    }
                }
                cursor.close();
            }
        }
    }

    private void addTag(String contactName) {
        TextView view = (TextView) inflater.inflate(R.layout.tag, flowView, false);
        view.setText(contactName);
        flowView.addView(view);
        Toast.makeText(SendMsgActivity.this, "flowView child count=" + flowView.getChildCount(), Toast.LENGTH_SHORT).show();

        Log.d(TAG, "flowView child count=" + flowView.getChildCount());

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(msendMsgReceiver);
        unregisterReceiver(mdeliverMsgReceiver);
        super.onDestroy();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
