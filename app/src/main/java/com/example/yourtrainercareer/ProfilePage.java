package com.example.yourtrainercareer;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfilePage extends Fragment {
    TextView fullNameTV, usernameTV, emailTV, addressTV, phoneTV, genderTV;
    Button updateBtn,addSpecialtiesBtn, addQualificationBtn;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> qualificationArray = new ArrayList<>();
    ListView specialtiesLV, qualificationLV;
    ImageView profilePicture;

    DatabaseReference databaseReference;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v =  inflater.inflate(R.layout.activity_profile_page,container,false);

        fullNameTV = v.findViewById(R.id.fullNameTV);
        usernameTV = v.findViewById(R.id.usernameTV);
        genderTV = v.findViewById(R.id.genderTV);
        emailTV = v.findViewById(R.id.emailTV);
        addressTV = v.findViewById(R.id.addressTV);
        phoneTV = v.findViewById(R.id.phoneTV);
        profilePicture = v.findViewById(R.id.profilePicture);
        updateBtn = v.findViewById(R.id.updateBtn);
        addSpecialtiesBtn = v.findViewById(R.id.addSpecialtiesBtn);
        addQualificationBtn = v.findViewById(R.id.addQualificationBtn);
        specialtiesLV = (ListView) v.findViewById(R.id.specialtiesLV);
        qualificationLV = (ListView) v.findViewById(R.id.qualificationLV);


        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        final String userId=firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Personal Trainer").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fullNameTV.setText(snapshot.child("trainerFullName").getValue().toString());
                usernameTV.setText(snapshot.child("trainerUsername").getValue().toString());
                genderTV.setText(snapshot.child("trainerGender").getValue().toString());
                phoneTV.setText(snapshot.child("trainerPhone").getValue().toString());
                emailTV.setText(snapshot.child("trainerEmail").getValue().toString());
                addressTV.setText(snapshot.child("trainerAddress").getValue().toString());

                String imageId = snapshot.child("imageId").getValue().toString();
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference ref = storageReference.child("Personal Trainer/"+imageId);
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri.toString()).into(profilePicture);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //List View SPECIALTIES
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,arrayList);
        specialtiesLV.setAdapter(arrayAdapter);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Personal Trainer").child(userId).child("Specialties");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    String trainerSpecialties = dataSnapshot.getValue().toString();
                    arrayList.add(trainerSpecialties);



                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //List View QUALIFICATION
        final ArrayAdapter<String> qualificationAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,qualificationArray);
        qualificationLV.setAdapter(qualificationAdapter);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Personal Trainer").child(userId).child("Qualification");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    String trainerQualification = dataSnapshot.getValue().toString();
                    qualificationArray.add(trainerQualification);



                qualificationAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                qualificationAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addSpecialtiesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddSpecialtiesPage fragment = new AddSpecialtiesPage();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, fragment, "TAG");
                transaction.commit();
            }
        });

        addQualificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddQualificationPage fragment = new AddQualificationPage();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, fragment, "TAG");
                transaction.commit();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfilePage fragment = new EditProfilePage();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, fragment, "TAG");
                transaction.commit();
            }
        });

        return v;
    }
}
