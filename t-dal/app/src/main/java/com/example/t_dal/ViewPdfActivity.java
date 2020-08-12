package com.example.t_dal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ViewPdfActivity extends AppCompatActivity {

    private TextView text1;


    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private StorageReference userProfileTAFiles;
    private String currUserID;

    private WebView webView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);

        mAuth= FirebaseAuth.getInstance();
        currUserID = mAuth.getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currUserID);
        userProfileTAFiles = FirebaseStorage.getInstance().getReference().child("TA Profile Files").child(currUserID);

//        pdfView =(PDFView) findViewById(R.id.pdfview);
//        text1 = (TextView) findViewById(R.id.pdftext);
        webView = (WebView) findViewById(R.id.webView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);


        usersRef.child("taprofile").child("resume").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                text1.setText(value);
                String url = "https://drive.google.com/viewerng/viewer?embedded=true&url="+value;//text1.getText().toString();
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setBuiltInZoomControls(true);
                webView.setWebChromeClient(new WebChromeClient(){
                    @Override
                    public void onProgressChanged(WebView view, int newProgress) {
                        super.onProgressChanged(view, newProgress);
                        getSupportActionBar().setTitle("Loading...");
                        if(newProgress==100){
                            progressBar.setVisibility(View.GONE);
//                            setSupportActionBar().setTitle(R.string.app_name);
                        }
                    }
                });
                webView.loadUrl(url);
                System.out.println("******************"+value+"*******************");
//                new RetrivePdfStream().execute(value);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//        class RetrivePdfStream extends AsyncTask<String,Void, InputStream>{
//
//            @Override
//            protected InputStream doInBackground(String... strings) {
//                InputStream inputStream = null;
//                try {
//                    URL url = new URL(strings[0]);
//                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                    if(urlConnection.getResponseCode()==200){
//                        inputStream = new BufferedInputStream(urlConnection.getInputStream());
//
//                    }
//                }
//                catch (IOException e){
//                    return null;
//                }
//                return inputStream;
//            }
//
//            @Override
//            protected void onPostExecute(InputStream inputStream) {
//                pdfView.fromStream(inputStream).load();
//            }
//        }

//    }
}