package com.example.MyUDPClient_testAPP;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {

    EditText msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        msg=findViewById(R.id.msg);
        Button send=findViewById(R.id.send);

    }

    public void send(View view) {
        String command=msg.getText().toString();
        msg.setText("");
        Log.d("UDPClient","Send: "+command);
        SendMsg(command);
    }

    public void upCmd(View view) {
        String command="U";
        SendMsg(command);
    }

    public void downCmd(View view) {
        String command="D";
        SendMsg(command);
    }

    public void SendMsg(String command){
        new Thread(new Runnable() {
            @Override
            public void run() {
           try {
               DatagramSocket ds=new DatagramSocket();
               InetAddress ia=InetAddress.getByName("192.168.0.39");
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



}