package com.goliveira.spendingcontrol.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.goliveira.spendingcontrol.R;
import com.goliveira.spendingcontrol.adapter.ExpenditureAdapter;
import com.goliveira.spendingcontrol.interfaces.IExpenditure;
import com.goliveira.spendingcontrol.model.Transaction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private final String currentUserUid;
    private final String TAG = "DashboardFragment";

    public DashboardFragment() {
        currentUserUid = FirebaseAuth.getInstance().getUid();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        RecyclerView transactionsList = root.findViewById(R.id.transactionsList);

        transactionsList.setLayoutManager(new LinearLayoutManager(root.getContext()));

        ArrayList<IExpenditure> expenditures  = new ArrayList<>();
        transactionsList.setHasFixedSize(true);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserUid).child("transactions");
        dbRef.orderByChild("dateUnix").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Transaction transaction =  snapshot.getValue(Transaction.class);
                expenditures.add(transaction);
                transactionsList.setAdapter(new ExpenditureAdapter(root.getContext(), expenditures.size(), expenditures));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Error fetching database");
            }
        });



        return root;
    }
}