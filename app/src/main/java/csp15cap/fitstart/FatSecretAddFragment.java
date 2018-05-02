package csp15cap.fitstart;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.fatsecret.platform.model.CompactFood;
import com.fatsecret.platform.model.Food;
import com.fatsecret.platform.services.Response;
import com.fatsecret.platform.services.android.Request;
import com.fatsecret.platform.services.android.ResponseListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FatSecretAddFragment extends Fragment {

    FirebaseAuth mAuth;
    DatabaseReference mDbRef;


    public FatSecretAddFragment() {
        // Required empty public constructor
    }
    private String mEntryName;
    private String mCompactFoodDesc;
    private String selectedDate;
    private int selectedType;
    private Food selectedFood = new Food();

    TextView tventryID;
    TextView tvDate;

    EditText etServingSize, etDesc;
    Button btnUpdate, btnConfirm, btnCancel;
    TextView tvCals, tvCarbs, tvFat, tvProtein;
    Spinner spType;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mEntryName = getArguments().getString("entryName");
            selectedDate = getArguments().getString("selectedDate");
            mCompactFoodDesc = getArguments().getString("compactFoodDesc");
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fat_secret_add, container, false);

        tventryID = view.findViewById(R.id.tv_add_fs_entryid);
        tvDate = view.findViewById(R.id.tv_add_fs_date);

        mAuth = FirebaseAuth.getInstance();
        String currentUid = mAuth.getCurrentUser().getUid();
        mDbRef = FirebaseDatabase.getInstance().getReference().child("FoodEntries").child(currentUid);

        etDesc = view.findViewById(R.id.et_add_fs_desc);
        tvCals = view.findViewById(R.id.tv_add_fs_cals);
        tvCarbs = view.findViewById(R.id.tv_add_fs_carbs);
        tvProtein = view.findViewById(R.id.tv_add_fs_protein);
        tvFat = view.findViewById(R.id.tv_add_fs_fat);

        etServingSize = view.findViewById(R.id.et_add_fs_servingsize);

        btnUpdate = view.findViewById(R.id.btn_add_fs_update_serving);
        btnConfirm = view.findViewById(R.id.btn_add_fs_confirm);

        spType = view.findViewById(R.id.sp_add_fs_type);
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
                selectedType = 100;
            }

        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String serving = etServingSize.getText().toString();

                if(TextUtils.isEmpty(serving)) {
                    Toast.makeText(getActivity(), "Please Enter a serving size", Toast.LENGTH_SHORT).show();
                }else{
                    int servingInt = Integer.valueOf(serving);
                    if (servingInt <=0){
                        servingInt = 1;
                    }
                    ArrayList<Long> parseArray = extractNumbersFromDesc(mCompactFoodDesc, servingInt);
                    etDesc.setText(mEntryName);
                    tvCals.setText(parseArray.get(0).toString());
                    tvCarbs.setText(parseArray.get(1).toString());
                    tvProtein.setText(parseArray.get(2).toString());
                    tvFat.setText(parseArray.get(3).toString());
                }

            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow((null == getActivity().getCurrentFocus()) ? null : getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                String desc,cals,carbs,protein,fat;
                desc = etDesc.getText().toString();
                cals = tvCals.getText().toString();
                carbs = tvCarbs.getText().toString();
                protein = tvProtein.getText().toString();
                fat = tvFat.getText().toString();

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

        tventryID.setText(mEntryName);
        tvDate.setText(selectedDate);



        System.out.println(mCompactFoodDesc);
        ArrayList<Long> parseArray = extractNumbersFromDesc(mCompactFoodDesc, 100);
        etDesc.setText(mEntryName);
        tvCals.setText(parseArray.get(0).toString());
        tvCarbs.setText(parseArray.get(1).toString());
        tvProtein.setText(parseArray.get(2).toString());
        tvFat.setText(parseArray.get(3).toString());

        // Inflate the layout for this fragment
        return view;
    }
    //input format example :- "Per 1329g - Calories: 4504kcal | Fat: 144.68g | Carbs: 815.49g | Protein: 52.35g"
    //outputs array of 4 Longs normalised to int serving, rounded and in the order of cals, carbs, protein, fat.
    public ArrayList<Long> extractNumbersFromDesc(String string, int serving) {
        Pattern p = Pattern.compile("(-?[0-9]+(?:[,.][0-9]+)?)");
        Matcher m = p.matcher(string);
        ArrayList<Double> doubleArray = new ArrayList<>();
        while (m.find()) {
            doubleArray.add(Double.parseDouble(m.group()));
        }
        ArrayList<Long> longArray= new ArrayList<>();
        //cals index 1
        longArray.add(Math.round(((doubleArray.get(1)/doubleArray.get(0))*serving)));
        //carbs index 3
        longArray.add(Math.round(((doubleArray.get(3)/doubleArray.get(0))*serving)));
        //protein index 4
        longArray.add(Math.round(((doubleArray.get(4)/doubleArray.get(0))*serving)));
        //fat index 2
        longArray.add(Math.round(((doubleArray.get(2)/doubleArray.get(0))*serving)));

        return longArray;

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
                    fm.popBackStack("diary",FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(getActivity(), "Error: "+ error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

