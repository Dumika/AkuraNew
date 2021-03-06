package com.example.hp.akura;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

public class ItemUpdate extends AppCompatActivity {
    private EditText editTextSerialNo, editTextType, editTextSection, editTextPersonInCharge, editTextQuantity, editTextSupplier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_update);

        editTextSerialNo = findViewById(R.id.editTextSerialNo);
        editTextType = findViewById(R.id.editTextType);
        editTextSection = findViewById(R.id.editTextSection);
        editTextPersonInCharge = findViewById(R.id.editTextPerson);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        editTextSupplier = findViewById(R.id.editTextSupply);
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
        editTextSerialNo.setText(data.getStringExtra("serial_no"));
        editTextType.setText(data.getStringExtra("type"));
        editTextSection.setText(data.getStringExtra("section"));
        editTextPersonInCharge.setText(data.getStringExtra("person_in_charge"));
        editTextQuantity.setText(data.getStringExtra("quantity"));
        editTextSupplier.setText(data.getStringExtra("supplier"));
    }

    void saveEdits() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving changes...");
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constants.ITEM_UPDATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getInt("success") == 1) {
                                new AlertDialog.Builder(ItemUpdate.this)
                                        .setTitle("Success")
                                        .setMessage(jsonObject.getString("message"))
                                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                                ItemUpdate.this.setResult(RESULT_OK);
                                                ItemUpdate.this.finish();
                                            }
                                        })
                                        .show();
                            } else {
                                new AlertDialog.Builder(ItemUpdate.this)
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
                            new AlertDialog.Builder(ItemUpdate.this)
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
                        new AlertDialog.Builder(ItemUpdate.this)
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
                map.put("Type", editTextType.getText().toString());
                map.put("Section", editTextSection.getText().toString());
                map.put("PersonInCharge", editTextPersonInCharge.getText().toString());
                map.put("Quantity", editTextQuantity.getText().toString());
                map.put("Supplier", editTextSupplier.getText().toString());
                return map;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}
