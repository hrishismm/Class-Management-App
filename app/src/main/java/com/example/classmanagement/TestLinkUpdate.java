package com.example.classmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TestLinkUpdate extends AppCompatActivity {
    Button submit;
    EditText link_txt;
    String link,subject,standard,TAG="TestLinkUpdate";
    Spinner std_spin,sub_spin;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_link_update);
        this.setTitle("Update Test Link");
        submit=(Button)findViewById(R.id.submit_link_button);
        link_txt=(EditText) findViewById(R.id.link_text);
        std_spin=(Spinner)findViewById(R.id.std_spinner);
        sub_spin=(Spinner)findViewById(R.id.sub_spinner);
        db=FirebaseFirestore.getInstance();


        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_expandable_list_item_1, getResources().getStringArray(R.array.standard));
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        std_spin.setAdapter(arrayAdapter1);

        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_expandable_list_item_1,getResources().getStringArray(R.array.subjects));
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sub_spin.setAdapter(arrayAdapter2);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                link=link_txt.getText().toString().trim();
                subject=sub_spin.getSelectedItem().toString().trim();
                standard=std_spin.getSelectedItem().toString().trim();
                if(link.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter the Link",Toast.LENGTH_SHORT).show();
                }
                else{

                    Map<String, Object> data= new HashMap<>();
                    data.put("url",link);

                    db.collection("Tests").document(standard).collection(subject).document("link")
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(),"Added Successfully",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(),"Error Adding Link",Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "onFailure: "+e.toString());
                                }
                            });

                    link_txt.setText("");
                }
            }
        });


    }
}