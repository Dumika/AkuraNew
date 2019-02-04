package com.example.hp.akura;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SoftUpdate extends AppCompatActivity {

    private EditText editTextSerialNo, editTextSoftware, editTextProductKey, editTextRDate, editTextWarranty, editTextPDate, editTextPDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_update);

        editTextSerialNo = findViewById(R.id.editTextSerialNo);
        editTextSoftware = findViewById(R.id.editTextSoftware);
        editTextProductKey = findViewById(R.id.editTextProductKey);
        editTextRDate = findViewById(R.id.editTextRDate);
        editTextWarranty = findViewById(R.id.editTextWarranty);
        editTextPDate = findViewById(R.id.editTextPDate);
        editTextPDetails = findViewById(R.id.editTextPDetails);

        Button buttonSave = findViewById(R.id.buttonAdd);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEdits();
            }
        });

        setDataFromIntent();
    }

    void setDataFromIntent() {
        Intent data = getIntent();
        editTextSerialNo.setText(data.getStringExtra("serialno"));
        editTextSoftware.setText(data.getStringExtra("software"));
        editTextProductKey.setText(data.getStringExtra("productkey"));
        editTextRDate.setText(data.getStringExtra("rdate"));
        editTextWarranty.setText(data.getStringExtra("warranty"));
        editTextPDate.setText(data.getStringExtra("pdate"));
        editTextPDetails.setText(data.getStringExtra("pdetails"));
    }

    void saveEdits() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving changes...");
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constants.SOFT_UPDATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getInt("success") == 1) {
                                new AlertDialog.Builder(SoftUpdate.this)
                                        .setTitle("Success")
                                        .setMessage(jsonObject.getString("message"))
                                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                                SoftUpdate.this.setResult(RESULT_OK);
                                                SoftUpdate.this.finish();
                                            }
                                        })
                                        .show();
                            } else {
                                new AlertDialog.Builder(SoftUpdate.this)
                                        .setTitle("Error")
                                        .setMessage("Failed to edit item.")
                                        .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                                saveEdits();
                                            }
                                        })
                                        .show();
                            }
                        } catch (JSONException e) {
                            new AlertDialog.Builder(SoftUpdate.this)
                                    .setTitle("Error")
                                    .setMessage("Failed to parse response")
                                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                            saveEdits();
                                        }
                                    })
                                    .show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        new AlertDialog.Builder(SoftUpdate.this)
                                .setTitle("Error")
                                .setMessage(error.toString())
                                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        saveEdits();
                                    }
                                })
                                .show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("SerialNo", editTextSerialNo.getText().toString());
                map.put("Software", editTextSoftware.getText().toString());
                map.put("ProductKey", editTextProductKey.getText().toString());
                map.put("RDate", editTextRDate.getText().toString());
                map.put("Warranty", editTextWarranty.getText().toString());
                map.put("PDate", editTextPDate.getText().toString());
                map.put("PDetails", editTextPDetails.getText().toString());
                return map;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}