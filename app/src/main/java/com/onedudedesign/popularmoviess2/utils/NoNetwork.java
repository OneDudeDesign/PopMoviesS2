package com.onedudedesign.popularmoviess2.utils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.onedudedesign.popularmoviess2.R;

//This file is to stop activity and warn the user that the network needs to be connected

public class NoNetwork extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_network);
    }
}
