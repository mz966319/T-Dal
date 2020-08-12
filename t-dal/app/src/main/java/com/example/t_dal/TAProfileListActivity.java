package com.example.t_dal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class TAProfileListActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private String currUserID;//,userType;//,checkResume,checkRecord;
    RecyclerView recyclerView;
    private DrawerLayout drawerLayout;
    private ImageButton searchButton;
    private NavigationView navigationView;
    private View navVeiew;
    private TextView filterCourseName;
    private Button filterSearchButton;

    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_a_profile_list);

        drawerLayout = (DrawerLayout) findViewById(R.id.taList_draweble_layout);
        navigationView = (NavigationView) findViewById(R.id.ta_list_navigation_view);
        navVeiew = navigationView.inflateHeaderView(R.layout.filter_tas_options);
        filterCourseName = (TextView) navVeiew.findViewById(R.id.findByCourseNameTxt_ta);;
        filterSearchButton = (Button) navVeiew.findViewById(R.id.filter_search_button_ta);
        searchButton = (ImageButton) findViewById(R.id.header_search_button_ta_list);

        recyclerView = (RecyclerView) findViewById(R.id.profile_list_rec);
        System.out.println("*************************************************************");
        mAuth= FirebaseAuth.getInstance();
        currUserID = mAuth.getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");//.child(currUserID);

        mToolbar = (Toolbar) findViewById(R.id.taList_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("TAs List");


        recyclerView = (RecyclerView) findViewById(R.id.profile_list_rec);
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
                String course="";
                if(filterCourseName.getText().toString()!=null)
                    course=filterCourseName.getText().toString().toUpperCase();
                Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("userType","filter TA" );
                intent.putExtra("course",course );
                startActivity(intent);
                finish();
            }
        });

        Intent intent = getIntent();
        String userType = intent.getExtras().getString("userType");
        String course = intent.getExtras().getString("course");


        if(userType.equals("mInstructor")) {
            DisplayTAsApplied();

        }
        else if(userType.equals("filter TA")){
            FilterTAList(course);
        }
        else
            DisplayAllTAs();
    }
    public void DisplayTAsApplied(){
        usersRef.child(currUserID).child("applications").addValueEventListener(new ValueEventListener() {
            final List<String> id= new ArrayList<String>();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    Iterable<DataSnapshot> children = snapshot.getChildren();
                    for(DataSnapshot child : children){
                        id.add(child.getKey());
                    }

                    /**here**/
                    usersRef.addValueEventListener(new ValueEventListener() {
                        final List<DataSnapshot> data= new ArrayList<DataSnapshot>();

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Iterable<DataSnapshot> children = snapshot.getChildren();
                                for(DataSnapshot child : children){
                                    if(id.contains(child.getKey())) {
                                        String userType = child.child("usertype").getValue().toString();
                                        if (userType.equals("TA")) {
                                            data.add(child);
                                        }
                                    }
                                }
                                TAsListAdapter tAsListAdapter = new TAsListAdapter(TAProfileListActivity.this,data);
                                recyclerView.setAdapter(tAsListAdapter);
                                recyclerView.setHasFixedSize(true);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TAProfileListActivity.this);
                                linearLayoutManager.setReverseLayout(true);
                                linearLayoutManager.setStackFromEnd(true);
                                recyclerView.setLayoutManager(linearLayoutManager);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });








    }

    public void FilterTAList(final String course){
        usersRef.addValueEventListener(new ValueEventListener() {
            final List<DataSnapshot> data= new ArrayList<DataSnapshot>();

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Iterable<DataSnapshot> children = snapshot.getChildren();
                    for(DataSnapshot child : children){
                        String userType = child.child("usertype").getValue().toString();
                        if(userType.equals("TA")){
                            if(child.hasChild("courses")){
                                if(child.child("courses").hasChild(course)){
                                    data.add(child);
                                }
                            }

                        }
                    }
                    TAsListAdapter tAsListAdapter = new TAsListAdapter(TAProfileListActivity.this,data);
                    recyclerView.setAdapter(tAsListAdapter);
                    recyclerView.setHasFixedSize(true);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TAProfileListActivity.this);
                    linearLayoutManager.setReverseLayout(true);
                    linearLayoutManager.setStackFromEnd(true);
                    recyclerView.setLayoutManager(linearLayoutManager);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void DisplayAllTAs(){
        usersRef.addValueEventListener(new ValueEventListener() {
            final List<DataSnapshot> data= new ArrayList<DataSnapshot>();

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Iterable<DataSnapshot> children = snapshot.getChildren();
                    for(DataSnapshot child : children){
                        String userType = child.child("usertype").getValue().toString();
                        if(userType.equals("TA")){
                            data.add(child);
                        }
                    }
                    TAsListAdapter tAsListAdapter = new TAsListAdapter(TAProfileListActivity.this,data);
                    recyclerView.setAdapter(tAsListAdapter);
                    recyclerView.setHasFixedSize(true);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TAProfileListActivity.this);
                    linearLayoutManager.setReverseLayout(true);
                    linearLayoutManager.setStackFromEnd(true);
                    recyclerView.setLayoutManager(linearLayoutManager);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(TAProfileListActivity.this, MainActivity.class);
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