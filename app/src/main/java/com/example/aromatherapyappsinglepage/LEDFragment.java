package com.example.aromatherapyappsinglepage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LEDFragment extends Fragment {

    TextView ledText;

    // Required empty constructor
    public LEDFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_led, container, false);
        ledText = view.findViewById(R.id.led_text);
        ledText.setText("Look at dat LED");
        return view;

    }
}
