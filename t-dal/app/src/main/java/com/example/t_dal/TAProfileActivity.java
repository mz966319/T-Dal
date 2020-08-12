package com.example.t_dal;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import static android.os.Environment.DIRECTORY_DOWNLOADS;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class TAProfileActivity extends AppCompatActivity {
    private TextView FullName,Username,Email;
    private Button viewResumeButton,viewRecordButton,createButton;
    private ImageButton editButton;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private StorageReference userProfileTAFiles;
    private String currUserID,userType,taID;//,checkResume,checkRecord;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_a_profile);




//        userType = intent.getExtras().getString("userType");

        mAuth= FirebaseAuth.getInstance();
        Intent intent = getIntent();
        taID = intent.getExtras().getString("taID");
        if(taID!=null){
            userType = "Instructor";
            currUserID = taID;
        }
        else{
            userType = intent.getExtras().getString("userType");
            currUserID = mAuth.getCurrentUser().getUid();

        }
//        currUserID = mAuth.getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currUserID);
        userProfileTAFiles = FirebaseStorage.getInstance().getReference().child("TA Profile Files").child(currUserID);


        FullName = (TextView) findViewById(R.id.full_name_ta_profile);
        Username = (TextView) findViewById(R.id.username_ta_profile);
        Email = (TextView) findViewById(R.id.email_ta_profile);
//        viewResumeButton = (Button) findViewById(R.id.upload_record_ta_profile);
        viewRecordButton = (Button) findViewById(R.id.upload_record_ta_profile);
        viewResumeButton = (Button) findViewById(R.id.upload_resume_ta_profile);
        createButton = (Button) findViewById(R.id.create_button_ta_profile);
        editButton = (ImageButton) findViewById(R.id.header_edit_ta_profile_button);

        if(userType.equals("TA")){
            createButton.setText("Back");
            createButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(TAProfileActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    intent.putExtra("userType","TA" );
                    startActivity(intent);
                    finish();
                }
            });
        }
        else{
            createButton.setText("Contact");
            editButton.setVisibility(View.GONE);
        }
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TAProfileActivity.this, EditTAProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    intent.putExtra("userType","TA" );
                startActivity(intent);
                finish();
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

        viewResumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                download("resume");
            }
        });
        viewRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                download("record");
            }
        });
    }

    private void download(final String filename) {
        userProfileTAFiles.child(filename+".pdf").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url=uri.toString();
                downloadFiles(TAProfileActivity.this,filename,".pdf",DIRECTORY_DOWNLOADS,url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }

    public void downloadFiles(Context context, String fileName,String fileExtension, String destinationDirectory,String url) {
        DownloadManager downloadManager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context,destinationDirectory,fileName+fileExtension);
        downloadManager.enqueue(request);
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//        if(id == android.R.id.home){
//
//            SendUserToMainActivity();
//        }
//        return super.onOptionsItemSelected(item);
//    }
    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(TAProfileActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
