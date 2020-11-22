package com.example.classmanagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class offline_downloads_file_upload_admin extends AppCompatActivity {

    EditText fileupload;

    Button upload_button;

    //Object Declaration
    StorageReference storageReference;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_downloads_file_upload_admin);

    fileupload=(EditText)findViewById(R.id.editText);
    upload_button=(Button)findViewById(R.id.button);

    //Initialize
    storageReference= FirebaseStorage.getInstance().getReference();
databaseReference= FirebaseDatabase.getInstance().getReference("uploads");


upload_button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
selectpdffile();
    }

    private void selectpdffile() {

    Intent i = new Intent();
    i.setType("application/pdf");
i.setAction(Intent.ACTION_GET_CONTENT);
startActivityForResult(i.createChooser(i,"Select PDF File"),1);


    }

});


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data != null && data.getData()!=null)
        {
           uploadPDFFile(data.getData());
        }

    }

    private void uploadPDFFile(Uri data) {

        
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.show();
        StorageReference reference = storageReference.child("uploads/"+System.currentTimeMillis()+".pdf");
        reference.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
while(!uri.isComplete());
Uri url=uri.getResult();

uploadPDF uploadPDF=new uploadPDF(fileupload.getText().toString(),url.toString());
databaseReference.child(databaseReference.push().getKey()).setValue(uploadPDF);
                Toast.makeText(offline_downloads_file_upload_admin.this, "File uploaded", Toast.LENGTH_SHORT).show();
fileupload.setText("");
            progressDialog.dismiss();
            }}).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

              @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress=(100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded   "+(int)progress+"%");
            }
        });
    }
}