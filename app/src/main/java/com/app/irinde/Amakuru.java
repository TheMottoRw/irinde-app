package com.app.irinde;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

public class Amakuru extends Fragment {
    private Context ctx;
    private Helper helper;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_amakuru, container, false);
        ctx = view.getContext();
        recyclerView = view.findViewById(R.id.recyclerAmakuru);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        helper = new Helper(ctx);
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setMessage("Kumanura amakuru...");

        if (helper.isNetworkConnected())
            //load notices
            loadData();
        else Toast.makeText(ctx, "Nta interineti ufite", Toast.LENGTH_LONG).show();

        return view;
    }

    public void loadData() {
        progressDialog.show();
        Log.d("world affected request", "Send world affected request");
        final String url = helper.getHost("remote") + "/requests/notices.php?cate=load";
        RequestQueue queue = Volley.newRequestQueue(ctx);
// prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Notices response", response.toString());
                        //sync on local storage
                        try {
                            JSONArray array = response.getJSONArray("response");
                            if (array.length() == 0)
                                Toast.makeText(ctx, "Nta amakuru abonetse", Toast.LENGTH_LONG).show();
                            else {
                                MyAdapter adapter = new MyAdapter(ctx, array);
                                recyclerView.setAdapter(adapter);
                            }
                            progressDialog.dismiss();
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Load notices err", error.getMessage());
                    }
                }
        );

// add it to the RequestQueue
        queue.add(getRequest);
    }
}
