package com.example.hp.akura;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import helper.CheckNetworkStatus;
import helper.HttpJsonParser;

public class ItemListing extends AppCompatActivity {
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_SERIALNO = "serialno";
    private static final String KEY_TYPE = "type";
    private static final String BASE_URL = "http://10.10.2.104/asset/";
    private ArrayList<HashMap<String, String>> itemList;
    private ListView itemListView;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_listing);
        itemListView = (ListView) findViewById(R.id.itemList);
        new FetchMoviesAsyncTask().execute();

    }

    /**
     * Fetches the list of movies from the server
     */


    private class FetchMoviesAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(ItemListing.this);
            pDialog.setMessage("Loading items. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "fetch_all_items.php", "GET", null);
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONArray items;
                if (success == 1) {
                    itemList = new ArrayList<>();
                    items = jsonObject.getJSONArray(KEY_DATA);
                    //Iterate through the response and populate movies list
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject movie = items.getJSONObject(i);
                        Integer serialNo = movie.getInt(KEY_SERIALNO);
                        String type = movie.getString(KEY_TYPE);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(KEY_SERIALNO, serialNo.toString());
                        map.put(KEY_TYPE, type);
                        itemList.add(map);
                    }
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
                    populateItemList();
                }
            });
        }

    }

    /**
     * Updating parsed JSON data into ListView
     * */
    private void populateItemList() {
        ListAdapter adapter = new SimpleAdapter(
                ItemListing.this,itemList,
                R.layout.list_item, new String[]{KEY_SERIALNO,
                KEY_TYPE},
                new int[]{R.id.serialNo, R.id.type});
        // updating listview
        itemListView.setAdapter(adapter);
        //Call MovieUpdateDeleteActivity when a movie is clicked
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Check for network connectivity
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    String serialNo = ((TextView) view.findViewById(R.id.serialNo))
                            .getText().toString();
                    Intent intent = new Intent(getApplicationContext(),
                            ItemUpdateDelete.class);
                    intent.putExtra(KEY_SERIALNO, serialNo);
                    startActivityForResult(intent, 20);

                } else {
                    Toast.makeText(ItemListing.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();

                }


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 20) {
            // If the result code is 20 that means that
            // the user has deleted/updated the movie.
            // So refresh the movie listing
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

}
