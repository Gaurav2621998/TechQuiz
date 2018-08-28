package com.gourav.quizapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultActivity extends AppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);

        Bundle bundle=getIntent().getBundleExtra("bundle");
        List<Result>resultList=bundle.getParcelable("resultlist");

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Result");
        int count=0;
        for(int i=0;i<resultList.size();i++)
        {
            
        }




    }


}
