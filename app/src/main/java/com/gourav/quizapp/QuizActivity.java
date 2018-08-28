package com.gourav.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuizActivity extends AppCompatActivity {

    RecyclerView quizrecycler;

    public static Button submit;
    List<Result> resultList = new ArrayList<>();
    DatabaseReference mref,ref;

    QuizAdapter adapter;
    public CountDownTimer waittimer;
    @BindView(R.id.timer)
    TextView timer;
    public long time;
    List<Quiz> questionlist = new ArrayList<>();
    @BindView(R.id.quizTitle)
    TextView quizTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String userid = intent.getStringExtra("id");
        String quizid = intent.getStringExtra("quizid");

        quizrecycler = (RecyclerView) findViewById(R.id.quizrecycler);
        submit = (Button) findViewById(R.id.submit);

        ref=FirebaseDatabase.getInstance().getReference().child("quiz").child(quizid);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String title=dataSnapshot.child("quiztitle").getValue().toString();
                String quiztime=dataSnapshot.child("quiztime").getValue().toString();
                quizTitle.setText(title);
                int timeinmin=Integer.parseInt(quiztime);
                time=timeinmin*60000;
                timer();
                Toast.makeText(QuizActivity.this, String.valueOf(time), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        quizrecycler.setLayoutManager(layoutManager);
        quizrecycler.setHasFixedSize(true);



        mref = FirebaseDatabase.getInstance().getReference().child("quiz").child(quizid).child("quizques");
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            int count = 1;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Quiz quiz = snapshot.getValue(Quiz.class);
                    questionlist.add(quiz);
                    Result r = new Result(count, null);
                    resultList.add(r);
                    count += 1;
                }
                adapter = new QuizAdapter(QuizActivity.this, resultList, questionlist);
                quizrecycler.setAdapter(adapter);

                //long mill = 600000;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    void timer()
    {
        long mill=time;
        Toast.makeText(QuizActivity.this, String.valueOf(mill), Toast.LENGTH_SHORT).show();

        waittimer = new CountDownTimer(mill, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                timer.setText("" + String.valueOf(minutes) + ":" + String.valueOf(seconds));

//                } else {
//                    this.cancel();

//                }


                //here you can have your logic to set text to edittext
            }

            public void onFinish() {

                timer.setText("Time Over!");

            }

        }.start();


    }


}
