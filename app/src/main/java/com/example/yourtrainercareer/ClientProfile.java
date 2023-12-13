package com.example.yourtrainercareer;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ClientProfile extends Fragment {
    TextView fullNameTV, usernameTV, emailTV, addressTV, phoneTV, genderTV;
    Button approveBtn;
    ImageView profilePicture;
    DatabaseReference databaseReference;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_client_profile,container,false);
        Bundle bundle = this.getArguments();
        final String clienId = bundle.getString("clienId","");

        fullNameTV = v.findViewById(R.id.fullNameTV);
        usernameTV = v.findViewById(R.id.usernameTV);
        genderTV = v.findViewById(R.id.genderTV);
        emailTV = v.findViewById(R.id.emailTV);
        addressTV = v.findViewById(R.id.addressTV);
        phoneTV = v.findViewById(R.id.phoneTV);
        profilePicture = v.findViewById(R.id.profilePicture);
        approveBtn = v.findViewById(R.id.approveBtn);

        approveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Client").child(clienId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapchat : dataSnapshot.getChildren()){
                        fullNameTV.setText(snapchat.child("clientFullName").getValue().toString());
                        usernameTV.setText(snapchat.child("clientUsername").getValue().toString());
                        genderTV.setText(snapchat.child("clientGender").getValue().toString());
                        phoneTV.setText(snapchat.child("clientPhone").getValue().toString());
                        emailTV.setText(snapchat.child("clientEmail").getValue().toString());
                        addressTV.setText(snapchat.child("clientAddress").getValue().toString());

                        String imageId = snapchat.child("imageId").getValue().toString();
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                        StorageReference ref = storageReference.child("Client/" + imageId);
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.get().load(uri.toString()).into(profilePicture);
                            }
                        });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return v;
    }
}
