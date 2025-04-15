package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class MainActivity extends AppCompatActivity {
    EditText editName;
    RadioGroup radioGroupDifficulty;
    Button button;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        editName = findViewById(R.id.editText);
        radioGroupDifficulty = findViewById(R.id.radioDifficulty);
        button = findViewById(R.id.button);
        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeIn.setDuration(1000);
        editName.startAnimation(fadeIn);
        button.startAnimation(fadeIn);
        TextView instructions = findViewById(R.id.textView);
        instructions.setText("--There will be 5 questions in this quiz\n\n--270 seconds will be given for the entire quiz\n\n--WAIT for ATLEAST 10 seconds before submitting the answer or API will mark you as a BOT");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation scaleAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.scale_up);
                button.startAnimation(scaleAnim);
//                Log.d(TAG, "onClick: "+editName.getText());
                String name = editName.getText().toString();
                int selectedbut = radioGroupDifficulty.getCheckedRadioButtonId();
                Intent intent = new Intent(MainActivity.this, Ques1Screen.class);
//                Log.d(, str);
                if(name.isEmpty()){
                    Toast.makeText(MainActivity.this, "Enter Your Name!", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "Welcome "+name, Toast.LENGTH_SHORT).show();}

//                if(selectedbut == 2131231221){
//                    intent.putExtra("NAME", name);
//                    intent.putExtra("DIFFICULTY", "easy");
//
//                } else if (selectedbut == 2131231224) {
//                    intent.putExtra("NAME", name);
//                    intent.putExtra("DIFFICULTY", "medium");
//
//                } else if (selectedbut == 2131231225) {
//                    intent.putExtra("NAME", name);
//                    intent.putExtra("DIFFICULTY", "hard");


                if(selectedbut ==-1){
                    Toast.makeText(MainActivity.this, "Pick a difficulty", Toast.LENGTH_SHORT).show();
                }
                RadioButton selectedDiff = findViewById(selectedbut);
                String difficulty = selectedDiff.getText().toString().toLowerCase();
                intent.putExtra("NAME", name);
                intent.putExtra("DIFFICULTY", difficulty);
                intent.putExtra("SCORE", 0);
                intent.putExtra("TIME_LEFT", 270);
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