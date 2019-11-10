package com.app.androidkt.mqtt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LauncherActivity extends AppCompatActivity {
    public static final String DEFAULT = "N/A";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("MyData",MODE_PRIVATE);
        Boolean islogged =sharedPreferences.getBoolean("islogged",false);
        Boolean isparked =sharedPreferences.getBoolean("isparked",false);

        if(islogged.equals(true))
        {
            if(isparked.equals(true))
            {
                Intent intent=new Intent(LauncherActivity.this,Detection.class);
                startActivity(intent);
            }
            else
            {Intent conncted=new Intent(LauncherActivity.this,ReservingActivity.class);
            startActivity(conncted);}
        }else {
            Intent notconnected =new Intent(LauncherActivity.this,SignIn.class);
            startActivity(notconnected);
        }

    }
}
