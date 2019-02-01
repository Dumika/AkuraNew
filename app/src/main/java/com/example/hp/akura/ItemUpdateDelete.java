package com.example.hp.akura;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class ItemUpdateDelete extends AppCompatActivity {
    private static String STRING_EMPTY = "";
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_SERIALNO = "serialno";
    private static final String KEY_TYPE = "type";
    private static final String KEY_SECTION = "section";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_SUPPLIER = "supplier";
    private static final String BASE_URL = "http://10.10.2.104/asset/";
    private String serialNo;
    private EditText typeEditText;
    private EditText sectionEditText;
    private EditText quantityEditText;
    private EditText supplierEditText;
    private String Type;
    private String Section;
    private String Quantity;
    private String Supplier;
    private Button deleteButton;
    private Button updateButton;
    private int success;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_update_delete);
        Intent intent = getIntent();
        typeEditText = (EditText) findViewById(R.id.txttypeUpdate);
        sectionEditText = (EditText) findViewById(R.id.txtsectionUpdate);
        quantityEditText = (EditText) findViewById(R.id.txtquantityUpdate);
        supplierEditText = (EditText) findViewById(R.id.txtsupplierUpdate);

        serialNo = intent.getStringExtra(KEY_SERIALNO);
        new FetchMovieDetailsAsyncTask().execute();
        deleteButton = (Button) findViewById(R.id.btnDelete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDelete();
            }
        });
        updateButton = (Button) findViewById(R.id.btnUpdate);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    updateMovie();

                } else {
                    Toast.makeText(ItemUpdateDelete.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();

                }

            }
        });


    }

    /**
     * Fetches single movie details from the server
     */
    private class FetchMovieDetailsAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(ItemUpdateDelete.this);
            pDialog.setMessage("Loading Item Details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_SERIALNO, serialNo);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "get_item_details.php", "GET", httpParams);
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONObject item;
                if (success == 1) {
                    //Parse the JSON response
                    item = jsonObject.getJSONObject(KEY_DATA);
                    Type = item.getString(KEY_TYPE);
                    Section = item.getString(KEY_SECTION);
                    Quantity = item.getString(KEY_QUANTITY);
                    Supplier = item.getString(KEY_SUPPLIER);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    //Populate the Edit Texts once the network activity is finished executing
                    typeEditText.setText(Type);
                    sectionEditText.setText(Section);
                    quantityEditText.setText(Quantity);
                    supplierEditText.setText(Supplier);

                }
            });
        }


    }

    /**
     * Displays an alert dialogue to confirm the deletion
     */
    private void confirmDelete() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ItemUpdateDelete.this);
        alertDialogBuilder.setMessage("Are you sure, you want to delete this item?");
        alertDialogBuilder.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                            //If the user confirms deletion, execute DeleteMovieAsyncTask
                            new DeleteMovieAsyncTask().execute();
                        } else {
                            Toast.makeText(ItemUpdateDelete.this,
                                    "Unable to connect to internet",
                                    Toast.LENGTH_LONG).show();

                        }
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel", null);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * AsyncTask to delete a movie
     */
    private class DeleteMovieAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(ItemUpdateDelete.this);
            pDialog.setMessage("Deleting Item. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            //Set movie_id parameter in request
            httpParams.put(KEY_SERIALNO, serialNo);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "delete_item.php", "POST", httpParams);
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
                        Toast.makeText(ItemUpdateDelete.this,
                                "Item Deleted", Toast.LENGTH_LONG).show();
                        Intent i = getIntent();
                        //send result code 20 to notify about movie deletion
                        setResult(20, i);
                        finish();

                    } else {
                        Toast.makeText(ItemUpdateDelete.this,
                                "Some error occurred while deleting item",
                                Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }

    /**
     * Checks whether all files are filled. If so then calls UpdateMovieAsyncTask.
     * Otherwise displays Toast message informing one or more fields left empty
     */
    private void updateMovie() {


        if (!STRING_EMPTY.equals(typeEditText.getText().toString()) &&
                !STRING_EMPTY.equals(sectionEditText.getText().toString()) &&
                !STRING_EMPTY.equals(quantityEditText.getText().toString()) &&
                !STRING_EMPTY.equals(supplierEditText.getText().toString())) {

            Type = typeEditText.getText().toString();
            Section = sectionEditText.getText().toString();
            Quantity = quantityEditText.getText().toString();
            Supplier = supplierEditText.getText().toString();
            new UpdateMovieAsyncTask().execute();
        } else {
            Toast.makeText(ItemUpdateDelete.this,
                    "One or more fields left empty!",
                    Toast.LENGTH_LONG).show();

        }


    }
    /**
     * AsyncTask for updating a movie details
     */

    private class UpdateMovieAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(ItemUpdateDelete.this);
            pDialog.setMessage("Updating Item. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            //Populating request parameters
            httpParams.put(KEY_SERIALNO, serialNo);
            httpParams.put(KEY_TYPE, Type);
            httpParams.put(KEY_SECTION, Section);
            httpParams.put(KEY_QUANTITY, Quantity);
            httpParams.put(KEY_SUPPLIER,Supplier);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "update_item.php", "POST", httpParams);
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
                        Toast.makeText(ItemUpdateDelete.this,
                                "Item Updated", Toast.LENGTH_LONG).show();
                        Intent i = getIntent();
                        //send result code 20 to notify about movie update
                        setResult(20, i);
                        finish();

                    } else {
                        Toast.makeText(ItemUpdateDelete.this,
                                "Some error occurred while updating item",
                                Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }
}