package com.cs646.ted.assignment4;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DrawCircleFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_draw_circle, container, false);
        return v;
    }

    public static DrawCircleFragment newInstance() {
        return new DrawCircleFragment();
    }
}
