package com.culexy.mehndiandrangoli.Adapter;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.culexy.mehndiandrangoli.HairStyleActivity;
import com.culexy.mehndiandrangoli.Modal.HairStyleModal;
import com.culexy.mehndiandrangoli.Modal.RangoliModal;
import com.culexy.mehndiandrangoli.R;
import com.culexy.mehndiandrangoli.RangoliActivity;

import java.util.List;

public class HairStyleAdapter extends RecyclerView.Adapter<HairStyleAdapter.ViewHolder> {
    private List<HairStyleModal> hairStyleModalList;



    public HairStyleAdapter(List<HairStyleModal> hairStyleModalList) {
        this.hairStyleModalList = hairStyleModalList;
    }

    @NonNull
    @Override
    public HairStyleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hairstyle_title_item_layout, parent, false);
        return new HairStyleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HairStyleAdapter.ViewHolder holder, final int position) {

        String Name = hairStyleModalList.get(position).getHairStyleName();

        holder.hairStyleTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HairStyleActivity.class);
                intent.putExtra("HairStyleName", hairStyleModalList.get(position).getHairStyleName());
                intent.putExtra("HairStylePosition",position);
                v.getContext().startActivity(intent);

            }
        });
        holder.setRangoliData(Name);
    }

    @Override
    public int getItemCount() {
        return hairStyleModalList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView hairStyleTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            hairStyleTitle = itemView.findViewById(R.id.hairStyle_title);
        }


        private void setRangoliData(String Name) {
            hairStyleTitle.setText(Name);

        }
    }
}
