package csp15cap.fitstart;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegActivity extends AppCompatActivity {
    private EditText regUserName,regUserEmail, regUserPassword, regUserConfirmPassword;
    private Button regButton;

    private FirebaseAuth mAuth;
    private DatabaseReference mDbRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        mAuth = FirebaseAuth.getInstance();

        regUserName = findViewById(R.id.editTextRegName);
        regUserEmail = findViewById(R.id.editTextRegEmail);
        regUserPassword = findViewById(R.id.editTextRegPassword);
        regUserConfirmPassword = findViewById(R.id.editTextRegConfirm);
        regButton = findViewById(R.id.buttonReg);

        regButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                final String name = regUserName.getText().toString();
                String email = regUserEmail.getText().toString();
                String password= regUserPassword.getText().toString();
                String confirmPass = regUserConfirmPassword.getText().toString();

                RegisterAccount(name, email, password, confirmPass);
            }
        });


    }

    private void RegisterAccount(final String name, String email, String password, String confirmPass) {

        if (TextUtils.isEmpty(name)){
            Toast.makeText(RegActivity.this, "Please enter your name.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(email)){
            Toast.makeText(RegActivity.this, "Please enter your Email.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(RegActivity.this, "Please enter your password.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(confirmPass)){
            Toast.makeText(RegActivity.this, "Please Confirm your password.", Toast.LENGTH_SHORT).show();
        }
        else if (!password.equals(confirmPass)){
            Toast.makeText(RegActivity.this, "Password and confirm password fields must match.", Toast.LENGTH_SHORT).show();
        }
        else{
            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegActivity.this, "Sign up Successful", Toast.LENGTH_SHORT).show();
                                //get unique id and assign user name
                                String currentUUID = mAuth.getCurrentUser().getUid();
                                mDbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUUID);
                                mDbRef.child("user_name").setValue(name)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    SendUserToMainActivity();
                                                }
                                            }
                                        });
                            }
                            else{
                                Toast.makeText(RegActivity.this, "Error: " +task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    }
    //sends users to main activity
    private void SendUserToMainActivity() {
        Intent intent = new Intent(RegActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
