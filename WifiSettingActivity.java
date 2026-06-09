package com.example.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.wifi.WifiNetworkSpecifier;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WifiSettingActivity extends AppCompatActivity {

    private LinearLayout layoutInputForm, layoutConnecting;
    private EditText etTargetSsid, etTargetPassword;
    private Button btnConnectWifi, btnCancelConnect;
    private TextView tvConnectStatus;

    // 네트워킹을 위한 백그라운드 스레드 및 핸들러
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    // 안드로이드 시스템 와이파이 연결 매니저 및 콜백
    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallback;

    // 쓰레기통이 초기 AP(설정) 모드일 때의 가상 IP 주소
    private final String esp32ApIp = "192.168.4.1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        // 상단 보라색 액션바 숨기기 (디자인 일체감 유지)
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // UI 컴포넌트 연결
        ImageButton btnBack = findViewById(R.id.btnBack);
        layoutInputForm = findViewById(R.id.layoutInputForm);
        layoutConnecting = findViewById(R.id.layoutConnecting);
        etTargetSsid = findViewById(R.id.etTargetSsid);
        etTargetPassword = findViewById(R.id.etTargetPassword);
        btnConnectWifi = findViewById(R.id.btnConnectWifi);
        btnCancelConnect = findViewById(R.id.btnCancelConnect);
        tvConnectStatus = findViewById(R.id.tvConnectStatus);

        // 뒤로가기 버튼 → 닫기
        btnBack.setOnClickListener(v -> finish());

        // X → 첫 화면(메인)으로
        ImageButton btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(v -> {
            Intent intent = new Intent(WifiSettingActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        // CONNECT 버튼 → 기기 등록 시작
        btnConnectWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ssid = etTargetSsid.getText().toString().trim();
                String password = etTargetPassword.getText().toString().trim();

                // 불필요한 etEsp32Ip 입력창 연결 및 SharedPreferences 저장 로직 완전 제거!
                if (ssid.isEmpty()) {
                    Toast.makeText(WifiSettingActivity.this, "Please enter Wi-Fi name.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 와이파이 전송 루틴 바로 실행
                startProvisioning(ssid, password);
            }
        });

        // CANCEL 버튼 → 연결 도중 취소 및 복귀
        btnCancelConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelProvisioning();
            }
        });
    }

    /**
     * 1단계: 화면을 '연결 중' 상태로 바꾸고 쓰레기통 와이파이로 자동 접속 시도
     */
    private void startProvisioning(final String ssid, final String password) {
        layoutInputForm.setVisibility(View.GONE);
        btnConnectWifi.setVisibility(View.GONE);
        layoutConnecting.setVisibility(View.VISIBLE);
        btnCancelConnect.setVisibility(View.VISIBLE);
        tvConnectStatus.setText("Connecting to Trash Can Hotspot...");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            WifiNetworkSpecifier specifier = new WifiNetworkSpecifier.Builder()
                    .setSsid("Smart_TrashCan_Setup")
                    .setWpa2Passphrase("12345678") // 아두이노가 생성한 임시 와이파이 비밀번호
                    .build();

            NetworkRequest request = new NetworkRequest.Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .setNetworkSpecifier(specifier)
                    .build();

            networkCallback = new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(@NonNull Network network) {
                    super.onAvailable(network);
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            tvConnectStatus.setText("Sending Wi-Fi credentials...");
                        }
                    });

                    // 2단계: 연결된 통로(Network)를 통해 아두이노에게 와이파이 정보 주입
                    sendDataToEsp32(network, ssid, password);
                }

                @Override
                public void onUnavailable() {
                    super.onUnavailable();
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            showErrorDialog("Failed to find Trash Can hotspot.\nPlease check if the device is powered on.");
                        }
                    });
                }
            };

            connectivityManager.requestNetwork(request, networkCallback);

        } else {
            Toast.makeText(this, "This Android version is not supported.", Toast.LENGTH_SHORT).show();
            resetUi();
        }
    }

    /**
     * 2단계: 쓰레기통 와이파이 통로를 통해 실제 공유기 정보(SSID, 비번)를 HTTP로 전송
     */
    private void sendDataToEsp32(final Network network, final String targetSsid, final String targetPassword) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String urlString = "http://" + esp32ApIp + "/setup?ssid=" + targetSsid + "&pass=" + targetPassword;
                    URL url = new URL(urlString);

                    HttpURLConnection connection = (HttpURLConnection) network.openConnection(url);
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            response.append(line);
                        }
                        br.close();

                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                showSuccessDialog();
                            }
                        });
                    } else {
                        throw new Exception("Server response error");
                    }
                    connection.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            showErrorDialog("Failed to send Wi-Fi details.\nPlease try again.");
                        }
                    });
                } finally {
                    stopNetworkCallback();
                }
            }
        });
    }

    private void cancelProvisioning() {
        stopNetworkCallback();
        resetUi();
        Toast.makeText(this, "Provisioning canceled.", Toast.LENGTH_SHORT).show();
    }

    /**
     * 완료 알림 팝업창 (영문 UI 톤앤매너 매칭)
     */
    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Registration Complete");
        builder.setMessage("Wi-Fi details successfully sent to your trash can!\n\nThe device will now connect to the network. You can monitor the status on the main dashboard.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(WifiSettingActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    /**
     * 에러 알림 팝업창 (오타 수정 및 영문 통일)
     */
    private void showErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Connection Failed");
        builder.setMessage(message);
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() { // 🌟 오타 완벽 교정 완료!
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resetUi();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void resetUi() {
        layoutInputForm.setVisibility(View.VISIBLE);
        btnConnectWifi.setVisibility(View.VISIBLE);
        layoutConnecting.setVisibility(View.GONE);
        btnCancelConnect.setVisibility(View.GONE);
    }

    private void stopNetworkCallback() {
        if (connectivityManager != null && networkCallback != null) {
            try {
                connectivityManager.unregisterNetworkCallback(networkCallback);
            } catch (Exception e) {
                e.printStackTrace();
            }
            networkCallback = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopNetworkCallback();
    }
}