package com.app.irinde;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    public LinearLayout v;
    public Context ctx;
    public JSONObject readStatusSymbol = new JSONObject();
    private JSONArray mDataset;
    public ImageView imgx;
    public MyViewHolder vh;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(Context context, JSONArray myDataset) {
        mDataset = myDataset;
        ctx = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_amakuru, parent, false);
        vh = new MyViewHolder(v);
        imgx = vh.img;
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
            final String link =  currentObj.getString("source_link");
            //holder.img.setImageDrawable(ctx.getDrawable(R.drawable.ic_warning_black_24dp));
                //v.setGravity(Gravity.LEFT);
                //LinearLayout.LayoutParams lyPar = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            holder.tvSender.setText(currentObj.getString("sender"));
            holder.tvAttachmentLink.setText(currentObj.getString("attachment"));
            holder.tvMsg.setText(currentObj.getString("message"));
            holder.tvLink.setText(link);
            holder.tvDate.setText(currentObj.getString("regdate"));
            //loading image attached to text
            Glide.with(ctx)
                    .load(currentObj.getString("attachment"))
                    .placeholder(R.drawable.logo_24dp) //placeholder
                    .centerCrop()
                    .error(R.drawable.logo_24dp) //error
                    .into(holder.img);
            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent(ctx,ViewImage.class);
                    intent.putExtra("image",holder.tvAttachmentLink.getText().toString());
                    ctx.startActivity(intent);
                }
            });
            //new Background(holder.img).execute(new String[]{currentObj.getString("attachment")});
            holder.tvSource.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
                }
            });
            holder.tvMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog dialog = new AlertDialog.Builder(ctx).create();
                    try {
                        dialog.setTitle("Imvo y'ubutumwa");
                        dialog.setMessage(currentObj.getString("source_description"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Funga", new DialogInterface.OnClickListener() {
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
        public TextView tvMsg, tvLink,tvSource, tvDate,tvSender,tvAttachmentLink;
        public ImageView img;

        public MyViewHolder(View view) {
            super(view);
            tvSender = view.findViewById(R.id.tvSender);
            tvAttachmentLink = view.findViewById(R.id.attachmentLink);
            tvMsg = view.findViewById(R.id.tvMessage);
            tvLink = view.findViewById(R.id.tvLink);
            tvMsg = view.findViewById(R.id.tvMessage);
            tvSource = view.findViewById(R.id.source);
            tvDate = view.findViewById(R.id.tvRegdate);
            img = view.findViewById(R.id.imgView);


            //tvMsg = lny.findViewById(R.id.tvRecyclerDate);
        }
    }
}

