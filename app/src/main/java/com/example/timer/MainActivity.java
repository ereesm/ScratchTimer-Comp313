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
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.content.Context;
import java.io.IOException;




import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;




import android.view.Menu;
import android.view.MenuItem;

//Trying to commit to bitbucket currently!
public class MainActivity extends AppCompatActivity {
    private Button startButton;         // Define startButton

    private Button stopButton;
    private Button incrementButton;
    private Button pauseButton;
    private TextView timerTextView;     // Define timerTextView
    private CountDownTimer countDownTimer;
    private long timeLeftInMilliseconds = 0; // initial val
    private boolean timerRunning;

    private int incrementCounter = 0;


    private AppBarConfiguration appBarConfiguration;

    private boolean isRingtonePlaying = false;
    private MediaPlayer mediaPlay;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = findViewById(R.id.start_button);
        pauseButton = findViewById(R.id.pause_button);
        timerTextView = findViewById(R.id.time_text_view);
        stopButton = findViewById(R.id.stop_button);
        incrementButton = findViewById(R.id.increment_button);


        //Event listeners for each button. Start, Stop, Pause, and increment.
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
                updateTimer();
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();
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

    //Reset timer method for abstraction. Just resets the time and increment count to 0. Used in StopTimer and onFinish
    private void resetTimer() {
        timeLeftInMilliseconds = 0;
        incrementCounter = 0;
        updateTimer();
    }

    //Method used once start button is pressed.
    private void startTimer() {
        //define a variable for an initial start time
        long initialTimeInMiliseconds;
        //if the timeLeftInMiliseconds is 0, the initial time is set as the incrementCount in seconds once the button is pressed. This is a check
        //on whether or not the timer is paused.
        //the else statement is used for that pause mechanism in case timeLeft is not 0.
        if(timeLeftInMilliseconds == 0){
            initialTimeInMiliseconds = incrementCounter *1000;
        }else{
            initialTimeInMiliseconds = timeLeftInMilliseconds;
        }

        //sets the increment counter to 0 once the timer starts so it does not
        //add the previous increment value when trying to start again.
        incrementCounter = 0;
        //Checks if the countDowntimer is null, and recreates/defines it.
        //Provides onTick which decrements the time and updates the timer.
        //Provides onFinish which resets the timer and plays audio once the time reaches 0.
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(initialTimeInMiliseconds, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeftInMilliseconds = millisUntilFinished;
                    updateTimer();
                }

                @Override
                public void onFinish() {
                    timerRunning = false;
                    countDownTimer = null;
                    resetTimer();
                    playRingtone(MainActivity.this);

                }
            };
        }
        countDownTimer.start();
        timerRunning = true;

    }

    //Used in the stopbutton listener.
    //if  the cuontdowntimer is not null, it will reset the timer completely.
    private void stopTimer() {
        resetTimer();
        if(countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        timerRunning = false;
        startButton.setText("Start");
    }

    //Used in the pause button listener
    // if the count down timer is not null, it stops the timer.
    //It updates timeLeft, subtracting it by the incremented time in seconds so the timer resumes properly.
    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            timeLeftInMilliseconds = timeLeftInMilliseconds - incrementCounter * 1000;
            countDownTimer = null;
            timerRunning = false;

        }
    }

    //Updates the timer display
    private void updateTimer() {
        int minutes = (int) timeLeftInMilliseconds / 60000;
        int seconds = (int) timeLeftInMilliseconds % 60000 / 1000;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timerTextView.setText(timeLeftFormatted);
    }



    //used to play a default ringtone and is used in onFinish.
    private void playRingtone(Context context) {
        if (mediaPlay !=null) {
            mediaPlay.stop();
            mediaPlay.release();
        }
            Uri defaultRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            MediaPlayer mediaPlayer = new MediaPlayer();

            try {
                mediaPlayer.setDataSource(context, defaultRingtoneUri);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
                mediaPlayer.prepare();

                mediaPlayer.start();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
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