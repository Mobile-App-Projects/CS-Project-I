package com.mutinda.csprojecti.NavigationBar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mutinda.csprojecti.R;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class EditProfile extends Fragment {
    Button saveButton;
    EditText userNameFirst,userNameLast, userPhone, userEmail;
    FirebaseAuth accountAuth;
    FirebaseFirestore mStore;
    DatabaseReference mDatabaseUser;
    FirebaseStorage fStorage;
    StorageReference mStorageRef;
    String userId;
    DocumentReference docRef;
    ImageButton userProfile;
    private final static int GALLERY_REQ = 1;
    private Uri profileImageUri=  null;

    public EditProfile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        saveButton = view.findViewById(R.id.save_user_profile_btn);
        userNameFirst = view.findViewById(R.id.userName_first);
        userNameLast = view.findViewById(R.id.userName_last);
        userPhone = view.findViewById(R.id.userPhone_text);
        userEmail = view.findViewById(R.id.userEmail_text);
        userProfile = view.findViewById(R.id.user_profile_picture);
        accountAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();
        mStorageRef = fStorage.getReference().child("profile_images");
        userId = accountAuth.getCurrentUser().getUid();



        docRef = mStore.collection("Users").document(userId);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                userNameFirst.setText(value.getString("First Name"));
                userNameLast.setText(value.getString("Last Name"));
                userPhone.setText(value.getString("Phone No"));
                userEmail.setText(value.getString("Email"));
            }
        });

        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQ);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newFirstName = userNameFirst.getText().toString();
                String newLastName = userNameLast.getText().toString();
                String newPhone = userPhone.getText().toString();
                String newEmail = userEmail.getText().toString();


                if(TextUtils.isEmpty(newFirstName) || TextUtils.isEmpty(newLastName) || TextUtils.isEmpty(newPhone)  || TextUtils.isEmpty(newEmail) ) {
                    notifyUser(String.valueOf(R.string.missing_credentials));
                }else {
                    mStorageRef.child(profileImageUri.getLastPathSegment());
                    mStorageRef.putFile(profileImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if(taskSnapshot.getMetadata() != null) {
                                if(taskSnapshot.getMetadata().getReference() != null) {
                                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            final String profileImage = uri.toString();

                                           mDatabaseUser.push();
                                            mDatabaseUser.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    mDatabaseUser.child("profilePhoto").setValue(profileImage).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()) {
                                                                Log.d("ProfilePic", "Success");
                                                            }
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        }
                    });
                    docRef = mStore.collection("Users").document(userId);

                    Map<String, Object> user = new HashMap<>();
                    user.put("First Name", newFirstName);
                    user.put("Last Name", newLastName);
                    user.put("Phone No", newPhone);
                    user.put("Email", newEmail);

                    docRef.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("FirebaseUpdate","Update Successful");
                            notifyUser("Profile Updated!");
                            Fragment fragment = new AccountProfileFragment();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView3, fragment, "Edit Profile Fragment").addToBackStack(null).commit();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("FirebaseUpdate","Failed to update user data");
                            notifyUser("Error from database");
                        }
                    });
                }
            }
        });

    }

    public void notifyUser(String notify){
        Toast.makeText(getContext(), notify, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQ && resultCode == -1){
            profileImageUri = data.getData();
            userProfile.setImageURI(profileImageUri);

        }
    }

}