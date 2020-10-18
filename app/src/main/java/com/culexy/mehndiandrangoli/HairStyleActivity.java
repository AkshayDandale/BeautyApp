package com.culexy.mehndiandrangoli;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.culexy.mehndiandrangoli.Adapter.HairStyleAdapter;
import com.culexy.mehndiandrangoli.Adapter.HairStyleGridAdapter;
import com.culexy.mehndiandrangoli.Interface.iRecyclerClickListener;
import com.culexy.mehndiandrangoli.Modal.HairStyleGridModal;
import com.culexy.mehndiandrangoli.Modal.HairStyleModal;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HairStyleActivity extends AppCompatActivity {

    private List<HairStyleModal> hairStyleModalList = new ArrayList<>();
    private List<HairStyleGridModal> hairStyleGridModalList = new ArrayList<>();

    private RecyclerView hairStyleRecyclerview;
    private RecyclerView hairStyleGridRecyclerview;

    RecyclerView.LayoutManager layoutManager;

    private HairStyleAdapter hairStyleAdapter;
    private HairStyleGridAdapter hairStyleGridAdapter;


    private TextView categoryName;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hair_style);


        categoryName = findViewById(R.id.category_display);

        final Integer HairStylePosition = getIntent().getIntExtra("HairStylePosition", 0);
        final String name = getIntent().getStringExtra("HairStyleName");

        final ArrayList<String> images = new ArrayList<>();

        mAdView = findViewById(R.id.adView);

        ////////////////////     grid  images

        hairStyleGridRecyclerview = findViewById(R.id.hairstyle_grid_recyclerview);
        layoutManager = new GridLayoutManager(this, 2);
        hairStyleGridRecyclerview.setHasFixedSize(true);
        hairStyleGridRecyclerview.setLayoutManager(layoutManager);

        //////////////////////////////////////////////////


        //////////////////////////////Horizontal Titles

        hairStyleRecyclerview = findViewById(R.id.hairstyle_recyclerview);
        LinearLayoutManager mehndiLayoutManager = new LinearLayoutManager(HairStyleActivity.this);
        mehndiLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        hairStyleRecyclerview.setLayoutManager(mehndiLayoutManager);


        ////////////////////////////////////////////

        if (HairStylePosition == 0) {

            categoryName.setText("Weeding");
        } else {

            categoryName.setText(String.valueOf(name));
        }


        MobileAds.initialize(this, "ca-app-pub-5883415686997852~4542044698");


        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        FirebaseFirestore.getInstance().collection("Hairstyle Categories")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                        long no_of_categories = (long) documentSnapshot.get("no_of_categories");

                        for (long x = 1; x < no_of_categories + 1; x++) {
                            hairStyleModalList.add(new HairStyleModal(documentSnapshot.get("title_" + x).toString()));

                            hairStyleAdapter = new HairStyleAdapter(hairStyleModalList);
                            hairStyleRecyclerview.setAdapter(hairStyleAdapter);
                            hairStyleAdapter.notifyDataSetChanged();
                        }

                    }

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(HairStyleActivity.this, error, Toast.LENGTH_SHORT).show();
                }

            }
        });


        FirebaseFirestore.getInstance().collection("Hairstyle")
                .document(String.valueOf(HairStylePosition)).collection("Perfect")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {


                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        ArrayList<String> arrayList = (ArrayList) documentSnapshot.get("Design");

                        for (int i = 0; i < arrayList.size(); i++) {

                            images.add(i, arrayList.get(i));

                            final iRecyclerClickListener listener = new iRecyclerClickListener() {
                                @Override
                                public void onClick(View view, int position) {

                                    Intent intent = new Intent(getApplicationContext(), FullScreenActivity.class);

                                    intent.putExtra("IMAGES", images);
                                    intent.putExtra("POSITION", position);
                                    startActivity(intent);


                                }
                            };

                            hairStyleGridModalList.add(new HairStyleGridModal(images));
                            hairStyleGridAdapter = new HairStyleGridAdapter(getApplicationContext(), hairStyleGridModalList, listener);
                            hairStyleGridRecyclerview.setAdapter(hairStyleGridAdapter);
                            hairStyleGridAdapter.notifyDataSetChanged();

                        }

                    }

                }

            }
        });


    }


}

