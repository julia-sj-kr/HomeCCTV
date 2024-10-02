package com.example.homecctv;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.Handler;

public class MyHomeCCTV extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    Thread threadSView;
    boolean threadRunning = true;
    String url;
    private Handler uiHandler; // Handler 선언

    public void setUrl(String url) {

        this.url = url;
    }

    public MyHomeCCTV(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        threadSView = new Thread(this);

        // Handler 초기화
        uiHandler = new Handler(Looper.getMainLooper()); // UI 스레드와 연동
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
            Log.v("ANR_TEST", "Attempting to join thread...");
            threadSView.join();
            Log.v("ANR_TEST", "Thread successfully joined.");

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

            con.setConnectTimeout(2000); // 2초 연결 타임아웃 설정
            con.setReadTimeout(2000); // 2초 읽기 타임아웃 설정

            Log.v("ANR_TEST", "Connecting to the CCTV stream at: " + url);

            InputStream in = con.getInputStream();//통신 시작, 네트워크 카드쪽 읽는거

            Log.v("ANR_TEST", "Successfully connected to the CCTV stream at: " + url);

            while (threadRunning) {
                int i = 0;
                for (; i < 1000; i++) {
                    int b = in.read();//한 바이트를 읽어
                    if (b == 0xff) {//Start OF Image의 시작 바이트
                        int b2 = in.read();//한 바이트를 더 읽어
                        if (i % 100 == 0 && threadRunning == false) {
                            in.close();
                            return;
                        }
                        if (b2 == 0xd8)//Start OF Image의 끝 바이트
                            break;
                    }
                }
                if (i > 999) {
                    Log.e("MyHomeCCTV", "Invalid image header detected.");
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
                        if (i % 100 == 0 && threadRunning == false) {
                            in.close();
                            return;
                        }
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
            in.close();

        } catch (IOException e) {
            Log.e("ANR_TEST", "IO Error: " + e.toString());
            showToast("영상 수신 실패: " + e.getMessage());
        } catch (Exception e) {
            Log.e("ANR_TEST", "Error: " + e.toString());
            showToast("영상 수신 중 오류 발생: " + e.getMessage());
        }
    }

    // UI 스레드에서 Toast 메시지를 표시하기 위한 메서드
    private void showToast(final String message) {
    // Handler를 사용하여 UI 스레드에서 Toast 표시
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }
}