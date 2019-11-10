package com.app.androidkt.mqtt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SignIn extends AppCompatActivity {
    EditText login ,password ;
    String URL_Login=Constants.url+"/smartparking/login.php";
    Boolean islogged =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        login= (EditText) findViewById(R.id.mailID);
        password= (EditText) findViewById(R.id.passID);

    }
    public void login(View view) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Chargement...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL_Login,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        if(response.equals("SUCCES")){
                            Intent intent =new Intent(SignIn.this,ReservingActivity.class);
                            //t5ali compte mta3k ma7loul

                            SharedPreferences sharedPref = getSharedPreferences("MyData",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("login",login.getText().toString());
                            editor.putBoolean("islogged",true);
                            editor.commit();
                            startActivity(intent);

                        }
                        else  {

                            Toast.makeText(SignIn.this, response.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }){
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<String,String>();
                params.put("login",login.getText().toString());
                params.put("pass",password.getText().toString());



                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    public void signup(View view) {
        Intent I= new Intent(SignIn.this,SignUp.class);
        startActivity(I);
    }
    public  void onBackPressed()
    {
        Toast.makeText(getApplicationContext(),"You can't Go Back",Toast.LENGTH_LONG).show();
    }
}
