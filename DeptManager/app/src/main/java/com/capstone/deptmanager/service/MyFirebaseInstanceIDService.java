package com.capstone.deptmanager.service;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.capstone.deptmanager.util.PrefUtil;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

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

            if ((PrefUtil.getPreference(getApplicationContext(), PrefUtil.KET_USER_ID) != null)) updateTokenToServer();
        }
//        sendRegistrationToServer(refreshedToken);
//        내가 추후 정의를 통해서 우리서버의 회원테이블에 토큰값이 저장될 수 있도록 해주어야 한다.
    }

    public void updateTokenToServer() {

        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://" + "192.168.200.168" + ":8080/member/updateMemberProc.do", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d("MyLog", "response: " + response);

                String utf8Str = "";
                try {
                    utf8Str = new String(response.getBytes("ISO-8859-1"), "KSC5601");
//                    Log.d("MyLog", "response encoded: " + utf8Str);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if (utf8Str != null && !"".equals(utf8Str)) {
                    Log.d("MyLog", "utf8Str : " + utf8Str);
                    JsonParser parser = new JsonParser();
                    JsonObject rootObj = (JsonObject) parser.parse(utf8Str);
                    String result = rootObj.get("result").getAsString();

                    if ("success".equals(result)) {
                        Log.d("MyLog", "onTokenRefresh : 토큰 업데이트 성공");
                    } else {
                        Log.d("MyLog", "onTokenRefresh : 토큰 업데이트 실패");
                    }
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("MyLog", "error : " + error);
                final VolleyError err = error;

                Log.d("MyLog", err.toString()+"");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("memberId", PrefUtil.getPreference(getApplicationContext(), PrefUtil.KET_USER_ID));
                params.put("memberToken", PrefUtil.getPreference(getApplicationContext(), PrefUtil.KET_PUSH_TOKEN));
                return params;
            }
        };
        requestQueue.add(stringRequest);

    } // end of updateTokenToServer
} // end of class
