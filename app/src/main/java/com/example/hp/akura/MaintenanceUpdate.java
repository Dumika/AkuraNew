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

public class MaintenanceUpdate extends AppCompatActivity {
    private EditText editTextMId, editTextType, editTextSProvider, editTextDate, editTextPayment, editTextDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_update);

        editTextMId = findViewById(R.id.editTextMId);
        editTextType = findViewById(R.id.editTextType);
        editTextSProvider = findViewById(R.id.editTextSProvider);
        editTextDate = findViewById(R.id.editTextDate);
        editTextPayment = findViewById(R.id.editTextPayment);
        editTextDescription = findViewById(R.id.editTextDescription);

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
        editTextMId.setText(data.getStringExtra("mid"));
        editTextType.setText(data.getStringExtra("type"));
        editTextSProvider.setText(data.getStringExtra("sprovider"));
        editTextDate.setText(data.getStringExtra("date"));
        editTextPayment.setText(data.getStringExtra("payment"));
        editTextDescription.setText(data.getStringExtra("description"));
    }

    void saveEdits() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving changes...");
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constants.MAIN_UPDATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getInt("success") == 1) {
                                new AlertDialog.Builder(MaintenanceUpdate.this)
                                        .setTitle("Success")
                                        .setMessage(jsonObject.getString("message"))
                                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                                MaintenanceUpdate.this.setResult(RESULT_OK);
                                                MaintenanceUpdate.this.finish();
                                            }
                                        })
                                        .show();
                            } else {
                                new AlertDialog.Builder(MaintenanceUpdate.this)
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
                            new AlertDialog.Builder(MaintenanceUpdate.this)
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
                        new AlertDialog.Builder(MaintenanceUpdate.this)
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
                map.put("MId", editTextMId.getText().toString());
                map.put("Type", editTextType.getText().toString());
                map.put("SProvider", editTextSProvider.getText().toString());
                map.put("Date", editTextDate.getText().toString());
                map.put("Payment", editTextPayment.getText().toString());
                map.put("Description", editTextDescription.getText().toString());
                return map;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}
