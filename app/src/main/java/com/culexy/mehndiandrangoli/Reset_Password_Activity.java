package com.culexy.mehndiandrangoli;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Reset_Password_Activity extends AppCompatActivity {

    private Button resetButton;
    private EditText resetEmail;
    private TextView emailSent;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_);


        resetButton=findViewById(R.id.reset_button);
        resetEmail=findViewById(R.id.reset_email);
        emailSent=findViewById(R.id.reset_email_sent);

        emailSent.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();


        resetEmail.addTextChangedListener(new TextWatcher() {
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


        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                firebaseAuth.sendPasswordResetEmail(resetEmail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    emailSent.setVisibility(View.VISIBLE);
                                    emailSent.setText("Email sent Sussesfully to your registered email");
                                    emailSent.setTextColor(Color.GREEN);

                                } else {
                                    String error = task.getException().getMessage();
                                    //TransitionMAnager

                                    emailSent.setText(error);
                                    emailSent.setTextColor(Color.RED);
                                    emailSent.setVisibility(View.VISIBLE);


                                }

                            }
                        });
            }
        });


    }

    private void checkInputs() {
        if (TextUtils.isEmpty(resetEmail.getText())) {
            resetButton.setEnabled(false);
        } else {
            resetButton.setEnabled(true);
        }
    }


}
