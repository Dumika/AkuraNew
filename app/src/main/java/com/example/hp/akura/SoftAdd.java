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

    private EditText editTextSerialNo, editTextSoftware, editTextProductKey, editTextRDate, editTextWarranty, editTextPDate, editTextPDetails;
    private Button buttonAdd;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_add);

        editTextSerialNo = (EditText) findViewById(R.id.editTextSerialNo);
        editTextSoftware = (EditText) findViewById(R.id.editTextSoftware);
        editTextProductKey = (EditText) findViewById(R.id.editTextProductKey);
        editTextRDate = (EditText) findViewById(R.id.editTextRDate);
        editTextWarranty = (EditText) findViewById(R.id.editTextWarranty);
        editTextPDate = (EditText) findViewById(R.id.editTextPDate);
        editTextPDetails = (EditText) findViewById(R.id.editTextPDetails);

        buttonAdd = (Button) findViewById(R.id.buttonAdd);

        progressDialog = new ProgressDialog(this);

        buttonAdd.setOnClickListener(this);
    }

    private void registerUser() {
        final String SerialNo = editTextSerialNo.getText().toString().trim();
        final String Software = editTextSoftware.getText().toString().trim();
        final String ProductKey = editTextProductKey.getText().toString().trim();
        final String RDate = editTextRDate.getText().toString().trim();
        final String Warranty = editTextWarranty.getText().toString().trim();
        final String PDate = editTextPDate.getText().toString().trim();
        final String PDetails = editTextPDetails.getText().toString().trim();

        progressDialog.setMessage("Add Item Details...");
        progressDialog.show();

        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                Constants.SOFT_ADD_URL,
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
                params.put("ProductKey", ProductKey);
                params.put("RDate", RDate);
                params.put("Warranty", Warranty);
                params.put("PDate", PDate);
                params.put("PDetails", PDetails);

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



//    private EditText editTextSerialNo,editTextSoftware, editTextPKey, editTextRDate, editTextWarranty, editTextPDate, editTextPDetails;
//    private Button buttonAdd;
//    private ProgressDialog progressDialog;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_soft_add);
//
//
//        editTextSerialNo = (EditText) findViewById(R.id.editTextSerialNo);
//        editTextSoftware = (EditText) findViewById(R.id.editTextSoftware);
//        editTextPKey = (EditText) findViewById(R.id.editTextPKey);
//        editTextRDate = (EditText) findViewById(R.id.editTextRDate);
//        editTextWarranty = (EditText) findViewById(R.id.editTextWarranty);
//        editTextPDate = (EditText) findViewById(R.id.editTextPDate);
//        editTextPDetails = (EditText) findViewById(R.id.editTextPDetails);
//
//        buttonAdd = (Button) findViewById(R.id.buttonAdd);
//
//        progressDialog = new ProgressDialog(this);
//
//        buttonAdd.setOnClickListener(this);
//
//
//    }
//
//    private void registerUser() {
//        final String SerialNo = editTextSerialNo.getText().toString().trim();
//        final String Software = editTextSoftware.getText().toString().trim();
//        final String ProductKey = editTextPKey.getText().toString().trim();
//        final String RDate = editTextRDate.getText().toString().trim();
//        final String Warranty = editTextWarranty.getText().toString().trim();
//        final String PDate = editTextPDate.getText().toString().trim();
//        final String PDetails = editTextPDetails.getText().toString().trim();
//
//        progressDialog.setMessage("Add Soft Asset Details...");
//        progressDialog.show();
//
//        StringRequest stringRequest=new StringRequest(Request.Method.POST,
////                getString(R.string.AddSoft),
//                Constants.SOFT_ADD_URL,
//                new Response.Listener <String>() {
//
//                    @Override
//                    public void onResponse(String response) {
//                        progressDialog.dismiss();
//                        // String array=response.substring(13);
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//
//                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
//
//                        } catch (JSONException e) {
//
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        progressDialog.hide();
//                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
//
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("SerialNo", SerialNo);
//                params.put("Software", Software);
//                params.put("PKey", ProductKey);
//                params.put("RDate", RDate);
//                params.put("Warranty", Warranty);
//                params.put("PDate", PDate);
//                params.put("PDetails", PDetails);
//
//
//                return params;
//            }
//        };
//
////        RequestQueue requestQueue=Volley.newRequestQueue(this);
////        requestQueue.add(stringRequest);
//
//
//        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
//
//
//    }
//
//    @Override
//    public void onClick(View view) {
//        if (view == buttonAdd)
//            registerUser();
//
//    }
//
//
//}
