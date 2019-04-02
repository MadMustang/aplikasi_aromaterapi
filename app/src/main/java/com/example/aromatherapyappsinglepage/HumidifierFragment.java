package com.example.aromatherapyappsinglepage;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class HumidifierFragment extends Fragment {

    private FragmentHumidifierListener listener;
    private TextView statusText;
    private Button testButton;

    // Listener
    public interface FragmentHumidifierListener {

       void onInputHumidifierSent(CharSequence topic, CharSequence message);

    }

    // Empty constructor
    public HumidifierFragment() {

    }

    // Receive data
    public void editText(String text) {
        statusText.setText(text);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_humidifier, container, false);
        statusText = view.findViewById(R.id.humidifier_text);
        //String myStr = getArguments().getString("data");
        statusText.setText("Hello from Humidifier");
        testButton = view.findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onInputHumidifierSent("switch", "Button Pressed");
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
