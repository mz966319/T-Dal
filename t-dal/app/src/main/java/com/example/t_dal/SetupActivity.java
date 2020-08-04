package com.example.t_dal;

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class SetupActivity extends AppCompatActivity {

    private EditText UserName, FullName;
    private Spinner UserType;
    private Button SaveInfoButton;
    private CircleImageView ProfileImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        UserName = (EditText) findViewById(R.id.setup_username);
        FullName = (EditText) findViewById(R.id.setup_full_name);
        UserType = (Spinner) findViewById(R.id.setup_user_type);
        SaveInfoButton = (Button) findViewById(R.id.setup_information_button);
        ProfileImage = (CircleImageView) findViewById(R.id.setup_profile_image);

    }
}