package csp15cap.fitstart;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private EditText editTextUsername, editTextCurrentWeight;
    private Button btnSaveChanges;
    private FirebaseAuth mAuth;
    private DatabaseReference mDbRef;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        //set action bar title
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Profile Settings");

        editTextUsername = view.findViewById(R.id.edittext_settings_name);
        editTextCurrentWeight = view.findViewById(R.id.edittext_settings_current_weight);
        btnSaveChanges = view.findViewById(R.id.btn_settings_save);

        mAuth = FirebaseAuth.getInstance();
        //if no user logged in sent to login activity.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            ((MainActivity) getActivity()).sendUserToLoginActivity();
        } else {
            //temp implementation to test DB access
            String CurrentUUID = mAuth.getCurrentUser().getUid();
            mDbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(CurrentUUID);
            mDbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("user_name").exists()) {
                        String name = dataSnapshot.child("user_name").getValue().toString();
                        editTextUsername.setText(name);
                    }
                    if (dataSnapshot.child("weight").exists()) {
                        String weight = dataSnapshot.child("weight").getValue().toString();
                        editTextCurrentWeight.setText(weight);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, weight;
                name = editTextUsername.getText().toString();
                weight = editTextCurrentWeight.getText().toString();
                SaveChanges(name,weight);
            }
        });

        // Inflate the layout for this fragment
        return view;

    }

    private void SaveChanges(final String name, String weight) {

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getActivity(), "Please enter your name.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(weight)) {
            Toast.makeText(getActivity(), "Please enter your weight.", Toast.LENGTH_SHORT).show();
        } else {

            //get unique id and assign user name
            String currentUUID = mAuth.getCurrentUser().getUid();
            mDbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUUID);
            mDbRef.child("user_name").setValue(name);
            mDbRef.child("weight").setValue(weight)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Changes saved", Toast.LENGTH_SHORT).show();
                                FragmentManager fm = getFragmentManager();
                                fm.popBackStack("Home", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            } else {
                                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }
}

