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
    private Button btnPrevDay, btnNextDay;

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

        mRecyclerView = view.findViewById(R.id.rv_food);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new FoodEntryListAdapter(mFoodEntries);
        mRecyclerView.setAdapter(mAdapter);




        //set current date to today
        String currentDate  = DbDateFormat.format(c.getTime());
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

        getFoodEntries(mFoodEntries, currentDate, mDbRef);
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
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v(TAG, databaseError.getMessage());
            }
        });

    }
    private void setSampleEntries(List<FoodEntry> foodEntries, DatabaseReference mDbRef, String selectedDate) {
        for(FoodEntry foodEntry: foodEntries){
            String foodEntryID = mDbRef.push().getKey();
            foodEntry.setFoodEntryId(foodEntryID);
            mDbRef.child(selectedDate).child(foodEntryID).setValue(foodEntry);
        }


    }

    public static List<FoodEntry> getSampleEntries() {
        //temp data list
        List<FoodEntry> foodEntries = new ArrayList<>();
        FoodEntry foodEntry1 = new FoodEntry();
        foodEntry1.setCals(200);
        foodEntry1.setCarbs(100);
        foodEntry1.setFat(50);
        foodEntry1.setProtein(50);
        foodEntry1.setDesc("bacon sambo");
        foodEntry1.setType(1);

        FoodEntry foodEntry3 = new FoodEntry();
        foodEntry3.setCals(200);
        foodEntry3.setCarbs(100);
        foodEntry3.setFat(50);
        foodEntry3.setProtein(50);
        foodEntry3.setDesc("tomato soup");
        foodEntry3.setType(1);

        FoodEntry foodEntry4 = new FoodEntry();
        foodEntry4.setCals(200);
        foodEntry4.setCarbs(100);
        foodEntry4.setFat(50);
        foodEntry4.setProtein(50);
        foodEntry4.setDesc("bread");
        foodEntry4.setType(1);


        FoodEntry foodEntry2 = new FoodEntry();
        foodEntry2.setCals(300);
        foodEntry2.setCarbs(150);
        foodEntry2.setFat(50);
        foodEntry2.setProtein(100);
        foodEntry2.setDesc("bangers and mash");
        foodEntry2.setType(3);

        foodEntries.add(foodEntry1);
        foodEntries.add(foodEntry2);
        foodEntries.add(foodEntry3);
        foodEntries.add(foodEntry4);
        return foodEntries;
    }
}
