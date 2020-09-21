package com.example.t_dal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class EditTAProfileActivity extends AppCompatActivity {

    private Button updateResumeButton,updateRecordButton,updateButton,addCourse,cancelButton;
    private EditText CourseName;
    private Spinner courseSpinner;




    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private StorageReference userProfileTAFiles;
    private String currUserID,docType,checkResume,checkRecord;
    private List<String> courseList;


    Uri resumeUri,recordUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_t_a_profile);

        mAuth= FirebaseAuth.getInstance();
        currUserID = mAuth.getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currUserID);
        userProfileTAFiles = FirebaseStorage.getInstance().getReference().child("TA Profile Files").child(currUserID);

        updateRecordButton = (Button) findViewById(R.id.update_resume_ta_edit);
        updateResumeButton = (Button) findViewById(R.id.update_record_ta_edit);
        updateButton = (Button) findViewById(R.id.update_button_ta_edit);
        cancelButton = (Button) findViewById(R.id.cancel_button_ta);
        addCourse = (Button) findViewById(R.id.add_course_button_edit);
        CourseName = (EditText) findViewById(R.id.add_course_text_edit);
        courseSpinner = (Spinner) findViewById(R.id.courses_spinner_edit);

        courseList= new ArrayList<String>();

        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!CourseName.getText().toString().equals("")) {
                    courseList.add(CourseName.getText().toString());
//                    String[] jobTitlesArray = new String[]{" Job Title:", "Marker", "TA"};
                    ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(EditTAProfileActivity.this,
                            android.R.layout.simple_spinner_item, courseList);
                    courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    courseSpinner.setAdapter(courseAdapter);
                    CourseName.setText("");
                }
            }
        });

        updateResumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docType="resume";
                Intent intent = new Intent();
                intent.setType("application/pdf");//pdf inst of *
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent.createChooser(intent,"SELECT PDF FILE"),555);
            }
        });

        updateRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docType="record";
                Intent intent = new Intent();
                intent.setType("application/pdf");//pdf inst of *
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent.createChooser(intent,"SELECT PDF FILE"),66);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(EditTAProfileActivity.this, MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainIntent);
                finish();
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0;i<courseList.size();i++) {
                    usersRef.child("courses").child(courseList.get(i)).setValue(courseList.get(i))
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(EditTAProfileActivity.this, "TA account updated!", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(EditTAProfileActivity.this, "Unable to add courses", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
                Intent mainIntent = new Intent(EditTAProfileActivity.this, MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainIntent);
                finish();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        updateResumeButton = (Button) findViewById(R.id.update_resume_ta_edit);
        updateRecordButton = (Button) findViewById(R.id.update_record_ta_edit);
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
                                            updateResumeButton.setText("Updated!");
                                        }

                                        else {
                                            updateRecordButton.setText("Updated!");
                                            checkRecord="DONE";
//                                            loadinBar.dismiss();
                                        }
                                    } else {
                                        String message = task.getException().getMessage();
                                        Toast.makeText(EditTAProfileActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
//                                            loadinBar.dismiss();
                                    }
                                }
                            });
                }
            }
        });


    }
}