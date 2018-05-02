package csp15cap.fitstart;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.util.List;

public class FoodEntryListAdapter extends RecyclerView.Adapter<FoodEntryListAdapter.ViewHolder> {

    private ArrayList<FoodEntry> mFoodEntries;

    public FoodEntryListAdapter(ArrayList<FoodEntry> foodEntries) {
        mFoodEntries = foodEntries;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.food_entry_layout, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mDbRef = FirebaseDatabase.getInstance().getReference().child("FoodEntries").child(currentUid);
        final FoodEntry fe = mFoodEntries.get(i);
        holder.vDesc.setText(fe.getDesc());
        int entryType = (int) fe.getType();
        switch(entryType){
            case 1:holder.vIvFoodIcon.setImageResource(R.drawable.ic_breakfast);break;
            case 2:holder.vIvFoodIcon.setImageResource(R.drawable.ic_lunch);break;
            case 3:holder.vIvFoodIcon.setImageResource(R.drawable.ic_dinner);break;
            case 4:holder.vIvFoodIcon.setImageResource(R.drawable.ic_snack);break;
            default:break;
        }
        holder.vCals.setText(String.valueOf(fe.getCals()));
        holder.vCarbs.setText(String.valueOf(fe.getCarbs()));
        holder.vProtein.setText(String.valueOf(fe.getProtein()));
        holder.vFat.setText(String.valueOf(fe.getFat()));

        mDbRef.child(fe.getSaveDate()).child("Lock").addValueEventListener(new ValueEventListener() {
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

                mDbRef.child(fe.getSaveDate()).child("Entries").child(fe.getFoodEntryId()).removeValue();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mFoodEntries.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView vDesc, vCals, vFat, vCarbs, vProtein;
        protected ImageView vBtnDelete, vIvFoodIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            vDesc = itemView.findViewById(R.id.food_entry_desc);
            vCals = itemView.findViewById(R.id.food_entry_cals);
            vFat = itemView.findViewById(R.id.food_entry_fat);
            vCarbs = itemView.findViewById(R.id.food_entry_carbs);
            vProtein = itemView.findViewById(R.id.food_entry_protein);
            vBtnDelete = itemView.findViewById(R.id.btn_food_entry_delete);
            vIvFoodIcon = itemView.findViewById(R.id.iv_food_icon);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
