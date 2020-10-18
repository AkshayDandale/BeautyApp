package com.culexy.mehndiandrangoli;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.culexy.mehndiandrangoli.Adapter.NailAdapter;
import com.culexy.mehndiandrangoli.Adapter.NailGridAdapter;
import com.culexy.mehndiandrangoli.Interface.iRecyclerClickListener;
import com.culexy.mehndiandrangoli.Modal.NailGridModal;
import com.culexy.mehndiandrangoli.Modal.NailModal;
import com.github.ybq.android.spinkit.style.ChasingDots;
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

public class NAilActivity extends AppCompatActivity {

    private List<NailModal> nailModalList = new ArrayList<>();
    private List<NailGridModal> nailGridModalList = new ArrayList<>();

    private RecyclerView nailRecyclerview;
    private RecyclerView nailGridRecyclerview;

    RecyclerView.LayoutManager layoutManager;

    private NailAdapter nailAdapter;
    private NailGridAdapter nailGridAdapter;


    private TextView categoryName;



    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nail);


        categoryName = findViewById(R.id.category_display);

        final Integer NailPosition = getIntent().getIntExtra("NailPosition", 0);
        final String name = getIntent().getStringExtra("NailName");

        final ArrayList<String> images = new ArrayList<>();


        ////////////////////     grid  images

        nailGridRecyclerview = findViewById(R.id.nail_grid_recyclerview);
        layoutManager = new GridLayoutManager(this, 2);
        nailGridRecyclerview.setHasFixedSize(true);
        nailGridRecyclerview.setLayoutManager(layoutManager);

        //////////////////////////////////////////////////


        //////////////////////////////Horizontal Titles

        nailRecyclerview = findViewById(R.id.nail_recyclerview);
        LinearLayoutManager mehndiLayoutManager = new LinearLayoutManager(NAilActivity.this);
        mehndiLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        nailRecyclerview.setLayoutManager(mehndiLayoutManager);


        ////////////////////////////////////////////



        mAdView=findViewById(R.id.adView);

        ChasingDots chasingDots = new ChasingDots();


        if (NailPosition==0) {

            categoryName.setText("Vintage");
        } else {

            categoryName.setText(String.valueOf(name));
        }

        MobileAds.initialize(this,"ca-app-pub-5883415686997852~4542044698");


        AdRequest adRequest=new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);




        FirebaseFirestore.getInstance().collection("Nail_Categories")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                        long no_of_categories = (long) documentSnapshot.get("no_of_categories");

                        for (long x = 1; x < no_of_categories + 1; x++) {
                            nailModalList.add(new NailModal(documentSnapshot.get("title_" + x).toString()));

                            nailAdapter = new NailAdapter(nailModalList);
                            nailRecyclerview.setAdapter(nailAdapter);
                            nailAdapter.notifyDataSetChanged();
                        }

                    }

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(NAilActivity.this, error, Toast.LENGTH_SHORT).show();
                }

            }
        });


        FirebaseFirestore.getInstance().collection("Nail")
                .document(String.valueOf(NailPosition)).collection("Perfect")
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

                            nailGridModalList.add(new NailGridModal(images));
                            nailGridAdapter = new NailGridAdapter(getApplicationContext(), nailGridModalList, listener);
                            nailGridRecyclerview.setAdapter(nailGridAdapter);
                            nailGridAdapter.notifyDataSetChanged();

                        }

                    }

                }

            }
        });


    }


}
