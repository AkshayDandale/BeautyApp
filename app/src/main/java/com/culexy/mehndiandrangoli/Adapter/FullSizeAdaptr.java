package com.culexy.mehndiandrangoli.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.culexy.mehndiandrangoli.FavouriteAtivity;
import com.culexy.mehndiandrangoli.LoginActivity;
import com.culexy.mehndiandrangoli.R;
import com.culexy.mehndiandrangoli.RegisterActivity;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FullSizeAdaptr extends PagerAdapter {

    Context context;

    List<String> imageList;
    LayoutInflater inflater;
    private TextView fav_tv;
    private LinearLayout share_linearLayout;
    private LinearLayout delete_linearLayout;
    private ArrayList<String> image_urls = new ArrayList<>();
    private boolean fromFavourite;

    private SparkButton likeButton;
    private CollectionReference collectionReference;
    private Dialog signInDialog;

    private Button DialogsignInButton, DialogSignUpButton;

    public FullSizeAdaptr(Context context, List<String> imageList, boolean fromFavourite) {
        this.context = context;
        this.imageList = imageList;
        this.fromFavourite = fromFavourite;

    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(R.layout.full_item_layout, null);


        fav_tv = v.findViewById(R.id.fav_tv);

        signInDialog = new Dialog(v.getContext());
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        signInDialog.setCancelable(true);

        DialogsignInButton = signInDialog.findViewById(R.id.sign_in_button);
        DialogSignUpButton = signInDialog.findViewById(R.id.sign_up_button);


//        Glide.with(context).load(imageList.get(position))
//                .apply(new RequestOptions().centerInside()).into(imageView);

        Glide.with(v.getContext()).asBitmap().load(imageList.get(position))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        final SubsamplingScaleImageView imageView = (SubsamplingScaleImageView) v.findViewById(R.id.imageview);
                        imageView.setMinimumDpi(80);
                        imageView.setImage(ImageSource.bitmap(resource));
                    }
                });

        final Typeface font = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/monsterrat.ttf");

        share_linearLayout = v.findViewById(R.id.share);
        delete_linearLayout = v.findViewById(R.id.delete);
        likeButton = v.findViewById(R.id.spark_button);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            collectionReference = FirebaseFirestore.getInstance().collection("USERS")
                    .document(FirebaseAuth.getInstance().getUid())
                    .collection("My Favourite");

        }

        if (fromFavourite) {

            delete_linearLayout.setVisibility(View.VISIBLE);
            likeButton.setVisibility(View.GONE);
            fav_tv.setVisibility(View.GONE);
        }

        if (!fromFavourite) {

            delete_linearLayout.setVisibility(View.GONE);
            likeButton.setVisibility(View.VISIBLE);
            fav_tv.setVisibility(View.VISIBLE);


        }


        final String rubbish = String.valueOf(imageList.get(position)).substring((imageList.get(position).length() - 9), imageList.get(position).length());

        final Map<String, Object> note = new HashMap<>();
        note.put("image_url", imageList.get(position));
        note.put("trimmed", rubbish);
        note.put("alreadyAdded", true);


        likeButton.setAnimationSpeed(1.5f);


        likeButton.setEventListener(new

                                            SparkEventListener() {
                                                @Override
                                                public void onEvent(ImageView button, boolean buttonState) {

                                                    if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                                                        signInDialog.show();

                                                        DialogsignInButton.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                                                                v.getContext().startActivity(intent);
                                                            }
                                                        });

                                                        DialogSignUpButton.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                Intent intent = new Intent(v.getContext(), RegisterActivity.class);
                                                                v.getContext().startActivity(intent);
                                                            }
                                                        });
                                                        Toast.makeText(context, "please Login first", Toast.LENGTH_SHORT).show();

                                                    } else {
                                                        signInDialog.dismiss();
                                                        likeButton.playAnimation();
                                                        final Query query = collectionReference.whereEqualTo("trimmed", rubbish);
                                                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    for (DocumentSnapshot documentSnapshot : task.getResult()) {

                                                                        String user = documentSnapshot.getString("trimmed");

                                                                        if (user.equals(rubbish)) {

                                                                            Snackbar snack = Snackbar.make(container, " Already Added to Favourite", Snackbar.LENGTH_LONG);

                                                                            TextView tv = snack.getView().findViewById(com.google.android.material.R.id.snackbar_text);
                                                                            tv.setTextColor(Color.parseColor("#ffffff"));
                                                                            tv.setTypeface(font);
                                                                            View view = snack.getView();
                                                                            //view.setBackgroundColor(Color.WHITE);
                                                                            view.setBackgroundResource(R.drawable.snackbar_background);
                                                                            snack.show();

                                                                        }
                                                                    }

                                                                }

                                                                if (task.getResult().size() == 0) {


                                                                    collectionReference.document(rubbish).set(note)
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                                    Snackbar snack = Snackbar.make(container, " Added to Favourite", Snackbar.LENGTH_LONG);
                                                                                    TextView tv = snack.getView().findViewById(com.google.android.material.R.id.snackbar_text);

                                                                                    tv.setTextColor(Color.parseColor("#ffffff"));
                                                                                    tv.setTypeface(font);
                                                                                    View view = snack.getView();
                                                                                    view.setBackgroundResource(R.drawable.snackbar_background);
                                                                                    snack.show();


                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });


                                                    }

                                                }

                                                @Override
                                                public void onEventAnimationEnd(ImageView button, boolean buttonState) {

                                                    likeButton.setChecked(true);
                                                }

                                                @Override
                                                public void onEventAnimationStart(ImageView button, boolean buttonState) {

                                                }
                                            });

        share_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareBody = imageList.get(position);
                String shareSubject = "Open Link to view image";
                intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                intent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
                v.getContext().startActivity(Intent.createChooser(intent, "Share Using Following Platforms"));

            }
        });


        delete_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                FirebaseFirestore.getInstance().collection("USERS")
                        .document(FirebaseAuth.getInstance().getUid())
                        .collection("My Favourite")
                        .document(rubbish).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(v.getContext(), "Succesfully Deleted", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(v.getContext(), FavouriteAtivity.class);
                                v.getContext().startActivity(intent);


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(v.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });


        final ViewPager vp = (ViewPager) container;
        vp.addView(v, 0);


        return v;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //super.destroyItem(container, position, object);

        ViewPager viewPager = (ViewPager) container;
        View v = (View) object;
        viewPager.removeView(v);
    }


}
