package com.example.test2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserProfileActivity extends AppCompatActivity {
    ArrayList<ReadWriteUserDetails> users = new ArrayList<ReadWriteUserDetails>();

    private TextView textViewWelcome, textViewFullName, textViewEmail, textViewDoB, textViewGender, textViewMobile;
    private ProgressBar progressBar;
    private String fullName, email, dob, gender, mobile;
    private ImageView imageView;
    private FirebaseAuth authProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar();

        textViewFullName = findViewById(R.id.textView_show_fullName);
        textViewEmail = findViewById(R.id.textView_show_email);
        textViewDoB = findViewById(R.id.textView_show_dob);
        textViewGender = findViewById(R.id.textView_show_gender);
        textViewMobile = findViewById(R.id.textView_show_mobile);
        progressBar = findViewById(R.id.progressBar);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if(firebaseUser == null){
            Toast.makeText(UserProfileActivity.this, "Something went wrong! User details aren't available", Toast.LENGTH_SHORT).show();
        }else{
            progressBar.setVisibility(View.GONE);
            showUserProfile(firebaseUser);
        }


    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userId = firebaseUser.getUid();
//        String email = firebaseUser.getEmail();
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        Toast.makeText(this, email, Toast.LENGTH_SHORT).show();

        //Extracting User Reference from Database for "Registered Users"
//        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered users");
//        referenceProfile.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
//                if(readUserDetails != null){
//                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Registered users");
//
//                    Query query = usersRef.orderByChild("email").equalTo(email);
//                    query.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                // Обработка каждой записи
//                                ReadWriteUserDetails readWriteUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
//                                users.add(readWriteUserDetails);
//                                // user содержит данные пользователя, где поле fieldName равно desiredValue
//                            }
//
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//                            // Обработка ошибок, если они возникли
//                        }
//                    });
////                    fullName = firebaseUser.getDisplayName();
////                    email = firebaseUser.getEmail();
////                    dob = readUserDetails.dob;
////                    mobile = readUserDetails.mobile;
////                    gender = readUserDetails.gender;
//
//                    fullName = users.get(0).fullName;
//
//
//                    textViewWelcome.setText("Welcome "+fullName+"!");
////                    textViewFullName.setText(fullName);
////                    textViewEmail.setText(email);
////                    textViewDoB.setText(dob);
////                    textViewGender.setText(gender);
////                    textViewMobile.setText(mobile);
//
//                    Toast.makeText(UserProfileActivity.this, fullName, Toast.LENGTH_SHORT).show();
//                }
//                progressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Registered users");

        Query query = usersRef.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Обработка каждой записи
                    ReadWriteUserDetails user = snapshot.getValue(ReadWriteUserDetails.class);
                    users.add(user);
                    // user содержит данные пользователя, где поле fieldName равно desiredValue
                }


                Toast.makeText(UserProfileActivity.this, users.get(0).fullName, Toast.LENGTH_SHORT).show();
                textViewWelcome = findViewById(R.id.textView_show_welcome);
                textViewWelcome.setText("Welcome, " + users.get(0).fullName);
                textViewFullName.setText(users.get(0).fullName);
                textViewEmail.setText(users.get(0).email);
                textViewDoB.setText(users.get(0).dob);
                textViewGender.setText(users.get(0).gender);
                textViewMobile.setText(users.get(0).mobile);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Обработка ошибок, если они возникли
            }
        });


    }
}