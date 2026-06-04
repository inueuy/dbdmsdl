package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AutoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto);

        // 뒤로가기 → 메뉴 화면
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // X → 첫 화면(메인)으로
        ImageButton btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(v -> {
            Intent intent = new Intent(AutoActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        Switch switchManual = findViewById(R.id.switchManual);
        Switch switchOpenLid = findViewById(R.id.switchOpenLid);
        TextView tvOpenLid = findViewById(R.id.tvOpenLid);

        // Manual Mode 토글
        switchManual.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Manual Mode ON → Open Lid 활성화
                    switchOpenLid.setEnabled(true);
                    tvOpenLid.setTextColor(getColor(android.R.color.black));
                } else {
                    // Manual Mode OFF → Open Lid 비활성화
                    switchOpenLid.setEnabled(false);
                    switchOpenLid.setChecked(false);
                    tvOpenLid.setTextColor(getColor(android.R.color.darker_gray));
                }
            }
        });

        // Open Lid 토글 (나중에 Wi-Fi 통신 코드 여기에)
        switchOpenLid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    LogHelper.saveLog(AutoActivity.this, "MANUAL", "Lid Opened");
                } else {
                    LogHelper.saveLog(AutoActivity.this, "MANUAL", "Lid Closed");
                }
            }
        });
    }
}