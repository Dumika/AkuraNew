package com.example.hp.akura;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import helper.CheckNetworkStatus;
import helper.HttpJsonParser;

public class AddHard extends AppCompatActivity {
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_TYPE = "type";
    private static final String KEY_SECTION = "section";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_SUPPLIER = "supplier";
    private static final String BASE_URL = "http://10.10.2.104/asset/";
    private static String STRING_EMPTY = "";
    private EditText typeEditText;
    private EditText sectionEditText;
    private EditText quantityEditText;
    private EditText supplierEditText;
    private String Type;  //
    private String Section;//
    private String Quantity;//
    private String Supplier;//
    private Button addButton;
    private int success;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hard);
        typeEditText = (EditText) findViewById(R.id.ettype);
        sectionEditText = (EditText) findViewById(R.id.etsection);
        quantityEditText = (EditText) findViewById(R.id.etquantity);
        supplierEditText = (EditText) findViewById(R.id.etsupplier);
        addButton = (Button) findViewById(R.id.btnAdd);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    addMovie();
                } else {
                    Toast.makeText(AddHard.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();

                }

            }
        });
    }
    private void addMovie() {
        if (!STRING_EMPTY.equals(typeEditText.getText().toString()) &&
                !STRING_EMPTY.equals(sectionEditText.getText().toString()) &&
                !STRING_EMPTY.equals(quantityEditText.getText().toString()) &&
                !STRING_EMPTY.equals(supplierEditText.getText().toString())) {

            Type = typeEditText.getText().toString();
            Section = sectionEditText.getText().toString();
            Quantity = quantityEditText.getText().toString();
            Supplier = supplierEditText.getText().toString();
            new AddMovieAsyncTask().execute();
        } else {
            Toast.makeText(AddHard.this,
                    "One or more fields left empty!",
                    Toast.LENGTH_LONG).show();

        }


    }

    /**
     * AsyncTask for adding a movie
     */
    private class AddMovieAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display proggress bar
            pDialog = new ProgressDialog(AddHard.this);
            pDialog.setMessage("Adding Item. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            //Populating request parameters
            httpParams.put(KEY_TYPE, Type);
            httpParams.put(KEY_SECTION, Section);
            httpParams.put(KEY_QUANTITY, Quantity);
            httpParams.put(KEY_SUPPLIER, Supplier);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "add_item.php", "POST", httpParams);
            try {
                success = jsonObject.getInt(KEY_SUCCESS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    if (success == 1) {
                        //Display success message
                        Toast.makeText(AddHard.this,
                                "Item Added", Toast.LENGTH_LONG).show();
                        Intent i = getIntent();
                        //send result code 20 to notify about movie update
                        setResult(20, i);
                        //Finish ths activity and go back to listing activity
                        finish();

                    } else {
                        Toast.makeText(AddHard.this,
                                "Some error occurred while adding item",
                                Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }
}

