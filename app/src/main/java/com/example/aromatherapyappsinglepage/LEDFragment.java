package com.example.aromatherapyappsinglepage;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SaturationBar;

public class LEDFragment extends Fragment {

    private ColorPicker picker;
    private SaturationBar saturationBar;
    private SeekBar brightnessBar;
    private Button applyButton;
    private LEDFragmentListener listener;
    private Switch ledSwtich;

    // Listener
    public interface LEDFragmentListener {

        void onLEDFragmentInputSent(CharSequence topic, CharSequence message);

    }

    // Required empty constructor
    public LEDFragment(){
        // Do not put anything in here
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Fragment view
        View view = inflater.inflate(R.layout.fragment_led, container, false);

        // Initialize Color Picker
        picker = view.findViewById(R.id.ledColorPicker);
        saturationBar = view.findViewById(R.id.ledSaturationBar);
        picker.addSaturationBar(saturationBar);
        picker.setShowOldCenterColor(false);
        saturationBar.setSaturation(0);

        // On/Off Switch
        ledSwtich = view.findViewById(R.id.ledOnOffSwitch);
        ledSwtich.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // if statements
                if (isChecked) {
                    listener.onLEDFragmentInputSent("led/switch", "on");
                } else {
                    listener.onLEDFragmentInputSent("led/switch", "off");
                }

            }
        });

        // Brightness Bar
        brightnessBar = view.findViewById(R.id.brightnessBar);
        brightnessBar.setMax(210);
        brightnessBar.setProgress(210);
        brightnessBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress() + 45;
                Log.d("Brightness", "Brightness: " + Integer.toString(progress));
                listener.onLEDFragmentInputSent("led/brightness", Integer.toString(progress));
            }
        });

        // Apply Button
        applyButton = view.findViewById(R.id.applyButton);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyLEDColor(picker.getColor());
            }
        });

        return view;

    }

    // Apply color
    private void applyLEDColor(int color) {

        StringBuilder colorMessage = new StringBuilder();

        colorMessage.append(String.format("%03d", Color.red(color)));
        colorMessage.append("|");
        colorMessage.append(String.format("%03d", Color.green(color)));
        colorMessage.append("|");
        colorMessage.append(String.format("%03d", Color.blue(color)));
        Log.d("Color", colorMessage.toString());

        listener.onLEDFragmentInputSent("led/color", colorMessage.toString());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof LEDFragmentListener) {
            listener = (LEDFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement LEDFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
