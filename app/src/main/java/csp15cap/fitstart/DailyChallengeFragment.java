package csp15cap.fitstart;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class DailyChallengeFragment extends Fragment {

    private Spinner spChallenge, spDiff;
    private TextView tvEx1Name, tvEx2Name, tvEx3Name,tvEx4Name,tvExp;
    private String  ex1Url,ex2Url,ex3Url,ex4Url;
    private Long experience;
    private ImageView btnEx1, btnEx2, btnEx3, btnEx4;
    private Button btnConfirm;

    private int selectedChallengeIndex=0;
    private int selectedDifficultyIndex=0;

    private DatabaseReference mDbChallengeRef;
    private DatabaseReference mDbRef;

    private FirebaseAuth mAuth;
    final ArrayList<DailyChallenge> challengeList = new ArrayList<>();
    final List<String> nameList = new ArrayList<>();
    public DailyChallengeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_daily_challenge, container, false);

        //set action bar title
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Select a Daily Challenge");

        Calendar c = Calendar.getInstance();
        SimpleDateFormat DbDateFormat = new SimpleDateFormat("yyyyMMdd");
        String selectedDate = DbDateFormat.format(c.getTime());

        mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        mDbRef = FirebaseDatabase.getInstance().getReference().child("DailyChallenges").child(currentUserId);
        mDbChallengeRef = FirebaseDatabase.getInstance().getReference().child("Challenges");



        spChallenge = view.findViewById(R.id.sp_challenge_name);
        spDiff = view.findViewById(R.id.sp_difficulty);

        tvEx1Name = view.findViewById(R.id.tv_selected_ex1);
        tvEx2Name = view.findViewById(R.id.tv_selected_ex2);
        tvEx3Name = view.findViewById(R.id.tv_selected_ex3);
        tvEx4Name = view.findViewById(R.id.tv_selected_ex4);
        tvExp = view.findViewById(R.id.tv_exp);

        btnEx1 = view.findViewById(R.id.btn_selected_ex1);
        btnEx2 = view.findViewById(R.id.btn_selected_ex2);
        btnEx3 = view.findViewById(R.id.btn_selected_ex3);
        btnEx4 = view.findViewById(R.id.btn_selected_ex4);

        btnConfirm = view.findViewById(R.id.btn_challenge_confirm);


        mDbChallengeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot challengeSnapshot: dataSnapshot.getChildren()) {
                    DailyChallenge challenge = challengeSnapshot.getValue(DailyChallenge.class);
                    challengeList.add(challenge);
                    nameList.add(challenge.getChallengeName());
                }

                ArrayAdapter<String> challengeNameAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, nameList);
                challengeNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spChallenge.setAdapter(challengeNameAdapter);
                spChallenge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        switch(i){
                            case 0:
                                selectedChallengeIndex = 0;
                                updateTextViews(selectedChallengeIndex,selectedDifficultyIndex);

                                break;
                            case 1:selectedChallengeIndex = 1;
                                updateTextViews(selectedChallengeIndex,selectedDifficultyIndex);


                                break;
                            case 2:selectedChallengeIndex = 2;
                                updateTextViews(selectedChallengeIndex,selectedDifficultyIndex);


                                break;
                            case 3:selectedChallengeIndex = 3;
                                updateTextViews(selectedChallengeIndex,selectedDifficultyIndex);

                                break;
                            default:selectedChallengeIndex = 0;
                                updateTextViews(selectedChallengeIndex,selectedDifficultyIndex);

                                break;
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });

                spDiff.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        switch(i){
                            case 0:
                                selectedDifficultyIndex = 0;
                                updateTextViews(selectedChallengeIndex,selectedDifficultyIndex);

                                break;
                            case 1:selectedDifficultyIndex = 1;
                                updateTextViews(selectedChallengeIndex,selectedDifficultyIndex);


                                break;
                            case 2:selectedDifficultyIndex = 2;
                                updateTextViews(selectedChallengeIndex,selectedDifficultyIndex);


                                break;
                            default:selectedDifficultyIndex = 0;
                                updateTextViews(selectedChallengeIndex,selectedDifficultyIndex);

                                break;
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Confirmation")
                        .setMessage("You cannot change your daily challenge once confirmed.")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                DailyChallengeEntry dce = new DailyChallengeEntry();
                                dce.setChallengeName(challengeList.get(selectedChallengeIndex).getChallengeName());
                                dce.setChallengeDiff(String.valueOf(selectedDifficultyIndex+1));
                                dce.setSaveDate(selectedDate);
                                dce.setEx1Name(tvEx1Name.getText().toString());
                                dce.setEx2Name(tvEx2Name.getText().toString());
                                dce.setEx3Name(tvEx3Name.getText().toString());
                                dce.setEx4Name(tvEx4Name.getText().toString());
                                dce.setEx1Url(ex1Url);
                                dce.setEx2Url(ex2Url);
                                dce.setEx3Url(ex3Url);
                                dce.setEx4Url(ex4Url);
                                dce.setExperience(experience);
                                dce.setCompleted("false");
                                saveEntry(dce,mDbRef,selectedDate);
                            }})
                        .setNegativeButton("Cancel", null).show();
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

                // Inflate the layout for this fragment
        return view;
    }

    private void updateTextViews(int i, int j) {
        if(j==0) {
            tvEx1Name.setText(challengeList.get(i).getDiff1ex1name());
            ex1Url = challengeList.get(i).getDiff1ex1url();
            tvEx2Name.setText(challengeList.get(i).getDiff1ex2name());
            ex2Url = challengeList.get(i).getDiff1ex2url();
            tvEx3Name.setText(challengeList.get(i).getDiff1ex3name());
            ex3Url = challengeList.get(i).getDiff1ex3url();
            tvEx4Name.setText(challengeList.get(i).getDiff1ex4name());
            ex4Url = challengeList.get(i).getDiff1ex4url();
            experience =challengeList.get(i).getDiff1experience();
            tvExp.setText("This challenge is worth "+experience +" experience.");

        }
        if(j==1) {
            tvEx1Name.setText(challengeList.get(i).getDiff2ex1name());
            ex1Url = challengeList.get(i).getDiff2ex1url();
            tvEx2Name.setText(challengeList.get(i).getDiff2ex2name());
            ex2Url = challengeList.get(i).getDiff2ex2url();
            tvEx3Name.setText(challengeList.get(i).getDiff2ex3name());
            ex3Url = challengeList.get(i).getDiff2ex3url();
            tvEx4Name.setText(challengeList.get(i).getDiff2ex4name());
            ex4Url = challengeList.get(i).getDiff2ex4url();
            experience =challengeList.get(i).getDiff2experience();
            tvExp.setText("This challenge is worth "+experience +" experience.");

        }
        if(j==2) {
            tvEx1Name.setText(challengeList.get(i).getDiff3ex1name());
            ex1Url = challengeList.get(i).getDiff3ex1url();
            tvEx2Name.setText(challengeList.get(i).getDiff3ex2name());
            ex2Url = challengeList.get(i).getDiff3ex2url();
            tvEx3Name.setText(challengeList.get(i).getDiff3ex3name());
            ex3Url = challengeList.get(i).getDiff3ex3url();
            tvEx4Name.setText(challengeList.get(i).getDiff3ex4name());
            ex4Url = challengeList.get(i).getDiff3ex4url();
            experience =challengeList.get(i).getDiff3experience();
            tvExp.setText("This challenge is worth "+experience +" experience.");

        }

    }
    private void saveEntry(DailyChallengeEntry challengeEntry, DatabaseReference mDbRef, String selectedDate) {
        String challengeEntryID = mDbRef.push().getKey();
        challengeEntry.setChallengeEntryId(challengeEntryID);
        mDbRef.child(selectedDate).setValue(challengeEntry).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Challenge Accepted!", Toast.LENGTH_SHORT).show();
                    FragmentManager fm = getFragmentManager();
                    fm.popBackStack();
                    fm.beginTransaction();
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(getActivity(), "Error: "+ error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
