package com.sajednt.arzalarm;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.pushwoosh.Pushwoosh;
import com.pushwoosh.notification.PushwooshNotificationSettings;
import com.pushwoosh.notification.SoundType;
import com.pushwoosh.notification.VibrateType;


//import co.pushe.plus.Pushe;
//import co.pushe.plus.notification.NotificationButtonData;
//import co.pushe.plus.notification.NotificationData;
//import co.pushe.plus.notification.PusheNotification;
//import co.pushe.plus.notification.PusheNotificationListener;

public class AppController extends MultiDexApplication {

    protected static Context context = null;


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

//        TimeKeeper.initialize(this);
        Pushwoosh.getInstance().registerForPushNotifications();
        PushwooshNotificationSettings.setMultiNotificationMode(true);
        PushwooshNotificationSettings.setVibrateNotificationType(VibrateType.ALWAYS);
        PushwooshNotificationSettings.setSoundNotificationType(SoundType.ALWAYS);
        PushwooshNotificationSettings.setLightScreenOnNotification(true);
        PushwooshNotificationSettings.setSoundNotificationType(SoundType.ALWAYS);

        PushwooshNotificationSettings.setEnableLED(true);



//        Pushwoosh.getInstance().setUserId("2596");


//        SendResponseBody response;
//        try {
//            response = new SendService().send(request);
//            System.out.println(response);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        PusheNotification notificationModule = Pushe.getPusheService(PusheNotification.class);
//        notificationModule.setNotificationListener(new PusheNotificationListener() {
//            @Override
//            public void onNotification(@NonNull NotificationData notificationData) {
//                Toast.makeText(getApplicationContext(), "onNotification", Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onCustomContentNotification(@NonNull Map<String, Object> map) {
//
//            }
//
//            @Override
//            public void onNotificationClick(@NonNull NotificationData notificationData) {
//
//            }
//
//            @Override
//            public void onNotificationDismiss(@NonNull NotificationData notificationData) {
//                Toast.makeText(getApplicationContext(), "onNotificationDismiss", Toast.LENGTH_LONG).show();
//
//            }
//
//            @Override
//            public void onNotificationButtonClick(@NonNull NotificationButtonData notificationButtonData, @NonNull NotificationData notificationData) {
//
//            }
//        });


    }


    @Override
    public void onTerminate() {
        super.onTerminate();

    }

    public static Context getContext() {
        return context;
    }
}
