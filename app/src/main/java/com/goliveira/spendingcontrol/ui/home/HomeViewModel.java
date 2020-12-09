package com.goliveira.spendingcontrol.ui.home;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.goliveira.spendingcontrol.R;
import com.goliveira.spendingcontrol.model.Expense;
import com.goliveira.spendingcontrol.model.Income;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeViewModel extends ViewModel {

    private final String currentUserUid = FirebaseAuth.getInstance().getUid();
    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void DisplayFirebaseData (View root) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserUid);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                calculateTodayExpense(dataSnapshot, root);
                calculateTodayIncome(dataSnapshot, root);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("HomeFragment", "Error fetching database");
            }
        });
    }

    private void calculateTodayExpense (DataSnapshot dataSnapshot, View root) {
        double totalExpense = 0;
        for (DataSnapshot ds : dataSnapshot.child("expenses").getChildren()) {
            Expense expense = ds.getValue(Expense.class);
            totalExpense = totalExpense + expense.getAmount();
        }
        TextView expenseTodayValue =  root.findViewById(R.id.expenseTodayValue);
        expenseTodayValue.setText( "$ " + (int) totalExpense);
    }

    private void calculateTodayIncome (DataSnapshot dataSnapshot, View root) {
        double totalIncome = 0;
        for (DataSnapshot ds : dataSnapshot.child("incomes").getChildren()) {
            Income income = ds.getValue(Income.class);
            totalIncome = totalIncome + income.getAmount();
        }
        TextView todayIncomeValue =  root.findViewById(R.id.todayIncomeValue);
        todayIncomeValue.setText( "$ " + (int) totalIncome);
    }

}