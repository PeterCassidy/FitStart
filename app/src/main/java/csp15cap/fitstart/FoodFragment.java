package csp15cap.fitstart;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.fatsecret.platform.model.CompactFood;
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
    private DatabaseReference mDbRef;
    private DatabaseReference mDbTargetRef;

    private TextView tvDate, tvTotCals, tvTotCarbs, tvTotProtein, tvTotFat, tvTargetCals;
    private Button btnPrevDay, btnNextDay, btnNewEntry, btnLockToday;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    //set date formats
    Calendar c = Calendar.getInstance();
    SimpleDateFormat DbDateFormat = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat displayDateFormat = new SimpleDateFormat("MMM dd, yyyy");
    SimpleDateFormat pickerFormat = new SimpleDateFormat("dd/MM/yyyy");
    String selectedDate = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_layout, container, false);
        //set action bar title
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Meals");

        mAuth = FirebaseAuth.getInstance();
        String currentUid = mAuth.getCurrentUser().getUid();
        mDbRef = FirebaseDatabase.getInstance().getReference().child("FoodEntries").child(currentUid);
        mDbTargetRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid);
        final ArrayList<FoodEntry> mFoodEntries = new ArrayList<>();

        tvDate = view.findViewById(R.id.food_date);
        btnNextDay = view.findViewById(R.id.btn_food_next);
        btnPrevDay = view.findViewById(R.id.btn_food_prev);
        btnNewEntry = view.findViewById(R.id.btn_new_food_entry);
        btnLockToday = view.findViewById(R.id.btn_confirm_food_today);

        tvTotCals = view.findViewById(R.id.tv_today_cals);
        tvTotCarbs = view.findViewById(R.id.tv_today_carbs);
        tvTotProtein = view.findViewById(R.id.tv_today_protein);
        tvTotFat = view.findViewById(R.id.tv_today_fat);
        tvTargetCals = view.findViewById(R.id.tv_target_cals);

        mRecyclerView = view.findViewById(R.id.rv_food);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new FoodEntryListAdapter(mFoodEntries);
        mRecyclerView.setAdapter(mAdapter);


        mDbTargetRef.child("target_cals").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    tvTargetCals.setText("Calorie target is not set...");
                }else{
                    tvTargetCals.setText("Your calorie target for today is "+dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        //set current date to today
        selectedDate  = DbDateFormat.format(c.getTime());
        //set display date
        tvDate.setText(displayDateFormat.format(c.getTime()));
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               int year = c.get(Calendar.YEAR);
               int month =  c.get(Calendar.MONTH);
               int day = c.get(Calendar.DAY_OF_MONTH);
               DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                       android.R.style.Theme_Holo_Light_DarkActionBar, mDateSetListener, year, month,day);
               dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
               dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                System.out.println(day+"/"+month+"/"+year);
                c.set(year,month,day);

                setTotsToZero();
                selectedDate = DbDateFormat.format(c.getTime());
                checkTodaysLock(selectedDate);
                tvDate.setText(displayDateFormat.format(c.getTime()));
                getFoodEntries(mFoodEntries, selectedDate, mDbRef);
                }
            };


        btnPrevDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTotsToZero();
                c.add(Calendar.DAY_OF_YEAR, -1);
                selectedDate = DbDateFormat.format(c.getTime());
                checkTodaysLock(selectedDate);
                tvDate.setText(displayDateFormat.format(c.getTime()));
                getFoodEntries(mFoodEntries, selectedDate, mDbRef);
            }
        });


        btnNextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTotsToZero();
                c.add(Calendar.DAY_OF_YEAR, 1);
                selectedDate = DbDateFormat.format(c.getTime());
                checkTodaysLock(selectedDate);
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
                        .addToBackStack("diary")
                        .commit();
            }
        });

        btnLockToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDbTargetRef.child("target_cals").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()){
                            Toast.makeText(getActivity(), "Your calorie target is not set, change this in your profile settings.", Toast.LENGTH_LONG).show();
                        }else{
                            Long targetCals = Long.valueOf(dataSnapshot.getValue().toString());
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Confirmation")
                                    .setMessage("Are you sure you wish to confirm your food entry for today? No changes can be made after this.")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            mDbRef.child(selectedDate).child("Lock").setValue("true");
                                            mAdapter.notifyDataSetChanged();

                                            //update experience
                                            mDbTargetRef.child("experience").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    long currentXP =0;
                                                    if( !dataSnapshot.exists()){
                                                        currentXP = 0;
                                                    }else{
                                                        currentXP = Long.valueOf(dataSnapshot.getValue().toString());
                                                    }
                                                    long totCals = Long.valueOf(tvTotCals.getText().toString());
                                                    long experience = calcExperienceFromCals(targetCals, totCals);


                                                    mDbTargetRef.child("experience").setValue(currentXP+experience);
                                                    Toast.makeText(getActivity(), "Meal entries locked, you earned " + experience + " experience today.", Toast.LENGTH_LONG).show();

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                        }})
                                    .setNegativeButton("Cancel", null).show();


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }
        });




        checkTodaysLock(selectedDate);

        getFoodEntries(mFoodEntries, selectedDate, mDbRef);
        return view;
    }

    private long calcExperienceFromCals(long targetCals, long totCals) {
        long difference = Math.abs(targetCals - totCals);
        long fivePercent = targetCals/20;
        long tenPercent = targetCals/10;
        if (difference>tenPercent){
            return 0;
        }else if(difference>fivePercent){
            return 50;
        }else{
            return 100;
        }


    }


    private void getFoodEntries(final List<FoodEntry> mFoodEntries, String selectedDate, DatabaseReference DbRef){
        DatabaseReference mDateRef = DbRef.child(selectedDate).child("Entries");
        mDateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                long totCal=0,totCarb=0,totProtein=0,totFat=0;

                mFoodEntries.clear();
                for(DataSnapshot foodEntrySnapShot: dataSnapshot.getChildren()){
                    FoodEntry foodEntry = foodEntrySnapShot.getValue(FoodEntry.class);
                    totCal = totCal + foodEntry.getCals();
                    totCarb = totCarb + foodEntry.getCarbs();
                    totProtein = totProtein + foodEntry.getProtein();
                    totFat = totFat + foodEntry.getFat();
                    mFoodEntries.add(foodEntry);
                    tvTotCals.setText(String.valueOf(totCal));
                    tvTotCarbs.setText(String.valueOf(totCarb));
                    tvTotProtein.setText(String.valueOf(totProtein));
                    tvTotFat.setText(String.valueOf(totFat));

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
    private void setTotsToZero(){
        tvTotCals.setText("0");
        tvTotCarbs.setText("0");
        tvTotProtein.setText("0");
        tvTotFat.setText("0");
    }

    private void checkTodaysLock(String selectedDate) {

        mDbRef.child(selectedDate).child("Lock").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String result;
                    result = dataSnapshot.getValue().toString();
                     if(result.equals("true")) {
                         Log.v(TAG, "result :"+result);
                         btnLockToday.setVisibility(View.INVISIBLE);
                        btnNewEntry.setVisibility(View.INVISIBLE);
                    }else{//set invisible if lock not true.
                         btnLockToday.setVisibility(View.VISIBLE);
                         btnNewEntry.setVisibility(View.VISIBLE);
                    }
                    }else{//set invisible if lock doesnt exist
                    btnLockToday.setVisibility(View.VISIBLE);
                    btnNewEntry.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}


