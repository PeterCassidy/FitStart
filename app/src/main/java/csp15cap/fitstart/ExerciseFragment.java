package csp15cap.fitstart;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

/**
 * Created by Peter Cassidy on 15/03/2018.
 */

public class ExerciseFragment extends Fragment {

    private static final String TAG = "ExerciseFragment";

    private FirebaseAuth mAuth;
    private DatabaseReference mDbRef, mDbTargetRef;

    private TextView tvDate, tvTotCals;
    private Button btnPrevDay, btnNextDay, btnNewEntry, btnLockToday;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private RecyclerView mRecycleView;
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
        View view = inflater.inflate(R.layout.fragment_exercise_layout, container, false);

        //set action bar title
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Exercise");

        mAuth = FirebaseAuth.getInstance();
        String currentUid = mAuth.getCurrentUser().getUid();
        mDbRef = FirebaseDatabase.getInstance().getReference().child("ExerciseEntries").child(currentUid);
        mDbTargetRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid);
        final ArrayList<ExerciseEntry> mExerciseEntries = new ArrayList<>();

        tvDate = view.findViewById(R.id.exe_date);
        btnNextDay = view.findViewById(R.id.btn_exe_next);
        btnPrevDay = view.findViewById(R.id.btn_exe_prev);
        btnNewEntry = view.findViewById(R.id.btn_new_exe_entry);
        btnLockToday = view.findViewById(R.id.btn_confirm_exe_today);

        tvTotCals = view.findViewById(R.id.tv_today_cals_burned);

        mRecycleView = view.findViewById(R.id.rv_exe);
        mRecycleView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecycleView.setLayoutManager(mLayoutManager);
        mAdapter = new ExerciseEntryListAdapter(mExerciseEntries);
        mRecycleView.setAdapter(mAdapter);

        selectedDate = DbDateFormat.format(c.getTime());

        tvDate.setText(displayDateFormat.format(c.getTime()));
        tvDate.setOnClickListener(new View.OnClickListener(){
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
                getExerciseEntries(mExerciseEntries, selectedDate, mDbRef);
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
                getExerciseEntries(mExerciseEntries, selectedDate, mDbRef);
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
                getExerciseEntries(mExerciseEntries, selectedDate, mDbRef);
            }
        });

        btnNewEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewExerciseEntryFragment entryFrag = new NewExerciseEntryFragment();
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


                new AlertDialog.Builder(getActivity())
                        .setTitle("Confirmation")
                        .setMessage("Are you sure you wish to confirm your exercise entry for today? No changes can be made after this.")
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
                                        long experience = 0;
                                        if (totCals== 0) {
                                            experience = 0;
                                        }else{
                                            experience = totCals/10;
                                        }


                                        mDbTargetRef.child("experience").setValue(currentXP+experience);
                                        Toast.makeText(getActivity(), "Exercise entries locked, you earned " + experience + " experience today.", Toast.LENGTH_LONG).show();

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }})
                        .setNegativeButton("Cancel", null).show();
            }
        });


        checkTodaysLock(selectedDate);

        getExerciseEntries(mExerciseEntries, selectedDate, mDbRef);

        return view;


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

    private void setTotsToZero(){
        tvTotCals.setText("0");
    }

    private void getExerciseEntries(ArrayList<ExerciseEntry> mExerciseEntries, String selectedDate, DatabaseReference mDbRef) {
        DatabaseReference mDateRef = mDbRef.child(selectedDate).child("Entries");
        mDateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                long totCal=0;

                mExerciseEntries.clear();
                for(DataSnapshot exerciseEntrySnapShot: dataSnapshot.getChildren()){
                    ExerciseEntry exerciseEntry = exerciseEntrySnapShot.getValue(ExerciseEntry.class);
                    totCal = totCal + exerciseEntry.getCalsBurned();
                    mExerciseEntries.add(exerciseEntry);
                    tvTotCals.setText(String.valueOf(totCal));
                }
                Collections.sort(mExerciseEntries);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v(TAG, databaseError.getMessage());
            }
        });
    }
}
