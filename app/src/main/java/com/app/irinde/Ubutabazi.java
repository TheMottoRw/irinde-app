package com.app.irinde;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Ubutabazi extends Fragment {
    private Context ctx;
    private Helper helper;
    private EditText edtName, edtPhone, edtDescr;
    private Button button;
    private TextView tvgpsinfo;
    private ProgressDialog progressDialog;
    private String latitude,longitude;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ubutabazi, container, false);
        ctx = view.getContext();
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setMessage("Kohereza amakuru...");
        helper = new Helper(ctx);
        edtName = view.findViewById(R.id.edtName);
        edtPhone = view.findViewById(R.id.edtPhone);
        edtDescr = view.findViewById(R.id.edtDescrption);
        button = view.findViewById(R.id.btnOhereza);
        tvgpsinfo = view.findViewById(R.id.tvgpsinfo);
        tvgpsinfo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(ctx).create();
                dialog.setTitle("Set up server");
                final EditText editText = new EditText(ctx);

                dialog.setView(editText);
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        AlertDialog dialogApply = new AlertDialog.Builder(ctx).create();
                        dialogApply.setTitle("Set up server");

                        dialogApply.setButton(DialogInterface.BUTTON_NEGATIVE, "Approve", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                SharedPreferences.Editor sharedPreferences = ctx.getSharedPreferences("server",Context.MODE_PRIVATE).edit();
                                sharedPreferences.putString("server",editText.getText().toString());
                                sharedPreferences.commit();
                                Toast.makeText(ctx,"Server address changed", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialogApply.setButton(DialogInterface.BUTTON_POSITIVE, "Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                    }
                });
                return false;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (helper.isNetworkConnected())
                //load notices
                {
                    // instantiate the location manager, note you will need to request permissions in your manifest

                    // now get the lat/lon from the location and do something with it.

                    sendRequestSupport();
                } else Toast.makeText(ctx, "Nta interineti ufite", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
    public void  getLocation(){
        LocationManager locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);

        // get the last know location from your location manager.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ctx.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ctx.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.

                ActivityCompat.requestPermissions(Objects.requireNonNull(Ubutabazi.super.getActivity()), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                return;
            }else{//permission granted
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                latitude = String.valueOf(location.getLatitude());
                longitude = String.valueOf(location.getLongitude());
                Log.d("latlong",location.getLatitude()+"=>:<="+location.getLongitude());
                Toast.makeText(ctx,"Lat "+location.getLatitude()+" Long "+location.getLongitude(), Toast.LENGTH_LONG).show();
            }
        }}

    public void sendRequestSupport() {//upload local data to cloud
        progressDialog.show();
        final String url = helper.getHost("remote")+"/requests/urgent.php";
        RequestQueue queue = Volley.newRequestQueue(ctx);
// prepare the Request
        StringRequest getRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {
                        Log.d("request support res", res);
                        // display response
                        if (res.equals("ok"))
                            Toast.makeText(ctx, "Kohereza gusaba ubufasha bigenze neza", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(ctx, "Gusaba ubufasha ntibikunze ongera ugerageze", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", "");
                    }
                }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cate", "register");
                params.put("names", edtName.getText().toString());
                params.put("phone", edtPhone.getText().toString());
                params.put("description", edtDescr.getText().toString());
                params.put("latitude", "0.0");
                params.put("longitude", "0.0");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> headers = new HashMap<>();
                return headers;
            }
        };

// add it to the RequestQueue
        queue.add(getRequest);
    }
}
