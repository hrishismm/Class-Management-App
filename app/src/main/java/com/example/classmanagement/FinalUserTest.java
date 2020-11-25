package com.example.classmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FinalUserTest extends AppCompatActivity {
    TextView display,url_text;
    String str,sub_text,std_text,url,TAG="FinalUserTest";
    WebView webView;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_user_test);
        display=(TextView)findViewById(R.id.display_text);
        url_text=(TextView)findViewById(R.id.display_url);
        webView=(WebView)findViewById(R.id.webview);
        this.setTitle("Test");
        db=FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        sub_text=intent.getStringExtra("subject");
        std_text=intent.getStringExtra("standard");
        str="Test for "+std_text+" for subject "+sub_text+":";
        display.setText(str);
        db.collection("Tests").document(std_text).collection(sub_text).document("link")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            url=documentSnapshot.getString("url");
                            url_text.setText(url);
                            webView.loadUrl(url);
                            Toast.makeText(getApplicationContext(),"Loading",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Test no added yet",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       Toast.makeText(getApplicationContext(),"Error Loading Test",Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: "+e.toString());
                    }
                });
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack();
        }
        else {
            super.onBackPressed();
        }
    }
}