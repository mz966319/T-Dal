package com.example.t_dal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ScheduleBookingMessageActivity extends AppCompatActivity {

    private EditText edit01;

    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_booking_message);

        send = (Button) findViewById(R.id.scheduleSendButton);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    public void openDialog() {
        edit01 = (EditText) findViewById(R.id.editTextSchedule1);
        String str01 = edit01.getText().toString();
        if (!str01.equals("")){
        MessageDialog messageDialog = new MessageDialog();
        messageDialog.show(getSupportFragmentManager(), "TA scheduling message");
        }
    }

}
