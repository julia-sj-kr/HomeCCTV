package com.example.homecctv;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        databaseHelper = new DatabaseHelper(this);

        Button showRegisterDialog = findViewById(R.id.showDialogButton);
        showRegisterDialog.setOnClickListener(v -> showRegisterDialog());

        Button loginbtnclicked = findViewById(R.id.loginbtn);
        loginbtnclicked.setOnClickListener(v -> loginUser());
    }

    private void showRegisterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("회원가입");

        // Inflate the custom layout
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_register, null);
        builder.setView(dialogView);

        EditText editTextID = dialogView.findViewById(R.id.editTextNewID);
        EditText editTextPassword = dialogView.findViewById(R.id.editTextNewPassword);

        builder.setPositiveButton("완료", (dialog, which) -> {
            String id = editTextID.getText().toString();
            String password = editTextPassword.getText().toString();

            // 데이터베이스에 사용자 정보 저장
            if (databaseHelper.insertUser(id, password)) {
                Toast.makeText(getApplicationContext(), "회원가입 성공!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "회원가입 실패! 같은 아이디가 존재합니다.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("취소", (dialog, which) -> dialog.cancel());

        AlertDialog dialog = builder.create();

        // 대화상자 외부를 클릭하여 닫히지 않도록 대화상자를 모달 형식으로 설정
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.show();
    }

    private void loginUser() {
        EditText editTextID = findViewById(R.id.editTextID); // ID 입력 필드
        EditText editTextPassword = findViewById(R.id.editTextpassword); // Password 입력 필드

        String id = editTextID.getText().toString();
        String password = editTextPassword.getText().toString();

        // 데이터베이스에서 사용자 인증
        if (databaseHelper.checkUser(id, password)) {
            // 로그인 성공, 새로운 액티비티로 전환
            Intent intent = new Intent(LoginActivity.this, MainActivity.class); // 전환할 액티비티
            startActivity(intent);
            finish(); // 현재 액티비티 종료
        } else {
            // 로그인 실패
            Toast.makeText(this, "로그인 실패! 아이디 또는 패스워드가 잘못되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}