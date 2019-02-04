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


public class ItemAdd extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextSerialNo, editTextType, editTextSection, editTextPersonInCharge, editTextQuantity, editTextSupplier;
    private Button buttonAdd;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_add);

        editTextSerialNo = (EditText) findViewById(R.id.editTextSerialNo);
        editTextType = (EditText) findViewById(R.id.editTextType);
        editTextSection = (EditText) findViewById(R.id.editTextSection);
        editTextPersonInCharge = (EditText) findViewById(R.id.editTextPerson);
        editTextQuantity = (EditText) findViewById(R.id.editTextQuantity);
        editTextSupplier = (EditText) findViewById(R.id.editTextSupply);

        buttonAdd = (Button) findViewById(R.id.buttonAdd);

        progressDialog = new ProgressDialog(this);

        buttonAdd.setOnClickListener(this);
    }

    private void registerUser() {
        final String SerialNo = editTextSerialNo.getText().toString().trim();
        final String Type = editTextType.getText().toString().trim();
        final String Section = editTextSection.getText().toString().trim();
        final String PersonInCharge = editTextPersonInCharge.getText().toString().trim();
        final String Quantity = editTextQuantity.getText().toString().trim();
        final String Supplier = editTextSupplier.getText().toString().trim();

        progressDialog.setMessage("Add Item Details...");
        progressDialog.show();

        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                Constants.ITEM_ADD_URL,
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
                params.put("Type", Type);
                params.put("Section", Section);
                params.put("PersonInCharge", PersonInCharge);
                params.put("Quantity", Quantity);
                params.put("Supplier", Supplier);

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




