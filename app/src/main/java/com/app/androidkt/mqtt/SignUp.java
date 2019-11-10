package com.app.androidkt.mqtt;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    EditText email,nom,prenom,pass,num,cin;
    final int CODE_GALLERY_REQUEST=999;
    String URL_iNSCRIT=Constants.url+"/smartparking/register.php";
    String imagedata="";
    ImageView profileImage;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        email= (EditText) findViewById(R.id.mail);
        nom= (EditText) findViewById(R.id.nom);
        prenom= (EditText) findViewById(R.id.prenom);
        pass= (EditText) findViewById(R.id.pass);
        num= (EditText) findViewById(R.id.num);
        cin= (EditText) findViewById(R.id.cin);
        profileImage= (ImageView) findViewById(R.id.profil);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        SignUp.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},CODE_GALLERY_REQUEST
                );
            }

        });





    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==CODE_GALLERY_REQUEST){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Select Image"),CODE_GALLERY_REQUEST);
            }else {
                Toast.makeText(this," Pour créer une  photo de profil merci de nous donner la permission",Toast.LENGTH_SHORT);
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==CODE_GALLERY_REQUEST&&resultCode==RESULT_OK&&data!=null){
            Uri filePath=data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                Bitmap converetdImage = getResizedBitmap(bitmap, 200);
                profileImage.setImageBitmap(converetdImage);
                imagedata = imageToString(converetdImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //*hedhi el on click taa el sign up
    //onclick sar fl xml
    public void inscription1(View view){
        final ProgressDialog progressDialog = new ProgressDialog(this); // mta3 chargement
        progressDialog.setMessage("Chargement...");
        progressDialog.show();
        //lhne chnaabaathou essm el fichier php wles données mtaana lel serveur
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL_iNSCRIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //ki tji reponse men 3and serveur
                        progressDialog.dismiss();
                        if(response.equals("SUCCES")){
                            Toast.makeText(getApplicationContext(),"You are a user now ",Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(SignUp.this, response, Toast.LENGTH_LONG).show();
                        }

                    }
                },
                //erreur tab3in connexion maa el serveur
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignUp.this,error.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }){
            @Override
            protected Map<String, String> getParams()  {
                // les var eli bech teb3athhom
                Map<String, String> params = new HashMap<String,String>();
                params.put("email",email.getText().toString());
                params.put("password",pass.getText().toString());
                params.put("nom",nom.getText().toString());
                params.put("prenom",prenom.getText().toString());
                params.put("num",num.getText().toString());
                params.put("cin",cin.getText().toString());
                params.put("image",imagedata);




                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void annuler(View view) {
        Intent I =new Intent(SignUp.this,MainActivity.class);
        startActivity(I);
    }
    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream outputStream =new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        byte[] imagebytes=outputStream.toByteArray();
        String encodedImage= android.util.Base64.encodeToString(imagebytes, android.util.Base64.DEFAULT);
        return  encodedImage;
    }
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}
