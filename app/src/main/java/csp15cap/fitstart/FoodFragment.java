package csp15cap.fitstart;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Peter Cassidy on 15/03/2018.
 */

public class FoodFragment extends Fragment {

    private static final String TAG = "FoodFragment";

    private FirebaseAuth mAuth;
    private DatabaseReference mDbRef; //ref to users>uid>foodentries>

    private TextView tvDate;
    private Button btnPrevDay, btnNextDay, btnNewEntry;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    //set date formats
    Calendar c = Calendar.getInstance();
    SimpleDateFormat DbDateFormat = new SimpleDateFormat("ddMMyyyy");
    SimpleDateFormat displayDateFormat = new SimpleDateFormat("MMM dd, yyyy");
    String selectedDate = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_layout, container, false);
        //set action bar title
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Meals");

        mAuth = FirebaseAuth.getInstance();
        String currentUid = mAuth.getCurrentUser().getUid();
        mDbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid).child("FoodEntries");
        final ArrayList<FoodEntry> mFoodEntries = new ArrayList<>();

        tvDate = view.findViewById(R.id.food_date);
        btnNextDay = view.findViewById(R.id.btn_food_next);
        btnPrevDay = view.findViewById(R.id.btn_food_prev);
        btnNewEntry = view.findViewById(R.id.btn_new_food_entry);

        mRecyclerView = view.findViewById(R.id.rv_food);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new FoodEntryListAdapter(mFoodEntries);
        mRecyclerView.setAdapter(mAdapter);




        //set current date to today
        selectedDate  = DbDateFormat.format(c.getTime());
        //set display date
        tvDate.setText(displayDateFormat.format(c.getTime()));

//        c.add(Calendar.DAY_OF_YEAR, 1);
//        final String selectedDate = DbDateFormat.format(c.getTime());
//
//        Date formatTomorrowDate = null;
//        try{
//            formatTomorrowDate = DbDateFormat.parse(selectedDate);
//            String finalTomorrowDate = displayDateFormat.format(formatTomorrowDate);
//            Log.v(TAG, finalTomorrowDate);
//        }catch(ParseException e){e.printStackTrace();}

        btnPrevDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.add(Calendar.DAY_OF_YEAR, -1);
                selectedDate = DbDateFormat.format(c.getTime());
                tvDate.setText(displayDateFormat.format(c.getTime()));
                getFoodEntries(mFoodEntries, selectedDate, mDbRef);
            }
        });


        btnNextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.add(Calendar.DAY_OF_YEAR, 1);
                selectedDate = DbDateFormat.format(c.getTime());
                tvDate.setText(displayDateFormat.format(c.getTime()));
                getFoodEntries(mFoodEntries, selectedDate, mDbRef);
            }
        });

        btnNewEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewFoodEntryFragment entryFrag = new NewFoodEntryFragment();
                Bundle bundle = new Bundle();
                entryFrag.setArguments(bundle);
                bundle.putString("selectedDate", selectedDate);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, entryFrag )
                        .addToBackStack(null)
                        .commit();
            }
        });

        getFoodEntries(mFoodEntries, selectedDate, mDbRef);
        return view;
    }


    private void getFoodEntries(final List<FoodEntry> mFoodEntries, String selectedDate, DatabaseReference DbRef){

        DatabaseReference mDateRef = DbRef.child(selectedDate);
        mDateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mFoodEntries.clear();
                for(DataSnapshot foodEntrySnapShot: dataSnapshot.getChildren()){
                    FoodEntry foodEntry = foodEntrySnapShot.getValue(FoodEntry.class);
                    FoodEntry tempEntry = foodEntry;
                    mFoodEntries.add(tempEntry);
                }
                Collections.sort(mFoodEntries);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v(TAG, databaseError.getMessage());
            }
        });

    }

}
