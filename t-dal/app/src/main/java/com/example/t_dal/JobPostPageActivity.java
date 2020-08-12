package com.example.t_dal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class JobPostPageActivity extends AppCompatActivity {

    TextView JobTitle,CourseName,Description,Date;
    Button apply;

    String courseName, description,title,profID,date;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef,allPostRef;
//    private StorageReference userProfileTAFiles;
    private String currUserID,profName;//,userType;//,checkResume,checkRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_post_page);

        mAuth= FirebaseAuth.getInstance();
        currUserID = mAuth.getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");//.child(currUserID);
        allPostRef = FirebaseDatabase.getInstance().getReference().child("General Posts");

        JobTitle = (TextView) findViewById(R.id.JobTitle_postPage);
        CourseName = (TextView) findViewById(R.id.CourseName_postPage);
        Description = (TextView) findViewById(R.id.Description_postPage);
        Date = (TextView) findViewById(R.id.Date_postPage);
        apply = (Button) findViewById(R.id.apply_button_postPage);


        Intent intent = getIntent();
        courseName = intent.getExtras().getString("courseName");
        description  = intent.getExtras().getString("description");
        title = intent.getExtras().getString("title");
        profID = intent.getExtras().getString("profID");
        date = intent.getExtras().getString("date");
        profName = intent.getExtras().getString("profName");

        JobTitle.setText(title);
        CourseName.setText(courseName);
        Description.setText(description);
        Date.setText(date);

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap userMap = new HashMap();
                userMap.put("coursename",courseName);
                userMap.put("description",description);
                userMap.put("jobtitle",title);
                userMap.put("date",date);
                userMap.put("userid",profID);
                userMap.put("fullname",profName);

                usersRef.child(currUserID).child("appliedfor").child(date).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            //SendUserToMainActivity();
                            Toast.makeText(JobPostPageActivity.this,"Post added to your profile successfully!",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String message = task.getException().getMessage();
                            Toast.makeText(JobPostPageActivity.this,"Error: "+message,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                usersRef.child(profID).child("applications").child(currUserID).setValue("").addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            //SendUserToMainActivity();
                            Toast.makeText(JobPostPageActivity.this,"Application submitted!",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String message = task.getException().getMessage();
                            Toast.makeText(JobPostPageActivity.this,"Error: "+message,Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                Intent intent = new Intent(JobPostPageActivity.this, JobPostsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("userType","TA" );
                startActivity(intent);
                finish();
            }
        });
    }
}