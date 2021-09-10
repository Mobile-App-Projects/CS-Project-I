package com.mutinda.csprojecti.Authentication;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mutinda.csprojecti.ContentActivity;
import com.mutinda.csprojecti.R;


public class LoginFragment extends Fragment {
    TextView textToSignUp, forgotPassword;
    EditText emailAddress, password;
    Button login;
    NavController navController;
    String userEmail, userPassword;
    FirebaseApp firebaseApp;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailAddress = view.findViewById(R.id.emailAddress);
        password = view.findViewById(R.id.userPassword);
        login = view.findViewById(R.id.login_btn);
        textToSignUp = view.findViewById(R.id.textToSignUp);
        forgotPassword = view.findViewById(R.id.forgotPassword);
        navController = Navigation.findNavController(view);
        firebaseApp = FirebaseApp.initializeApp(requireContext());
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        textToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_loginFragment_to_signUpFragment);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userEmail = emailAddress.getText().toString().trim();
                userPassword = password.getText().toString().trim();

                if(!isConnected()){
                    Toast.makeText(getContext(), "Internet Unavailable", Toast.LENGTH_LONG).show();
                }
                else if (userEmail.isEmpty() || userPassword.isEmpty()) {
                    notifyUser(getString(R.string.missing_credentials));
                } else {
                    mAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Log.d("TAG", "Successful Login");
                            notifyUser("Successful Login");
                            Intent intent = new Intent(getActivity(), ContentActivity.class);
                            startActivity(intent);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (e instanceof FirebaseAuthInvalidCredentialsException || e instanceof FirebaseAuthInvalidUserException) {
                                notifyUser("Invalid email or password");
                            }
                        }
                    });
                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_loginFragment_to_passwordResetFragment);
            }
        });

    }

    public void notifyUser(String notify){
        Toast.makeText(getContext(), notify, Toast.LENGTH_SHORT).show();
    }

    public boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netConn = connectivityManager.getActiveNetworkInfo();

        return (netConn != null && netConn.isConnected());

    }
}