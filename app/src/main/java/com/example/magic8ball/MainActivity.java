package com.example.magic8ball;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Vibrator myVib;
    private SensorManager sensorManager;
    public SensorEventListener listener;
    Sensor gyroscopeReading;


    void makePrediction(TextView prediction){
        String[] predictions = {"It is certain", "It is decidedly so", "Without a doubt",
                "Yes definitely", "You may rely on it", "As I see it yes",
                "Most likely", "Outlook good", "Yes", "Signs point to yes",
                "Reply hazy try again", "Ask again later", "Better not tell you now",
                "Cannot predict now", "Concentrate and ask again", "Don't count on it",
                "My reply is no", "My sources say no", "Outlook not so good", "Very doubtful"};

        //Randomly pick a prediction
        int randomNum = (int) (20.0 * Math.random());
        prediction.setText(predictions[randomNum]);

        //Fade-in animation
        Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        prediction.startAnimation(animFadeIn);

        //Haptic feedback
        myVib.vibrate(50);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView prediction = (TextView) findViewById(R.id.pred);
        final Button predButton = (Button) findViewById(R.id.button);
        predButton.setBackgroundColor(Color.TRANSPARENT);
        myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);


        // Touch Feedback
        predButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePrediction(prediction);
            }
        });


        // Orientation Feedback
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroscopeReading = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                Log.d("Debug", sensorEvent.values[0] + "");
                if(sensorEvent.values[0] < -5){
                    makePrediction(prediction);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        sensorManager.registerListener(listener, gyroscopeReading, SensorManager.SENSOR_DELAY_UI);

    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(listener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(listener, gyroscopeReading, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(listener);
    }
}


