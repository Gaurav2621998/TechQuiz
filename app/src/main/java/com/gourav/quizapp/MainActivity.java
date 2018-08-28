package com.gourav.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    Button startbutton;
    DatabaseReference mref;
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.usermobile)
    EditText usermobile;

    FirebaseUser user;
    @BindView(R.id.post)
    Button post;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        startbutton = (Button) findViewById(R.id.startbutton);


        // user= FirebaseAuth.getInstance().getCurrentUser();
        mref = FirebaseDatabase.getInstance().getReference().child("quiz").child("1").child("players").push();
        startbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check()) {
                    Toast.makeText(MainActivity.this, "yes", Toast.LENGTH_SHORT).show();
                    String a = mref.getKey().toString();
                    mref.child("username").setValue(username.getText().toString());
                    mref.child("usermobile").setValue(usermobile.getText().toString());
                    Intent i = new Intent(MainActivity.this, QuizActivity.class);
                    i.putExtra("id", a);
                    i.putExtra("quizid", "1");
                    startActivity(i);

                }

            }
        });
    }

    public Boolean check() {
        if (username.getText().equals("") && usermobile.getText().equals("")) {
            Toast.makeText(this, "Fill the Details", Toast.LENGTH_SHORT).show();
            return false;
        }
//        else if(!(usermobile.getText().toString().matches("639[0-9]{9}")))
//        {
//            Toast.makeText(this, "Mobile no. is not valid", Toast.LENGTH_SHORT).show();
//            return  false;
//        }
        else {
            return true;
        }
    }


    @OnClick(R.id.post)
    public void onViewClicked() {
        startActivity(new Intent(this,Post_upload.class));
    }
}
