package com.example.hp.akura;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import helper.CheckNetworkStatus;
import helper.HttpJsonParser;

public class ItemView extends AppCompatActivity {
//    private static final String KEY_SUCCESS = "success";
//    private static final String KEY_DATA = "data";
//    private static final String KEY_SerialNo = "SerialNo";
//    private static final String KEY_Type = "Type";
//    //private static final String BASE_URL = "http://192.168.43.72/movies/";
//    private static final String BASE_URL = "http://192.168.8.108/android/goods/";
//    private ArrayList<HashMap<String, String>> ItemList;
//    private ListView ItemListView;
//    private ProgressDialog pDialog;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_item_view);

//        ItemListView = (ListView) findViewById(R.id.ItemList);
//        new FetchMoviesAsyncTask().execute();

//    private class FetchMoviesAsyncTask extends AsyncTask<String, String, String> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            //Display progress bar
//            pDialog = new ProgressDialog(ItemView.this);
//            pDialog.setMessage("Loading items. Please wait...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(false);
//            pDialog.show();
//        }
//        @Override
//        protected String doInBackground(String... params) {
//            HttpJsonParser httpJsonParser = new HttpJsonParser();
//            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
//                    BASE_URL + "ItemView.php", "GET", null);
//            try {
//                int success = jsonObject.getInt(KEY_SUCCESS);
//                JSONArray Items;
//                if (success == 1) {
//                    ItemList = new ArrayList<>();
//                    Items = jsonObject.getJSONArray(KEY_DATA);
//                    //Iterate through the response and populate movies list
//                    for (int i = 0; i < Items.length(); i++) {
//                        JSONObject Item = Items.getJSONObject(i);
//                        String SerialNo = Item.getString(KEY_SerialNo);
//                        String Type = Item.getString(KEY_Type);
//                        HashMap<String, String> map = new HashMap<String, String>();
//                        map.put(KEY_SerialNo, SerialNo);
//                        map.put(KEY_Type, Type);
//                        ItemList.add(map);
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        protected void onPostExecute(String result) {
//            pDialog.dismiss();
//            runOnUiThread(new Runnable() {
//                public void run() {
//                    populateItemList();
//                }
//            });
//        }
//
//    }
//
//    /**
//     * Updating parsed JSON data into ListView
//     * */
//    private void populateItemList() {
//        ListAdapter adapter = new SimpleAdapter(
//                ItemView.this, ItemList,
//                R.layout.list_item, new String[]{KEY_SerialNo,
//                KEY_Type},
//                new int[]{R.id.SerialNo, R.id.Type});
//        // updating listview
//        ItemListView.setAdapter(adapter);
//        //Call MovieUpdateDeleteActivity when a movie is clicked
//        ItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                //Check for network connectivity
//                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
//                    String ItemId = ((TextView) view.findViewById(R.id.SerialNo))
//                            .getText().toString();
//                    Intent intent = new Intent(getApplicationContext(),
//                            ItemUpdateDelete.class);
//                    intent.putExtra(KEY_SerialNo, ItemId);
//                    startActivityForResult(intent, 20);
//
//                } else {
//                    Toast.makeText(ItemView.this,
//                            "Unable to connect to internet",
//                            Toast.LENGTH_LONG).show();
//
//                }
//
//
//            }
//        });
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == 20) {
//            // If the result code is 20 that means that
//            // the user has deleted/updated the movie.
//            // So refresh the movie listing
//            Intent intent = getIntent();
//            finish();
//            startActivity(intent);
//        }
//    }

    ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);

        listView = findViewById(R.id.ItemList);

        sendRequest();
    }

    void sendRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET,
                "localhost/android/v1/ItemsGet.php",
                null,
                new Response.Listener <JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Toast.makeText(ItemView.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ItemView.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(jsonObjectRequest);
    }

    void updateList() {

    }
}
