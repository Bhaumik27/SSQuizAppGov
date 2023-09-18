package com.ssnsp.myquizapp;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ssnsp.myquizapp.R;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {
    private TextView questionTextView;
    private RadioGroup answerRadioGroup;
    private Button submitButton;

    // Define your quiz questions and answers
    private String[] questions = {"Who is Srk?", "India renamed to ?", "Who is PM of BHARAT?"};
    private String[] correctAnswers = {"Answer 1", "Answer 2", "Answer 3"};

    private String[] optionAnswers = {"Answer 1", "Answer 2", "Answer 3","Answer 4", "Answer 5", "Answer 6"};

    private int currentQuestionIndex = 0;
    private int currentoptionIndex = 0;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private List<Question> questionsList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secondactivity);

        FirebaseApp.initializeApp(this);

        questionTextView = findViewById(R.id.questionTextView);
        answerRadioGroup = findViewById(R.id.radioGroup);
        submitButton = findViewById(R.id.submitButton);
        database= FirebaseDatabase.getInstance("https://ss-quiz-app-default-rtdb.asia-southeast1.firebasedatabase.app/");
        myRef=database.getReference();
        displayQuestion();
        firebaseQuestion();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer();
            }
        });
    }

    private void displayQuestion() {
        if (currentQuestionIndex < questionsList.size()) {
            Question question = questionsList.get(currentQuestionIndex);

            questionTextView.setText(question.getQuestion());

            // Populate RadioButtons with answer choices
            RadioButton radioButtonA = findViewById(R.id.option1RadioButton);
            RadioButton radioButtonB = findViewById(R.id.option2RadioButton);
            RadioButton radioButtonC = findViewById(R.id.option3RadioButton);
            RadioButton radioButtonD = findViewById(R.id.option4RadioButton);

            radioButtonA.setText(question.getChoice1());
            radioButtonB.setText(question.getChoice2());
            radioButtonC.setText(question.getChoice3());
            radioButtonD.setText(question.getChoice4());

            currentQuestionIndex++;
        } else {
            Log.d("SecondActivity:", "All questions displayed");
        }
    }


    private void checkAnswer() {
        int selectedRadioButtonId = answerRadioGroup.getCheckedRadioButtonId();

        if (selectedRadioButtonId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
            String selectedAnswer = selectedRadioButton.getText().toString();

            if (selectedAnswer.equals(correctAnswers[currentQuestionIndex])) {
                // Correct answer
                Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            } else {
                // Incorrect answer
                Toast.makeText(this, "Incorrect!", Toast.LENGTH_SHORT).show();
            }

            // Move to the next question
            currentQuestionIndex++;

            if (currentQuestionIndex < questions.length) {
                displayQuestion();
                answerRadioGroup.clearCheck();
            } else {
                // Quiz completed
                //Toast.makeText(this, "Quiz completed!", Toast.LENGTH_SHORT).show();
                Intent intent  = new Intent(SecondActivity.this,FinalResultActivity.class);
                startActivity(intent);
            }
        } else {
            // No answer selected
            Toast.makeText(this, "Please select an answer.", Toast.LENGTH_SHORT).show();
        }
    }
    private void firebaseQuestion() {
        try {
            myRef.child("Questions").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot questionSnapshot : dataSnapshot.getChildren()) {
                            Question question = questionSnapshot.getValue(Question.class);
                            if (question != null) {
                                questionsList.add(question);
                            }
                        }

                        // Start displaying questions
                        displayQuestion();
                    } else {
                        Log.d("SecondActivity:", "No data");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("SecondActivity:", "No error");
                }
            });
        } catch (Exception e) {
            Log.d("SecondActivity:", "Error: " + e.getMessage());
        }
    }


}

