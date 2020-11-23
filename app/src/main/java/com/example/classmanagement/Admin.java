package com.example.classmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Admin extends AppCompatActivity {

    Button testUpdate;
    Button time_table,add_pdf;
    Button linkUpdate,noticeUpload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        testUpdate = (Button) findViewById(R.id.testUpdateButton);
        time_table= (Button) findViewById(R.id.time_table);
        add_pdf= (Button) findViewById(R.id.add_pdf);
        linkUpdate=(Button) findViewById(R.id.testlinkupdate_button);
        noticeUpload=(Button) findViewById(R.id.notice_upload_btn);

        time_table.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),time_table.class);
                startActivity(intent);
            }
        });
        testUpdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
               // Toast.makeText(getApplicationContext(),"Working",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),TestUpdate.class);
                startActivity(intent);
            }
        });
        add_pdf.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Toast.makeText(getApplicationContext(),"Working",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),offline_downloads_file_upload_admin.class);
                startActivity(intent);
            }
        });
        linkUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),TestLinkUpdate.class);
                startActivity(intent);
            }
        });

        noticeUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),NoticeUpload.class);
                startActivity(intent);
            }
        });


    }
}