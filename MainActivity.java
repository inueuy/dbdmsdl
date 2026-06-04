package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TrashCanView trashCanView;
    TextView tvPercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trashCanView = findViewById(R.id.trashCanView);
        tvPercent = findViewById(R.id.tvPercent);

        // 테스트용: 50%로 설정
        setTrashLevel(50);

        // ⋮ 메뉴 버튼
        ImageButton btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MenuActivity.class));
            }
        });
    }

    public void setTrashLevel(int percent) {
        trashCanView.setFillPercent(percent);
        tvPercent.setText(percent + "%");
    }
}