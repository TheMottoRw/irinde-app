package com.app.irinde;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Covid extends Fragment {
    private ExpandableListView expListView;
    private ExpandableListAdapter listAdapter;
    List<String> listDataHeader,covidTestingTreatment = new ArrayList<>();
    HashMap<String, List<String>> listDataChild;
    private Context ctx;
    private Helper helper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_covid, container, false);
        ctx = view.getContext();
        // get the listview
        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);
         helper = new Helper(ctx);
        //loading data,reason is sometime it may change & need to update
        loadData();
        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(ctx, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        return view;

    }
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add(ctx.getString(R.string.header_covid_info));
        listDataHeader.add(ctx.getString(R.string.header_covid_summary));
        listDataHeader.add(ctx.getString(R.string.header_covid_kwirinda_kwanduza));
        listDataHeader.add(ctx.getString(R.string.header_covid_kwirinda_gukwirakwiza));
        listDataHeader.add(ctx.getString(R.string.header_covid_ibyago_kwandura));
        listDataHeader.add(ctx.getString(R.string.header_covid_kwirinda_wegereye_umurwayi));
        listDataHeader.add(ctx.getString(R.string.header_covid_kwita_abarwayi));
        listDataHeader.add(ctx.getString(R.string.gukingirwakuvurwa));

        // Adding child data
        List<String> covidInfo = new ArrayList<String>();
        covidInfo.add(ctx.getString(R.string.body_covid_info));

        List<String> covidSummary = new ArrayList<String>();
        covidSummary.add(ctx.getString(R.string.body_covid_summary));

        List<String> covidKwanduza = new ArrayList<String>();
        covidKwanduza.add(ctx.getString(R.string.body_covid_kwirinda_kwanduza));

        List<String> covidGukwirakwiza= new ArrayList<String>();
        covidGukwirakwiza.add(ctx.getString(R.string.body_covid_kwirinda_gukwirakwiza));

        List<String> covidIbyago= new ArrayList<String>();
        covidIbyago.add(ctx.getString(R.string.body_covid_ibyago_kwandura));

        List<String> covidUmurwayi = new ArrayList<String>();
        covidUmurwayi.add(ctx.getString(R.string.body_covid_kwirinda_wegereye_umurwayi));

        List<String> covidUmuganga = new ArrayList<String>();
        covidUmuganga.add(ctx.getString(R.string.body_covid_kwita_abarwayi));



        listDataChild.put(listDataHeader.get(0), covidInfo); // Header, Child data
        listDataChild.put(listDataHeader.get(1), covidSummary);
        listDataChild.put(listDataHeader.get(2), covidKwanduza);
        listDataChild.put(listDataHeader.get(3), covidGukwirakwiza);
        listDataChild.put(listDataHeader.get(4), covidIbyago);
        listDataChild.put(listDataHeader.get(5), covidUmurwayi);
        listDataChild.put(listDataHeader.get(6), covidUmuganga);
    }
    public void loadData() {
        final String url = helper.getHost("remote")+"/requests/reports.php?cate=treatment";
        Log.d("world affected request","Send world affected request "+url);

        RequestQueue queue = Volley.newRequestQueue(ctx);
// prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Affected states", response.toString());
                        //sync on local storage
                        try{
                            JSONArray array = response.getJSONArray("response");
                            if(array.length()==0) Toast.makeText(ctx,"Nta amakuru abonetse", Toast.LENGTH_LONG).show();
                            else{
                                covidTestingTreatment.add(array.getJSONObject(0).getString("testing")+"\n\n"+array.getJSONObject(0).getString("treatment"));
                                listDataChild.put(listDataHeader.get(7),covidTestingTreatment);

                            }
                        }catch (JSONException ex){
                            ex.printStackTrace();
                        }

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

}
