package com.app.androidkt.mqtt;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ReservingActivity extends AppCompatActivity {

    private MqttAndroidClient client;

    public static final String DEFAULT1 = "empty";
    String cole=Constants.url+"/smartparking/cole.php";
    String URl_user=Constants.url+"/smartparking/userinfo.php";
    Button slot1,slot2,slot3,slot4;
    private PahoMqttClient pahoMqttClient;
String login;
    public static final String DEFAULT = "N/A";


    Context context;
    String last;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserving);
        final SharedPreferences sharedPreferences = ReservingActivity.this.getSharedPreferences("MyData", MODE_PRIVATE);
        context=getApplicationContext();
        status="empty";

        login = sharedPreferences.getString("login",DEFAULT);
        slot1= (Button) findViewById(R.id.slot1);
        slot2= (Button) findViewById(R.id.slot2);
        slot3= (Button) findViewById(R.id.slot3);
        slot4= (Button) findViewById(R.id.slot4);
        pahoMqttClient = new PahoMqttClient();

        client = pahoMqttClient.getMqttClient(getApplicationContext(), Constants.MQTT_BROKER_URL, login);




        slot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int color = ((ColorDrawable)slot1.getBackground()).getColor();
                if (color == -16744448)

                {try {
                        pahoMqttClient.publishMessage(client, "true/"+login+"smartParking/1", 1, Constants.PUBLISH_TOPIC);
                        Intent intent=new Intent(ReservingActivity.this,BluetoothDetection.class);
                        intent.putExtra("slot","slot1");
                    inscription1("true/"+login+"/smartParking/1");

                    startActivity(intent);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


            }
            else
                {
                    Toast.makeText(getApplicationContext(),"Slot already reserved",Toast.LENGTH_LONG).show();
                }}
        });



        slot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int color = ((ColorDrawable)slot2.getBackground()).getColor();
                if (color == -16744448)

                {try {
                    pahoMqttClient.publishMessage(client, "true/"+login+"/smartParking/2", 1, Constants.PUBLISH_TOPIC);
                    Intent intent=new Intent(ReservingActivity.this,BluetoothDetection.class);
                    intent.putExtra("slot","slot2");
                    inscription1("true/"+login+"/smartParking/2");
                    startActivity(intent);
                } catch (MqttException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Slot already reserved",Toast.LENGTH_LONG).show();
                }}
        });

        slot3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int color = ((ColorDrawable)slot3.getBackground()).getColor();
                if (color == -16744448)

                {try {
                    pahoMqttClient.publishMessage(client, "true/"+login+"/smartParking/3", 1, Constants.PUBLISH_TOPIC);
                    Intent intent=new Intent(ReservingActivity.this,BluetoothDetection.class);
                    intent.putExtra("slot","slot3");
                    inscription1("true/"+login+"/smartParking/3");

                    startActivity(intent);
                } catch (MqttException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Slot already reserved",Toast.LENGTH_LONG).show();
                }}
        });



        slot4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int color = ((ColorDrawable)slot4.getBackground()).getColor();
                if (color ==-16744448)

                {try {
                    pahoMqttClient.publishMessage(client, "true/"+login+"/smartParking/4", 1, Constants.PUBLISH_TOPIC);
                    Intent intent=new Intent(ReservingActivity.this,BluetoothDetection.class);
                    inscription1("true/"+login+"/smartParking/4");
                    intent.putExtra("slot","slot4");
                    startActivity(intent);
                } catch (MqttException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Slot already reserved",Toast.LENGTH_LONG).show();
                }}
        });

        client = pahoMqttClient.getMqttClient(getApplicationContext(), Constants.MQTT_BROKER_URL, login);
        final Handler ha=new Handler();
        ha.postDelayed(new Runnable() {

            @Override
            public void run() {
                //call function
                gethotelslist();
                final SharedPreferences sharedPreferences =context.getSharedPreferences("MyData", MODE_PRIVATE);
                status = sharedPreferences.getString("status",DEFAULT1);
                Toast.makeText(getApplicationContext(),status,Toast.LENGTH_LONG).show();

                if (!status.equalsIgnoreCase("empty")&&!status.equalsIgnoreCase(last))
                {
                    Toast.makeText(getApplicationContext(),status,Toast.LENGTH_LONG).show();

                    inscription1(status);}
                last=status;

                ha.postDelayed(this, 1000);
            }
        }, 1000);
        Intent intent = new Intent(ReservingActivity.this, MqttMessageService.class);
        startService(intent);


    }
    public void inscription1(final String status){

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
                    }
                }){
            @Override
            protected Map<String, String> getParams()  {
                // les var eli bech teb3athhom
                Map<String, String> params = new HashMap<String,String>();

                params.put("slot", status);




                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public void gethotelslist(){





        StringRequest stringRequest = new StringRequest(Request.Method.POST, URl_user,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JSONArray array = null;
                        try {
                            array = jsonObject.getJSONArray("slots");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JSONObject o = null;
                        try {
                            o = array.getJSONObject(0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            if(o.getString("slot1").equalsIgnoreCase("true"))
                                {
                                    slot1.setBackgroundColor(getResources().getColor(R.color.refresh2));

                                }else{
                                    slot1.setBackgroundColor(getResources().getColor(R.color.refresh));

                                }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            if(o.getString("slot2").equalsIgnoreCase("true"))
                            {
                                slot2.setBackgroundColor(getResources().getColor(R.color.refresh2));

                            }else{
                                slot2.setBackgroundColor(getResources().getColor(R.color.refresh));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            if(o.getString("slot3").equalsIgnoreCase("true"))
                            {
                                slot3.setBackgroundColor(getResources().getColor(R.color.refresh2));

                            }else{
                                slot3.setBackgroundColor(getResources().getColor(R.color.refresh));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            if(o.getString("slot4").equalsIgnoreCase("true"))
                            {
                                slot4.setBackgroundColor(getResources().getColor(R.color.refresh2));

                            }else{
                                slot4.setBackgroundColor(getResources().getColor(R.color.refresh));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {



                    }
                }){
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<String, String>();


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


}
