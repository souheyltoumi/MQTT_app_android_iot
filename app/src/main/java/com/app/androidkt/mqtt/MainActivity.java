package com.app.androidkt.mqtt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private MqttAndroidClient client;
    private String TAG = "MainActivity";
    private PahoMqttClient pahoMqttClient;
    public static final String DEFAULT = "N/A";
    String status;
    public static final String DEFAULT1 = "empty";
    String cole=Constants.url+"/smartparking/cole.php";
    Context context;
    String last="";
    String login;

    private EditText textMessage, subscribeTopic, unSubscribeTopic;
    private Button publishMessage, subscribe, unSubscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      pahoMqttClient = new PahoMqttClient();
        final SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("MyData", MODE_PRIVATE);
        context=getApplicationContext();

        login = sharedPreferences.getString("login",DEFAULT);

        textMessage = (EditText) findViewById(R.id.textMessage);
        publishMessage = (Button) findViewById(R.id.publishMessage);

        subscribe = (Button) findViewById(R.id.subscribe);
        unSubscribe = (Button) findViewById(R.id.unSubscribe);

        subscribeTopic = (EditText) findViewById(R.id.subscribeTopic);
        unSubscribeTopic = (EditText) findViewById(R.id.unSubscribeTopic);
        client = pahoMqttClient.getMqttClient(getApplicationContext(), Constants.MQTT_BROKER_URL, login);
        final Handler ha=new Handler();
        ha.postDelayed(new Runnable() {

            @Override
            public void run() {
                //call function
                final SharedPreferences sharedPreferences =context.getSharedPreferences("MyData", MODE_PRIVATE);
                status = sharedPreferences.getString("status",DEFAULT1);
                Toast.makeText(getApplicationContext(),status,Toast.LENGTH_LONG).show();

                if (!status.equalsIgnoreCase("empty")&&!status.equalsIgnoreCase(last))
                {
                    inscription1();}
                     last=status;

                ha.postDelayed(this, 1000);
            }
        }, 1000);


        publishMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String topic = subscribeTopic.getText().toString().trim();

                String msg = textMessage.getText().toString().trim();
                if (!msg.isEmpty()) {
                    try {
                        pahoMqttClient.publishMessage(client,msg,1,topic);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String topic = subscribeTopic.getText().toString().trim();
                if (!topic.isEmpty()) {
                    try {
                        pahoMqttClient.subscribe(client, topic, 1);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        unSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String topic = unSubscribeTopic.getText().toString().trim();
                if (!topic.isEmpty()) {
                    try {
                        pahoMqttClient.unSubscribe(client, topic);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Intent intent = new Intent(MainActivity.this, MqttMessageService.class);
        startService(intent);
    }
    public void inscription1(){

        //lhne chnaabaathou essm el fichier php wles données mtaana lel serveur
        StringRequest stringRequest = new StringRequest(Request.Method.POST,cole,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //ki tji reponse men 3and serveur

                        Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();


                    }
                },
                //erreur tab3in connexion maa el serveur
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams()  {
                // les var eli bech teb3athhom
                Map<String, String> params = new HashMap<String,String>();

                params.put("slot",status);




                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}

