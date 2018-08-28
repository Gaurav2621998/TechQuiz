package com.gourav.quizapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Result implements Parcelable {

    int Quesno;
    String ans;

    public Result(){

    }

    protected Result(Parcel in) {
        Quesno = in.readInt();
        ans = in.readString();
    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    public int getQuesno() {
        return Quesno;
    }

    public void setQuesno(int quesno) {
        Quesno = quesno;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }


    public Result(int quesno, String ans) {
        this.Quesno = quesno;
        this.ans = ans;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Quesno);
        dest.writeString(ans);
    }
}
