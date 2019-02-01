package com.example.hp.akura;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SoftAdd extends AppCompatActivity implements View.OnClickListener {


    private EditText editTextSerialNo, editTextSoftware, editTextSProvider, editTextStatus, editTextWarranty, editTextPurchasedD;
    private Button buttonAdd;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_add);


        editTextSerialNo = (EditText) findViewById(R.id.editTextSerialNo);
        editTextSoftware = (EditText) findViewById(R.id.editTextSoftware);
        editTextSProvider = (EditText) findViewById(R.id.editTextSProvider);
        editTextStatus = (EditText) findViewById(R.id.editTextStatus);
        editTextWarranty = (EditText) findViewById(R.id.editTextWarranty);
        editTextPurchasedD = (EditText) findViewById(R.id.editTextPurchasedD);

        buttonAdd = (Button) findViewById(R.id.buttonAdd);

        progressDialog = new ProgressDialog(this);

        buttonAdd.setOnClickListener(this);


    }

    private void registerUser() {
        final String SerialNo = editTextSerialNo.getText().toString().trim();
        final String Software = editTextSoftware.getText().toString().trim();
        final String SProvider = editTextSProvider.getText().toString().trim();
        final String Status = editTextStatus.getText().toString().trim();
        final String Warranty = editTextWarranty.getText().toString().trim();
        final String PurchasedD = editTextPurchasedD.getText().toString().trim();

        progressDialog.setMessage("Add Soft Asset Details...");
        progressDialog.show();

        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                getString(R.string.AddSoft),
                new Response.Listener <String>() {

                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        // String array=response.substring(13);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("SerialNo", SerialNo);
                params.put("Software", Software);
                params.put("SProvider", SProvider);
                params.put("Status", Status);
                params.put("Warranty", Warranty);
                params.put("PurchasedD", PurchasedD);


                return params;
            }
        };

//        RequestQueue requestQueue=Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);


        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);


    }

    @Override
    public void onClick(View view) {
        if (view == buttonAdd)
            registerUser();

    }


}
