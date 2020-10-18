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

import com.culexy.mehndiandrangoli.Adapter.RangoliAdapter;
import com.culexy.mehndiandrangoli.Adapter.RangoliGridAdapter;
import com.culexy.mehndiandrangoli.Interface.iRecyclerClickListener;
import com.culexy.mehndiandrangoli.Modal.RangoliGridModal;
import com.culexy.mehndiandrangoli.Modal.RangoliModal;
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

public class RangoliActivity extends AppCompatActivity {

    private List<RangoliModal> rangoliModalList = new ArrayList<>();
    private List<RangoliGridModal> rangoliGridModalList = new ArrayList<>();

    private RecyclerView rangoliRecyclerview;
    private RecyclerView rangoliGridRecyclerview;

    RecyclerView.LayoutManager layoutManager;

    private RangoliAdapter rangoliAdapter;
    private RangoliGridAdapter rangoliGridAdapter;


    private TextView categoryName;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rangoli);



        categoryName = findViewById(R.id.category_display);
        mAdView=findViewById(R.id.adView);

        final Integer RangoliPosition = getIntent().getIntExtra("RangoliPosition", 0);
        final String name = getIntent().getStringExtra("RangoliName");

        final ArrayList<String> images = new ArrayList<>();

        ////////////////////     grid  images

        rangoliGridRecyclerview = findViewById(R.id.rangoli_grid_recyclerview);
        layoutManager = new GridLayoutManager(this, 2);
        rangoliGridRecyclerview.setHasFixedSize(true);
        rangoliGridRecyclerview.setLayoutManager(layoutManager);

        //////////////////////////////////////////////////


        //////////////////////////////Horizontal Titles

        rangoliRecyclerview=findViewById(R.id.rangoli_recyclerview);
        LinearLayoutManager mehndiLayoutManager = new LinearLayoutManager(RangoliActivity.this);
        mehndiLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rangoliRecyclerview.setLayoutManager(mehndiLayoutManager);


        ////////////////////////////////////////////

        if (RangoliPosition==0) {

            categoryName.setText("Free Hand");
        } else {

            categoryName.setText(String.valueOf(name));
        }



        MobileAds.initialize(this,"ca-app-pub-5883415686997852~4542044698");


        AdRequest adRequest=new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        FirebaseFirestore.getInstance().collection("Rangoli_Categories")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                        long no_of_categories = (long) documentSnapshot.get("no_of_categories");

                        for (long x = 1; x < no_of_categories + 1; x++) {
                            rangoliModalList.add(new RangoliModal(documentSnapshot.get("title_" + x).toString()));

                            rangoliAdapter = new RangoliAdapter(rangoliModalList);
                            rangoliRecyclerview.setAdapter(rangoliAdapter);
                            rangoliAdapter.notifyDataSetChanged();
                        }

                    }

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(RangoliActivity.this, error, Toast.LENGTH_SHORT).show();
                }

            }
        });


        FirebaseFirestore.getInstance().collection("Rangoli")
                .document(String.valueOf(RangoliPosition)).collection("Perfect")
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

                            rangoliGridModalList.add(new RangoliGridModal(images));
                            rangoliGridAdapter = new RangoliGridAdapter(getApplicationContext(), rangoliGridModalList, listener);
                            rangoliGridRecyclerview.setAdapter(rangoliGridAdapter);
                            rangoliGridAdapter.notifyDataSetChanged();

                        }

                    }

                }

            }
        });


    }


}
