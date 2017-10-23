package com.capstone.deptmanager;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.Map;

/**
 * Created by Daehee on 2017-05-24.
 * 백그라운드에서 앱의 알림을 수신하는 것 외에 다른 방식으로 메시지를 처리하려는 경우에 필요합니다.
 * 포그라운드 앱의 알림 수신, 데이터 페이로드 수신, 업스트림 메시지 전송 등을 수행하려면
 * 이 서비스를 확장해야 합니다.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage); // 내용 없음. 삭제 무관

        Log.e("MyLog", "onMessageRecieved");

        Map<String, String> data = remoteMessage.getData();
        Log.e("MyLog", "data : " + data.toString());

        String strTitle="", strMessage = "";
//        내소스
//        if (data != null && data.size() > 0) {
//            strTitle = data.get("title");
//            strMessage = data.get("message");
//        }
        if (data != null && data.size() > 0) {
            // json 파싱처리 한다.
            Gson gson = new Gson();
            String pushSendStr = gson.toJson(data);

            Log.e("MyLog", "pushSendStr : " + pushSendStr);

            // JSON ===> Bean 변환 : 서버측에서 보내는 값이 많이 달라진다 하는 경우에는 키값으로 일일히 빼는것보다 한번 빈으로 바꿔주는게 좋다.
            PushMsgBean.Data bean_data = gson.fromJson(pushSendStr, PushMsgBean.Data.class);
//            PushMsgBean msgBean = gson.fromJson(pushSendStr, PushMsgBean.class);

//            noti(msgBean);
            noti(bean_data);
        }
    }

    public void noti(PushMsgBean.Data bean_data) {
//    public void noti(PushMsgBean msgBean) {
        NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(this);
        notiBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                .setSmallIcon(R.drawable.ic_launcher)
                .setTicker("푸시테스트 티커")
                .setContentTitle(bean_data.getTitle())
//                .setContentText("푸시 내용")
                .setContentText(bean_data.getMessage())
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify((int)System.currentTimeMillis(), notiBuilder.build());
    }
} // end of class
