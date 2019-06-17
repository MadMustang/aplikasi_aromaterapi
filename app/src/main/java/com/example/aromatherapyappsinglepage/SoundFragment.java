package com.example.aromatherapyappsinglepage;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class SoundFragment extends Fragment {

    // Global variables
    private TextView soundText;
    private FragmentSoundsListener listener;
    private Button generateCSV;
    private SeekBar volumeBar;

    // Buttons
    private Button playPauseButton;
    private Button rain;
    private Button storm;
    private Button river;
    private Button fire;
    private Button forest;
    private Button waterfall;
    private Button night;

    // Empty constructor
    public SoundFragment() {
        // Must be empty. Do not put anything here
    }

    // Listener
    public interface FragmentSoundsListener {
        void onInputSoundsSent(CharSequence topic, CharSequence message);
        void onGenerateCSV();
    }

    // On Create
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sounds, container, false);
        soundText = view.findViewById(R.id.sounds_text);
        soundText.setText("Sounds");

        // Response test
        generateCSV = view.findViewById(R.id.generateCSV);
        generateCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onGenerateCSV();
            }
        });

        // Volume bar
        volumeBar = view.findViewById(R.id.volume_bar);
        volumeBar.setMax(20);
        volumeBar.setProgress(10);
        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int volProgress = volumeBar.getProgress() + 10;
                Log.d("Volume", "Volume level: " + Integer.toString(volProgress));
                listener.onInputSoundsSent("dfplayer/volume", Integer.toString(volProgress));
            }
        });

        // Play/Pause Button
        playPauseButton = view.findViewById(R.id.play_pause_button);
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onInputSoundsSent("dfplayer/toggle", "");
            }
        });

        // Rain
        rain = view.findViewById(R.id.rain);
        rain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onInputSoundsSent("dfplayer/playtrack", "1");
                volumeBar.setProgress(10);
            }
        });

        // Storm
        storm = view.findViewById(R.id.storm);
        storm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onInputSoundsSent("dfplayer/playtrack", "3");
                volumeBar.setProgress(10);
            }
        });

        // River
        river = view.findViewById(R.id.river);
        river.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onInputSoundsSent("dfplayer/playtrack", "4");
                volumeBar.setProgress(10);
            }
        });

        // Fireplace
        fire = view.findViewById(R.id.fire);
        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onInputSoundsSent("dfplayer/playtrack", "2");
                volumeBar.setProgress(10);
            }
        });

        // Forest
        forest = view.findViewById(R.id.forest);
        forest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onInputSoundsSent("dfplayer/playtrack", "5");
                volumeBar.setProgress(10);
            }
        });

        // Waterfall
        waterfall = view.findViewById(R.id.waterfall);
        waterfall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onInputSoundsSent("dfplayer/playtrack", "6");
                volumeBar.setProgress(10);
            }
        });

        // Night
        night = view.findViewById(R.id.night);
        night.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onInputSoundsSent("dfplayer/playtrack", "8");
                volumeBar.setProgress(10);
            }
        });

        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof FragmentSoundsListener) {
            listener = (FragmentSoundsListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FragmentSoundsListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
