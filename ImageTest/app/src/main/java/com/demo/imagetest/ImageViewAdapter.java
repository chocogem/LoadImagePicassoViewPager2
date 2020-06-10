package com.demo.imagetest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.imagetest.model.ImageList;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageViewAdapter extends RecyclerView.Adapter<ImageViewAdapter.ViewHolder> {
    List<ImageList> imageList;
    Context context;

    public ImageViewAdapter(List<ImageList> imageList, Context context) {
        this.imageList = imageList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String uri = imageList.get(position).imageUri;
        String title = imageList.get(position).title;
        Picasso mPicasso = Picasso.with(context);
        mPicasso.setIndicatorsEnabled(true);
        mPicasso.with(context)
                .load(new File(uri))
                .into(holder.imageView);
        holder.titleTv.setText(title);
       // holder.description.setText(ob.getDescription());
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;

        TextView titleTv,description;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.image_show);
            titleTv=(TextView) itemView.findViewById(R.id.txt_title);
        }
    }
}