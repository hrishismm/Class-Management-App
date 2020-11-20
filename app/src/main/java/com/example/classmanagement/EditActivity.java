package com.example.classmanagement;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.Time;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int RESULT_OK_ADD = 1;
    public static final int RESULT_OK_EDIT = 2;
    public static final int RESULT_OK_DELETE = 3;
    FirebaseFirestore fstore;

    private Context context;

    private Button deleteBtn;
    private Button submitBtn;
    private EditText subjectEdit;
    private EditText classroomEdit;
    private EditText professorEdit;
    private Spinner daySpinner,stdspinner;
    private TextView startTv;
    private TextView endTv;

    //request mode
    private int mode;

    private Schedule schedule;
    private int editIdx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        fstore=FirebaseFirestore.getInstance();
        init();
    }

    private void init(){
        this.context = this;
        deleteBtn = findViewById(R.id.delete_btn);
        submitBtn = findViewById(R.id.submit_btn);
        subjectEdit = findViewById(R.id.subject_edit);
        classroomEdit = findViewById(R.id.classroom_edit);
        professorEdit = findViewById(R.id.professor_edit);
        daySpinner = findViewById(R.id.day_spinner);
        stdspinner = findViewById(R.id.std_std);

        startTv = findViewById(R.id.start_time);
        endTv = findViewById(R.id.end_time);

        //set the default time
        schedule = new Schedule();
        schedule.setStartTime(new Time(10,0));
        schedule.setEndTime(new Time(13,30));

        checkMode();
        initView();
    }

    /** check whether the mode is ADD or EDIT */
    private void checkMode(){
        Intent i = getIntent();
        mode = i.getIntExtra("mode",time_table.REQUEST_ADD);

        if(mode == time_table.REQUEST_EDIT){
            loadScheduleData();
            deleteBtn.setVisibility(View.VISIBLE);
            daySpinner.setVisibility(View.INVISIBLE);
            startTv.setVisibility(View.INVISIBLE);
            endTv.setVisibility(View.INVISIBLE);
deleteBtn.setVisibility(View.INVISIBLE);
        }
    }
    private void initView(){
        submitBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);

        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                schedule.setDay(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        startTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(context,listener,schedule.getStartTime().getHour(), schedule.getStartTime().getMinute(), false);
                dialog.show();
            }

            private TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    startTv.setText(hourOfDay + ":" + minute);
                    schedule.getStartTime().setHour(hourOfDay);
                    schedule.getStartTime().setMinute(minute);
                }
            };
        });
        endTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(context,listener,schedule.getEndTime().getHour(), schedule.getEndTime().getMinute(), false);
                dialog.show();
            }

            private TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    endTv.setText(hourOfDay + ":" + minute);
                    schedule.getEndTime().setHour(hourOfDay);
                    schedule.getEndTime().setMinute(minute);
                }
            };
        });
    }

    @Override
    public void onClick(View v) {
        final String subject = subjectEdit.getText().toString();
        final String classroom = classroomEdit.getText().toString();
        final String professor = professorEdit.getText().toString();
        final String lecstart = startTv.getText().toString();
        final String lecend = endTv.getText().toString();
        final String day=String.valueOf(daySpinner.getSelectedItemPosition());
        switch (v.getId()){
            case R.id.submit_btn:
                if(mode == time_table.REQUEST_ADD){
                    DocumentReference documentReference1 = fstore.collection("num").document("count");
                    documentReference1.get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        String text =stdspinner.getSelectedItem().toString();
                                        String number = documentSnapshot.getString("number");
                                        DocumentReference documentReference = fstore.collection("Time_table").document("class").collection(text).document(number);
                                        Map<String,Object> numss=new HashMap<>();
                                        int numint=Integer.parseInt(number);
                                        String uniqueno=String.valueOf(numint);
                                        numint+=1;
                                        number=String.valueOf(numint);
                                        numss.put("number",number);
                                        documentReference1.set(numss).addOnSuccessListener(new OnSuccessListener<Void>()
                                        {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getApplicationContext(), "Added Sucessfully", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("subject", subject);
                                        user.put("classroomno", classroom);
                                        user.put("professor", professor);
                                        user.put("lecturestart", lecstart);
                                        user.put("lectureend", lecend);
                                        user.put("day", day);
                                        user.put("uniqueno",uniqueno);
                                        inputDataProcessing();
                                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getApplicationContext(), "Added Sucessfully", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                    else {
                                       // Toast.makeText(EditActivity.this, "Document does not exist", Toast.LENGTH_SHORT).show();
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

                    int j=0;
                    String is=String.valueOf(j);
                    j++;

                    Intent i = new Intent();
                    ArrayList<Schedule> schedules = new ArrayList<Schedule>();
                    //you can add more schedules to ArrayList
                    schedules.add(schedule);
                    i.putExtra("schedules",schedules);
                    setResult(RESULT_OK_ADD,i);
                    finish();
                }
                else if(mode == time_table.REQUEST_EDIT) {

                    String text =daySpinner.getSelectedItem().toString();

                    for (int j = 0; j < 100; j++) {
                        DocumentReference documentReference1 = fstore.collection("Time_table").document("class").collection(text).document(String.valueOf(j));

                        int finalJ = j;
                        documentReference1.get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists()) {
                                            String uniqueno = documentSnapshot.getString("uniqueno");
                                            DocumentReference documentReference = fstore.collection("Time_table").document("class").collection("seven").document(uniqueno);
                                                Map<String, Object> numss = new HashMap<>();



                                                Map<String, Object> user = new HashMap<>();
                                                user.put("subject", subject);
                                                user.put("classroomno", classroom);
                                                user.put("professor", professor);
                                                user.put("lecturestart", lecstart);
                                                user.put("lectureend", lecend);
                                                user.put("day", day);
                                            user.put("uniqueno", String.valueOf(finalJ));

                                                inputDataProcessing();

                                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                       // Toast.makeText(getApplicationContext(), "Added Sucessfully", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                            }
                                        else {
                                          //  Toast.makeText(EditActivity.this, "Document does not exist", Toast.LENGTH_SHORT).show();
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

                        inputDataProcessing();

                        Intent i = new Intent();
                        ArrayList<Schedule> schedules = new ArrayList<Schedule>();
                        schedules.add(schedule);
                        i.putExtra("idx", editIdx);
                        i.putExtra("schedules", schedules);
                        setResult(RESULT_OK_EDIT, i);
                        finish();
                    }
                }
                break;
            }
    }

    private void loadScheduleData(){
        Intent i = getIntent();
        editIdx = i.getIntExtra("idx",-1);
        ArrayList<Schedule> schedules = (ArrayList<Schedule>)i.getSerializableExtra("schedules");
        schedule = schedules.get(0);
        subjectEdit.setText(schedule.getClassTitle());
        classroomEdit.setText(schedule.getClassPlace());
        professorEdit.setText(schedule.getProfessorName());
        daySpinner.setSelection(schedule.getDay());
    }

    private void inputDataProcessing(){
        schedule.setClassTitle(subjectEdit.getText().toString());
        schedule.setClassPlace(classroomEdit.getText().toString());
        schedule.setProfessorName(professorEdit.getText().toString());
    }
}
