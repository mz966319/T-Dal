package com.example.t_dal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class CreateTAProfileActivity extends AppCompatActivity {
    private TextView FullName,Username,Email;
    private Button uploadResumeButton,uploadRecordButton,createButton,addCourse;
    private EditText CourseName;
    private Spinner courseSpinner;




    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private StorageReference userProfileTAFiles;
    private String currUserID,docType,checkResume,checkRecord;
    private List<String> courseList;


    Uri resumeUri,recordUri;

    public CreateTAProfileActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_t_a_profile);

        checkResume="";
        checkRecord="";

        mAuth= FirebaseAuth.getInstance();
        currUserID = mAuth.getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currUserID);
        userProfileTAFiles = FirebaseStorage.getInstance().getReference().child("TA Profile Files").child(currUserID);


        FullName = (TextView) findViewById(R.id.full_name_ta);
        Username = (TextView) findViewById(R.id.username_ta);
        Email = (TextView) findViewById(R.id.email_ta);
        uploadRecordButton = (Button) findViewById(R.id.upload_record_ta);
        uploadResumeButton = (Button) findViewById(R.id.upload_resume_ta);
        createButton = (Button) findViewById(R.id.create_button_ta);
        addCourse = (Button) findViewById(R.id.add_course_button);
        CourseName = (EditText) findViewById(R.id.add_course_text);
        courseSpinner = (Spinner) findViewById(R.id.courses_spinner);

        courseList= new ArrayList<String>();

        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!CourseName.getText().toString().equals("")) {
                    courseList.add(CourseName.getText().toString());
//                    String[] jobTitlesArray = new String[]{" Job Title:", "Marker", "TA"};
                    ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(CreateTAProfileActivity.this,
                            android.R.layout.simple_spinner_item, courseList);
                    courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    courseSpinner.setAdapter(courseAdapter);
                    CourseName.setText("");

                }
            }
        });

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    FullName.setText(snapshot.child("fullname").getValue().toString());
                    Username.setText(snapshot.child("username").getValue().toString());
                    if(snapshot.hasChild("email"))
                        Email.setText(snapshot.child("email").getValue().toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        uploadResumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docType="resume";
                Intent intent = new Intent();
                intent.setType("application/pdf");//pdf inst of *
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent.createChooser(intent,"SELECT PDF FILE"),555);
            }
        });

        uploadRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docType="record";
                Intent intent = new Intent();
                intent.setType("application/pdf");//pdf inst of *
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent.createChooser(intent,"SELECT PDF FILE"),66);
            }
        });
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkResume.equals("DONE")&&checkRecord.equals("DONE")) {
                    for(int i=0;i<courseList.size();i++) {
                        usersRef.child("courses").child(courseList.get(i)).setValue(courseList.get(i))
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                        } else {
                                            Toast.makeText(CreateTAProfileActivity.this, "Unable to add courses", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                    }
                    usersRef.child("usertype").setValue("TA")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(CreateTAProfileActivity.this, "TA account created!", Toast.LENGTH_SHORT).show();
                                        Intent mainIntent = new Intent(CreateTAProfileActivity.this, MainActivity.class);
                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(mainIntent);
                                        finish();
//                                            loadinBar.dismiss();
                                    } else {
                                        String message = task.getException().getMessage();
                                        Toast.makeText(CreateTAProfileActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
//                                            loadinBar.dismiss();
                                    }
                                }
                            });
                }
                else
                    Toast.makeText(CreateTAProfileActivity.this,"A file is missing",Toast.LENGTH_LONG).show();

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        uploadResumeButton = (Button) findViewById(R.id.upload_resume_ta);
        uploadRecordButton = (Button) findViewById(R.id.upload_record_ta);
        super.onActivityResult(requestCode, resultCode, data);
            recordUri= data.getData();
            StorageReference filePath = userProfileTAFiles.child(docType+".pdf");//+".pdf"
            filePath.putFile(recordUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        final String downloadUrl = task.getResult().getStorage().getDownloadUrl().toString();
                        usersRef.child("taprofile").child(docType).setValue(downloadUrl)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            if(docType.equals("resume")) {
                                                checkResume="DONE";
                                                uploadResumeButton.setText("Uploaded!");
                                            }

                                            else {
                                                uploadRecordButton.setText("Uploaded!");
                                                checkRecord="DONE";
//                                            loadinBar.dismiss();
                                            }
                                        } else {
                                            String message = task.getException().getMessage();
                                            Toast.makeText(CreateTAProfileActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
//                                            loadinBar.dismiss();
                                        }
                                    }
                                });
                    }
                }
            });


    }
}