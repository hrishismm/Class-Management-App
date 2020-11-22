package com.example.classmanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class QuestionItemAdapter extends FirestoreRecyclerAdapter<QuestionItem, QuestionItemAdapter.NoteHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public QuestionItemAdapter(@NonNull FirestoreRecyclerOptions<QuestionItem> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull QuestionItemAdapter.NoteHolder holder, int position, @NonNull QuestionItem model) {
        holder.qname_textview.setText(model.getqName());
        holder.op1_textview.setText(model.getOp1());
        holder.op2_textview.setText(model.getOp2());
        holder.op3_textview.setText(model.getOp3());
        holder.op4_textview.setText(model.getOp4());
        holder.cop_textview.setText(model.getCop());
    }

    @NonNull
    @Override
    public QuestionItemAdapter.NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item,parent,false);
        return new NoteHolder(v);
    }

    class NoteHolder extends RecyclerView.ViewHolder{
        TextView qname_textview,op1_textview,op2_textview,op3_textview,op4_textview,cop_textview;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            qname_textview=itemView.findViewById(R.id.qName_display);
            op1_textview=itemView.findViewById(R.id.op1_display);
            op2_textview=itemView.findViewById(R.id.op2_display);
            op3_textview=itemView.findViewById(R.id.op3_display);
            op4_textview=itemView.findViewById(R.id.op4_display);
            cop_textview=itemView.findViewById(R.id.correct_op_display);
        }
    }
}
