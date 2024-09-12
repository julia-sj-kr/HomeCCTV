package com.example.homecctv;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

public class cctvActivity extends AppCompatActivity {

    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1;

    private MyHomeCCTV cctvView1,cctvView2,cctvView3,cctvView4;
    private LinearLayout fullLayout;
    private FrameLayout zoomLayout;
    private GridLayout gl;
    String selected_ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cctv_activity);

        cctvView1 = findViewById(R.id.videoView1);
        cctvView2 = findViewById(R.id.videoView2);
        cctvView3 = findViewById(R.id.videoView3);
        cctvView4 = findViewById(R.id.videoView4);

        gl=findViewById(R.id.gridLayout);
        fullLayout=findViewById(R.id.fullframe);
        zoomLayout=findViewById(R.id.zoomframe);

        //URI는 URL과 URN(Uniform Resource Name)을 포함합니다. 따라서 URI는 더 넓은 개념입니다.
        //예시 URL: https://www.example.com / URN: urn:isbn:0451450523 (특정 자원을 식별하는 이름)

        // 각 뷰에 사용할 URL 리스트
        String[] urls = {
                //원격 영상 링크
                "http://195.222.51.206:81/mjpg/video.mjpg",
                //USB 카메라에서 수신되는 ip 주소, 동일한 네트워크 상에서 출력이 가능하다.
                //외부에서 내부 네트워크로의 접근을 허용하고 싶다면 포트포워딩
                //ip 권한을 허용해주는 permission 추가해줘야함.(xml 디렉터리에서 파일 추가하여 도메인 주소 추가)
                "http://80.75.112.38:9080/axis-cgi/mjpg/video.cgi",
                "http://80.75.112.38:9080/axis-cgi/mjpg/video.cgi",
                "http://195.222.51.206:81/mjpg/video.mjpg",


//                "http://192.168.0.100:8000/camera/mjpeg",
//                "http://192.168.0.109:8000/camera/mjpeg",
//                "http://192.168.0.39:8000/camera/mjpeg",
                //"http://192.168.137.1:8000/camera/mjpeg",//무선 네트워크 주소
        };

        String[] ipAddress = {
                "192.168.0.100",//어댑터 ip...
                "192.168.137.1",//pc 핫스팟 ip...
                "192.168.0.109",//유석 어댑터 ip...
                "192.168.0.39",//4층-5G 와이파이 ip
        };

        //내 PC 핫스팟 연결했을때, 웹캡 영상 성공, 모터 조작 성공
        //4층 와이파이 연결했을때, 웹캡 영상 출력 실패, 모터 조작 성공

        //다른 PC 핫스팟 연결했을때,
        //어제 오류는 4층 와이파이 연결했을때는 리모컨 조작만 PC 핫스팟 연결했을때는 웹캠만 동작했음...

        cctvView1.setUrl(urls[0]);
        cctvView2.setUrl(urls[1]);
        cctvView3.setUrl(urls[2]);
        cctvView4.setUrl(urls[3]);

        // 비디오 클릭 리스너 설정
        cctvView1.setOnClickListener(view -> openFullscreenVideo(urls[0],ipAddress[0]));
        cctvView2.setOnClickListener(view -> openFullscreenVideo(urls[1],ipAddress[1]));
        cctvView3.setOnClickListener(view -> openFullscreenVideo(urls[2],ipAddress[2]));
        cctvView4.setOnClickListener(view -> openFullscreenVideo(urls[3],ipAddress[3]));

    }

    protected void onDestroy() {
        super.onDestroy();

        cctvView1.stopStreaming();
        cctvView2.stopStreaming();
        cctvView3.stopStreaming();
        cctvView4.stopStreaming();
    }

    //온클릭 함수, 음성 인식 활동 시작
    public void speakBtn(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");
        try {
            startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(this, "Speech recognition is not supported on your device.", Toast.LENGTH_SHORT).show();
        }
    }

    //음성 인식 활동 결과 처리
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            TextView msg = findViewById(R.id.speak_msg);
            msg.setText(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0));
            String command=msg.getText().toString();
            String ip=selected_ip;
            sendVoiceMsg(command, ip);
        }
    }

    public void upCmd(View view) {
        String command="Up";
        String ip=selected_ip;
        SendMsg(command, ip);
    }

    public void downCmd(View view) {
        String command="Down";
        String ip=selected_ip;
        SendMsg(command, ip);
    }

    public void leftCmd(View view) {
        String command="Left";
        String ip=selected_ip;
        SendMsg(command, ip);
    }

    public void rightCmd(View view) {
        String command="Right";
        String ip=selected_ip;
        SendMsg(command, ip);
    }

    //UDP통신: 버튼 명령은 자바 서버로 전송됨
    public void SendMsg(String command,String ip){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DatagramSocket ds=new DatagramSocket();
                    //InetAddress ia=InetAddress.getByName("192.168.0.100");
                    InetAddress ia=InetAddress.getByName(ip);

                    byte[] data=command.getBytes();
                    DatagramPacket dp=new DatagramPacket(data,data.length,ia,7777);
                    ds.send(dp);
                    ds.close();
                }catch (Exception e){
                    Log.d("UDPClient","Error: "+e.getMessage());
                }
            }
        }).start();
    }

    //UDP통신: 음성 명령은 챗GPT API를 활용한 파이썬에 전송된 후 아두이노에 필요한 명령어로 변환되어 자바 서버로 전송됨
    public void sendVoiceMsg(String msg,String ip){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DatagramSocket ds=new DatagramSocket();
                    //InetAddress ia=InetAddress.getByName("192.168.0.100");
                    InetAddress ia=InetAddress.getByName(ip);
                    DatagramPacket dp=new DatagramPacket(msg.getBytes(),msg.getBytes().length,ia,9999);
                    ds.send(dp);
                    ds.close();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void openFullscreenVideo(String url,String ip) {

        selected_ip=ip;

        gl.setVisibility(View.GONE);
        fullLayout.setVisibility(View.VISIBLE);

        // 기존의 zoomLayout에서 모든 뷰를 제거합니다.
        zoomLayout.removeAllViews();

        MyHomeCCTV fullscreenCCTVView = new MyHomeCCTV(this,null);
        fullscreenCCTVView.setUrl(url); // URL 설정

        fullscreenCCTVView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        zoomLayout.addView(fullscreenCCTVView); //새로운 비디오 추가

        // 클릭 시 전체화면 닫기 (여기에 종료 이벤트를 추가할 수 있음)
        zoomLayout.setOnClickListener(v -> {
            fullLayout.setVisibility(View.GONE);
            fullscreenCCTVView.stopStreaming(); // 비디오 정지
            gl.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void onBackPressed() {
        // zoomLayout이 보이는 경우, 전체화면을 종료하고 4개의 화면으로 돌아감
        if (fullLayout.getVisibility() == View.VISIBLE) {
            fullLayout.setVisibility(View.GONE);

            // zoomLayout에서 모든 뷰 제거
            zoomLayout.removeAllViews();

            gl.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed(); // 기본 동작 실행
        }
    }

}

