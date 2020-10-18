package com.culexy.mehndiandrangoli.Adapter;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.culexy.mehndiandrangoli.Modal.NailModal;
import com.culexy.mehndiandrangoli.Modal.RangoliModal;
import com.culexy.mehndiandrangoli.NAilActivity;
import com.culexy.mehndiandrangoli.R;
import com.culexy.mehndiandrangoli.RangoliActivity;

import java.util.List;

public class NailAdapter extends RecyclerView.Adapter<NailAdapter.ViewHolder> {
    private List<NailModal> nailModalList;


    public NailAdapter(List<NailModal> nailModalList) {
        this.nailModalList = nailModalList;
    }

    @NonNull
    @Override
    public NailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nail_title_item_layout, parent, false);
        return new NailAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NailAdapter.ViewHolder holder, final int position) {

        String Name = nailModalList.get(position).getNailName();

        holder.nailTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NAilActivity.class);
                intent.putExtra("NailName", nailModalList.get(position).getNailName());
                intent.putExtra("NailPosition",position);
                v.getContext().startActivity(intent);

            }
        });
        holder.setRangoliData(Name);
    }

    @Override
    public int getItemCount() {
        return nailModalList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nailTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nailTitle = itemView.findViewById(R.id.nail_title);
        }


        private void setRangoliData(String Name) {
            nailTitle.setText(Name);

        }
    }
}
