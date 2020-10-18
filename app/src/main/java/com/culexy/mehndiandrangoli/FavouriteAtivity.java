package com.culexy.mehndiandrangoli;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.culexy.mehndiandrangoli.Adapter.FavouriteAdapter;
import com.culexy.mehndiandrangoli.Interface.iRecyclerClickListener;
import com.culexy.mehndiandrangoli.Modal.FavouriteModal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FavouriteAtivity extends AppCompatActivity {

    RecyclerView favouriteRecyclerview;
    RecyclerView.LayoutManager layoutManager;
    private boolean fromFavourite = false;

    private FavouriteAdapter favouriteAdapter;

    private List<FavouriteModal> favouriteModalList = new ArrayList<>();


    final ArrayList<String> images = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_ativity);


        favouriteRecyclerview = findViewById(R.id.favourite_recyclerview);

        layoutManager = new GridLayoutManager(this, 2);
        favouriteRecyclerview.setHasFixedSize(true);
        favouriteRecyclerview.setLayoutManager(layoutManager);
        final CollectionReference collectionReference = FirebaseFirestore.getInstance()
                .collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("My Favourite");



        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("My Favourite")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {


                    for (final QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                        String anna = documentSnapshot.get("image_url").toString();

                        images.add(anna);


                        final iRecyclerClickListener listener = new iRecyclerClickListener() {
                            @Override
                            public void onClick(View view, int position) {

                                Intent intent = new Intent(getApplicationContext(), FullScreenActivity.class);

                                intent.putExtra("IMAGES", images);
                                intent.putExtra("POSITION", position);
                                intent.putExtra("fromFavourite", true);


                                startActivity(intent);


                            }
                        };


                        favouriteModalList.add(new FavouriteModal(images));
                        favouriteAdapter = new FavouriteAdapter(getApplicationContext(), favouriteModalList, listener);
                        favouriteRecyclerview.setAdapter(favouriteAdapter);
                        favouriteAdapter.notifyDataSetChanged();
                        favouriteAdapter.notifyItemRemoved(0);



                    }

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(FavouriteAtivity.this, error, Toast.LENGTH_SHORT).show();

                }
            }
        });


    }



    @Override
    protected void onStart() {
        super.onStart();
        invalidateOptionsMenu();
    }


}
