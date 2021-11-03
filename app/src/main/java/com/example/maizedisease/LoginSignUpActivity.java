package com.example.maizedisease;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class LoginSignUpActivity extends AppCompatActivity {
    private Button btnLogin, btnSignup;
    private EditText etUserNameLogin, etPasswordLogin, etUserNameSignUp, etPasswordSignUp, etEmailSignUp, etConfirmPasswordSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sign_up);
        Global.setURL("http://87f9-203-129-219-162.ngrok.io/data/");

        btnLogin = findViewById(R.id.loginBtn);
        etUserNameLogin = findViewById(R.id.userNameLoginET);
        etPasswordLogin = findViewById(R.id.passwordLoginET);

        btnSignup = findViewById(R.id.signUpBtn);
        etUserNameSignUp = findViewById(R.id.userNameSignUpET);
        etPasswordSignUp = findViewById(R.id.passwordSignUpET);
        etEmailSignUp = findViewById(R.id.emailSignUPET);
        etConfirmPasswordSignUp = findViewById(R.id.confirmPasswordSignUpET);

        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("RANDOM", "Button clicked");
                String userNameLogin=etUserNameLogin.getText().toString();
                String passwordLogin=HashPasswords(etPasswordLogin.getText().toString());
                SendLoginReq(userNameLogin, passwordLogin);
            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userNameSignUp= etUserNameSignUp.getText().toString();
                String passwordSignUp=HashPasswords(etPasswordSignUp.getText().toString());
                String confirmPasswordSignUp= HashPasswords(etConfirmPasswordSignUp.getText().toString());
                String emailSignUp= etEmailSignUp.getText().toString();
                Log.d("RANDOM", passwordSignUp+" "+confirmPasswordSignUp);
                if(passwordSignUp.equals(confirmPasswordSignUp)){
                    //send only if passwords match
                    SendSignUpReq(userNameSignUp, emailSignUp, passwordSignUp);
                }
                else{
                    Toast.makeText(getApplicationContext(),
                            "Passwords do not match", Toast.LENGTH_LONG).show();
                }
            }
        });
    }



    public void SendLoginReq(String user_name, String password){
//        String postUrl = "http://10.0.2.2:8000/data/";
//        String postUrl = "http://0ec0-203-129-219-162.ngrok.io/data/";
//        String postUrl = "http://6aed-203-129-219-162.ngrok.io/data/";

        String postUrl = Global.getURL()+"UserLogin/";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject postData = new JSONObject();
        try {
            postData.put("user_name",  user_name);
            postData.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("RANDOM", user_name+" "+password);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl,
                postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("RANDOM", "response received");
                try {
                    if (response.getString("message") != "User Verified") {
                        Global.setUserName(user_name);
                        startActivity(new Intent(LoginSignUpActivity.this, MainActivity.class));
                    }
                    else {
                        Toast.makeText(getApplicationContext(),
                                "Invalid Credentials", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    Log.d("RANDOM", response.toString(4));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("RANDOM", error.toString());
                Log.d("RANDOM", String.valueOf(error.networkResponse.statusCode));
                if(String.valueOf(error.networkResponse.statusCode).equals("401") || String.valueOf(error.networkResponse.statusCode).equals("400")){
                    Toast.makeText(getApplicationContext(),
                            "Invalid Credentials", Toast.LENGTH_LONG).show();
                }
                Log.d("RANDOM", String.valueOf(error.getMessage()));
                error.printStackTrace();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void SendSignUpReq(String user_name, String email, String password){

        String postUrl = Global.getURL()+"UserSignUp/";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject postData = new JSONObject();
        try {
            postData.put("user_name",  user_name);
            postData.put("email", email);
            postData.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl,
                postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("RANDOM", "response received");
                try {
                    if (response.getString("message") != "User Created") {
                        Global.setUserName(user_name);
                        Log.d("RANDOM", "User Created");
                        startActivity(new Intent(LoginSignUpActivity.this, MainActivity.class));
                    }
                    else {
                        Toast.makeText(getApplicationContext(),
                                "Invalid Credentials", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    Log.d("RANDOM", response.toString(4));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("RANDOM", error.toString());
                Log.d("RANDOM", String.valueOf(error.networkResponse.statusCode));
                Log.d("RANDOM", String.valueOf(error.getMessage()));
                error.printStackTrace();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public static String HashPasswords(String pw) {
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(pw.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
//        System.out.println(generatedPassword);
    }
}
