package com.example.t_dal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BookingActivity extends AppCompatActivity {

    private EditText edit01, edit02, edit03, edit04;

    private Button schedule, book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        schedule = (Button) findViewById(R.id.buttonScheduleBooking);
        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookingActivity.this, ScheduleBookingMessageActivity.class);
                startActivity(intent);
            }
        });

        book = (Button) findViewById(R.id.bookingSubmitButton);
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    public void openDialog() {
        edit01 = (EditText) findViewById(R.id.editTextBooking2);
        String str01 = edit01.getText().toString();
        edit02 = (EditText) findViewById(R.id.editTextBooking4);
        String str02 = edit02.getText().toString();
        edit03 = (EditText) findViewById(R.id.editTextBooking1);
        String str03 = edit03.getText().toString();
        edit04 = (EditText) findViewById(R.id.editTextBooking3);
        String str04 = edit04.getText().toString();

        if (((str01.equals("Monday") && str02.equals("Alex")) || (str01.equals("Tuesday") && str02.equals("Laura")) ||
                (str01.equals("Wednesday") && str02.equals("Amir")) || (str01.equals("Thursday") &&
                str02.equals("Vahid")) || (str01.equals("Friday") && str02.equals("Max"))) && (!str03.equals("") && !str04.equals(""))){
            BookingDialog bookingDialog = new BookingDialog();
            bookingDialog.show(getSupportFragmentManager(), "booking submission message");
        }

    }

}