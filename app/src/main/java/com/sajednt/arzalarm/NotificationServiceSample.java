package com.sajednt.arzalarm;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.MainThread;

import com.pushwoosh.notification.NotificationServiceExtension;
import com.pushwoosh.notification.PushMessage;

import java.util.List;

public class NotificationServiceSample extends NotificationServiceExtension {


    @Override
    public boolean onMessageReceived(final PushMessage message) {
        Log.d("Push Notif", "NotificationService.onMessageReceived: " + message.toJson().toString());

        // automatic foreground push handling

        if (isAppOnForeground()) {
            Handler mainHandler = new Handler(getApplicationContext().getMainLooper());
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    handlePush(message);
                }
            });

            // this indicates that notification should not be displayed
            return true;
        }
        else{
            Handler mainHandler = new Handler(getApplicationContext().getMainLooper());
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), message.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

        return false;
    }



    @Override
    protected void onMessagesGroupOpened(List<PushMessage> pushMessagesList) {
        Log.d("Push Notif", "NotificationService.onMessagesGroupOpened with: " + pushMessagesList.size() + " messages");

        // TODO: handle push messages group
    }

    @Override
    protected void startActivityForPushMessage(PushMessage message) {
        super.startActivityForPushMessage(message);

        // TODO: start custom activity if necessary

        handlePush(message);
    }

    @MainThread
    private void handlePush(PushMessage message) {
        Log.d("Push Notif", "NotificationService.handlePush: " + message.toJson().toString());
        Toast.makeText(getApplicationContext(), message.getMessage(), Toast.LENGTH_SHORT).show();

        // TODO: handle push message
    }
}