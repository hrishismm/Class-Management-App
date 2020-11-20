package com.example.classmanagement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddQuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddQuestionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String standard,subject,qNo,qName,op1,op2,op3,op4,correct_op;
    Spinner spinner1,spinner2;
    TextView t1;
    EditText qNo_text,qName_text,op1_text,op2_text,op3_text,op4_text,correct_op_text;
    FirebaseFirestore db;
    String TAG = "AddQuestionFragment";

    public AddQuestionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddQuestionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddQuestionFragment newInstance(String param1, String param2) {
        AddQuestionFragment fragment = new AddQuestionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_question, container, false);

        db = FirebaseFirestore.getInstance();
        t1=(TextView) view.findViewById(R.id.testing_text);
        qNo_text=(EditText) view.findViewById(R.id.question_no_text);
        qName_text=(EditText) view.findViewById(R.id.question_name_text);
        op1_text=(EditText) view.findViewById(R.id.option1_text);
        op2_text=(EditText) view.findViewById(R.id.option2_text);
        op3_text=(EditText) view.findViewById(R.id.option3_text);
        op4_text=(EditText) view.findViewById(R.id.option4_text);
        correct_op_text=(EditText) view.findViewById(R.id.correct_option_text);



        Button b1=(Button) view.findViewById(R.id.testing_button);


        spinner1=(Spinner)view.findViewById(R.id.select_standard_spinner);
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_expandable_list_item_1, getResources().getStringArray(R.array.standard));
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(arrayAdapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                standard=spinner1.getSelectedItem().toString().trim();
                //t1.setText(standard);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner2= (Spinner) view.findViewById(R.id.select_subject_spinner);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_expandable_list_item_1,getResources().getStringArray(R.array.subjects));
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(arrayAdapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subject=spinner2.getSelectedItem().toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qNo=qNo_text.getText().toString().trim();
                qName=qName_text.getText().toString().trim();
                op1=op1_text.getText().toString().trim();
                op2=op2_text.getText().toString().trim();
                op3=op3_text.getText().toString().trim();
                op4=op4_text.getText().toString().trim();
                correct_op=correct_op_text.getText().toString().trim();

                if (qNo.isEmpty() || qName.isEmpty() || op1.isEmpty() || op2.isEmpty() || op3.isEmpty() ||
                    op4.isEmpty() || correct_op.isEmpty()){
                    Toast.makeText(getContext(),"Please fill the fields",Toast.LENGTH_SHORT).show();
                }
                else {
                    qName_text.setText("");
                    qNo_text.setText("");
                    op1_text.setText("");
                    op2_text.setText("");
                    op3_text.setText("");
                    op4_text.setText("");
                    correct_op_text.setText("");
                    //Toast.makeText(getContext(),"Noice",Toast.LENGTH_SHORT).show();

                    Map<String, Object> qdetails = new HashMap<>();
                    qdetails.put("question name",qName);
                    qdetails.put("option 1",op1);
                    qdetails.put("option 2",op2);
                    qdetails.put("option 3",op3);
                    qdetails.put("option 4", op4);
                    qdetails.put("correct option", correct_op);

                    db.collection("Tests").document(standard).collection(subject).document(qNo)
                            .set(qdetails)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext(),"Added Successfully", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, e.toString());
                                }
                            });

                }
            }
        });

        return view;
    }


}