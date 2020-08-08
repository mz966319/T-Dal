package com.example.t_dal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.t_dal.R.layout.job_post_row;

public class JobPostsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView postList;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef, allPostRef;
    private String currUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_posts);

        mAuth = FirebaseAuth.getInstance();
        currUserID = mAuth.getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        allPostRef = FirebaseDatabase.getInstance().getReference().child("General Posts");

        mToolbar = (Toolbar) findViewById(R.id.jobPosts_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Job Posts");

        postList = findViewById(R.id.jobPostsrecyclerview);

        DisplayAllJobPosts();


    }

    private void DisplayAllJobPosts() {
        allPostRef = FirebaseDatabase.getInstance().getReference().child("General Posts");
        postList = (RecyclerView) findViewById(R.id.jobPostsrecyclerview);

        allPostRef.addValueEventListener(new ValueEventListener() {
            final List<JobPost> list= new ArrayList<JobPost>();


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    Iterable<DataSnapshot> children = snapshot.getChildren();

                    for(DataSnapshot child : children){
                        JobPost jobPost = child.getValue(JobPost.class);
                        list.add(jobPost);
                    }
                    JobPostAdapter jobPostAdapter = new JobPostAdapter(JobPostsActivity.this,list);
                    postList.setAdapter(jobPostAdapter);
                    postList.setHasFixedSize(true);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(JobPostsActivity.this);
                    linearLayoutManager.setReverseLayout(true);
                    linearLayoutManager.setStackFromEnd(true);
                    postList.setLayoutManager(linearLayoutManager);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(JobPostsActivity.this, MainActivity.class);
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