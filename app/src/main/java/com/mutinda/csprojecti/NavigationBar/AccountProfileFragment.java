package com.mutinda.csprojecti.NavigationBar;

import android.content.Intent;
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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.mutinda.csprojecti.ContentActivity;
import com.mutinda.csprojecti.MainActivity;
import com.mutinda.csprojecti.R;
import com.mutinda.csprojecti.User;

public class AccountProfileFragment extends Fragment {
    User accountUser;
    Button editUserProfile, logout;
    FirebaseAuth accountAuth;
    NavController navController;
    public AccountProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editUserProfile = view.findViewById(R.id.edit_user_profile_btn);
        logout = view.findViewById(R.id.logout_btn);
        accountAuth = FirebaseAuth.getInstance();
        navController = Navigation.findNavController(view);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accountAuth.signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

            }
        });
    }
}