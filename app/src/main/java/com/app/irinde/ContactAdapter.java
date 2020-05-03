package com.app.irinde;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {
    public LinearLayout v;
    public Context ctx;
    private JSONArray mDataset;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ContactAdapter(Context context, JSONArray myDataset) {
        mDataset = myDataset;
        ctx = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ContactAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_contact, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        try {
            final JSONObject currentObj = mDataset.getJSONObject(position);

            //v.setGravity(Gravity.LEFT);
            //LinearLayout.LayoutParams lyPar = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            holder.tvName.setText(currentObj.getString("name"));
            holder.tvPhone.setText(currentObj.getString("contact"));

            holder.tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog dialog = new AlertDialog.Builder(ctx).create();
                        dialog.setTitle("Emeza guhamagara");
                        dialog.setMessage(holder.tvName.getText().toString()+"\n"+ holder.tvPhone.getText().toString());

                    dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Hamagara", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ctx.startActivity(new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+holder.tvPhone.getText().toString())));
                            dialog.dismiss();
                        }
                    });
                    dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Funga", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });
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
        public TextView tvName, tvPhone;

        public MyViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.owner);
            tvPhone = view.findViewById(R.id.phone);
            //tvMsg = lny.findViewById(R.id.tvRecyclerDate);
        }
    }
}


