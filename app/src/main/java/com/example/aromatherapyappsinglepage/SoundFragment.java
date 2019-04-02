package com.example.aromatherapyappsinglepage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SoundFragment extends Fragment {

    // Widgets
    TextView soundText;

    // Empty constructor
    public SoundFragment() {
        // Must be empty. Do not put anything here
    }

    // On Create
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sounds, container, false);
        soundText = view.findViewById(R.id.sounds_text);
        soundText.setText("Hello from Sounds");
        return view;

    }

}
