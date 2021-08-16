package com.mutinda.csprojecti.Authentication.PasswordReset;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mutinda.csprojecti.R;


public class PasswordResetThroughPhoneFragment extends Fragment {

    public PasswordResetThroughPhoneFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_password_reset_through_phone, container, false);
    }
}