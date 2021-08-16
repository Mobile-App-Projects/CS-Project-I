package com.mutinda.csprojecti.Authentication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mutinda.csprojecti.R;

import java.util.HashMap;
import java.util.Map;

public class SignUpFragment extends Fragment {
    String userFirstName, userLastName, userPhone, userEmail, userPassword, userConfirmPassword;
    EditText firstName, lastName, phoneNumber,emailAddress, password, confirmPassword;
    TextView textToLogin;
    Button signup;
    CheckBox agreement;
    NavController navController;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firstName = view.findViewById(R.id.userFirstName);
        lastName = view.findViewById(R.id.userLastName);
        phoneNumber = view.findViewById(R.id.userPhoneNumber);
        emailAddress = view.findViewById(R.id.emailAddress);
        password = view.findViewById(R.id.userPassword);
        confirmPassword = view.findViewById(R.id.confirmPassword);
        textToLogin = view.findViewById(R.id.textToLogin);
        agreement = view.findViewById(R.id.terms_and_conditions);
        signup = view.findViewById(R.id.signup_btn);
        navController = Navigation.findNavController(view);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userFirstName = firstName.getText().toString().trim();
                userLastName = lastName.getText().toString().trim();
                userPhone = phoneNumber.getText().toString().trim();
                userEmail = emailAddress.getText().toString().trim();
                userPassword = password.getText().toString().trim();
                userConfirmPassword = confirmPassword.getText().toString().trim();

                if (userFirstName.isEmpty() || userLastName.isEmpty() || userPhone.isEmpty() || userEmail.isEmpty() || userPassword.isEmpty() || userConfirmPassword.isEmpty())
                {
                    notifyUser(getString(R.string.missing_credentials));
                }
                else if (userPassword.length() < 6)
                {
                    notifyUser(getString(R.string.password_is_less_than_6_characters));
                }
                else if (!userPassword.equals(userConfirmPassword))
                {
                    notifyUser(getString(R.string.password_does_not_match));
                }
                else if (!agreement.isChecked())
                {
                    notifyUser(getString(R.string.kindly_accept_terms_and_conditions));
                }else{
                    navController.navigate(R.id.action_signUpFragment_to_listingsFragment);

                    Map<String, Object> user = new HashMap<>();
                    user.put("First Name", userFirstName);
                    user.put("Last Name", userLastName);
                    user.put("Phone No", userPhone);
                    user.put("Email", userEmail);
                    user.put("Password", userPassword);
                    if(fStore.collection("Users").document(userEmail).equals(userEmail)){
                        notifyUser("Account is already in use");
                    }else{
                        fStore.collection("Users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("TAG","New User Created" );
                                notifyUser("Success");
                                mAuth.createUserWithEmailAndPassword(userEmail,userPassword);
                                FirebaseUser fUser = mAuth.getCurrentUser();
                                fUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("EmailVerification","Email verification sent successfully.");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("EmailVerificationError","Failed to send email verification.");
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                if(e instanceof FirebaseAuthUserCollisionException){
                                    notifyUser("Account already in use.");
                                }else{
                                    Log.e("TAG", "Failed to create new user");
                                    notifyUser("Error. Account not created");
                                }
                            }
                        });
                    }
                }
            }
        });

        textToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_signUpFragment_to_loginFragment);
            }
        });

    }

    public void notifyUser(String notify){
        Toast.makeText(getContext(), notify, Toast.LENGTH_SHORT).show();
    }
}

