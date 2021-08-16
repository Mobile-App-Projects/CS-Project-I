package com.mutinda.csprojecti.Authentication.PasswordReset;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mutinda.csprojecti.R;

public class PasswordResetFragment extends Fragment {
    Button emailButton, phoneButton;
    NavController navController;
    public PasswordResetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_password_reset, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailButton = view.findViewById(R.id.email_button);
        phoneButton = view.findViewById(R.id.phone_button);
        navController = Navigation.findNavController(view);

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               navController.navigate(R.id.action_passwordResetFragment_to_passwordResetThroughEmailFragment);
            }
        });

        phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_passwordResetFragment_to_passwordResetThroughPhoneFragment);
            }
        });

    }
}