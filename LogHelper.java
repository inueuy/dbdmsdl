package com.example.myapplication;

import android.content.Context;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class LogHelper {

    private static final String FILE_NAME = "trash_log.txt";

    // 로그 저장
    public static void saveLog(Context context, String mode, String action) {
        try {
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                    .format(new Date());
            String line;
            if (mode.equals("MANUAL")) {
                line = "[MANUAL] " + date + "  " + action;
            } else {
                line = date + "  " + action;
            }

            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_APPEND);
            fos.write((line + "\n").getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 로그 불러오기
    public static List<String> loadLogs(Context context) {
        List<String> logs = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(context.openFileInput(FILE_NAME)));
            String line;
            while ((line = br.readLine()) != null) {
                logs.add(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.reverse(logs); // 최신 순으로
        return logs;
    }
}