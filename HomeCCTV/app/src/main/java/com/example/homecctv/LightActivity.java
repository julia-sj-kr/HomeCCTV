package com.example.homecctv;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class LightActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.light_activity);

        ToggleButton toggleButton1 = findViewById(R.id.toggleButton1);
        ToggleButton toggleButton2 = findViewById(R.id.toggleButton2);
        ToggleButton toggleButton3 = findViewById(R.id.toggleButton3);
        ToggleButton toggleButton4 = findViewById(R.id.toggleButton4);
        ToggleButton toggleButton5 = findViewById(R.id.toggleButton5);
        ToggleButton toggleButton6 = findViewById(R.id.toggleButton6);

        // toggleButton5와 toggleButton6의 초기 가시성 설정
        toggleButton5.setVisibility(View.INVISIBLE);
        toggleButton6.setVisibility(View.INVISIBLE);

        // 각 ToggleButton에 클릭 리스너 추가
        toggleButton1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sendCommand(isChecked ? "ONLED1" : "OFFLED1"); // LED1 제어
        });
        toggleButton2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sendCommand(isChecked ? "ONLED2" : "OFFLED2"); // LED2 제어
        });
        toggleButton3.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sendCommand(isChecked ? "ONLED3" : "OFFLED3"); // LED3 제어
        });
        toggleButton4.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sendCommand(isChecked ? "ONLED4" : "OFFLED4"); // LED4 제어
        });
    }

    //UDP통신: 버튼 명령은 자바 서버로 전송됨
    public void sendCommand(String command){


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DatagramSocket ds=new DatagramSocket();
                    InetAddress ia=InetAddress.getByName("192.168.0.100");

                    //UDP 통신에서 데이터를 전송할 때는 패킷을 사용하며, 패킷 데이터는 기본적으로 바이트 배열 형태로 전송되어야 함.
                    //이는 UDP가 낮은 수준의 네트워크 프로토콜로, 데이터를 바이트 스트림으로 처리하기 때문
                    //char 타입 데이터는 바이트 배열로 변환할 때 문제가 있을 수 있음. String으로 변환한 뒤 이를 바이트 배열로 변환해야한다.
                    //(기존 char 타입 데이터를 string 타입으로 수정(아두이노 코드 포함)하여 해결 완료)
                    byte[] data = command.getBytes();
                    DatagramPacket dp=new DatagramPacket(data,data.length,ia,7777);
                    ds.send(dp);
                    ds.close();
                }catch (Exception e){
                    Log.d("UDPClient","Error: "+e.getMessage());
                }
            }
        }).start();
    }
}

