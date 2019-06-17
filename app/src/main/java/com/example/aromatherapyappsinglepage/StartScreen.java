package com.example.aromatherapyappsinglepage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StartScreen extends Fragment {


    // Empty constructor
    public StartScreen() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Fragment view
        View view = inflater.inflate(R.layout.fragment_opening_screen, container, false);

        return view;

    }
}
