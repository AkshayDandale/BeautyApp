package com.culexy.mehndiandrangoli;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AccountSettingActivity extends AppCompatActivity {

    private ImageView accountprofile;
    private EditText accountName, accountEmail;
    private Button updateBtn;

    private static final int CHOOSE_IMAGE_1 = 1;

    private StorageReference storageReference;

    private Uri imgUrl;

    private Dialog updatingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);

        accountprofile = findViewById(R.id.account_profile);
        accountName = findViewById(R.id.account_name);
        accountEmail = findViewById(R.id.account_email);
        updateBtn = findViewById(R.id.update_btn);

        storageReference = FirebaseStorage.getInstance().getReference("profile");

        updatingDialog = new Dialog(AccountSettingActivity.this);
        updatingDialog.setContentView(R.layout.updating_progress);
//        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        updatingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        updatingDialog.setCancelable(false);

        FirebaseFirestore.getInstance().collection("USERS")
                .document(FirebaseAuth.getInstance().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    accountName.setText(task.getResult().get("fullname").toString());
                    accountEmail.setText(task.getResult().get("email").toString());
                    Glide.with(getApplicationContext()).load(task.getResult().get("profile").toString())
                            .apply(new RequestOptions().placeholder(R.drawable.ic_account_circle_black_24dp)).into(accountprofile);

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(AccountSettingActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });


        accountprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chooseImage();

            }
        });


        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updatingDialog.show();
                uploadImage();
            }
        });

        accountName.addTextChangedListener(new TextWatcher() {
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

        accountEmail.addTextChangedListener(new TextWatcher() {
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

    }


    private String getFileExtension(Uri uri) {

        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    private void checkInputs() {

        if (!TextUtils.isEmpty(accountName.getText())) {
            if (!TextUtils.isEmpty(accountEmail.getText())) {

                updateBtn.setEnabled(true);
            } else {
                updateBtn.setEnabled(false);

            }


        } else {
            updateBtn.setEnabled(false);

        }
    }


    private void uploadImage() {


        if (imgUrl != null) {
            final StorageReference fileReferance = storageReference.child(System.currentTimeMillis() + "-"
                    + getFileExtension(imgUrl));

            fileReferance.putFile(imgUrl)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReferance.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {


                                    Map<String, Object> uploadData = new HashMap<>();
                                    uploadData.put("profile", uri.toString());
                                    uploadData.put("fullname", accountName.getText().toString());
                                    uploadData.put("email", accountEmail.getText().toString());

                                    FirebaseFirestore.getInstance().collection("USERS")
                                            .document(FirebaseAuth.getInstance().getUid())
                                            .update(uploadData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()){
                                                updatingDialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "Updated Succesfully", Toast.LENGTH_SHORT).show();


                                            }
                                        }
                                    });




                                }
                            });
                        }
                    });


        } else {

            Map<String, Object> uploadPersonalData = new HashMap<>();

            uploadPersonalData.put("fullname", accountName.getText().toString());
            uploadPersonalData.put("email", accountEmail.getText().toString());


            FirebaseFirestore.getInstance().collection("USERS")
                    .document(FirebaseAuth.getInstance().getUid())
                    .update(uploadPersonalData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        updatingDialog.dismiss();
                    }
                }
            });
        }

    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, CHOOSE_IMAGE_1);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE_1 && resultCode == RESULT_OK) {

            imgUrl = data.getData();
            Glide.with(this).load(imgUrl).into(accountprofile);


        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        invalidateOptionsMenu();
        Intent intent= new Intent(AccountSettingActivity.this,MainActivity.class);
        startActivity(intent);

    }
}
