package com.example.task_03_stopwatchapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView timerDisplay;
    private Button startButton, pauseButton, resetButton;

    private Handler handler = new Handler();
    private long startTime, timeInMilliseconds = 0L, timeSwapBuff = 0L, updateTime = 0L;
    private int seconds, minutes, milliseconds;
    private boolean isRunning = false;
    private Runnable updateTimer = new Runnable() {
        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updateTime = timeSwapBuff + timeInMilliseconds;
            seconds = (int) (updateTime / 1000);
            minutes = seconds / 60;
            seconds %= 60;
            milliseconds = (int) (updateTime % 1000);
            timerDisplay.setText(String.format("%02d:%02d:%03d", minutes, seconds, milliseconds));
            handler.postDelayed(this, 0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerDisplay = findViewById(R.id.timerDisplay);
        startButton = findViewById(R.id.startButton);
        pauseButton = findViewById(R.id.pauseButton);
        resetButton = findViewById(R.id.resetButton);

        startButton.setOnClickListener(v -> {
            if (!isRunning) {
                startTime = SystemClock.uptimeMillis();
                handler.post(updateTimer);
                isRunning = true;
            }
        });

        pauseButton.setOnClickListener(v -> {
            if (isRunning) {
                timeSwapBuff += timeInMilliseconds;
                handler.removeCallbacks(updateTimer);
                isRunning = false;
            }
        });

        resetButton.setOnClickListener(v -> {
            timeSwapBuff = 0L;
            timeInMilliseconds = 0L;
            updateTime = 0L;
            minutes = 0;
            seconds = 0;
            milliseconds = 0;
            timerDisplay.setText("00:00:000");
            handler.removeCallbacks(updateTimer);
            isRunning = false;
        });
    }
}
