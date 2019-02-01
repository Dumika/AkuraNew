package com.example.hp.akura;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class MaintenanceAdd extends AppCompatActivity implements View.OnClickListener  {

    private EditText editTextMId, editTextType, editTextSProvider, editTextDate, editTextPayment, editTextDescription;
    private Button buttonAdd;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_add);

        editTextMId = (EditText) findViewById(R.id.editTextMId);
        editTextType = (EditText) findViewById(R.id.editTextType);
        editTextSProvider = (EditText) findViewById(R.id.editTextSProvider);
        editTextDate = (EditText) findViewById(R.id.editTextDate);
        editTextPayment = (EditText) findViewById(R.id.editTextPayment);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);

        buttonAdd = (Button) findViewById(R.id.buttonAdd);

        progressDialog = new ProgressDialog(this);

        buttonAdd.setOnClickListener(this);
    }

    private void registerUser() {
        final String MId = editTextMId.getText().toString().trim();
        final String Type = editTextType.getText().toString().trim();
        final String SProvider = editTextSProvider.getText().toString().trim();
        final String Date = editTextDate.getText().toString().trim();
        final String Payment= editTextPayment.getText().toString().trim();
        final String Description = editTextDescription.getText().toString().trim();

        progressDialog.setMessage("Add Maintenance Details...");
        progressDialog.show();

        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                getString(R.string.AddMain),
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
                params.put("MId", MId);
                params.put("Type", Type);
                params.put("SProvider", SProvider);
                params.put("Date", Date);
                params.put("Payment", Payment);
                params.put("Description", Description);


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
