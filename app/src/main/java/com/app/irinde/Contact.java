package com.app.irinde;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Contact extends AppCompatActivity {
    private Helper helper;
    private ProgressDialog progressDialog;
    private RecyclerView listView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        listView = findViewById(R.id.listView);
        layoutManager = new LinearLayoutManager(Contact.this);
        listView.setLayoutManager(layoutManager);
        listView.setHasFixedSize(true);
        helper = new Helper(Contact.this);
        progressDialog = new ProgressDialog(Contact.this);
        progressDialog.setMessage("Kumanura nomero...");
        //arrow back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        loadData();

    }

    public void loadData() {
        progressDialog.show();
        final String url = helper.getHost("remote") + "/requests/contacts.php?cate=list";
        Log.d("world affected request", "Send world affected request " + url);

        RequestQueue queue = Volley.newRequestQueue(this);
// prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Affected states", response.toString());
                        //sync on local storage
                        try {
                            JSONArray array = response.getJSONArray("response");
                            if (array.length() == 0)
                                Toast.makeText(Contact.this, "Nta amakuru abonetse", Toast.LENGTH_LONG).show();
                            else {
                                ContactAdapter adapter = new ContactAdapter(Contact.this, array);
                                listView.setAdapter(adapter);
                            }
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.e("Load world affected err", error.getMessage());
                    }
                }
        );

// add it to the RequestQueue
        queue.add(getRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        String msg = "";
        int id = item.getItemId();
        Fragment fragment;
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
