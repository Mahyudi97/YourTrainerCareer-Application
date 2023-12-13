package com.example.yourtrainercareer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddSpecialtiesPage extends Fragment {
    ArrayList<String> arrayList = new ArrayList<>();
    Spinner spinner;
    Button addBtn;
    long maxId=0;
    ListView specialtiesLV;
    Specialties specialties;
    DatabaseReference databaseReference,databaseReference1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.activity_add_specialties_page,container,false);

        spinner = v.findViewById(R.id.spinner);
        addBtn = v.findViewById(R.id.addBtn);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,arrayList);

        FirebaseUser firebaseUser1= FirebaseAuth.getInstance().getCurrentUser();
        String userId1=firebaseUser1.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Personal Trainer").child(userId1).child("Specialties");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    maxId= snapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String specialtiesName = parent.getSelectedItem().toString();
                /**/
                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
                        String userId=firebaseUser.getUid();

                        /*specialties = new Specialties();
                        specialties.setSpecialtiesName(specialtiesName);
                        specialties.setTrainerId(userId);
                        databaseReference.child(String.valueOf(maxId)).setValue(specialties);*/
                        databaseReference.child(String.valueOf(maxId)).setValue(specialtiesName);
                        Toast.makeText(getActivity(),"Succesfully Added new Specialties",Toast.LENGTH_LONG).show();


                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        //List View
        specialtiesLV = (ListView) v.findViewById(R.id.specialtiesLV);
        specialtiesLV.setAdapter(arrayAdapter);
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        final String userId=firebaseUser.getUid();
        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Personal Trainer").child(userId).child("Specialties");
        databaseReference1.addChildEventListener(new ChildEventListener() {
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

        return v;
    }
}
