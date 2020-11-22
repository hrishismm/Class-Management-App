package com.example.classmanagement;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Spinner spin1,spin2;
    String sub,std,TAG="QuestionListFragment";
    private FirebaseFirestore db;
    private CollectionReference ref;
    private QuestionItemAdapter adapter;
    RecyclerView recyclerView;


    public QuestionListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuestionListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuestionListFragment newInstance(String param1, String param2) {
        QuestionListFragment fragment = new QuestionListFragment();
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
        View view = inflater.inflate(R.layout.fragment_question_list, container, false);

        db=FirebaseFirestore.getInstance();

        spin1=(Spinner)view.findViewById(R.id.list_standard_spinner);
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_expandable_list_item_1, getResources().getStringArray(R.array.standard));
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin1.setAdapter(arrayAdapter1);



        spin2= (Spinner) view.findViewById(R.id.list_subject_spinner);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_expandable_list_item_1,getResources().getStringArray(R.array.subjects));
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin2.setAdapter(arrayAdapter2);
       spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //sub=spin2.getSelectedItem().toString().trim();
                setUpRecylerView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        recyclerView= view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }
    public void setUpRecylerView(){
        for (int i=1;i<=50;i++){
            Log.d(TAG, "setUpRecylerView: In setuprecylerview");
            std=spin1.getSelectedItem().toString().trim();
            sub=spin2.getSelectedItem().toString().trim();
            CollectionReference cref;

            ref=db.collection("Tests").document(std).collection(sub).document("QuestionNum").collection(String.valueOf(i));

            Query query = ref.orderBy("qName",Query.Direction.ASCENDING);
            FirestoreRecyclerOptions<QuestionItem> options = new FirestoreRecyclerOptions.Builder<QuestionItem>()
                    .setQuery(query,QuestionItem.class)
                    .build();

            adapter=new QuestionItemAdapter(options);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
            adapter.startListening();

        }
    }


 /*   public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }*/
}

