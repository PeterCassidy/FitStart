package csp15cap.fitstart;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewExerciseEntryFragment extends Fragment {

    FirebaseAuth mAuth;
    DatabaseReference mDbRef;

    EditText etDesc, etCals;
    Spinner spType;
    Button btnCancel, btnSave;
    String selectedDate = "19990101";
    long selectedType = 1;


    public NewExerciseEntryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedIntanceState){
        super.onCreate(savedIntanceState);
        if (getArguments() != null){
            selectedDate = getArguments().getString("selectedDate");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_exercise_entry, container, false);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("New Exercise Entry");

        mAuth = FirebaseAuth.getInstance();
        String currentUid = mAuth.getCurrentUser().getUid();
        mDbRef = FirebaseDatabase.getInstance().getReference().child("ExerciseEntries").child(currentUid);

        etDesc = view.findViewById(R.id.et_new_exe_desc);
        etCals = view.findViewById(R.id.et_new_exe_cals);
        spType = view.findViewById(R.id.sp_new_exe_type);
        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        selectedType = 1;
                        break;
                    case 1:
                        selectedType = 2;
                        break;
                    default:
                        selectedType = 1;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {selectedType=1;

            }
        });

        btnCancel = view.findViewById(R.id.btn_new_exe_cancel);
        btnSave = view.findViewById(R.id.btn_new_exe_confirm);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow((null == getActivity().getCurrentFocus()) ? null : getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                FragmentManager fm = getFragmentManager();
                fm.popBackStack();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow((null == getActivity().getCurrentFocus()) ? null : getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                String desc,cals;
                desc = etDesc.getText().toString();
                cals = etCals.getText().toString();
                if(TextUtils.isEmpty(desc)||TextUtils.isEmpty(cals)) {
                    Toast.makeText(getActivity(), "Please complete all fields", Toast.LENGTH_SHORT).show();
                }else{
                    ExerciseEntry exerciseEntry = new ExerciseEntry();
                    exerciseEntry.setDesc(desc);
                    exerciseEntry.setCalsBurned(Long.parseLong(cals));
                    exerciseEntry.setSaveDate(selectedDate);
                    exerciseEntry.setType(selectedType);
                    saveEntry(exerciseEntry, mDbRef, selectedDate);
                }
            }
        });


        return view;
    }
    private void saveEntry(ExerciseEntry exerciseEntry, DatabaseReference mDbRef, String selectedDate) {
        String exerciseEntryID = mDbRef.push().getKey();
        exerciseEntry.setExerciseEntryId(exerciseEntryID);
        mDbRef.child(selectedDate).child("Entries").child(exerciseEntryID).setValue(exerciseEntry).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Entry added.", Toast.LENGTH_SHORT).show();
                    FragmentManager fm = getFragmentManager();
                    fm.popBackStack();
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(getActivity(), "Error: "+ error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
