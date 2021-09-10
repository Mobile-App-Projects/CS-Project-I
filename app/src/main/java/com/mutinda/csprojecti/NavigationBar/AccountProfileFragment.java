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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.mutinda.csprojecti.MainActivity;
import com.mutinda.csprojecti.R;
import com.mutinda.csprojecti.User;
import com.squareup.picasso.Picasso;

public class AccountProfileFragment extends Fragment {
    User accountUser;
    Button editUserProfile, postButton;
    TextView username, phone, email;
    FirebaseAuth accountAuth;
    FirebaseFirestore mStore;
    String userId;
    DocumentReference docRef;
    ImageView userProfile;
    private final static int GALLERY_REQ = 1;
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
        username = view.findViewById(R.id.userName_text);
        phone = view.findViewById(R.id.userPhone_text);
        email = view.findViewById(R.id.userEmail_text);
        userProfile = view.findViewById(R.id.user_profile_picture);
        postButton = view.findViewById(R.id.postBtn);
        accountAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        userId = accountAuth.getCurrentUser().getUid();

        docRef = mStore.collection("Users").document(userId);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                username.setText((new StringBuilder().append(value.getString("First Name")).append(" ").append(value.getString("Last Name")).toString()));
                phone.setText(value.getString("Phone No"));
                email.setText(value.getString("Email"));
                Picasso.with(getContext()).load(value.getString("Profile Image Url")).into(userProfile);
            }
        });

        editUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Fragment fragment = new EditProfile();
               getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView3, fragment, "Edit Profile Fragment").addToBackStack(null).commit();
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment1 = new PostListingFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView3, fragment1, "Post Listing Fragment").addToBackStack(null).commit();
            }
        });

    }
}