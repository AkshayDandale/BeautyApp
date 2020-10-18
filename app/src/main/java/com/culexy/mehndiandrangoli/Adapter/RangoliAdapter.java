package com.culexy.mehndiandrangoli.Adapter;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.culexy.mehndiandrangoli.Modal.RangoliModal;
import com.culexy.mehndiandrangoli.R;
import com.culexy.mehndiandrangoli.RangoliActivity;

import java.util.List;

public class RangoliAdapter extends RecyclerView.Adapter<RangoliAdapter.ViewHolder> {
    private List<RangoliModal> rangoliModalList;


    public RangoliAdapter(List<RangoliModal> rangoliModalList) {
        this.rangoliModalList = rangoliModalList;
    }

    @NonNull
    @Override
    public RangoliAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rangoli_title_item_layout, parent, false);
        return new RangoliAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RangoliAdapter.ViewHolder holder, final int position) {

        String Name = rangoliModalList.get(position).getRangoliName();

        holder.rangoliTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RangoliActivity.class);
                intent.putExtra("RangoliName", rangoliModalList.get(position).getRangoliName());
                intent.putExtra("RangoliPosition",position);
                v.getContext().startActivity(intent);

            }
        });
        holder.setRangoliData(Name);
    }

    @Override
    public int getItemCount() {
        return rangoliModalList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView rangoliTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rangoliTitle = itemView.findViewById(R.id.hairStyle_title);
        }


        private void setRangoliData(String Name) {
            rangoliTitle.setText(Name);

        }
    }
}
