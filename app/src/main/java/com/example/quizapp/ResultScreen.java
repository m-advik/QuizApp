package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ResultScreen extends AppCompatActivity {

//    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result_screen);

        TextView scoreText = findViewById(R.id.textView5);
        TextView accuracyText = findViewById(R.id.textView1);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        Button reAttempt = findViewById(R.id.button2);


        Intent intent = getIntent();
        int finalScore = intent.getIntExtra("SCORE", 0);
        String name = intent.getStringExtra("NAME");
        int accuracy = (finalScore*100)/5;
        int timeLeft = intent.getIntExtra("TIME_LEFT", 180);
        String diff = intent.getStringExtra("DIFFICULTY");



        scoreText.setText("Score: "+finalScore+"/5");
        accuracyText.setText(name+" your Accuracy was "+accuracy+"% with "+diff.toUpperCase()+" difficulty in "+(270-timeLeft)+" seconds");
        for(int i =0; i <= accuracy; i++){

            progressBar.setProgress(accuracy);
        }

        reAttempt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultScreen.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
    }
}