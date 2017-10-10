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

public class MainActivity extends AppCompatActivity {

    private WebView wv;
    private Toast t;
    private JSInterface mJSInterface;

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
        String address = "http://192.168.1.41:8080/member/loginMemberForm.do";
        wv.loadUrl(address); // 해당 주소로 사이트 연결
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
                view.loadUrl("javascript:(function() { "
//                        + "var x= $('.main-header>a').css('display');"
//                        + "window.mJSInterface.setSmile(x);"
                        + "})()");
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

    class JSInterface {
        @JavascriptInterface
        public void setSmile(String msg) {
            showToast(msg);
        }
    } // end of inner class

} // end of class
