package csp15cap.fitstart;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        FoodEntry fe = mFoodEntries.get(i);
        holder.vDesc.setText(fe.getDesc());
        holder.vCals.setText(String.valueOf(fe.getCals()));
        holder.vCarbs.setText(String.valueOf(fe.getCarbs()));
        holder.vProtein.setText(String.valueOf(fe.getProtein()));
        holder.vFat.setText(String.valueOf(fe.getFat()));

    }

    @Override
    public int getItemCount() {
        return mFoodEntries.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView vDesc, vCals, vFat, vCarbs, vProtein;

        public ViewHolder(View itemView) {
            super(itemView);
            vDesc = itemView.findViewById(R.id.food_entry_desc);
            vCals = itemView.findViewById(R.id.food_entry_cals);
            vFat = itemView.findViewById(R.id.food_entry_fat);
            vCarbs = itemView.findViewById(R.id.food_entry_carbs);
            vProtein = itemView.findViewById(R.id.food_entry_protein);
        }
    }
}
