package com.example.aromatherapyappsinglepage;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.john.waveview.WaveView;

public class HumidifierFragment extends Fragment {

    private FragmentHumidifierListener listener;
    private TextView statusText;
    private TextView waterLevelText;
    private Switch humidifierSwitch;
    private WaveView waterLevelWidget;

    // Listener
    public interface FragmentHumidifierListener {

       void onInputHumidifierSent(CharSequence topic, CharSequence message);

    }

    // Empty constructor (required)
    public HumidifierFragment() {
        // Do not put anything in here
    }

    // Set switch status
    public void editText(String text) {
        statusText.setText(text);
    }

    // Set water level
    public void setWaterLevel(String level) {
        int waterLevel = Integer.parseInt(level);
        waterLevelWidget.setProgress(waterLevel);
        waterLevelText.setText(level + "%");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Fragment view
        View view = inflater.inflate(R.layout.fragment_humidifier, container, false);

        // Text
        statusText = view.findViewById(R.id.humidifierStatusText);
        waterLevelText = view.findViewById(R.id.waterLevelText);
        statusText.setText("off");

        // Water Level
        waterLevelWidget = view.findViewById(R.id.waterLevelWidget);

        // On/Off Switch
        humidifierSwitch = view.findViewById(R.id.humidifierSwitch);
        humidifierSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // if statements
                if (isChecked) {
                    listener.onInputHumidifierSent("switch", "on");
                } else {
                    listener.onInputHumidifierSent("switch", "off");
                }

            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof FragmentHumidifierListener) {
            listener = (FragmentHumidifierListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FragmentHumidifierListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
