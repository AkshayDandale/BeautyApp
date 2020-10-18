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
import com.culexy.mehndiandrangoli.Modal.FavouriteModal;
import com.culexy.mehndiandrangoli.R;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {


    private Context context;
    private List<FavouriteModal> favouriteModalList;
    iRecyclerClickListener clickListener;


    public FavouriteAdapter(Context context, List<FavouriteModal> favouriteModalList, iRecyclerClickListener clickListener) {
        this.context = context;
        this.favouriteModalList = favouriteModalList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_grid_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String currentImage = favouriteModalList.get(position).getFavouriteImageList().get(position);
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
        return favouriteModalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        ProgressBar progressBar;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_fav_don);
            progressBar = itemView.findViewById(R.id.progressBar_don);
            itemView.setOnClickListener(this);



        }

        @Override
        public void onClick(View v) {

            clickListener.onClick(v, getAdapterPosition());

        }
    }


}
