package com.capstone.deptmanager;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private WebView wv;
    private Toast t;
    private JSInterface mJSInterface;
    private boolean isAutoLogin = false;
    private boolean firstAccessToLogin = false;
    private String ip = "192.168.200.168";
    private String id = "";
    private String pw = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        wv = (WebView) findViewById(R.id.webView1);

        /**
         * http://{IP 주소}:{포트}/{이하 원하는 Url}
         */

        // 메인화면
        String address = "http://" + ip + ":8080/index.do";

        // 화면 분기
        String prefStr = PrefUtil.getPreference(getApplicationContext(), PrefUtil.KET_USER_ID);
        if (prefStr != null && !"".equals(prefStr)) {
            // SharedPref ID 값이 있다면 (자동 로그인이라면)
            Log.d("MyLog", "ID: " + PrefUtil.getPreference(getApplicationContext(), PrefUtil.KET_USER_ID));
            Log.d("MyLog", "PW: " + PrefUtil.getPreference(getApplicationContext(), PrefUtil.KET_USER_PW));
            try {
                Log.d("MyLog", "autoLoginMemberForm.do");
                String memberId = PrefUtil.getPreference(getApplicationContext(), PrefUtil.KET_USER_ID);
                String memberPw = PrefUtil.getPreference(getApplicationContext(), PrefUtil.KET_USER_PW);
                String data = "memberId=" + URLEncoder.encode(memberId, "UTF-8")
                        + "&memberPw=" + URLEncoder.encode(memberPw, "UTF-8");
                wv.postUrl("http://" + ip + ":8080/member/autoLoginMemberForm.do", data.getBytes());

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Log.d("MyLog", "login.do");
            wv.loadUrl(address); // 자동 로그인 아니라면 디펄트 화면으로 분기 (로그인 화면)
        }
         // 해당 주소로 사이트 연결
        // 안드로이드에서는 기본적으로 WebView 로 사이트 이동 시도시
        // 기본 브라우저를 통해서 접속하도록 유도하고있다.

        //time = System.currentTimeMillis();
        // 웹뷰 셋팅
        WebSettings setting = wv.getSettings();
        setting.setLoadWithOverviewMode(true); // 웹뷰에서 페이지가 확대되는 문제 해결
        setting.setUseWideViewPort(true);
        setting.setJavaScriptEnabled(true);
        wv.setInitialScale(1); // 기기별 화면사이트에 맞게 조절

        mJSInterface = new JSInterface();
        wv.addJavascriptInterface(mJSInterface, "mJSInterface");
        //setting.setJavaScriptCanOpenWindowsAutomatically(true);
        //자바스크립트가 window.open() 사용할 수 있도록 설정
        //setting.setPluginState(WebSettings.PluginState.ON_DEMAND); // 플러그인 사용
        //setting.setSupportZoom(true); // 줌 제스처 가능
        //setting.setBuiltInZoomControls(true); // 줌인 줌아웃 사용버튼

        wv.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                // super.onPageFinished(view, url); 부모메서드 들어가보면 하는 것 없음 지워도됨
                //float elapsedTime = (System.currentTimeMillis() - time)/1000f;
                //Toast.makeText(getApplicationContext(), "onPageFinished", Toast.LENGTH_SHORT).show();

//                view.loadUrl("javascript:(function() { "
////                        + "var x= $('.main-header>a').css('display');"
////                        + "window.mJSInterface.setSmile(x);"
//                        + "})()");
                if (url.contains("index.do")) {

                    // index.do 화면 최초진입인지를 확인한다 => 최초진입일시 이전화면에서 자동로그인 선택여부에 따라 pref 에 저장한다.
                    if (firstAccessToLogin == false) {
                        firstAccessToLogin = true;
                        //Toast.makeText(getApplicationContext(), "index.do 최초 진입입", Toast.LENGTH_SHORT).show();
                        if (isAutoLogin) {
                            Toast.makeText(getApplicationContext(), "isAutoLogin", Toast.LENGTH_SHORT).show();

                            // 로그인 화면 이후의 첫 index 화면일때 + 로그인화면에서 자동로그인 선택하였을때
                            // -> pref 아이디 저장 및 서버로 아이디와 토큰을 보내 수정

                            // 토큰 전송 로직
                           //
                            saveLoginInfo();
                            updateTokenToServer();

                        } else {
                            Toast.makeText(getApplicationContext(), "notAutoLogin", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                // super.onReceivedError(view, errorCode, description, failingUrl); 지워도됨
                // 에러 발생시
                Toast.makeText(getApplicationContext(), "onReceivedError", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onScaleChanged(WebView view, float oldScale, float newScale) {
                // super.onScaleChanged(view, oldScale, newScale); 지워도됨
                // 화면 scale 변경시
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // return super.shouldOverrideUrlLoading(view, url);
                // 새로운 URL 불러올때
                return false;
            }
        }); // 현재화면의 WebVew 에서 사이트 이동하게 하는 코드

        String refreshedToken = FirebaseInstanceId.getInstance().getToken(); // 토큰을 발급받는다.
        Log.e("MyLog", "토큰 : " + refreshedToken);


    } // end of onCreate

    public void showToast(String msg) {
        if (t != null) t.cancel();
        t = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        t.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (t != null) t.cancel();
    }

    @Override
    public void onBackPressed() {
        String str = wv.getUrl();
        if (str.contains("index.do")) {
            super.onBackPressed();
        } else if (str.contains("login")) {
            super.onBackPressed();
        } else {
            wv.goBack();
        }
    }

    public void updateTokenToServer() {

        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://" + ip + ":8080/member/updateMemberProc.do", new Response.Listener<String>() {

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
                        showToast("토큰 업데이트 성공");
                    } else {
                        showToast("토큰 업데이트 실패");
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
                params.put("memberId", id);
                params.put("memberToken", PrefUtil.getPreference(getApplicationContext(), PrefUtil.KET_PUSH_TOKEN));
                return params;
            }
        };
        requestQueue.add(stringRequest);

    } // end of updateTokenToServer

    class JSInterface {
        @JavascriptInterface
        public void setSmile(String msg) {
            showToast(msg);
        }

        @JavascriptInterface
        public void setAutoLogin(String paramId, String paramPw) {
            isAutoLogin = !isAutoLogin;

            id = paramId;
            pw = paramPw;
        };

        // 웹뷰로 부터 넘어온 로그인 정보를 로컬변수에 저장 (추후 로그인 성공여부에 따라 SharedPreference 저장 여부가 결정됨
        @JavascriptInterface
        public void setLoginInfo(String paramId, String paramPw) {
            isAutoLogin = true;
            id = paramId;
            pw = paramPw;
        } // end of setLoginInfo

        @JavascriptInterface
        public void doLogOut() {
            doLogOutNative();
        }
    } // end of inner class


    // 로그인 정보 저장 함수. JSInterface 내 setLoginInfo 메서드와 혼동 주의
    public void saveLoginInfo() {
        PrefUtil.setPreference(getApplicationContext(), PrefUtil.KET_USER_ID, id);
        PrefUtil.setPreference(getApplicationContext(), PrefUtil.KET_USER_PW, pw);
    } // end of saveLoginInfo

    public void doLogOutNative() {
        Log.d("MyLog", "doLogOutNative()");
        PrefUtil.rmPreference(getApplicationContext(), PrefUtil.KET_USER_ID);
        PrefUtil.rmPreference(getApplicationContext(), PrefUtil.KET_USER_PW);
    } // doLogOutNative

    @Override
    protected void onStop() {
        // TODO 세션 해제 작업 요망
        wv.loadUrl("http://" + ip + ":8080/member/logoutMemberProc.do");
        super.onStop();
    }
} // end of class
