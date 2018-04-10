package csp15cap.fitstart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
        mDbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid).child("FoodEntries");

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
                    case 1:
                        selectedType = 1;
                        break;
                    case 2:
                        selectedType = 2;
                        break;
                    case 3:
                        selectedType = 3;
                        break;
                    case 4:
                        selectedType = 4;
                        break;
                    default:
                        selectedType = 1;
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
                FoodEntry foodEntry = new FoodEntry();
                foodEntry.setDesc(etDesc.getText().toString());
                foodEntry.setCals(Long.parseLong(etCals.getText().toString()));
                foodEntry.setFat(Long.parseLong(etFat.getText().toString()));
                foodEntry.setCarbs(Long.parseLong(etCarbs.getText().toString()));
                foodEntry.setProtein(Long.parseLong(etProtein.getText().toString()));
                foodEntry.setSaveDate(selectedDate);
                foodEntry.setType(selectedType);
                saveEntry(foodEntry, mDbRef,selectedDate);
            }
        });


        // Inflate the layout for this fragment
        return view;
    }

    private void saveEntry(FoodEntry foodEntry, DatabaseReference mDbRef, String selectedDate) {
        String foodEntryID = mDbRef.push().getKey();
        foodEntry.setFoodEntryId(foodEntryID);
        mDbRef.child(selectedDate).child(foodEntryID).setValue(foodEntry);
    }
}