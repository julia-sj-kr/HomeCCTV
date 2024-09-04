package com.example.homecctv;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private View v;
    Button cctvButton;
    Button lightButton;
    Button etcButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);

        cctvButton=findViewById(R.id.cctv_btn);
        lightButton=findViewById(R.id.light_btn);
        etcButton=findViewById(R.id.etc_btn);
        Log.d("test","oncreate");

        //권한 요청
        requestPermissions(new String[]{android.Manifest.permission.RECORD_AUDIO}, 0);
    }

    //권한 요청 결과 처리
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "RECORD_AUDIO 퍼미션이 허용되었습니다.", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "RECORD_AUDIO 퍼미션이 거부되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public void cctvbtnclicked(View v){

        Intent intent = new Intent(this, cctvActivity.class);
        startActivity(intent);
    }

    public void lightbtnclicked(View v){

        Intent intent = new Intent(this, LightActivity.class);
        startActivity(intent);
    }

    public void callbtnclicked(View v){

        Intent intent = new Intent(this, VoiceTalkActivity.class);
        startActivity(intent);
    }

    protected void onResume() {
        super.onResume();
        Log.d("test", "cctvButton enabled: " + cctvButton.isEnabled());
        // 버튼 상태를 여기에서 초기화할 수 있습니다.
        // cctvButton이 활성화되어 있는지 확인합니다.
        //cctvButton.setEnabled(true); // 버튼을 활성화합니다.
    }
}

