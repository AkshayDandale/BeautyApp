package com.culexy.mehndiandrangoli.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.culexy.mehndiandrangoli.MehndiActivity;
import com.culexy.mehndiandrangoli.Modal.GridModal;
import com.culexy.mehndiandrangoli.Modal.MehndiModal;
import com.culexy.mehndiandrangoli.R;

import java.util.ArrayList;
import java.util.List;

public class MehndiAdapter extends RecyclerView.Adapter<MehndiAdapter.ViewHolder> {

    private List<MehndiModal> mehndiModalList;
    private List<GridModal>  gridModalList= new ArrayList<>();

    public MehndiAdapter(List<MehndiModal> mehndiModalList) {
        this.mehndiModalList = mehndiModalList;
    }

    @NonNull
    @Override
    public MehndiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mehndi_layout_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MehndiAdapter.ViewHolder holder, final int position) {

        String Name = mehndiModalList.get(position).getMehndiname();

        holder.mehndiTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MehndiActivity.class);
                intent.putExtra("name", mehndiModalList.get(position).getMehndiname());
                intent.putExtra("pos",position);
                v.getContext().startActivity(intent);

            }
        });

        holder.setMehndiData(Name);
    }

    @Override
    public int getItemCount() {
        return mehndiModalList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mehndiTitle;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mehndiTitle = itemView.findViewById(R.id.mehndi_title);


        }

        private void setMehndiData(String Name) {
            mehndiTitle.setText(Name);

        }
    }

}
