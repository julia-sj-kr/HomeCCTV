package com.example.homecctv;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private View v;
    Button cctvButton;
    Button lightButton;
    Button temperButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);

        cctvButton=findViewById(R.id.cctv_btn);
        lightButton=findViewById(R.id.light_btn);
        temperButton=findViewById(R.id.temper_btn);
        Log.d("test","oncreate");
    }

    public void cctvbtnclicked(View v){
        Log.d("test","cctvbtnclick");

        Intent intent = new Intent(this, cctvActivity.class); // 전환할 액티비티
        startActivity(intent);
    }

    protected void onResume() {
        super.onResume();
        Log.d("test", "cctvButton enabled: " + cctvButton.isEnabled());
        // 버튼 상태를 여기에서 초기화할 수 있습니다.
        // cctvButton이 활성화되어 있는지 확인합니다.
        cctvButton.setEnabled(true); // 버튼을 활성화합니다.
    }
}
