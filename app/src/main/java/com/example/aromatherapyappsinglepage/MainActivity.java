package com.example.aromatherapyappsinglepage;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity implements HumidifierFragment.FragmentHumidifierListener {

    // Widgets
    Bundle bun = new Bundle();

    // MQTT global variables
    MQTTHandler mqttHandler;
    String[] topics = {"switch", "Sensor"};

    // Fragments
    final FragmentManager fm = getSupportFragmentManager();
    HumidifierFragment humidifierFragment;
    LEDFragment ledFragment;
    SoundFragment soundFragment;
    Fragment active;

    // Fragments getID
    //HumidifierFragment humFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create and connect to MQTT Broker
        mqttHandler = new MQTTHandler(getApplicationContext());
        mqttHandler.connect(topics);
        mqttHandler.setCallBack(callback);

        // Initializing fragments
        humidifierFragment = new HumidifierFragment();
        ledFragment = new LEDFragment();
        soundFragment = new SoundFragment();
        active = humidifierFragment;

        // Set-up bottom navigation menu
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavMenu);
        bottomNav.setOnNavigationItemSelectedListener(bottomNavListener);

        // Start each fragments
        fm.beginTransaction().add(R.id.main_container, soundFragment, "3").hide(soundFragment).commit();
        fm.beginTransaction().add(R.id.main_container, ledFragment, "2").hide(ledFragment).commit();
        fm.beginTransaction().add(R.id.main_container, humidifierFragment, "1").commit();

        // Get fragments by ID
        //humFrag = (HumidifierFragment) fm.findFragmentById(R.id.fragment_humidifier);

    }

    // Bottom navigation menu listener
    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            // Handle different choices
            switch (menuItem.getItemId()) {

                case R.id.nav_humidifier:
                    fm.beginTransaction().hide(active).show(humidifierFragment).commit();
                    active = humidifierFragment;
                    Log.d("Main Activity", "Showing Humidifier fragment");
                    return true;

                case R.id.nav_led:
                    fm.beginTransaction().hide(active).show(ledFragment).commit();
                    active = ledFragment;
                    Log.d("Main Activity", "Showing LED fragment");
                    return true;

                case R.id.nav_sounds:
                    fm.beginTransaction().hide(active).show(soundFragment).commit();
                    active = soundFragment;
                    Log.d("Main Activity", "Showing Sounds Fragment");
                    return true;

            }

            return false;
        }
    };

    // MQTT Callback function
    private MqttCallbackExtended callback = new MqttCallbackExtended() {
        @Override
        public void connectComplete(boolean reconnect, String serverURI) {

        }

        @Override
        public void connectionLost(Throwable cause) {

        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {

            // Debug log
            Log.d("MQTTClient", "New message from: " + topic);
            Log.d("MQTTClient", "Message content: " + message.toString());

            if (topic.equals("switch")){
                humidifierFragment.editText(message.toString());
                Log.d("MQTT Handler", message.toString() + " received from " + topic);
            } else if (topic.equals("Sensor")) {
                humidifierFragment.setWaterLevel(message.toString());
                Log.d("MQTT Handler", message.toString() + " received from " + topic);
            }

        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {

        }
    };

    @Override
    public void onInputHumidifierSent(CharSequence topic, CharSequence message) {
        mqttHandler.publish(topic.toString(), message.toString());
    }
}
