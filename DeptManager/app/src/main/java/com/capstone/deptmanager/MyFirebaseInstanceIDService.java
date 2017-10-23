package com.capstone.deptmanager;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Daehee on 2017-05-24.
 * 등록 토큰 생성, 순환, 업데이트를 처리하기 위해 FirebaseInstanceIdService를 확장하는 서비스를 추가합니다.
 * 특정 기기로 전송하거나 기기 그룹을 만드는 경우에 필요합니다.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh(); // 내용 없음. 삭제 무관
        // 단말기의 고유 ID = 토큰 을 발급 받는다.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken(); // 토큰을 발급받는다.
        Log.d("onTokenRefresh", refreshedToken);

        if (refreshedToken != null && refreshedToken.length() > 0) {
            PrefUtil.setPreference(getApplicationContext(), PrefUtil.KET_PUSH_TOKEN, refreshedToken);
        }
//        sendRegistrationToServer(refreshedToken);
//        내가 추후 정의를 통해서 우리서버의 회원테이블에 토큰값이 저장될 수 있도록 해주어야 한다.
    }
} // end of class
