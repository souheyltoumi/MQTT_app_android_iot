package com.app.androidkt.mqtt;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Detection extends AppCompatActivity {

    String URL_iNSCRIT=Constants.url+"/smartparking/count.php";
    String URL_iNSCRIT2=Constants.url+"/smartparking/break.php";
String id="111111";

    String login;
public static final String DEFAULT = "N/A";
TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection);
        final SharedPreferences sharedPreferences = Detection.this.getSharedPreferences("MyData", MODE_PRIVATE);
        textView=(TextView)findViewById(R.id.pairedTv);
        login = sharedPreferences.getString("login",DEFAULT);
        inscription1();
        Button but=(Button)findViewById(R.id.onBtn);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!id.equalsIgnoreCase("00000"))
                { inscription2();
                    SharedPreferences sharedPref = getSharedPreferences("MyData",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("isparked",false);
                    editor.commit();
                id="00000";



                }

            }
        });




    }
    public void inscription1(){

        //lhne chnaabaathou essm el fichier php wles données mtaana lel serveur
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL_iNSCRIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //ki tji reponse men 3and serveur


                    }
                },
                //erreur tab3in connexion maa el serveur
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Detection.this,error.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams()  {
                // les var eli bech teb3athhom
                Map<String, String> params = new HashMap<String,String>();
                params.put("login",login);





                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public void inscription2(){
        final ProgressDialog progressDialog = new ProgressDialog(this); // mta3 chargement
        progressDialog.setMessage("Chargement...");
        progressDialog.show();
        //lhne chnaabaathou essm el fichier php wles données mtaana lel serveur
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL_iNSCRIT2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //ki tji reponse men 3and serveur
                        progressDialog.dismiss();
                        if (response.equals("no account")){
                            Toast.makeText(Detection.this, response.toString(), Toast.LENGTH_SHORT).show();


                        }else{
                           String cos=response;
                      textView.setText("  Money:  "+cos+"  milimes");


                        }





                    }
                },
                //erreur tab3in connexion maa el serveur
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Detection.this,error.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }){
            @Override
            protected Map<String, String> getParams()  {
                // les var eli bech teb3athhom
                Map<String, String> params = new HashMap<String,String>();
                params.put("login",login);





                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }





}