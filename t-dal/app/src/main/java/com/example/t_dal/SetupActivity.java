package com.example.t_dal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import java.util.HashMap;

public class SetupActivity extends AppCompatActivity {

    private EditText UserName, FullName;
    private Spinner UserType;
    private Button SaveInfoButton;
    private CircleImageView ProfileImage;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private StorageReference userProfileImageRef;

    String currUserID;
    private ProgressDialog loadinBar;
    final static int Gallery_pic=1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mAuth= FirebaseAuth.getInstance();
        currUserID = mAuth.getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currUserID);
        userProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        UserName = (EditText) findViewById(R.id.setup_username);
        FullName = (EditText) findViewById(R.id.setup_full_name);
        UserType = (Spinner) findViewById(R.id.setup_user_type);
        SaveInfoButton = (Button) findViewById(R.id.setup_information_button);
        ProfileImage = (CircleImageView) findViewById(R.id.setup_profile_image);
        String[] arraySpinnerType = new String[] {"User Type:","Instructor", "Student"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinnerType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        UserType.setAdapter(adapter);
        loadinBar = new ProgressDialog(this);


        SaveInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveAccountSetupInfo();
            }
        });

        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,Gallery_pic);
            }
        });

//        usersRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    if(snapshot.hasChild("profileimage")) {
//                    String image = snapshot.child("profileimage").getValue().toString();
//                    System.out.println("*******************************************");
//                    System.out.println("*******************************************");
//                    System.out.println(image);
//                    System.out.println("*******************************************");
//                    System.out.println("*******************************************");
//
////                    Picasso.with(SetupActivity.this).load(image).placeholder(R.drawable.profile).into(ProfileImage);
////                    Picasso.get().load(image).placeholder(R.drawable.profile).into(ProfileImage);
////                    Picasso.get().load(image).into(ProfileImage);
////                    Picasso.get().load(image).placeholder(R.drawable.profile).into(ProfileImage);
////                    Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(ProfileImage);
//
        //}
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Gallery_pic && resultCode == RESULT_OK && data!=null){
            Uri ImageUri = data.getData();
            CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1).start(this);
        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                loadinBar.setTitle("Profile Image");
                loadinBar.setMessage("Please wait, profile image being updated...");
                loadinBar.show();
                loadinBar.setCanceledOnTouchOutside(true);
                Uri resultUri = result.getUri();
                StorageReference filePath = userProfileImageRef.child(currUserID + ".jpg");

                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SetupActivity.this,"Profile image stored successfully!",Toast.LENGTH_SHORT).show();
                            final String downloadUrl = task.getResult().getStorage().getDownloadUrl().toString();
                            usersRef.child("profileimage").setValue(downloadUrl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){

                                                Intent selfIntent = new Intent(SetupActivity.this, SetupActivity.class);
                                                startActivity(selfIntent);
                                                Toast.makeText(SetupActivity.this,"Profile image stored to database successfully!",Toast.LENGTH_SHORT).show();
                                                loadinBar.dismiss();
                                            }
                                            else{
                                                String message = task.getException().getMessage();
                                                Toast.makeText(SetupActivity.this,"Error: "+message,Toast.LENGTH_SHORT).show();
                                                loadinBar.dismiss();
                                            }
                                        }
                                    });
                        }
                    }
                });
            }
            else {
                Toast.makeText(this,"Error: Image cant be cropped. Try Again! ",Toast.LENGTH_SHORT).show();
                loadinBar.dismiss();
            }

        }
    }

    private void SaveAccountSetupInfo() {
        String username = UserName.getText().toString();
        String fullname = FullName.getText().toString();
        String userType = UserType.getSelectedItem().toString();

        Intent intent = getIntent();
        String email = intent.getExtras().getString("email");
        System.out.println("************************"+email+"*****************************");

        String course=intent.getExtras().getString("course");

        if(TextUtils.isEmpty(username)){
            Toast.makeText(this,"Username field is empty!",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(fullname)){
            Toast.makeText(this,"Full name field is empty!",Toast.LENGTH_SHORT).show();
        }
        else if(userType.equals("User Type:")){
            Toast.makeText(this,"User type field is empty!",Toast.LENGTH_SHORT).show();
        }
        else {
            loadinBar.setTitle("Saving Information");
            loadinBar.setMessage("Please wait, information being saved...");
            loadinBar.show();
            loadinBar.setCanceledOnTouchOutside(true);
            HashMap userMap = new HashMap();
            userMap.put("username",username);
            userMap.put("fullname",fullname);
            userMap.put("usertype",userType);
            userMap.put("email",email);
            usersRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        SendUserToMainActivity();
                        Toast.makeText(SetupActivity.this,"Acouut Created Successfully!",Toast.LENGTH_LONG).show();
                        loadinBar.dismiss();
                    }
                    else{
                        String message = task.getException().getMessage();
                        Toast.makeText(SetupActivity.this,"Error: "+message,Toast.LENGTH_SHORT).show();
                        loadinBar.dismiss();

                    }
                }
            });

        }
    }
    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(SetupActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}