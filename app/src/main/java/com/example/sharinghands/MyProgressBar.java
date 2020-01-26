package com.example.sharinghands;

import android.content.Context;
import android.os.AsyncTask;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.EditText;
import android.widget.ProgressBar;

public class MyProgressBar extends Animation {

    private ProgressBar progressBar;
    private float from;
    private float  to;

    public MyProgressBar(ProgressBar progressBar, float from, float to) {
        super();
        this.progressBar = progressBar;
        this.from = from;
        this.to = to;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        float value = from + (to - from) * interpolatedTime;
        progressBar.setProgress((int) value);
    }

}
