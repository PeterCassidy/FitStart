package csp15cap.fitstart;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fatsecret.platform.model.CompactFood;

import java.util.ArrayList;

public class FatsecretResultsListAdapter extends RecyclerView.Adapter<FatsecretResultsListAdapter.FatSecretViewHolder> {

    private ArrayList<CompactFood> mResultList;
    private String mSelectedDate;

    public FatsecretResultsListAdapter(ArrayList<CompactFood> resultList, String selectedDate) {
        mResultList = resultList;
        mSelectedDate =selectedDate;
    }

    @NonNull
    @Override
    public FatSecretViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fs_result_layout, viewGroup, false);

        return new FatSecretViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FatSecretViewHolder holder, int i) {
        CompactFood cf = mResultList.get(i);
        if(cf!=null) {
            holder.vName.setText(cf.getName());
            holder.vDesc.setText(cf.getDescription());
            holder.vBtnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println(cf.getId());
                    FatSecretAddFragment addFrag = new FatSecretAddFragment();
                    Bundle bundle = new Bundle();
                    addFrag.setArguments(bundle);
                    bundle.putString("selectedDate", mSelectedDate);
                    bundle.putString("entryName", cf.getName());
                    bundle.putString("compactFoodDesc", cf.getDescription());
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, addFrag )
                            .addToBackStack(null)
                            .commit();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mResultList.size();
    }


    public static class FatSecretViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView vName, vDesc;
        protected ImageView vBtnAdd;

        public FatSecretViewHolder(View itemView) {
            super(itemView);
            vName = itemView.findViewById(R.id.tv_fs_name);
            vDesc = itemView.findViewById(R.id.tv_fs_desc);
            vBtnAdd = itemView.findViewById(R.id.btn_add_fs_entry);
        }

        @Override
        public void onClick(View view) {

        }

    }
}