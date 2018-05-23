package csp15cap.fitstart;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ExerciseEntryListAdapter extends RecyclerView.Adapter<ExerciseEntryListAdapter.ViewHolder>{

    private ArrayList<ExerciseEntry> mExerciseEntries;

    public ExerciseEntryListAdapter(ArrayList<ExerciseEntry> exerciseEntries){mExerciseEntries = exerciseEntries;}

    @NonNull
    @Override
    public ExerciseEntryListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.exercise_entry_layout, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseEntryListAdapter.ViewHolder holder, int i) {
        String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mDbRef = FirebaseDatabase.getInstance().getReference().child("ExerciseEntries").child(currentUid);
        final ExerciseEntry ee = mExerciseEntries.get(i);
        holder.vDesc.setText(ee.getDesc());
        int entryType = (int) ee.getType();
        switch(entryType){
            case 1:holder.vIvExeIcon.setImageResource(R.drawable.ic_cardio);break;
            case 2:holder.vIvExeIcon.setImageResource(R.drawable.ic_strength);break;
            default:break;
        }
        holder.vCals.setText(String.valueOf(ee.getCalsBurned()));

        mDbRef.child(ee.getSaveDate()).child("Lock").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String result;
                    result = dataSnapshot.getValue().toString();
                    if(result.equals("true")) {
                        holder.vBtnDelete.setVisibility(View.INVISIBLE);
                    }else{//set visible if lock not true.
                        holder.vBtnDelete.setVisibility(View.VISIBLE);
                    }
                }else{//set visible if lock doesnt exist
                    holder.vBtnDelete.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.vBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDbRef.child(ee.getSaveDate()).child("Entries").child(ee.getExerciseEntryId()).removeValue();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mExerciseEntries.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView vDesc, vCals;
        protected ImageView vBtnDelete, vIvExeIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            vDesc = itemView.findViewById(R.id.exe_entry_desc);
            vCals = itemView.findViewById(R.id.exe_entry_cals);
            vBtnDelete = itemView.findViewById(R.id.btn_exe_entry_delete);
            vIvExeIcon = itemView.findViewById(R.id.iv_exe_icon);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
