package com.example.aromatherapyappsinglepage;

import android.database.sqlite.SQLiteDatabase;
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

public class MainActivity extends AppCompatActivity
        implements HumidifierFragment.FragmentHumidifierListener,
                   LEDFragment.LEDFragmentListener,
                    SoundFragment.FragmentSoundsListener {


    // MQTT global variables
    private MQTTHandler mqttHandler;
    String[] topics = {"humidifier/status", "Sensor", "led/brightness/status", "led/colorstatus"};

    // Fragments
    final FragmentManager fm = getSupportFragmentManager();
    private HumidifierFragment humidifierFragment;
    private LEDFragment ledFragment;
    private SoundFragment soundFragment;
    private StartScreen startScreen;
    private Fragment active;

    // Database
    private SQLDatabase dbSQL;

    // Millisecond variables
    private long responseStart;
    private long responseEnd;
    private long responseTime;

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
        startScreen = new StartScreen();
        active = startScreen;

        // Set-up bottom navigation menu
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavMenu);
        bottomNav.setOnNavigationItemSelectedListener(bottomNavListener);

        // Start each fragments
        fm.beginTransaction().add(R.id.main_container, soundFragment, "3").hide(soundFragment).commit();
        fm.beginTransaction().add(R.id.main_container, ledFragment, "2").hide(ledFragment).commit();
        fm.beginTransaction().add(R.id.main_container, humidifierFragment, "1").hide(humidifierFragment).commit();
        fm.beginTransaction().add(R.id.main_container, startScreen, "0").commit();

        // Init db
        //dbSQL = new SQLDatabase(this);

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

            if (topic.equals("humidifier/status")){
                humidifierFragment.editText(message.toString());

                // Insert data into database
                responseEnd = System.currentTimeMillis();
                responseTime = responseEnd - responseStart;
                //dbSQL.insertData("NaN", "NaN", (int)responseTime, 0, 0, 0);
                Log.d("ResponseTime", Long.toString(responseTime));

                Log.d("MQTT Handler", message.toString() + " received from " + topic);
            } else if (topic.equals("Sensor")) {
                humidifierFragment.setWaterLevel(message.toString());
                Log.d("MQTT Handler", message.toString() + " received from " + topic);
            } else if(topic.equals("led/colorstatus")) {

                // Insert data into database
                responseEnd = System.currentTimeMillis();
                responseTime = responseEnd - responseStart;
                //dbSQL.insertData("NaN", "NaN", (int)responseTime, 0, 0, 0);
                Log.d("ResponseTime", Long.toString(responseTime));
            }

        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {

        }
    };

    @Override
    public void onInputHumidifierSent(CharSequence topic, CharSequence message) {
        mqttHandler.publish(topic.toString(), message.toString());
        responseStart = System.currentTimeMillis();
    }

    @Override
    public void onLEDFragmentInputSent(CharSequence topic, CharSequence message) {
        mqttHandler.publish(topic.toString(), message.toString());
        responseStart = System.currentTimeMillis();
    }

    @Override
    public void onInputSoundsSent(CharSequence topic, CharSequence message) {
        mqttHandler.publish(topic.toString(), message.toString());
    }

    @Override
    public void onGenerateCSV() {
        Log.d("GenerateCSV", "Button Pressed");
        //dbSQL.exportDB();
        //dbSQL.deleteAll();
    }
}
