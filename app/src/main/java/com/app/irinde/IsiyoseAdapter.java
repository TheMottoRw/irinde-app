package com.app.irinde;


import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class IsiyoseAdapter extends RecyclerView.Adapter<IsiyoseAdapter.MyViewHolder> {
    public LinearLayout v;
    public Context ctx;
    public JSONObject readStatusSymbol = new JSONObject();
    private JSONArray mDataset;

    // Provide a suitable constructor (depends on the kind of dataset)
    public IsiyoseAdapter(Context context, JSONArray myDataset) {
        mDataset = myDataset;
        ctx = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public IsiyoseAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_isiyose, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        try {
            JSONObject currentObj = mDataset.getJSONObject(position);
           // v.setBackground(ctx.getDrawable(R.drawable.logo_24dp));
            //v.setGravity(Gravity.LEFT);
            //LinearLayout.LayoutParams lyPar = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            holder.tvCountry.setText(currentObj.getString("country"));
            holder.tvAffected.setText(currentObj.getString("confirmed"));
            holder.tvRecovered.setText(currentObj.getString("recovered"));
            holder.tvDeaths.setText(currentObj.getString("deaths"));
            holder.tvDate.setText("Isaha "+currentObj.getString("last_update").replace("T"," ").substring(0,(currentObj.getString("last_update").length()> 16?16:currentObj.getString("last_update").length()-1)));
            if(currentObj.getString("region").equals("") || currentObj.getString("region").equals(currentObj.getString("country")))
               holder.tvRegion.setVisibility(View.GONE);
            else
                holder.tvRegion.setText(currentObj.getString("region"));

        } catch (JSONException ex) {

        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvAffected,tvRecovered,tvDeaths, tvDate,tvCountry,tvRegion;
        public ImageView img;
        public Button btnSave;

        public MyViewHolder(View view) {
            super(view);
            tvCountry = view.findViewById(R.id.tvCountry);
            tvRegion = view.findViewById(R.id.tvRegion);
            tvAffected = view.findViewById(R.id.tvAffected);
            tvRecovered = view.findViewById(R.id.tvRecovered);
            tvDeaths = view.findViewById(R.id.tvDeaths);
            tvDate = view.findViewById(R.id.tvRegdate);
            //btnSave = view.findViewById(R.id.btnBika);
        }
    }
}


