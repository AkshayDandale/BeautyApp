package com.culexy.mehndiandrangoli;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.culexy.mehndiandrangoli.Adapter.FullSizeAdaptr;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FullScreenActivity extends Activity {

    ViewPager viewPager;

    ArrayList<String> imageList;
    int position;
    private boolean fromFavourite;
    private FullSizeAdaptr fullSizeAdaptr;
    private boolean guestLogin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

//        if (savedInstanceState == null) {

        Intent i = getIntent();
        imageList = (ArrayList<String>) getIntent().getSerializableExtra("IMAGES");
        position = i.getIntExtra("POSITION", 0);
        fromFavourite = getIntent().getBooleanExtra("fromFavourite", false);


        viewPager = findViewById(R.id.full_viewpager);



        fullSizeAdaptr = new FullSizeAdaptr(this, imageList, fromFavourite);
        viewPager.setAdapter(fullSizeAdaptr);
        viewPager.setCurrentItem(position, true);
        fullSizeAdaptr.notifyDataSetChanged();

    }

}
