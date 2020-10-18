package com.culexy.mehndiandrangoli.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.culexy.mehndiandrangoli.Interface.iRecyclerClickListener;
import com.culexy.mehndiandrangoli.Modal.GridModal;
import com.culexy.mehndiandrangoli.Modal.RangoliGridModal;
import com.culexy.mehndiandrangoli.Modal.RangoliModal;
import com.culexy.mehndiandrangoli.R;

import java.util.ArrayList;
import java.util.List;

public class RangoliGridAdapter extends RecyclerView.Adapter<RangoliGridAdapter.ImageViewHolder> {

    ArrayList<String> urlList;
    Context context;
    private List<RangoliGridModal> rangoliGridModalList ;
    iRecyclerClickListener clickListener;

    public RangoliGridAdapter(Context context, List<RangoliGridModal> rangoliGridModalList, iRecyclerClickListener clickListener) {
        this.context = context;
        this.rangoliGridModalList = rangoliGridModalList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public RangoliGridAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rangoli_grid_item_layout, parent, false);
        return new RangoliGridAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RangoliGridAdapter.ImageViewHolder holder, int position) {

        String currentImage = rangoliGridModalList.get(position).getRangoliallImageList().get(position);
        ImageView imageView = holder.imageView;
        final ProgressBar progressBar = holder.progressBar;

        Glide.with(context).load(currentImage).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(imageView);

    }

    @Override
    public int getItemCount() {
        return rangoliGridModalList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        ProgressBar progressBar;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.rangoli_grid_image);
            progressBar = itemView.findViewById(R.id.progressBar);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            clickListener.onClick(v, getAdapterPosition());
        }
    }
}
