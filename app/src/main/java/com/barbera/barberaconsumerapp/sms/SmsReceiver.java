package com.barbera.barberaconsumerapp.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import com.mukesh.OtpView;

public class SmsReceiver extends BroadcastReceiver {
    //    private static SmsListener mListener;
    Boolean b;
    private static OtpView editText;

    public void setEditText(OtpView editText) {
        SmsReceiver.editText = editText;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for (SmsMessage sms : messages) {

            String msg = sms.getMessageBody();

            String[] splited = msg.split("\\s");
            Log.d("ATM", "OTP is: " + splited[0]);
            editText.setText(splited[0]);
        }
    }
}