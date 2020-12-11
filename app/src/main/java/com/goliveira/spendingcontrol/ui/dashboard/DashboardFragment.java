package com.goliveira.spendingcontrol.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.goliveira.spendingcontrol.R;
import com.goliveira.spendingcontrol.adapter.ExpenditureAdapter;
import com.goliveira.spendingcontrol.interfaces.IExpenditure;
import com.goliveira.spendingcontrol.model.BudgetList;
import com.goliveira.spendingcontrol.model.Transaction;
import com.goliveira.spendingcontrol.model.TransactionType;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserUid);
        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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

        ArrayList<IExpenditure> expenditures = BudgetList.getInstance().budget;

        transactionsList.setHasFixedSize(true);
        transactionsList.setAdapter(new ExpenditureAdapter(root.getContext(), 3, expenditures));

        return root;
    }
}