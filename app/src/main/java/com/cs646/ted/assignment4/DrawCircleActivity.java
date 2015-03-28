package com.cs646.ted.assignment4;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class DrawCircleActivity extends ActionBarActivity {
    private DrawCircleFragment mDrawCircleFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_circle);

        mDrawCircleFragment = DrawCircleFragment.newInstance();

        getFragmentManager().beginTransaction()
                .add(R.id.draw_fragment_container, mDrawCircleFragment).commit();
    }
}
