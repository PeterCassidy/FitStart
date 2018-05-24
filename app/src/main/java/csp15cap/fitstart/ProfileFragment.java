package csp15cap.fitstart;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private EditText editTextUsername, editTextCurrentWeight, editTextTargetCals;
    private ImageView imageViewProfilePic;
    private Button btnSaveChanges;
    private FirebaseAuth mAuth;
    private DatabaseReference mDbRef;
    private StorageReference userImageRef;
    final static int Gallery_Pick = 1;

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
        editTextTargetCals = view.findViewById(R.id.edittext_settings_calorie_daily);
        btnSaveChanges = view.findViewById(R.id.btn_settings_save);
        imageViewProfilePic = view.findViewById(R.id.imageview_settings_profile_pic);

        mAuth = FirebaseAuth.getInstance();
        userImageRef = FirebaseStorage.getInstance().getReference().child("profile_images");
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
                    if (dataSnapshot.child("target_cals").exists()) {
                        String cals = dataSnapshot.child("target_cals").getValue().toString();
                        editTextTargetCals.setText(cals);
                    }
                    if(dataSnapshot.child("profile_image").exists()){
                        String url = dataSnapshot.child("profile_image").getValue().toString();
                        Picasso.get().load(url).placeholder(R.drawable.common_google_signin_btn_icon_light).into(imageViewProfilePic);

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
                String name, weight, cals;
                name = editTextUsername.getText().toString();
                weight = editTextCurrentWeight.getText().toString();
                cals = editTextTargetCals.getText().toString();
                SaveChanges(name,weight,cals);
            }
        });

        imageViewProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, Gallery_Pick);

            }
        });
        // Inflate the layout for this fragment
        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Gallery_Pick && resultCode== Activity.RESULT_OK && data !=null){
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(getContext(),this);

        }

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if(resultCode==RESULT_OK){

                Uri resultUri = result.getUri();

                StorageReference filePath = userImageRef.child(mAuth.getCurrentUser().getUid());
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(), "Profile image saved.", Toast.LENGTH_SHORT).show();
                            final String downloadUrl = task.getResult().getDownloadUrl().toString();
                            mDbRef.child("profile_image").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getActivity(),"Profile image added to DB",Toast.LENGTH_SHORT).show();

                                    }else{
                                        String message = task.getException().getMessage();
                                        Toast.makeText(getActivity(),"error:"+message,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(getActivity(),"error",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    private void SaveChanges(final String name, String weight,String cals) {

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getActivity(), "Please enter your name.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(weight)) {
            Toast.makeText(getActivity(), "Please enter your weight.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(cals)) {
            Toast.makeText(getActivity(), "Please enter your daily target calories.", Toast.LENGTH_SHORT).show();
        } else {

            //get unique id and assign user name
            String currentUUID = mAuth.getCurrentUser().getUid();
            mDbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUUID);
            mDbRef.child("user_name").setValue(name);
            mDbRef.child("target_cals").setValue(cals);
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

