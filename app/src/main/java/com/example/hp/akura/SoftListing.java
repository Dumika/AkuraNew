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

public class SoftListing extends AppCompatActivity {

    private ListView itemsListView;
    private ProgressDialog progressDialog;
    private JSONArray itemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_listing);

        itemsListView = findViewById(R.id.itemsList);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading asset...");

        sendGetItemsRequest();

    }

    void updateList() {
        itemsListView.setAdapter(new ItemsListAdapter());
    }

    void sendGetItemsRequest() {
        progressDialog.show();
        DataRequester dataRequester = new DataRequester(
                SoftListing.this,
                Request.Method.GET,
                Constants.SOFT_GET_URL,
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
                            new AlertDialog.Builder(SoftListing.this)
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
                        new AlertDialog.Builder(SoftListing.this)
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
        progressDialog.setMessage("Deleting item...");
        progressDialog.show();

        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constants.SOFT_DELETE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String strResponse) {
                        progressDialog.dismiss();
                        try {
                            JSONObject response = new JSONObject(strResponse);
                            Log.d("MY_APP", response.toString());
                            if (response.getInt("success") == 1) {
                                new AlertDialog.Builder(SoftListing.this)
                                        .setTitle("Success")
                                        .setMessage("Successfully deleted the item.")
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
                                new AlertDialog.Builder(SoftListing.this)
                                        .setTitle("Error")
                                        .setMessage("Error deleting item\n" + response.getString("message"))
                                        .setPositiveButton(
                                                "Retry",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        sendDeleteRequest(serialNo);
                                                    }
                                                }
                                        ).show();
                            }
                        } catch (JSONException e) {
                            new AlertDialog.Builder(SoftListing.this)
                                    .setTitle("Error")
                                    .setMessage("Error deleting item\nError parsing data")
                                    .setPositiveButton(
                                            "Retry",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    sendDeleteRequest(serialNo);
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
                        new AlertDialog.Builder(SoftListing.this)
                                .setTitle("Error")
                                .setMessage("Error deleting item\n" + error.toString())
                                .setPositiveButton(
                                        "Retry",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                sendDeleteRequest(serialNo);
                                            }
                                        }
                                ).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("SerialNo", serialNo);
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
                view = getLayoutInflater().inflate(R.layout.soft_asset_list_item, viewGroup, false);
                TextView serialno = view.findViewById(R.id.serialno);
                TextView software = view.findViewById(R.id.software);
                TextView productkey = view.findViewById(R.id.productkey);
                TextView rdate = view.findViewById(R.id.rdate);
                TextView warranty = view.findViewById(R.id.warranty);
                TextView pdate = view.findViewById(R.id.pdate);
                TextView pdetails = view.findViewById(R.id.pdetails);

                Button deleteBtn = view.findViewById(R.id.button_delete);
                Button editBtn = view.findViewById(R.id.button_edit);

                try {
                    final String s_serialno = getItem(i).getString("SerialNo");
                    final String s_software = getItem(i).getString("Software");
                    final String s_productkey = getItem(i).getString("ProductKey");
                    final String s_rdate = getItem(i).getString("RDate");
                    final String s_warranty = getItem(i).getString("Warranty");
                    final String s_pdate = getItem(i).getString("PDate");
                    final String s_pdetails = getItem(i).getString("PDetails");

                    serialno.setText(String.format("Serial No : %s", s_serialno));
                    software.setText(String.format("Software : %s", s_software));
                    productkey.setText(String.format("Product Key : %s", s_productkey));
                    rdate.setText(String.format("Renewal Date : %s", s_rdate));
                    warranty.setText(String.format("Warrantyy : %s", s_warranty));
                    pdate.setText(String.format("Purchased Date : %s", s_pdate));
                    pdetails.setText(String.format("Purchased Details : %s", s_pdetails));

                    deleteBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                sendDeleteRequest(getItem(i).getString("SerialNo"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    editBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(SoftListing.this, SoftUpdate.class);
                            intent.putExtra("serialno", s_serialno);
                            intent.putExtra("software", s_software);
                            intent.putExtra("productkey", s_productkey);
                            intent.putExtra("rdate", s_rdate);
                            intent.putExtra("warranty", s_warranty);
                            intent.putExtra("pdate", s_pdate);
                            intent.putExtra("pdetails", s_pdetails);

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
