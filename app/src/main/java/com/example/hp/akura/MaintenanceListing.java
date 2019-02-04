package com.example.hp.akura;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MaintenanceListing extends AppCompatActivity {
    private ListView itemsListView;
    private ProgressDialog progressDialog;
    private JSONArray itemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_listing);

        itemsListView = findViewById(R.id.itemsList);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading records...");

        sendGetItemsRequest();

    }

    void updateList() {
        itemsListView.setAdapter(new ItemsListAdapter());
    }

    void sendGetItemsRequest() {
        progressDialog.show();
        DataRequester dataRequester = new DataRequester(
                MaintenanceListing.this,
                Request.Method.GET,
                Constants.MAIN_GET_URL,
                null,
                null
        );
        dataRequester.sendRequest(
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {
                            itemsList = response.getJSONArray("items");
                            updateList();
                        } catch (JSONException e) {
                            new AlertDialog.Builder(MaintenanceListing.this)
                                    .setTitle("Error")
                                    .setMessage("Error parsing data.")
                                    .setNegativeButton(
                                            "Retry",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    sendGetItemsRequest();
                                                }
                                            }
                                    )
                                    .show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        new AlertDialog.Builder(MaintenanceListing.this)
                                .setTitle("Error")
                                .setMessage(error.toString())
                                .setNegativeButton(
                                        "Retry",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                sendGetItemsRequest();
                                            }
                                        }
                                )
                                .show();
                    }
                }
        );
    }

    void sendDeleteRequest(final String serialNo) {
//        try {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Deleting records...");
        progressDialog.show();

        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constants.MAIN_DELETE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String strResponse) {
                        progressDialog.dismiss();
                        try {
                            JSONObject response = new JSONObject(strResponse);
                            Log.d("MY_APP", response.toString());
                            if (response.getInt("success") == 1) {
                                new AlertDialog.Builder(MaintenanceListing.this)
                                        .setTitle("Success")
                                        .setMessage("Successfully deleted the record.")
                                        .setPositiveButton(
                                                "Close",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.dismiss();
                                                    }
                                                }
                                        ).show();
                                sendGetItemsRequest();
                            } else {
                                new AlertDialog.Builder(MaintenanceListing.this)
                                        .setTitle("Error")
                                        .setMessage("Error deleting item\n" + response.getString("message"))
                                        .setPositiveButton(
                                                "Retry",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        sendDeleteRequest(serialNo);////////////////////
                                                    }
                                                }
                                        ).show();
                            }
                        } catch (JSONException e) {
                            new AlertDialog.Builder(MaintenanceListing.this)
                                    .setTitle("Error")
                                    .setMessage("Error deleting item\nError parsing data")
                                    .setPositiveButton(
                                            "Retry",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    sendDeleteRequest(serialNo);///////////////////////
                                                }
                                            }
                                    )
                                    .show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        new AlertDialog.Builder(MaintenanceListing.this)
                                .setTitle("Error")
                                .setMessage("Error deleting item\n" + error.toString())
                                .setPositiveButton(
                                        "Retry",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                sendDeleteRequest(serialNo);////////////////
                                            }
                                        }
                                ).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("MId", serialNo);  ////////////////////
                return map;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    class ItemsListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if (itemsList != null) {
                return itemsList.length();
            }
            return 0;
        }

        @Override
        public JSONObject getItem(int i) {
            if (itemsList != null) {
                try {
                    return itemsList.getJSONObject(i);
                } catch (JSONException ignore) {
                }
            }
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.main_asset_list_item, viewGroup, false);
                TextView mid = view.findViewById(R.id.mid);
                TextView type = view.findViewById(R.id.type);
                TextView sprovider = view.findViewById(R.id.sprovider);
                TextView date = view.findViewById(R.id.date);
                TextView payment = view.findViewById(R.id.payment);
                TextView description = view.findViewById(R.id.description);

                Button deleteBtn = view.findViewById(R.id.button_delete);
                Button editBtn = view.findViewById(R.id.button_edit);

                try {
                    final String s_mid = getItem(i).getString("MId");
                    final String s_type = getItem(i).getString("Type");
                    final String s_sprovider = getItem(i).getString("SProvider");
                    final String s_date = getItem(i).getString("Date");
                    final String s_payment = getItem(i).getString("Payment");
                    final String s_description = getItem(i).getString("Description");

                    mid.setText(String.format("Maintenance Id : %s", s_mid));
                    type.setText(String.format("Type : %s", s_type));
                    sprovider.setText(String.format("Service Provider : %s", s_sprovider));
                    date.setText(String.format("Date : %s", s_date));
                    payment.setText(String.format("Payment : %s", s_payment));
                    description.setText(String.format("Description : %s", s_description));

                    deleteBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                sendDeleteRequest(getItem(i).getString("MId"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    editBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MaintenanceListing.this, MaintenanceUpdate.class);
                            intent.putExtra("mid", s_mid);
                            intent.putExtra("type", s_type);
                            intent.putExtra("sprovider", s_sprovider);
                            intent.putExtra("date", s_date);
                            intent.putExtra("payment", s_payment);
                            intent.putExtra("description", s_description);

                            startActivityForResult(intent, 0);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return view;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            sendGetItemsRequest();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}