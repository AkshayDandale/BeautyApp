package com.culexy.mehndiandrangoli;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.culexy.mehndiandrangoli.Adapter.GridAdapter;
import com.culexy.mehndiandrangoli.Adapter.MehndiAdapter;
import com.culexy.mehndiandrangoli.Interface.iRecyclerClickListener;
import com.culexy.mehndiandrangoli.Modal.GridModal;
import com.culexy.mehndiandrangoli.Modal.MehndiModal;
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

public class MehndiActivity extends AppCompatActivity {

    private List<MehndiModal> mehndiModalList = new ArrayList<>();
    private List<GridModal> gridModalList = new ArrayList<>();

    private RecyclerView mehndiRecyclerview;
    private RecyclerView gangRecyclerView;

    RecyclerView.LayoutManager layoutManager;

    private MehndiAdapter mehndiAdapter;
    private GridAdapter gridAdapter;

    private TextView categoryName;

    private LinearLayout linearLayout;
    private Button handDesign;
    private Button footDesign;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mehndi);


        mehndiRecyclerview = findViewById(R.id.mehndi_recyclerview);
        categoryName = findViewById(R.id.category_display);
        mAdView = findViewById(R.id.adView);


        linearLayout = findViewById(R.id.hand_foot_design_layout);
        handDesign = findViewById(R.id.hand_design);
        footDesign = findViewById(R.id.foot_design);

        gangRecyclerView = findViewById(R.id.mehndi_grid_recyclerview);
        layoutManager = new GridLayoutManager(this, 2);
        gangRecyclerView.setHasFixedSize(true);
        gangRecyclerView.setLayoutManager(layoutManager);

        final ArrayList<String> images = new ArrayList<>();


        final Integer pos = getIntent().getIntExtra("pos", 0);

        final String name = getIntent().getStringExtra("name");

        LinearLayoutManager mehndiLayoutManager = new LinearLayoutManager(MehndiActivity.this);
        mehndiLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mehndiRecyclerview.setLayoutManager(mehndiLayoutManager);

        if (pos == 0) {

            categoryName.setText("Indian");
        } else {

            categoryName.setText(String.valueOf(name));
        }

        MobileAds.initialize(this, "ca-app-pub-5883415686997852~4542044698");


        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        FirebaseFirestore.getInstance().collection("Mehndi_Categories")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {


                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                        long no_of_categories = (long) documentSnapshot.get("no_of_categories");

                        for (long x = 1; x < no_of_categories + 1; x++) {
                            mehndiModalList.add(new MehndiModal(documentSnapshot.get("title_" + x).toString()));

                            mehndiAdapter = new MehndiAdapter(mehndiModalList);
                            mehndiRecyclerview.setAdapter(mehndiAdapter);
                            mehndiAdapter.notifyDataSetChanged();
                        }

                    }

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(MehndiActivity.this, error, Toast.LENGTH_SHORT).show();
                }

            }
        });


        handDesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FirebaseFirestore.getInstance().collection("Mehndi")
                        .document(String.valueOf(pos)).collection("Perfect")
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            gridModalList.clear();

                            images.clear();

                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                ArrayList<String> arrayList = (ArrayList) documentSnapshot.get("Hand_Design");

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

                                    gridModalList.add(new GridModal(images));
                                    gridAdapter = new GridAdapter(getApplicationContext(), gridModalList, listener);
                                    gangRecyclerView.setAdapter(gridAdapter);
                                    gridAdapter.notifyDataSetChanged();

                                }


                            }

                        }

                    }
                });


            }
        });


        footDesign.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FirebaseFirestore.getInstance().collection("Mehndi")
                        .document(String.valueOf(pos)).collection("Perfect")
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {


                            gridModalList.clear();
                            images.clear();

                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                ArrayList<String> arrayList = (ArrayList) documentSnapshot.get("Foot_Design");

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

                                    gridModalList.add(new GridModal(images));
                                    gridAdapter = new GridAdapter(getApplicationContext(), gridModalList, listener);
                                    gangRecyclerView.setAdapter(gridAdapter);
                                    gridAdapter.notifyDataSetChanged();

                                }

                            }

                        }

                    }
                });
            }
        });


        //////////////////////////////////////////////////

        FirebaseFirestore.getInstance().collection("Mehndi")
                .document(String.valueOf(pos)).collection("Perfect")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {

                    gridModalList.clear();
                    images.clear();

                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        ArrayList<String> arrayList = (ArrayList) documentSnapshot.get("Hand_Design");

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

                            gridModalList.add(new GridModal(images));
                            gridAdapter = new GridAdapter(getApplicationContext(), gridModalList, listener);
                            gangRecyclerView.setAdapter(gridAdapter);
                            gridAdapter.notifyDataSetChanged();

                        }

                    }

                }

            }
        });
        //////////////////////////////////////////


    }


}

