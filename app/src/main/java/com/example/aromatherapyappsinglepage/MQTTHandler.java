package com.example.aromatherapyappsinglepage;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class MQTTHandler {

    // Init MQTTAndroidClient object
    public MqttAndroidClient mqttClient;

    // Global variables
    private final String serverURI = "tcp://m16.cloudmqtt.com:14311";
    private final String username = "xmxzgirv";
    private final String password = "VIlzm7f9S8So";
    private final String debugTag = "MQTT Client";
    private String clientId;

    // Constructor
    public MQTTHandler(Context context) {

        // Set new connection
        Log.d(debugTag, "Constructor | Initializing client ...");
        clientId = MqttClient.generateClientId();
        mqttClient = new MqttAndroidClient(context, serverURI, clientId);

    }

    // Function for setting callbacks
    public void setCallBack(MqttCallbackExtended callback) {
        mqttClient.setCallback(callback);
    }

    // Connect method
    public void connect(final String[] topics) {

        // Define MQTT access options
        Log.d(debugTag, "Connect | Setting up options ...");
        MqttConnectOptions options = new MqttConnectOptions();
        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
        options.setAutomaticReconnect(true);        // Set auto-reconnect
        options.setCleanSession(false);
        options.setUserName(username);
        options.setPassword(password.toCharArray());

        // Connect
        try {

            // Debug
            Log.d(debugTag, "Connect | Trying to connect ...");

            // Create Mqtt token
            IMqttToken token = mqttClient.connect(options);
            token.setActionCallback(new IMqttActionListener() {

                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                    // Subscribe to topics
                    for (int i = 0; i < topics.length; i++){
                        subscribe(topics[i]);
                    }

                    // Debug for the MQTT Broker
                    publish("hello/guys", "hello");

                    // Debug log
                    Log.d("MQTTClient", "Successful connection");

                    DisconnectedBufferOptions disBuffOptions = new DisconnectedBufferOptions();
                    disBuffOptions.setBufferEnabled(true);
                    disBuffOptions.setBufferSize(100);
                    disBuffOptions.setPersistBuffer(false);
                    disBuffOptions.setDeleteOldestMessages(false);
                    mqttClient.setBufferOpts(disBuffOptions);

                    // Set callbacks
                    //mqttClient.setCallback(callbacks);

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });

        } catch (MqttException e) {
            Log.d(debugTag, "Connect | exception occurred during connection");
            e.printStackTrace();
        }

    }


    // Subscribe method
    private void subscribe(final String topic) {

        try {

            mqttClient.subscribe(topic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(debugTag, "Subscribe | Successful subscription to " + topic);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(debugTag, "Subscribe | Subscribing to " + topic + " caused an exception");
                }
            });

        } catch (MqttException e) {
            Log.d(debugTag, "Subscribe | Subscribing to " + topic + " caused an exception");
            e.printStackTrace();
        }

    }

    // Publish method
    public void publish(final String topic, String payload) {

        // Prepare payload for sending
        byte[] encodedPayload = new byte[0];

        // Publish payload to topic
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            mqttClient.publish(topic, message);
            Log.d(debugTag, "Message: \"" + message + "\" has been sent to topic: " + topic);
        } catch (UnsupportedEncodingException | MqttException e) {
            Log.d(debugTag, "Publish | Exception occurred");
            e.printStackTrace();
        }

    }

}
