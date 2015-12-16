package com.kisdy.sdt13411.util;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;

import com.kisdy.sdt13411.bean.SendedMsg;
import com.kisdy.sdt13411.contentprovider.SmsContentProvider;
import com.kisdy.sdt13411.db.SmsDbOpenHelper;

import java.util.HashSet;
import java.util.List;

/**
 * Created by sdt13411 on 2015/12/4.
 */
public class SmsUtil {

    /**
     * 发送短息
     *
     * @param reciverPhoneNo
     * @param smsContent
     */
    public static int sendSms(Context context,String reciverPhoneNo, String smsContent, PendingIntent sentIndent, PendingIntent deliveryIntent) {
        int result = 0;
        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
        List<String> divideContents = smsManager.divideMessage(smsContent);
        for (String str : divideContents) {
            smsManager.sendTextMessage(reciverPhoneNo, null, str, sentIndent, deliveryIntent);
            result++;
        }

        return result;
    }

    public static int sendSms(Context context,HashSet<String> phoneNumlist, SendedMsg msg, PendingIntent sentIndent, PendingIntent deliveryIntent) {
        String smsContent = msg.getSmsContent();
        int result = 0;
        for (String no : phoneNumlist) {
            result += sendSms(context,no, smsContent, sentIndent, deliveryIntent);
        }
        SmsDbOpenHelper.insertSendedMsg(context,msg);

        return result;


    }

    public static void doSendSMSTo(Context context, String phoneNumber, String message) {
        if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
            intent.putExtra("sms_body", message);
            context.startActivity(intent);
        }
    }

}


