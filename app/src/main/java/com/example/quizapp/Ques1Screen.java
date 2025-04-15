package com.example.quizapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class Ques1Screen extends AppCompatActivity {
    Handler handler;
    Runnable runnable;
    int[] timeLeft = {270};
//    int[] score = {0};
    String correctAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ques1_screen);

        TextView textView1 = findViewById(R.id.ques1View);
        TextView timeText = findViewById(R.id.timer);
        Button nextButton = findViewById(R.id.ques1Next);
        RadioGroup radioGroup = findViewById(R.id.ques1RG);
//        String name;

        Intent intent = getIntent();

        String difficulty = intent.getStringExtra("DIFFICULTY");
        int carriedScore = getIntent().getIntExtra("SCORE", 0);
        final int[] score = {carriedScore};
//        score[0] = intent.getIntExtra("SCORE", 0);
        timeLeft[0] = intent.getIntExtra("TIME_LEFT", 270);
        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeIn.setDuration(1000);
        textView1.setAnimation(fadeIn);
        timeText.setAnimation(fadeIn);
        nextButton.startAnimation(fadeIn);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (timeLeft[0] > 0) {
                    timeText.setText("Time Left:\t" + timeLeft[0] + "s");
                    timeText.setAnimation(fadeIn);
                    timeLeft[0]--;
                    handler.postDelayed(this, 1000);
                } else {
                    Toast.makeText(Ques1Screen.this, "Time Is UP!", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(Ques1Screen.this, ResultScreen.class);
                    String name = intent.getStringExtra("NAME");
                    intent1.putExtra("SCORE", score[0]);
                    intent1.putExtra("NAME", name);
                    startActivity(intent1);
                    finish();
                }
            }
        };
        handler.post(runnable);
        Log.d("DIFF", "ques 1: "+difficulty);
        String URL = "https://opentdb.com/api.php?amount=1&category=9&difficulty=" + difficulty + "&type=boolean";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, response -> {
            try {
                JSONArray results = response.getJSONArray("results");
                JSONObject ques = results.getJSONObject(0);
                String quesText = android.text.Html.fromHtml(ques.getString("question")).toString();
                correctAnswer = ques.getString("correct_answer");

                textView1.setText(quesText);

                nextButton.setOnClickListener(v -> {
                    Animation scaleAnim = AnimationUtils.loadAnimation(Ques1Screen.this, R.anim.scale_up);
                    nextButton.startAnimation(scaleAnim);
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    if (selectedId == -1) {
                        Toast.makeText(Ques1Screen.this, "Please select an answer.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    RadioButton selectedBut = findViewById(selectedId);
                    String userAns = selectedBut.getText().toString();

                    if (userAns.equalsIgnoreCase(correctAnswer)) {
                        score[0]++;
                    }

                    Intent intent1 = new Intent(Ques1Screen.this, Ques2Screen.class); // For next question
                    String name = intent.getStringExtra("NAME");
                    intent1.putExtra("SCORE", score[0]);
                    intent1.putExtra("TIME_LEFT", timeLeft[0]);
                    intent1.putExtra("NAME", name);
                    intent1.putExtra("DIFFICULTY", difficulty);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    String TAG = "score";
                    Log.d(TAG, "onCreate: "+score[0]);
                    finish();
                });

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error loading question", Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(this, "Could not Load the question!", Toast.LENGTH_SHORT).show());

        queue.add(jsonObjectRequest);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
