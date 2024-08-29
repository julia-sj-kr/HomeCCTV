package com.example.homecctv;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

public class cctvActivity extends AppCompatActivity {

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
                "http://220.233.144.165:8888/mjpg/video.mjpg",
                //USB 카메라에서 수신되는 ip 주소, 동일한 네트워크 상에서 출력이 가능하다.
                //외부에서 내부 네트워크로의 접근을 허용하고 싶다면 포트포워딩
                //ip 권한을 허용해주는 permission 추가해줘야함.(xml 디렉터리에서 파일 추가하여 도메인 주소 추가)
                "http://192.168.0.100:8000/camera/mjpeg",
                "http://192.168.0.109:8000/camera/mjpeg",
                "http://192.168.0.39:8000/camera/mjpeg",
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

    public void upCmd(View view) {
        String command="U";
        String ip=selected_ip;
        SendMsg(command, ip);
    }

    public void downCmd(View view) {
        String command="D";
        String ip=selected_ip;
        SendMsg(command, ip);
    }

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
}

    class MyHomeCCTV extends SurfaceView implements SurfaceHolder.Callback, Runnable {
        Thread threadSView;
        boolean threadRunning = true;
        String url;

        public void setUrl(String url) {

            this.url = url;
        }

        public MyHomeCCTV(Context context, AttributeSet attrs) {
            super(context, attrs);
            getHolder().addCallback(this);
            threadSView = new Thread(this);
        }

        @Override
        public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {

            threadSView.start();
        }

        @Override
        public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        }

        @Override
        public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
            threadRunning = false;
            try {
                threadSView.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void stopStreaming() {
            threadRunning = false;
            try {
                threadSView.join();
            } catch (InterruptedException e) {
                Log.e("MyHomeCCTV", "Thread interruption error: " + e.getMessage());
            }
        }

        @Override
        public void run() {
            final int maxImgSize = 1000000;//1MG 바이트를 메모리에 할당

            byte[] arr = new byte[maxImgSize];//이미지 크기가 클수록 바이트 수가 많이 필요

            try {
                //URL url = new URL("http://220.233.144.165:8888/mjpg/video.mjpg");
                URL streamUrl = new URL(url);
                HttpURLConnection con = (HttpURLConnection) streamUrl.openConnection();//주소 옮겨주고
                InputStream in = con.getInputStream();//통신 시작, 네트워크 카드쪽 읽는거
                while (threadRunning) {
                    int i = 0;
                    for (; i < 1000; i++) {
                        int b = in.read();//한 바이트를 읽어
                        if (b == 0xff) {//Start OF Image의 시작 바이트
                            int b2 = in.read();//한 바이트를 더 읽어
                            if (b2 == 0xd8)//Start OF Image의 끝 바이트
                                break;
                        }
                    }
                    if (i > 999) {
                        Log.e("MyHomeCCTV", "Bad head");
                        continue;//=>하면 다시 while문 첫 줄로 감, 천장씩 잘라서 읽는 결과
                    }
                    arr[0] = (byte) 0xff;
                    arr[1] = (byte) 0xd8;
                    i = 2;
                    for (; i < maxImgSize; i++) {
                        int b = in.read();
                        arr[i] = (byte) b;
                        if (b == 0xff) {//End OF Image의 시작 바이트
                            i++;
                            int b2 = in.read();
                            arr[i] = (byte) b2;
                            if (b2 == 0xd9) {//End OF Image의 끝 바이트
                                break;
                            }
                        }
                    }
                    i++;
                    int nBytes = i;
                    Log.e("MyHomeCCTV", "got an image, " + nBytes + "bytes!");

                    Bitmap bitmap = BitmapFactory.decodeByteArray(arr, 0, nBytes);
                    bitmap = Bitmap.createScaledBitmap(bitmap, getWidth(), getHeight(), true);//수신되는 이미지를 뷰 크기에 맞춰주기

                    SurfaceHolder holder = getHolder();
                    Canvas canvas = null;
                    canvas = holder.lockCanvas();
                    if (canvas != null) {
                        canvas.drawColor(Color.TRANSPARENT);
                        canvas.drawBitmap(bitmap, 0, 0, null);
                        holder.unlockCanvasAndPost(canvas);
                    }
                }

            } catch (Exception e) {
                Log.e("MyHomeCCTV", "Error:" + e.toString());
            }
        }
    }
