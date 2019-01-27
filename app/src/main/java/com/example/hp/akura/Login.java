package com.example.hp.akura;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//
//        if(SharedPrefManager.getInstance(this).isLoggedIn()){
//            finish();
//            startActivity(new Intent(this, Home.class));
//            return;
//        }

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        progressDialog = new ProgressDialog(this);
        // progressDialog.setMessage("Please wait...");

        buttonLogin.setOnClickListener(this);

    }

    private void userLogin() {
        final String username = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                // Constants.URL_LOGIN,
                getString(R.string.login),
                new Response.Listener <String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                   //     JSONObject obj = null;
//                        try {
//                            obj = new JSONObject(response);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                        try {
                            JSONObject obj=new JSONObject(response);
                            if (!obj.getBoolean("error")) {

//                                Toast.makeText(
//                                        Login.this,
//                                        "User login successful",
//                                        Toast.LENGTH_LONG
//                                ).show();

                                startActivity(new Intent(getApplicationContext(), Home.class));
                                finish();
                                    SharedPrefManager.getInstance(getApplicationContext())
                                            .userLogin(
                                                    obj.getInt("userid"),
                                                    obj.getString("fullname"),
                                                    obj.getString("username")
                                            );
//                                Toast.makeText(
//                                        Login.this,
//                                        "User login successful",
//                                        Toast.LENGTH_LONG
//                                ).show();

//                                    startActivity(new Intent(getApplicationContext(), Home.class));
//                                    finish();



                                } else {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            obj.getString("message"),
                                            Toast.LENGTH_LONG
                                    ).show();
                                }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        Toast.makeText(
                                getApplicationContext(),
                                error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        ) {
            @Override
            protected Map <String, String> getParams() throws AuthFailureError {
                Map <String, String> params = new HashMap <>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }

        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonLogin) {
            userLogin();
        }
    }
}


//        loading=findViewById(R.id.loading);
//        id=findViewById(R.id.UserId);
//        password=findViewById(R.id.Password);
//        btn_login=findViewById(R.id.btn_login);
//
//        btn_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String mId=id.getText().toString().trim();
//                String mpass=password.getText().toString().trim();
//
//                if(!mId.isEmpty()|| !mpass.isEmpty()){
//                     new Login();
//                }else{
//                    id.setError("Please insert Id");
//                    password.setError("Please insert password");
//                }
//            }
//        });
//
////       private void Login(String id, String password){
////           loading.setVisibility(View.VISIBLE);
////           btn_login.setVisibility(View.GONE);
////            StringRequest stringRequest=new StringRequest(Request.Method,POST,URL_LOGIN,
////                    new Response.Listener<String>(){
////                @Override
////                        public void onResponse(String response){
////                    try{
////                        JSONObject isonObject=new JSONObject(response);
////                        String success = jsonObject.getString("Success");
////                        JSONArray jsonArray = jsonObject.getJSONArray("login");
////
////                        if(success.equals("l")){
////                            for(int i=0;i<jsonArray.length();i++){
////                                JSONObject object=jsonArray.getJSONObject(i);
////                                String
////                            }
////                        }
////
////                    }catch (JSONException e){
////                        e.printStackTrace();
////                    }
////
////                }
////                    },
////                new Response.ErrorListener(){
////                @Override
////                    public void onErrorResponse(VolleyError error){
////
////                }
////                })
////            {
////                @Override
////                protected Map<String, String> getParams()throws AuthFailureError{
////                    return super.getParams();
////                }
////            }
////
////        }
//
//
//
//    }
//}
