package com.example.t_dal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText UserEmail, UserPassword, UserConfirmPassword;
    private Button CreateAccountButton;
    private FirebaseAuth mAuth;
    private ProgressDialog loadinBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        UserEmail = (EditText) findViewById(R.id.register_email);
        UserPassword = (EditText) findViewById(R.id.register_password);
        UserConfirmPassword = (EditText) findViewById(R.id.register_confirm_password);
        CreateAccountButton = (Button) findViewById(R.id.register_create_account);
        loadinBar = new ProgressDialog(this);


        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewAccount();
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currUser = mAuth.getCurrentUser();
        if(currUser!=null)
            SendUserToMainActivity();
    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
    private void CreateNewAccount() {
        String email = UserEmail.getText().toString();
        String password = UserPassword.getText().toString();
        String confirmPassword = UserConfirmPassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Email field is empty!",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Password field is empty!",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(confirmPassword)){
            Toast.makeText(this,"Confirm your password!",Toast.LENGTH_SHORT).show();
        }
        else if (!password.equals(confirmPassword)){
            Toast.makeText(this,"Password does not match with confirm password!",Toast.LENGTH_SHORT).show();
        }
        else{
            loadinBar.setTitle("Creating new Account");
            loadinBar.setMessage("Please wait, account being created...");
            loadinBar.show();
            loadinBar.setCanceledOnTouchOutside(true);
            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                SendUserToSetupActivity();

                                Toast.makeText(RegisterActivity.this,"authenticated successfully!",Toast.LENGTH_SHORT).show();
                                loadinBar.dismiss();
                            }
                            else{
                                String message = task.getException().getMessage();
                                Toast.makeText(RegisterActivity.this,"Error: " + message,Toast.LENGTH_SHORT).show();
                                loadinBar.dismiss();
                            }
                        }
                    });
        }
    }

    private void SendUserToSetupActivity() {
        String email = UserEmail.getText().toString();
        Intent setupIntent = new Intent(RegisterActivity.this, SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        setupIntent.putExtra("email",email);
        startActivity(setupIntent);
        finish();
    }


}