package com.sajednt.arzalarm.functions;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.sajednt.arzalarm.AppController;
import com.sajednt.arzalarm.R;
import com.pushwoosh.notification.NotificationServiceExtension;
import com.pushwoosh.notification.PushMessage;



public class YourNotificationServiceExtension extends NotificationServiceExtension {

    Dialog dialog;

    @Override
    protected void startActivityForPushMessage(PushMessage message) {
        // super.startActivityForPushMessage() starts default launcher activity 
        // or activity marked with ${applicationId}.MESSAGE action.
        // Simply do not call it to override this behaviour.
        // super.startActivityForPushMessage(message);
        Log.e("service notif", "startActivityForPushMessage " + message.toJson().toString());

        // start your activity instead:
    }

    @Override
    protected boolean onMessageReceived(PushMessage pushMessage) {
        Log.e("service notif", "NotificationService.onMessageReceived: " + pushMessage.getMessage());


//        Intent i = new Intent();
//        i.setAction(Intent.ACTION_MAIN);
//        i.addCategory(Intent.CATEGORY_LAUNCHER);
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
//        i.setComponent(new ComponentName(getApplicationContext().getPackageName(), Login.class.getName()));
//        AppController.getContext().startActivity(i);
//        runOnUiThread(new Runnable() {
//            public void run() {
//                Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
//            }
//        });

        new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {
                //wakeDevice();
                dialog = new Dialog(getApplicationContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.menu_drawer);
                dialog.getWindow()
                        .setType(
                                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);

                dialog.show();


                if(isAppOnForeground()){

                    Toast.makeText(YourNotificationServiceExtension.this.getApplicationContext(),"Foreground",Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(YourNotificationServiceExtension.this.getApplicationContext(),"Background",Toast.LENGTH_SHORT).show();

                }
            }
        });
        return false;
    }

    public void wakeDevice() {
        PowerManager powerManager = (PowerManager) AppController.getContext().getSystemService(Context.POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wakeLock = powerManager.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
        wakeLock.acquire();

        KeyguardManager keyguardManager = (KeyguardManager) AppController.getContext().getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
        keyguardLock.disableKeyguard();

//        new Handler(Looper.getMainLooper()).post(new Runnable() {
//
//            @Override
//            public void run() {
//                getWindow().addFlags(
//                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
//                                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
//                                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
//                                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
//            }
//        });
    }
}