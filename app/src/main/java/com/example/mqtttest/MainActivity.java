package com.example.mqtttest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    TextView dataReceived;
    Button dataBtn;
    EditText input;
    MqttAndroidClient subClient;
    MqttAndroidClient pubClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataReceived = (TextView) findViewById(R.id.dataReceived);
        dataBtn = (Button) findViewById(R.id.dataBtn);
        input = (EditText) findViewById(R.id.quantity);

        ///////////////////////////////////////////////////////connect

        String clientId1 = MqttClient.generateClientId();
        subClient = new MqttAndroidClient(this.getApplicationContext(), "tcp://18.217.227.236:1883",
                        clientId1);
        MqttConnectOptions options1 = new MqttConnectOptions();
        options1.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
        options1.setUserName("Chenyuc");
        options1.setPassword("15a88k".toCharArray());
        try {
            IMqttToken token1 = subClient.connect(options1);
            token1.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(MainActivity.this, "connected!", Toast.LENGTH_LONG).show();
                    setSub();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(MainActivity.this, "connection failed!", Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException ex){
            ex.printStackTrace();
        }

        //------------------------------------------------

        String clientId2 = MqttClient.generateClientId();
        pubClient = new MqttAndroidClient(this.getApplicationContext(), "tcp://18.217.227.236:1883",
                clientId2);
        MqttConnectOptions options2 = new MqttConnectOptions();
        options2.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
        options2.setUserName("Chenyuchang_seller");
        options2.setPassword("ua5fsl".toCharArray());
        try {
            IMqttToken token2 = pubClient.connect(options2);
            token2.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(MainActivity.this, "connected!", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(MainActivity.this, "connection failed!", Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException ex){
            ex.printStackTrace();
        }

        //////////////////////////////////////////////////////////publish

        dataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputNum = input.getText().toString();
                String topic = "Air_quality";
                String payload = inputNum;
                byte[] encodedPayload = new byte[0];
                try {
                    encodedPayload = payload.getBytes("UTF-8");
                    MqttMessage message = new MqttMessage(encodedPayload);
                    pubClient.publish(topic, message);
                } catch (UnsupportedEncodingException | MqttException e) {
                    e.printStackTrace();
                }
            }
        });

        //////////////////////////////////////////////////////////////subscribe
        subClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                dataReceived.setText(new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

    }
    private void setSub(){
        String topic = "Air_quality";
        try {
            IMqttToken subToken = subClient.subscribe(topic, 0);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
        MqttMessage mqttMessage = new MqttMessage();
        dataReceived.setText(mqttMessage.toString());
    }
}
/*
    private void startMqtt() {
        mqttHelper = new MQTTHelper(getApplicationContext());
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {

            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Debug", mqttMessage.toString());
                dataReceived.setText(mqttMessage.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }
}
*/