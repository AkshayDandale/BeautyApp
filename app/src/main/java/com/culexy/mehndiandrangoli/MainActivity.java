package com.culexy.mehndiandrangoli;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ConstraintLayout constraintLayout;

    private LinearLayout mehndi;
    private LinearLayout rangoli;
    private LinearLayout nail;
    private LinearLayout hairStyle;

    private FirebaseUser currentUser;
    private FirebaseAuth firebaseAuth;

    private Button ReloadBtn;

    //////Navigation Drawer
    private CircleImageView profileView;
    private TextView name, email;
    private Uri facebookPhoto;
    private String facebookName;
    private String facebookEmail;
    private boolean guestLogin;


    private boolean fromFacebbok;
    boolean facebooklogin;

    private Dialog noInternetConnectionDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(Color.parseColor("#f4eae9"));
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        Menu menuNav = navigationView.getMenu();
        final MenuItem nav_share = menuNav.findItem(R.id.account_setting);
        MenuItem favourite = menuNav.findItem(R.id.favourite);
        MenuItem logout = menuNav.findItem(R.id.logout);


        constraintLayout = findViewById(R.id.activity_main_constraint);


        noInternetConnectionDialog = new Dialog(MainActivity.this);
        noInternetConnectionDialog.setContentView(R.layout.no_internet_connection_layout);
//        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        noInternetConnectionDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        noInternetConnectionDialog.setCancelable(false);

        ReloadBtn = noInternetConnectionDialog.findViewById(R.id.reload_btn);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        fromFacebbok = getIntent().getBooleanExtra("value", false);
        guestLogin = getIntent().getBooleanExtra("guestLogin", false);


        profileView = navigationView.getHeaderView(0).findViewById(R.id.navigation_photo);
        name = navigationView.getHeaderView(0).findViewById(R.id.navigation_name);
        email = navigationView.getHeaderView(0).findViewById(R.id.navigation_email);


        mehndi = findViewById(R.id.mehndi_linear);
        rangoli = findViewById(R.id.rangoli_linear);
        nail = findViewById(R.id.nail_linear);
        hairStyle = findViewById(R.id.hairstyle_linear);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move);
        mehndi.startAnimation(animation);
        rangoli.startAnimation(animation);
        nail.startAnimation(animation);
        hairStyle.startAnimation(animation);

        if (guestLogin) {

            profileView.setImageResource(R.drawable.mehndi_logo);
            name.setText("Geust Login");
            email.setVisibility(View.GONE);
            nav_share.setVisible(false);
            favourite.setEnabled(false);
            logout.setEnabled(false);

        }


        mehndi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MehndiActivity.class);
                startActivity(intent);
            }
        });

        rangoli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RangoliActivity.class);
                startActivity(intent);
            }
        });

        nail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NAilActivity.class);
                startActivity(intent);

            }
        });

        hairStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HairStyleActivity.class);
                startActivity(intent);
            }
        });

        if (fromFacebbok) {
            nav_share.setEnabled(false);
            facebookName = getIntent().getStringExtra("facebookName");
            facebookEmail = getIntent().getStringExtra("facebookEmail");
            facebookPhoto = getIntent().getParcelableExtra("facebookProfile");

            Map<String, Object> userdata = new HashMap<>();
            userdata.put("fullname", facebookName);
            userdata.put("email", facebookEmail);
            userdata.put("profile", facebookPhoto.toString());
            userdata.put("facebooklogin", true);


            FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid())
                    .set(userdata);

            name.setText(currentUser.getDisplayName());
            email.setText(currentUser.getEmail());
            Glide.with(getApplicationContext()).load(facebookPhoto).into(profileView);


        }

        if (!guestLogin) {
            FirebaseFirestore.getInstance().collection("USERS")
                    .document(FirebaseAuth.getInstance().getUid())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {
                        facebooklogin = task.getResult().getBoolean("facebooklogin");
                        if (facebooklogin) {
                            nav_share.setEnabled(false);
                        }
                    }
                }
            });
        }

        if (!fromFacebbok) {
            nav_share.setEnabled(true);
            navigationSetting();
        }

        /////code for network connectivity

        checkConnection();

        ReloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }


    private void navigationSetting() {

        if (currentUser != null) {

            FirebaseFirestore.getInstance().collection("USERS")
                    .document(FirebaseAuth.getInstance().getUid())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {

                        name.setText(task.getResult().get("fullname").toString());
                        email.setText(task.getResult().get("email").toString());
                        Glide.with(getApplicationContext()).load(task.getResult().get("profile"))
                                .into(profileView);

                    } else {

                        String error = task.getException().getMessage();
                        Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                    }


                }
            });

        }


    }


    public void checkConnection() {

        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {

                //     Toast.makeText(this, "Wifi Enabled", Toast.LENGTH_SHORT).show();
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

//                Toast.makeText(this, "Data Network Enabled", Toast.LENGTH_SHORT).show();
            }

        } else {

            constraintLayout.setVisibility(View.GONE);
            noInternetConnectionDialog.show();
           // Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.mehndi_section) {
            // Handle the camera action
            Intent intent = new Intent(this, MehndiActivity.class);
            startActivity(intent);

        } else if (id == R.id.rangoli_section) {

            Intent intent = new Intent(this, RangoliActivity.class);
            startActivity(intent);


        } else if (id == R.id.nailart_section) {
            Intent intent = new Intent(this, NAilActivity.class);
            startActivity(intent);


        } else if (id == R.id.hairstyle_section) {
            Intent intent = new Intent(this, HairStyleActivity.class);
            startActivity(intent);

        } else if (id == R.id.favourite) {

            Intent intent = new Intent(this, FavouriteAtivity.class);
            startActivity(intent);

        } else if (id == R.id.account_setting) {


            Intent intent = new Intent(MainActivity.this, AccountSettingActivity.class);
            startActivity(intent);


        } else if (id == R.id.logout) {

            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        invalidateOptionsMenu();

    }
}
