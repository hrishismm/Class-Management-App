package com.example.classmanagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.Time;
import com.github.tlaabs.timetableview.TimetableView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class time_table extends AppCompatActivity {

    private Context context;
    public static final int REQUEST_ADD = 1;
    public static final int REQUEST_EDIT = 2;
    private FirebaseFirestore fstore=FirebaseFirestore.getInstance();
    private DocumentReference noteRef = fstore.document("Time_table/seven");
    private Button addBtn;
    private Button clearBtn;
    private Button saveBtn;
    private Button loadBtn;
private Spinner daySpinner;
    private TimetableView timetable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        this.setTitle("Update Timetable");
        init();



    }

    private void init(){
        this.context = this;
        addBtn = findViewById(R.id.add_btn);
        clearBtn = findViewById(R.id.clear_btn);
        //saveBtn = findViewById(R.id.save_btn);
        loadBtn = findViewById(R.id.load_btn);
        daySpinner = findViewById(R.id.day_spinner);

        timetable = findViewById(R.id.timetable);
        timetable.setHeaderHighlight(2);
        initView();


    }

    private void initView(){
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "HHH", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(),EditActivity.class);
                i.putExtra("mode",REQUEST_ADD);
                startActivityForResult(i,REQUEST_ADD);

            }
        });
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timetable.removeAll();
                String text =daySpinner.getSelectedItem().toString();

                for (int i=0;i<1000;i++)
                {
                    DocumentReference documentReference = fstore.collection("Time_table").document("class").collection(text).document(String.valueOf(i));

                    documentReference.delete();

                }
                Toast.makeText(context, "Data has been cleared", Toast.LENGTH_SHORT).show();
            }
        });

        loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadSavedData();

            }
        });


        timetable.setOnStickerSelectEventListener(new TimetableView.OnStickerSelectedListener() {
            @Override
            public void OnStickerSelected(int idx, ArrayList<Schedule> schedules) {
                Intent i = new Intent(context, EditActivity.class);
                i.putExtra("mode",REQUEST_EDIT);
                i.putExtra("idx", idx);
                i.putExtra("schedules", schedules);
                startActivityForResult(i,REQUEST_EDIT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ADD:
                if (resultCode == EditActivity.RESULT_OK_ADD) {
                    ArrayList<Schedule> item = (ArrayList<Schedule>) data.getSerializableExtra("schedules");
                    timetable.add(item);
                }
                break;
            case REQUEST_EDIT:
                /** Edit -> Submit */
                if (resultCode == EditActivity.RESULT_OK_EDIT) {
                    int idx = data.getIntExtra("idx", -1);
                    ArrayList<Schedule> item = (ArrayList<Schedule>) data.getSerializableExtra("schedules");
                    timetable.edit(idx, item);
                }
                /** Edit -> Delete */
                else if (resultCode == EditActivity.RESULT_OK_DELETE) {
                    int idx = data.getIntExtra("idx", -1);
                    timetable.remove(idx);
                }
                break;
        }
    }


    /** get  data from Firestore and then restore it in the timetable */
    private void loadSavedData(){
        timetable.removeAll();
        ArrayList<Schedule> schedules = new ArrayList<Schedule>();
        String text =daySpinner.getSelectedItem().toString();
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        for(int i=0;i<200;i++) {
            DocumentReference documentReference = fstore.collection("Time_table").document("class").collection(text).document(String.valueOf(i));
            documentReference.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String classroomno = documentSnapshot.getString("classroomno");
                                String day = documentSnapshot.getString("day");
                                int dayint=Integer.parseInt(day);
                                String lectureend = documentSnapshot.getString("lectureend");
                                String lecturestart = documentSnapshot.getString("lecturestart");
                                String professor = documentSnapshot.getString("professor");
                                String subject = documentSnapshot.getString("subject");
                                int startindex=lecturestart.indexOf(":");
                                int endindex=lectureend.indexOf(":");

                                ArrayList<Schedule> schedules = new ArrayList<Schedule>();
                                Schedule schedule = new Schedule();
                                schedule.setClassTitle(subject);
                                schedule.setDay(dayint);
                                schedule.setClassPlace(classroomno); // sets place
                                schedule.setProfessorName(professor); // sets professor
                                schedule.setStartTime(new Time(Integer.valueOf(lecturestart.substring(0,startindex)),lecturestart.lastIndexOf("0"+1))); // sets the beginning of class time (hour,minute)
                                schedule.setEndTime(new Time(Integer.valueOf(lectureend.substring(0,endindex)),lectureend.lastIndexOf("0"+1))); // sets the end of class time (hour,minute)
                                schedules.add(schedule);
//.. add one or more schedules
                                timetable.add(schedules);


                            }
                            else {
                               // Toast.makeText(time_table.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                            //Log.d(TAG, e.toString());
                        }
                    });








            timetable.add(schedules);
        }
        }
}