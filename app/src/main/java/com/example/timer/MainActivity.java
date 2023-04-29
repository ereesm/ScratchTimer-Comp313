package com.example.timer;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;


import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.timer.databinding.ActivityMainBinding;


import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private Button startButton;         // Define startButton

    private Button stopButton;
    private Button incrementButton;
    private TextView timerTextView;     // Define timerTextView
    private CountDownTimer countDownTimer;
    private long timeLeftInMilliseconds = 0; // initial val
    private boolean timerRunning;

    private int incrementCounter = 0;


    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = findViewById(R.id.start_button);
        timerTextView = findViewById(R.id.time_text_view);
        stopButton = findViewById(R.id.stop_button);
        incrementButton = findViewById(R.id.increment_button);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerRunning) {
                    stopTimer();
                } else {
                    startTimer();
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
                timeLeftInMilliseconds = 0;
                updateTimer();
            }
        });

        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (incrementCounter < 99) {
                    incrementCounter++;
                    timerTextView.setText(String.format(Locale.getDefault(), "%02d", incrementCounter));
                }
            }
        });
    }






    private void startTimer() {
        timeLeftInMilliseconds = incrementCounter * 1000;
        incrementCounter = 0;
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(timeLeftInMilliseconds, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeftInMilliseconds = millisUntilFinished;
                    updateTimer();
                }

                @Override
                public void onFinish() {
                    timerRunning = false;
                    updateButtons();
                }
            };
        }
        countDownTimer.start();
        timerRunning = true;
        updateButtons();
    }

    private void updateButtons() {
        if (timerRunning) {
            startButton.setText("Pause");
            incrementButton.setEnabled(true);
            stopButton.setEnabled(true);
        } else {
            startButton.setText("Start");
            incrementButton.setEnabled(false);
            stopButton.setEnabled(false);
        }
    }




    private void stopTimer() {
        countDownTimer.cancel();
        timerRunning = false;
        startButton.setText("Start");
    }

    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
            timerRunning = false;
            updateButtons();
        }
    }

    private void updateTimer() {
        int minutes = (int) timeLeftInMilliseconds / 60000;
        int seconds = (int) timeLeftInMilliseconds % 60000 / 1000;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timerTextView.setText(timeLeftFormatted);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}