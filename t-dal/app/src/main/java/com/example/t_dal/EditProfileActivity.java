package com.example.t_dal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {

    private Button editButton,cancelButton;
    private EditText Name,UserName;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    String currUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        currUserID=mAuth.getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currUserID);

        editButton = (Button) findViewById(R.id.change_button_edit_profile);
        cancelButton = (Button) findViewById(R.id.back_button_edit_profile);
        Name = (EditText) findViewById(R.id.name_edit_profile);
        UserName = (EditText) findViewById(R.id.username_edit_profile);


        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    if(snapshot.hasChild("fullname"))
                        Name.setText(snapshot.child("fullname").getValue().toString());
                    if(snapshot.hasChild("username"))
                        UserName.setText(snapshot.child("username").getValue().toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = Name.getText().toString();
                String username = UserName.getText().toString();
                if(name!=null && !name.equals("")) {
                    usersRef.child("fullname").setValue(name)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(EditProfileActivity.this, "Full name changed!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        String message = task.getException().getMessage();
                                        Toast.makeText(EditProfileActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                if(username!=null && !name.equals("")) {
                    usersRef.child("username").setValue(username)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(EditProfileActivity.this, "Username changed!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        String message = task.getException().getMessage();
                                        Toast.makeText(EditProfileActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}