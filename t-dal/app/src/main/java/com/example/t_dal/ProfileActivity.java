package com.example.t_dal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private TextView Name,Email,Username,UserType;
    private Button backButton,editButton;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    String currUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        currUserID=mAuth.getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        Name = (TextView) findViewById(R.id.name_profile);
        Email = (TextView) findViewById(R.id.email_profile);
        Username = (TextView) findViewById(R.id.username_profile);
        UserType = (TextView) findViewById(R.id.user_type_profile);
        backButton = (Button) findViewById(R.id.back_button_profile);
        editButton = (Button) findViewById(R.id.editButton_profile);

        usersRef.child(currUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    if(snapshot.hasChild("fullname"))
                        Name.setText(snapshot.child("fullname").getValue().toString());
                    if(snapshot.hasChild("email"))
                        Email.setText(snapshot.child("email").getValue().toString());
                    if(snapshot.hasChild("username"))
                        Username.setText(snapshot.child("username").getValue().toString());
                    if (snapshot.hasChild("usertype"))
                        UserType.setText( snapshot.child("usertype").getValue().toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(ProfileActivity.this,MainActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginIntent);
                finish();
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(ProfileActivity.this,EditProfileActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginIntent);
                finish();
            }
        });


    }
}