package com.example.t_dal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class JobPostsActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private TextView filterCourseName;
    private Spinner filterTitleSpinner, filterProfNameSpinner;
    private Button filterSearchButton;

    private Toolbar mToolbar;
    private RecyclerView postList;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef, allPostRef;
    private String currUserID;
    private View navVeiew;
    private ImageButton searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_posts);

        drawerLayout = (DrawerLayout) findViewById(R.id.jobList_draweble_layout);

//        actionBarDrawerToggle = new ActionBarDrawerToggle(JobPostsActivity.this,drawerLayout,R.string.drawer_open,R.string.drawer_close);
//        drawerLayout.addDrawerListener(actionBarDrawerToggle);
//        actionBarDrawerToggle.syncState();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView) findViewById(R.id.jobPost_navigation_view);
        navVeiew = navigationView.inflateHeaderView(R.layout.filter_job_posts_options);
//        View navVeiew = navigationView.inflateHeaderView(R.layout.navigation_header);
        filterCourseName = (TextView) navVeiew.findViewById(R.id.findByCourseNameTxt);
        filterTitleSpinner = (Spinner) navVeiew.findViewById(R.id.findByJobTitleSpinner);
        filterProfNameSpinner = (Spinner) navVeiew.findViewById(R.id.findByInstructorNameSpinner);
        filterSearchButton = (Button) navVeiew.findViewById(R.id.filter_search_button);

        searchButton = (ImageButton) findViewById(R.id.header_search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.END); //Edit Gravity.START need API 14

            }
        });

        filterSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String course="",title="",prof="";
                if(filterCourseName.getText().toString()!=null)
                    course=filterCourseName.getText().toString().toUpperCase();
                if(filterTitleSpinner.getSelectedItem().toString()!=null && !filterTitleSpinner.getSelectedItem().toString().equals(" Job Title:"))
                    title=filterTitleSpinner.getSelectedItem().toString();
                if(filterProfNameSpinner.getSelectedItem().toString()!=null && !filterProfNameSpinner.getSelectedItem().toString().equals(" Instructor Name:"))
                    prof=filterProfNameSpinner.getSelectedItem().toString();

                Intent intent = getIntent();
//                startActivity(intent);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("userType","filter TA" );

                intent.putExtra("course",course );
                intent.putExtra("title", title);
                intent.putExtra("prof",prof);

                startActivity(intent);
                    finish();
//                finishAffinity();


//                FilterJobPosts(course,title,prof);
            }
        });


        String[] jobTitlesArray = new String[] {" Job Title:", "Marker","TA"};
        ArrayAdapter<String> titleAdapter = new ArrayAdapter<String>(JobPostsActivity.this,
                android.R.layout.simple_spinner_item, jobTitlesArray);
        titleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterTitleSpinner.setAdapter(titleAdapter);




        mAuth = FirebaseAuth.getInstance();
        currUserID = mAuth.getCurrentUser().getUid();
