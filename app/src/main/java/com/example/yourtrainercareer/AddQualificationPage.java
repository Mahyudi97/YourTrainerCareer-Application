package com.example.yourtrainercareer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddQualificationPage extends Fragment {
    ArrayList<String> arrayList = new ArrayList<>();
    EditText qualificationET;
    Button addBtn;
    long maxId=0;
    ListView qualificationLV;
    Qualification qualification;
    DatabaseReference databaseReference,databaseReference1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.activity_add_qualification_page,container,false);

        qualificationET = v.findViewById(R.id.qualificationET);
        addBtn = v.findViewById(R.id.addBtn);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,arrayList);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Personal Trainer").child(userId).child("Qualification");
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

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser firebaseUser1 = FirebaseAuth.getInstance().getCurrentUser();
                String userId1 = firebaseUser1.getUid();
                /*qualification = new Qualification();
                qualification.setQualificationName(qualificationET.getText().toString().trim());
                qualification.setTrainerId(userId1);*/
                databaseReference.child(String.valueOf(maxId)).setValue(qualificationET.getText().toString().trim());
                Toast.makeText(getActivity(), "Succesfully Added new Specialties", Toast.LENGTH_LONG).show();
            }
        });

        //List View
        qualificationLV = (ListView) v.findViewById(R.id.qualificationLV);
        qualificationLV.setAdapter(arrayAdapter);
        FirebaseUser firebaseUser2= FirebaseAuth.getInstance().getCurrentUser();
        final String userId2=firebaseUser2.getUid();
        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Personal Trainer").child(userId).child("Qualification");
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
