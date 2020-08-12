package com.example.t_dal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;

public class AddPostActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private Spinner jobTitle;
    private EditText courseName,jobDescription;
    private Button addPostButton;
    private ProgressDialog loadinBar;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private DatabaseReference userPostsRef;
    private  DatabaseReference generalPostsRef;
    String currUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        mAuth= FirebaseAuth.getInstance();
        currUserID = mAuth.getCurrentUser().getUid();
        userPostsRef = FirebaseDatabase.getInstance().getReference().child("Users Posts").child(currUserID);
        generalPostsRef = FirebaseDatabase.getInstance().getReference().child("General Posts");
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        mToolbar = (Toolbar) findViewById(R.id.add_post_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add New Post");

        courseName = (EditText) findViewById(R.id.courseNameText);
        jobDescription = (EditText) findViewById(R.id.description_text);
        jobTitle = (Spinner) findViewById(R.id.jobTitleSpinner);
        addPostButton = (Button) findViewById(R.id.addJobPostButton);
        loadinBar = new ProgressDialog(this);
        String[] arraySpinnerTitle = new String[] {"Job Title:","TA", "Marker"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinnerTitle);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobTitle.setAdapter(adapter);

        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveNewPost();
            }
        });


    }

    private void SaveNewPost() {

        final String coursename = courseName.getText().toString().toUpperCase();
        final String description = jobDescription.getText().toString();
        final String jobtitle = jobTitle.getSelectedItem().toString();

        if(TextUtils.isEmpty(coursename)){
            Toast.makeText(this,"Course name field is empty!",Toast.LENGTH_SHORT).show();
        }
        else if(validateCourseName(coursename)){
            Toast.makeText(this,"Course name is in wrong format!",Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(description)){
            Toast.makeText(this,"Description field is empty!",Toast.LENGTH_SHORT).show();
        }
        else if(jobtitle.equals("Job Title:")){
            Toast.makeText(this,"Job title field is empty!",Toast.LENGTH_SHORT).show();
        }
        else {


            usersRef.child(currUserID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        if(snapshot.hasChild("fullname")) {

                            String fullname = snapshot.child("fullname").getValue().toString();
                            loadinBar.setTitle("Saving New Post");
                            loadinBar.setMessage("Please wait, post being saved...");
                            loadinBar.show();
                            loadinBar.setCanceledOnTouchOutside(true);

                            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                            Date d = new Date();
                            String date = formatter.format(d);
                            HashMap userMap = new HashMap();
                            userMap.put("coursename",coursename);
                            userMap.put("description",description);
                            userMap.put("jobtitle",jobtitle);
                            userMap.put("date",date);
                            userMap.put("userid",currUserID);
                            userMap.put("fullname",fullname);

                            userPostsRef.child(date).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if(task.isSuccessful()){
                                        //SendUserToMainActivity();
                                        Toast.makeText(AddPostActivity.this,"Post added to your profile successfully!",Toast.LENGTH_SHORT).show();
                                        loadinBar.dismiss();
                                    }
                                    else{
                                        String message = task.getException().getMessage();
                                        Toast.makeText(AddPostActivity.this,"Error: "+message,Toast.LENGTH_SHORT).show();
                                        loadinBar.dismiss();

                                    }
                                }
                            });
                            generalPostsRef.child(date).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if(task.isSuccessful()){
                                        SendUserToMainActivity();
                                        Toast.makeText(AddPostActivity.this,"Post added general list successfully!",Toast.LENGTH_LONG).show();
                                        loadinBar.dismiss();
                                    }
                                    else{
                                        String message = task.getException().getMessage();
                                        Toast.makeText(AddPostActivity.this,"Error: "+message,Toast.LENGTH_SHORT).show();
                                        loadinBar.dismiss();

                                    }
                                }
                            });

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



//            loadinBar.setTitle("Saving New Post");
//            loadinBar.setMessage("Please wait, post being saved...");
//            loadinBar.show();
//            loadinBar.setCanceledOnTouchOutside(true);
//
//            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//            Date d = new Date();
//            String date = formatter.format(d);
//            HashMap userMap = new HashMap();
//            userMap.put("coursename",coursename);
//            userMap.put("description",description);
//            userMap.put("jobtitle",jobtitle);
//            userMap.put("date",date);
//            userMap.put("userid",currUserID);
//
//            userPostsRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
//                @Override
//                public void onComplete(@NonNull Task task) {
//                    if(task.isSuccessful()){
//                        //SendUserToMainActivity();
//                        Toast.makeText(AddPostActivity.this,"Post added to your profile successfully!",Toast.LENGTH_SHORT).show();
//                        loadinBar.dismiss();
//                    }
//                    else{
//                        String message = task.getException().getMessage();
//                        Toast.makeText(AddPostActivity.this,"Error: "+message,Toast.LENGTH_SHORT).show();
//                        loadinBar.dismiss();
//
//                    }
//                }
//            });
//            generalPostsRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
//                @Override
//                public void onComplete(@NonNull Task task) {
//                    if(task.isSuccessful()){
//                        SendUserToMainActivity();
//                        Toast.makeText(AddPostActivity.this,"Post added general list successfully!",Toast.LENGTH_LONG).show();
//                        loadinBar.dismiss();
//                    }
//                    else{
//                        String message = task.getException().getMessage();
//                        Toast.makeText(AddPostActivity.this,"Error: "+message,Toast.LENGTH_SHORT).show();
//                        loadinBar.dismiss();
//
//                    }
//                }
//            });

        }
    }
    public boolean validateCourseName(String course){
        if(course.length()!=9)
            return true;
        else if(!course.matches("^[A-Z]{4}\\s\\d{4}"))
            return true;

        return false;
    }
    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(AddPostActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            SendUserToMainActivity();
        }
        return super.onOptionsItemSelected(item);
    }
}