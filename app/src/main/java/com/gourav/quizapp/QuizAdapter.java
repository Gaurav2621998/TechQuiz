package com.gourav.quizapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.gourav.quizapp.QuizActivity.submit;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ViewHolder> {

    Context context;
    int count;
    List<Result> resultList;
    int score=0;
    List<Quiz>questionlist;

    private RadioGroup lastCheckedRadioGroup = null;


    public QuizAdapter(Context context, List<Result> results,List<Quiz>questionlist) {
        this.context = context;
        this.resultList = results;
        this.questionlist=questionlist;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Quiz q=questionlist.get(position);



        holder.quesno.setText("Q."+q.getQno());
        holder.ques.setText(q.getQues());
        holder.optionA.setText(q.getOptionA());
        holder.optionB.setText(q.getOptionB());
        holder.optionC.setText(q.getOptionC());
        holder.optionD.setText(q.getOptionD());



        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                notifyDataSetChanged();
//                notifyItemRangeChanged(0,resultList.size()-1);



                RadioButton rb=(RadioButton)group.findViewById(checkedId);
              //  rb.setChecked(checkedId==position);
              //  rb.setBackgroundColor(R.color.colorPrimary);
                String option= (String) rb.getText();
                Result s=resultList.get(position);
                s.setAns(option);
                Toast.makeText(context, option, Toast.LENGTH_SHORT).show();

//                switch(checkId)
//                {
//                    case R.id.rb0:
//                        .get(vh.pos).setState(0);
//                        break;
//                    case R.id.rb1:
//                        answers.get(vh.pos).setState(1);
//                        break;
//                }

                //

//                if (lastCheckedRadioGroup != null
//                        && lastCheckedRadioGroup.getCheckedRadioButtonId()
//                        != group.getCheckedRadioButtonId()
//                        && lastCheckedRadioGroup.getCheckedRadioButtonId() != -1) {
//                    lastCheckedRadioGroup.clearCheck();
//
////                    Toast.makeText(context,
////                            "Radio button clicked " + group.getCheckedRadioButtonId(),
////                            Toast.LENGTH_SHORT).show();
//
//                }
//                lastCheckedRadioGroup = group;






            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(context,ResultActivity.class);
                Bundle bundle=new Bundle();
                bundle.putParcelable("resultlist", (Parcelable) resultList);
                i.putExtra("bundle",bundle);
                context.startActivity(i);

//                for(int i=0;i<10;i++){esult
//                    Result a=resultList.get(i);
//
//                    Toast.makeText(context, a.getAns().toString(), Toast.LENGTH_SHORT).show();
//
//
//                }
//                Toast.makeText(context, String.valueOf(score), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return questionlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ImageView quesimage;
        public TextView quesno,ques;
        public RadioGroup radioGroup;
        public RadioButton optionA,optionB,optionC,optionD;


        public ViewHolder(View itemView) {
            super(itemView);


            quesimage = itemView.findViewById(R.id.quesimage);
            radioGroup=itemView.findViewById(R.id.radiogroup);
            quesno=itemView.findViewById(R.id.quesno);
            ques=itemView.findViewById(R.id.ques);
            optionA=itemView.findViewById(R.id.optionA);
            optionB=itemView.findViewById(R.id.optionB);
            optionC=itemView.findViewById(R.id.optionC);
            optionD=itemView.findViewById(R.id.optionD);



        }
    }

}
