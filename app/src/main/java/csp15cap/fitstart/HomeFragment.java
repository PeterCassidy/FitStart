package csp15cap.fitstart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by Peter Cassidy on 15/03/2018.
 */

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    private FirebaseAuth mAuth;
    private DatabaseReference mDbCalRef, mDbExeRef, mDbChallRef;

    private TextView tvTodayCalsConsumed, tvTodayCalsBurned, tvWeekChallenges, tvWeekOf;
    private int count=0;
        //set date formats
    Calendar c = Calendar.getInstance();
    SimpleDateFormat DbDateFormat = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat displayDateFormat = new SimpleDateFormat("MMM dd, yyyy");
    String selectedDate = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_layout, container, false);
        //set action bar title
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Home");


        tvTodayCalsConsumed = view.findViewById(R.id.home_calories);
        tvTodayCalsBurned = view.findViewById(R.id.home_cals_burned);
        tvWeekChallenges = view.findViewById(R.id.home_challenges);
        tvWeekOf = view.findViewById(R.id.tv_week_of);

        mAuth = FirebaseAuth.getInstance();
        String currentUid = mAuth.getCurrentUser().getUid();
        mDbCalRef = FirebaseDatabase.getInstance().getReference().child("FoodEntries").child(currentUid);
        mDbExeRef = FirebaseDatabase.getInstance().getReference().child("ExerciseEntries").child(currentUid);
        mDbChallRef = FirebaseDatabase.getInstance().getReference().child("DailyChallenges").child(currentUid);
        selectedDate = DbDateFormat.format(c.getTime());
        final ArrayList<FoodEntry> mFoodEntries= new ArrayList<>();



        mDbCalRef.child(selectedDate).child("Entries").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long totCal = 0;
                    mFoodEntries.clear();
                    for (DataSnapshot foodEntrySnapShot : dataSnapshot.getChildren()) {
                        FoodEntry foodEntry = foodEntrySnapShot.getValue(FoodEntry.class);
                        totCal = totCal + foodEntry.getCals();
                    }
                    tvTodayCalsConsumed.setText(String.valueOf(totCal));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        mDbExeRef.child(selectedDate).child("Entries").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long totCal = 0;
                mFoodEntries.clear();
                for (DataSnapshot exerciseEntrySnapShot : dataSnapshot.getChildren()) {
                    ExerciseEntry exerciseEntry = exerciseEntrySnapShot.getValue(ExerciseEntry.class);
                    totCal = totCal + exerciseEntry.getCalsBurned();
                }
                tvTodayCalsBurned.setText(String.valueOf(totCal));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        c.add(Calendar.DAY_OF_YEAR, -7);
        selectedDate = DbDateFormat.format(c.getTime());
        String startDate = displayDateFormat.format(c.getTime());
        tvWeekOf.setText("Week of "+ startDate);

        count = 0;
        for(int i =0; i<7;i++ ) {

            mDbChallRef.child(selectedDate).child("completed").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String result = dataSnapshot.getValue().toString();
                        System.out.println("result = " + result);
                        if (result.equals("true")) {
                            count = count + 1;
                        }
                        System.out.println("Count = " +count);
                        tvWeekChallenges.setText(String.valueOf(count));
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });

            c.add(Calendar.DAY_OF_YEAR,1);
            selectedDate = DbDateFormat.format(c.getTime());
        }

        return view;


    }
}
