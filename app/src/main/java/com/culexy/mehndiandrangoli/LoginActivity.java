package com.culexy.mehndiandrangoli;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    LoginButton loginButton;
    CallbackManager callbackManager;
    FirebaseAuth auth;

    private TextView login_tv;
    private TextView login_Register_tv;
    private EditText email, password;

    public static boolean fromFacebook = false;

    private Button guestButton;

    private Button logLoginButton;
    private FirebaseAuth firebaseAuth;

    private Dialog loadingDialog;
    private TextView forgotPAssword;

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        loadingDialog = new Dialog(LoginActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress);
//        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);


        guestButton = findViewById(R.id.guest_button);

        auth = FirebaseAuth.getInstance();

        forgotPAssword = findViewById(R.id.forgot_password);

        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        loginButton = findViewById(R.id.login_button);

        login_tv = findViewById(R.id.login_login);
        login_Register_tv = findViewById(R.id.login_register);
        logLoginButton = findViewById(R.id.log_login_btn);

        email = findViewById(R.id.reset_email);
        password = findViewById(R.id.login_password);

        firebaseAuth = FirebaseAuth.getInstance();


        loginButton.setReadPermissions(Arrays.asList("email"));

        guestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("guestLogin", true);
                startActivity(intent);
            }
        });


        login_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        login_Register_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


        logLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailandpassword();
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        forgotPAssword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, Reset_Password_Activity.class);
                startActivity(intent);
            }
        });

    }


    private void checkEmailandpassword() {
        if (email.getText().toString().matches(emailPattern)) {
            if (password.length() >= 6) {

                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } else {

                                    Toast.makeText(getApplicationContext(), "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            } else {
                Toast.makeText(getApplicationContext(), "Incorrect Password or Email", Toast.LENGTH_SHORT).show();
            }
        } else {

        }
    }

    private void checkInputs() {
        if (!TextUtils.isEmpty(email.getText())) {
            if (!TextUtils.isEmpty(password.getText())) {
                logLoginButton.setEnabled(true);

            } else {
                logLoginButton.setEnabled(false);
            }
        } else {
            logLoginButton.setEnabled(false);
        }

    }


    public void buttonClickLoginFb(final View v) {


        fromFacebook = true;

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                handleFacebookToken(loginResult.getAccessToken());
                loadingDialog.show();

            }

            @Override
            public void onCancel() {
                Toast.makeText(v.getContext(), "User Cancelled it", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void handleFacebookToken(AccessToken accessToken) {

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            FirebaseUser myUser = auth.getCurrentUser();

                            updateUI(myUser);


                        } else {

                            Toast.makeText(LoginActivity.this, "Could not register to Firebase", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void updateUI(FirebaseUser myUser) {


        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        fromFacebook = true;


        intent.putExtra("facebookEmail", myUser.getEmail());
        intent.putExtra("facebookName", myUser.getDisplayName());
        intent.putExtra("facebookProfile", myUser.getPhotoUrl());
        intent.putExtra("value", true);
        startActivity(intent);
        finish();


    }


}
