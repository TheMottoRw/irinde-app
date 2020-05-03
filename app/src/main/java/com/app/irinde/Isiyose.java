package com.app.irinde;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.PrecomputedText;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Isiyose extends Fragment {
private Context ctx;
private Helper helper;
private ProgressDialog progressDialog;
private RecyclerView recyclerView;
private TextView tvBlue,tvGreen,tvRed,tvSelectedDate;
private EditText edtSearch;
private Button btnSearch,btnFilterByDate;
public JSONArray array,searchArray;
private DatePickerDialog datePickerDialog;
private DatePicker datePicker;
private RecyclerView.LayoutManager layoutManager;
public String selDate  = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_isiyose, container, false);
        ctx = view.getContext();
        recyclerView = view.findViewById(R.id.recyclerIsiyose);
                tvBlue = view.findViewById(R.id.tvAffectedColor);
                tvGreen = view.findViewById(R.id.tvRecoveredColor);
                tvRed = view.findViewById(R.id.tvDeathsColor);
                edtSearch = view.findViewById(R.id.edtSearch);
                btnSearch = view.findViewById(R.id.btnSearch);
                tvSelectedDate = view.findViewById(R.id.tvSelectedDate);
        btnFilterByDate = view.findViewById(R.id.btnFilterByDate);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //set width for textview
        setDisplayForTextview();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchKeyword();
            }
        });

        final Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(ctx, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                final String chosenDate = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                loadData(chosenDate);
                datePickerDialog.dismiss();
                //Toast.makeText(ctx,"Selected date "+chosenDate, Toast.LENGTH_LONG).show();
                //activitydate.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        btnFilterByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
                    datePickerDialog.show();
            }
        });
        helper = new Helper(ctx);
        array = new JSONArray();
        searchArray = new JSONArray();
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setMessage("Kumanura amakuru...");
        if (helper.isNetworkConnected())
            loadData("");
        else Toast.makeText(ctx,"Nta interineti ufite",Toast.LENGTH_LONG).show();

        return view;
    }
    public void setDisplayForTextview(){
        DisplayMetrics display = ctx.getResources().getDisplayMetrics();
        int width = display.widthPixels/3;
        int filterWidth = display.widthPixels/10,
                searchBoxWidth = filterWidth*6,
                searchBtnWidth = filterWidth*2,
                searchHeight = 80;
        LinearLayout.LayoutParams pars = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT),
                searchBoxPars = new LinearLayout.LayoutParams(searchBoxWidth, searchHeight),
                btnPars = new LinearLayout.LayoutParams(searchBtnWidth, searchHeight);
        tvBlue.setLayoutParams(pars);
        tvGreen.setLayoutParams(pars);
        tvRed.setLayoutParams(pars);
        edtSearch.setLayoutParams(searchBoxPars);
        btnSearch.setLayoutParams(btnPars);
        btnFilterByDate.setLayoutParams(btnPars);
    }
    public void loadData(String date) {
        progressDialog.show();
        final String url = helper.getHost("remote")+"/requests/reports.php?cate=processtoarray&date="+getDate(date);
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
                            array = response.getJSONArray("response");
                            tvSelectedDate.setText("Amakuru yo kuwa "+response.getString("date"));
                            if(array.length()==0) {
                                Toast.makeText(ctx,"Nta amakuru abonetse", Toast.LENGTH_LONG).show();
                                AlertDialog dialog = new AlertDialog.Builder(ctx).create();
                                dialog.setTitle("Hitamo indi itariki");
                                dialog.setMessage("Raporo yo kuwa "+selDate+" ntibonetse,kanda INDI TARIKI urebe raporo y'itariki ushaka");
                                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Indi tariki", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        btnFilterByDate.callOnClick();
                                    }
                                });
                                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Funga", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
                            }
                            else{
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                    array.remove(0);
                                }
                                recyclerAdapter(array);
                            }
                            progressDialog.dismiss();
                        }catch (JSONException ex){
                            ex.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Load world affected err", "Err "+error.getMessage());
                    }
                }
        );

// add it to the RequestQueue
        queue.add(getRequest);
    }
    public void searchKeyword() {
        if(edtSearch.getText().toString().equals("")){
            loadData("");
        } else {
            try {
                searchArray = new JSONArray();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    //Log.d("match result", obj.getString("country").toLowerCase() + "= " + edtSearch.getText().toString() + " Result " + obj.getString("country").contains(edtSearch.getText().toString()));
                    if (obj.getString("country").toLowerCase().contains(edtSearch.getText().toString().toLowerCase().trim())) {
                        searchArray.put(obj);
                        Log.d("matched ", obj.toString());
                    }
                }
                recyclerAdapter(searchArray);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }
    private void recyclerAdapter(JSONArray arr){
        IsiyoseAdapter isiyoseAdapter = new IsiyoseAdapter(ctx,arr);
        recyclerView.setAdapter(isiyoseAdapter);
    }
    String getDate(String init) {
        String date = "";

        Date currentDate = new Date();
        //currentDate.setTime(currentDate.getTime() - 1000*3600*24);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (init.equals("") || init.equals(null)) {
                date = sdf.format(currentDate);
            } else {
                date = sdf.format(sdf.parse(init));
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        selDate = date;
        return date;
    }

}
