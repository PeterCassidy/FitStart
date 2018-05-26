package csp15cap.fitstart;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class DailyChallengeSelectedFragment extends Fragment {

    private TextView tvChallengeName, tvDifficulty, tvEx1Name, tvEx2Name, tvEx3Name,tvEx4Name, tvExperience;
    private String  ex1Url,ex2Url,ex3Url,ex4Url;
    private Long experience;
    private ImageView btnEx1, btnEx2, btnEx3, btnEx4;
    private Button btnComplete;

    private DatabaseReference mDbRef, mDbExperienceRef;
    private FirebaseAuth mAuth;
    public DailyChallengeSelectedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_daily_challenge_selected, container, false);

//set action bar title
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Today's Challenge");

        Calendar c = Calendar.getInstance();
        SimpleDateFormat DbDateFormat = new SimpleDateFormat("yyyyMMdd");
        String selectedDate = DbDateFormat.format(c.getTime());

        mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        mDbRef = FirebaseDatabase.getInstance().getReference().child("DailyChallenges").child(currentUserId);
        mDbExperienceRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

        tvChallengeName = view.findViewById(R.id.tv_challenge_name);
        tvDifficulty = view.findViewById(R.id.tv_difficulty);

        tvEx1Name = view.findViewById(R.id.tv_ex1);
        tvEx2Name = view.findViewById(R.id.tv_ex2);
        tvEx3Name = view.findViewById(R.id.tv_ex3);
        tvEx4Name = view.findViewById(R.id.tv_ex4);
        tvExperience = view.findViewById(R.id.tv_challenge_selected_exp);

        btnEx1 = view.findViewById(R.id.btn_ex1);
        btnEx2 = view.findViewById(R.id.btn_ex2);
        btnEx3 = view.findViewById(R.id.btn_ex3);
        btnEx4 = view.findViewById(R.id.btn_ex4);

        btnComplete = view.findViewById(R.id.btn_challenge_Complete);

        mDbRef.child(selectedDate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    DailyChallengeEntry challengeEntry = new DailyChallengeEntry();
                    challengeEntry = dataSnapshot.getValue(DailyChallengeEntry.class);

                    tvChallengeName.setText(challengeEntry.getChallengeName());
                    tvDifficulty.setText(challengeEntry.getChallengeDiff());

                    tvEx1Name.setText(challengeEntry.getEx1Name());
                    ex1Url = challengeEntry.getEx1Url();
                    tvEx2Name.setText(challengeEntry.getEx2Name());
                    ex2Url = challengeEntry.getEx2Url();
                    tvEx3Name.setText(challengeEntry.getEx3Name());
                    ex3Url = challengeEntry.getEx3Url();
                    tvEx4Name.setText(challengeEntry.getEx4Name());
                    ex4Url = challengeEntry.getEx4Url();
                    experience = challengeEntry.getExperience();
                    tvExperience.setText("Complete today's challenge for "+experience+ " experience points.");



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        btnEx1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ex1Url));
                startActivity(browserIntent);
            }
        });
        btnEx2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ex2Url));
                startActivity(browserIntent);
            }
        });
        btnEx3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ex3Url));
                startActivity(browserIntent);
            }
        });
        btnEx4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ex4Url));
                startActivity(browserIntent);
            }
        });

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDbRef.child(selectedDate).child("completed").setValue("true");
                //update xp
                mDbExperienceRef.child("experience").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long currentXP =0;
                        if( !dataSnapshot.exists()){
                            currentXP = 0;
                        }else{
                            currentXP = Long.valueOf(dataSnapshot.getValue().toString());
                        }
                        mDbExperienceRef.child("experience").setValue(currentXP+experience);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                tvExperience.setText("Challenge complete! You earned "+experience+" for today's challenge");

            }
        });

        checkTodaysLock(selectedDate);

        // Inflate the layout for this fragment
        return view;
    }
    private void checkTodaysLock(String selectedDate) {

        mDbRef.child(selectedDate).child("completed").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String result;
                    result = dataSnapshot.getValue().toString();
                    if(result.equals("true")) {
                        btnComplete.setVisibility(View.INVISIBLE);
                        tvExperience.setText("Challenge complete! You earned "+experience+" for today's challenge");
                        Toast.makeText(getContext(), "Challenge complete! You earned "+experience+" for today's challenge", Toast.LENGTH_SHORT).show();
                    }else{//set invisible if lock not true.
                        btnComplete.setVisibility(View.VISIBLE);

                    }
                }else{//set invisible if lock doesnt exist
                    btnComplete.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
