package com.mutinda.csprojecti.Authentication.PasswordReset;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.mutinda.csprojecti.R;

public class PasswordResetThroughEmailFragment extends Fragment {
    String retrievedEmailAddress;
    EditText passwordResetEmailAddress;
    Button resetPasswordBtn;
    FirebaseAuth userAuth;

    public PasswordResetThroughEmailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_password_reset_through_email, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        passwordResetEmailAddress = view.findViewById(R.id.pwordResetEmailAddress);
        resetPasswordBtn = view.findViewById(R.id.password_reset_email_button);
        userAuth = FirebaseAuth.getInstance();


        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrievedEmailAddress = passwordResetEmailAddress.getText().toString().trim();

                userAuth.sendPasswordResetEmail(retrievedEmailAddress).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("RESET", "Email Reset Link Sent");
                        notifyUser("Email Reset Link Sent.");
                    }
                });
            }
        });

    }

    public void notifyUser(String notify){
        Toast.makeText(getContext(), notify, Toast.LENGTH_SHORT).show();
    }
}