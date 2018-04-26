package csp15cap.fitstart;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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


public class NewFoodEntryFragment extends Fragment {

    FirebaseAuth mAuth;
    DatabaseReference mDbRef;


    TextView tvTitle;
    EditText etDesc, etCals, etCarbs, etProtein, etFat;
    Spinner spType;
    Button btnCancel, btnSave;
    String selectedDate = "01011999";
    long selectedType = 1;


    public NewFoodEntryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedDate = getArguments().getString("selectedDate");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_food_entry, container, false);
        //set action bar title
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("New Food Entry");

        mAuth = FirebaseAuth.getInstance();
        String currentUid = mAuth.getCurrentUser().getUid();
        mDbRef = FirebaseDatabase.getInstance().getReference().child("FoodEntries").child(currentUid);

        tvTitle = view.findViewById(R.id.tv_new_food_test);
        etDesc = view.findViewById(R.id.et_new_food_desc);
        etCals = view.findViewById(R.id.et_new_food_cals);
        etCarbs = view.findViewById(R.id.et_new_food_carbs);
        etProtein = view.findViewById(R.id.et_new_food_protein);
        etFat = view.findViewById(R.id.et_new_food_fat);

        spType = view.findViewById(R.id.sp_new_food_type);
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
                    case 2:
                        selectedType = 3;
                        break;
                    case 3:
                        selectedType = 4;
                        break;
                    default:
                        selectedType = 4;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                    selectedType = 1;
                }

        });

        btnSave = view.findViewById(R.id.btn_food_save);

        tvTitle.setText("Entry new food entry for: " + selectedDate);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow((null == getActivity().getCurrentFocus()) ? null : getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                String desc,cals,carbs,protein,fat;
                desc = etDesc.getText().toString();
                cals = etCals.getText().toString();
                carbs = etCarbs.getText().toString();
                protein = etProtein.getText().toString();
                fat = etFat.getText().toString();

                if(TextUtils.isEmpty(desc)||TextUtils.isEmpty(cals)||TextUtils.isEmpty(carbs)||TextUtils.isEmpty(protein)||TextUtils.isEmpty(fat)) {
                    Toast.makeText(getActivity(), "Please complete all fields", Toast.LENGTH_SHORT).show();
                }else{
                FoodEntry foodEntry = new FoodEntry();
                foodEntry.setDesc(desc);
                foodEntry.setCals(Long.parseLong(cals));
                foodEntry.setFat(Long.parseLong(fat));
                foodEntry.setCarbs(Long.parseLong(carbs));
                foodEntry.setProtein(Long.parseLong(protein));
                foodEntry.setSaveDate(selectedDate);
                foodEntry.setType(selectedType);
                saveEntry(foodEntry, mDbRef, selectedDate);
                }
            }
        });


        // Inflate the layout for this fragment
        return view;
    }

    private void saveEntry(FoodEntry foodEntry, DatabaseReference mDbRef, String selectedDate) {
        String foodEntryID = mDbRef.push().getKey();
        foodEntry.setFoodEntryId(foodEntryID);
        mDbRef.child(selectedDate).child("Entries").child(foodEntryID).setValue(foodEntry).addOnCompleteListener(new OnCompleteListener<Void>() {
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