//        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
//        allPostRef = FirebaseDatabase.getInstance().getReference().child("General Posts");

        mToolbar = (Toolbar) findViewById(R.id.jobPosts_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");

        postList = findViewById(R.id.jobPostsrecyclerview);
        Intent intent = getIntent();
        String userType = intent.getExtras().getString("userType");

        String course=intent.getExtras().getString("course");
        String title=intent.getExtras().getString("title");
        String prof=intent.getExtras().getString("prof");;

        if(userType.equals("TA")) {
            DisplayAllJobPosts();
            addItemsToProfSpinner();
//            allPostRef.addValueEventListener(new ValueEventListener() {
//            final List<String> profNames = new ArrayList<String>();
//
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    profNames.add(" Instructor Name:");
//                    if(snapshot.exists()) {
//                        Iterable<DataSnapshot> children = snapshot.getChildren();
//                        for(DataSnapshot child : children){
//                            JobPost jobPost = child.getValue(JobPost.class);
//                        if (profNames.isEmpty())
//                            profNames.add(jobPost.getFullname());
//                        else if(!profNames.contains(jobPost.getFullname()))
//                            profNames.add(jobPost.getFullname());
//                        }
//                    }
//                    ArrayAdapter<String> profAdapter = new ArrayAdapter<String>(JobPostsActivity.this,
//                            android.R.layout.simple_spinner_item, profNames);
//                    profAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    filterProfNameSpinner.setAdapter(profAdapter);
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                }
//            });
        }
        else if (userType.equals("Instructor"))
            DisplayUserJobPosts();
        else if (userType.equals("filter TA")){
//            addItemsToProfSpinner();
            FilterJobPosts( course, title, prof);

        }
        else if(userType.equals("mTA")){
            DisplayTAaplications();
        }



    }

    private void DisplayTAaplications() {
        final List<String> profNames = new ArrayList<String>();

//        allPostRef = FirebaseDatabase.getInstance().getReference().child("General Posts");
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currUserID).child("appliedfor");
        postList = (RecyclerView) findViewById(R.id.jobPostsrecyclerview);

        usersRef.addValueEventListener(new ValueEventListener() {
            final List<JobPost> list= new ArrayList<JobPost>();


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    profNames.add(" Instructor Name:");
                    Iterable<DataSnapshot> children = snapshot.getChildren();

                    for(DataSnapshot child : children){
                        JobPost jobPost = child.getValue(JobPost.class);
                        list.add(jobPost);
                        if (profNames.isEmpty())
                            profNames.add(jobPost.getFullname());
                        else if(!profNames.contains(jobPost.getFullname()))
                            profNames.add(jobPost.getFullname());
                    }
                    JobPostAdapter jobPostAdapter = new JobPostAdapter(JobPostsActivity.this,list,"NO");
                    postList.setAdapter(jobPostAdapter);
                    postList.setHasFixedSize(true);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(JobPostsActivity.this);
                    linearLayoutManager.setReverseLayout(true);
                    linearLayoutManager.setStackFromEnd(true);
                    postList.setLayoutManager(linearLayoutManager);

                    ArrayAdapter<String> profAdapter = new ArrayAdapter<String>(JobPostsActivity.this,
                            android.R.layout.simple_spinner_item, profNames);
                    profAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    filterProfNameSpinner.setAdapter(profAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }



    private void addItemsToProfSpinner(){
        allPostRef.addValueEventListener(new ValueEventListener() {
            final List<String> profNames = new ArrayList<String>();

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                profNames.add(" Instructor Name:");
                if(snapshot.exists()) {
                    Iterable<DataSnapshot> children = snapshot.getChildren();
                    for(DataSnapshot child : children){
                        JobPost jobPost = child.getValue(JobPost.class);
                        if (profNames.isEmpty())
                            profNames.add(jobPost.getFullname());
                        else if(!profNames.contains(jobPost.getFullname()))
                            profNames.add(jobPost.getFullname());
                    }
                }
                ArrayAdapter<String> profAdapter = new ArrayAdapter<String>(JobPostsActivity.this,
                        android.R.layout.simple_spinner_item, profNames);
                profAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                filterProfNameSpinner.setAdapter(profAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void FilterJobPosts(final String course, final String title, final String prof) {
//
//
//
        final List<String> profNames = new ArrayList<String>();

        allPostRef = FirebaseDatabase.getInstance().getReference().child("General Posts");
        postList = (RecyclerView) findViewById(R.id.jobPostsrecyclerview);
        allPostRef.addValueEventListener(new ValueEventListener() {
            final List<JobPost> list= new ArrayList<JobPost>();
            final List<JobPost> filteredList= new ArrayList<JobPost>();
            boolean inFilter=true;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                profNames.add(" Instructor Name:");

                if(snapshot.exists()) {
                    Iterable<DataSnapshot> children = snapshot.getChildren();
//
                    for(DataSnapshot child : children){
                        inFilter=true;
                        JobPost jobPost = child.getValue(JobPost.class);
                        list.add(jobPost);
                        filteredList.add(jobPost);
                        System.out.println("*******************"+course+"*******************");
                        if(!(TextUtils.isEmpty(course))) {
//                            if ((validateCourseName(course))) {
                                System.out.println("*******************"+course+"*******************");
                                if(!jobPost.getCoursename().equals(course)){
                                    if(filteredList.contains(jobPost))
                                        filteredList.remove(jobPost);
                                }
//                                    inFilter=false;
//                                Toast.makeText(JobPostsActivity.this, "Course name is in wrong format!", Toast.LENGTH_SHORT).show();
//                            }
                        }
//
                        if(!(TextUtils.isEmpty(title))){
                            if(!jobPost.getJobtitle().equals(title)){
                                if(filteredList.contains(jobPost))
                                    filteredList.remove(jobPost);

                            }
//                                inFilter=false;
                            Toast.makeText(JobPostsActivity.this,"Job title field is empty!",Toast.LENGTH_SHORT).show();
                        }
                        if (!(TextUtils.isEmpty(prof))){
                            if(!jobPost.getFullname().equals(prof)){
                                if(filteredList.contains(jobPost))
                                    filteredList.remove(jobPost);
                            }
//                                inFilter=false;
                            Toast.makeText(JobPostsActivity.this,"Instructor Name field is empty!",Toast.LENGTH_SHORT).show();
                        }
//                        if(inFilter==true)
//                            filteredList.add(jobPost);
                        if (profNames.isEmpty())
                            profNames.add(jobPost.getFullname());
                        else if(!profNames.contains(jobPost.getFullname()))
                            profNames.add(jobPost.getFullname());
                    }
//                    if(!course.equals("")) {
//                        if ((validateCourseName(course))) {
//                            Toast.makeText(JobPostsActivity.this, "Course name is in wrong format!", Toast.LENGTH_SHORT).show();
//                        }
//                    }
////
//                    if(!(TextUtils.isEmpty(title))){
//                        Toast.makeText(JobPostsActivity.this,"Job title field is empty!",Toast.LENGTH_SHORT).show();
//                    }
//                    if (!(TextUtils.isEmpty(prof))){
//                        Toast.makeText(JobPostsActivity.this,"Instructor Name field is empty!",Toast.LENGTH_SHORT).show();
//                    }
                         System.out.println("*******************************HERE WE STOP **********************************");
                        System.out.println("********* "+filteredList.size()+" ********************");
                    JobPostAdapter jobPostAdapter = new JobPostAdapter(JobPostsActivity.this,filteredList,"YES");
                    postList.setAdapter(jobPostAdapter);
                    postList.setHasFixedSize(true);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(JobPostsActivity.this);
                    linearLayoutManager.setReverseLayout(true);
                    linearLayoutManager.setStackFromEnd(true);
                    postList.setLayoutManager(linearLayoutManager);

                    ArrayAdapter<String> profAdapter = new ArrayAdapter<String>(JobPostsActivity.this,
                            android.R.layout.simple_spinner_item, profNames);
                    profAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    filterProfNameSpinner.setAdapter(profAdapter);
                }
//
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
//
    }

    public boolean validateCourseName(String course){
        if(course.length()!=9)
            return true;
        else if(!course.matches("^[A-Z]{4}\\s\\d{4}"))
            return true;

        return false;
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
;
                    }
                    JobPostAdapter jobPostAdapter = new JobPostAdapter(JobPostsActivity.this,list,"YES");
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

    private void DisplayUserJobPosts() {
        final List<String> profNames = new ArrayList<String>();

//        allPostRef = FirebaseDatabase.getInstance().getReference().child("General Posts");
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users Posts");
        postList = (RecyclerView) findViewById(R.id.jobPostsrecyclerview);

        usersRef.child(currUserID).addValueEventListener(new ValueEventListener() {
            final List<JobPost> list= new ArrayList<JobPost>();


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    profNames.add(" Instructor Name:");
                    Iterable<DataSnapshot> children = snapshot.getChildren();

                    for(DataSnapshot child : children){
                        JobPost jobPost = child.getValue(JobPost.class);
                        list.add(jobPost);
                        if (profNames.isEmpty())
                            profNames.add(jobPost.getFullname());
                        else if(!profNames.contains(jobPost.getFullname()))
                            profNames.add(jobPost.getFullname());
                    }
                    JobPostAdapter jobPostAdapter = new JobPostAdapter(JobPostsActivity.this,list,"NO");
                    postList.setAdapter(jobPostAdapter);
                    postList.setHasFixedSize(true);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(JobPostsActivity.this);
                    linearLayoutManager.setReverseLayout(true);
                    linearLayoutManager.setStackFromEnd(true);
                    postList.setLayoutManager(linearLayoutManager);

                    ArrayAdapter<String> profAdapter = new ArrayAdapter<String>(JobPostsActivity.this,
                            android.R.layout.simple_spinner_item, profNames);
                    profAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    filterProfNameSpinner.setAdapter(profAdapter);
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