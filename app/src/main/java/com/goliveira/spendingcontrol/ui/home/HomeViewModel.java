package com.goliveira.spendingcontrol.ui.home;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.goliveira.spendingcontrol.R;
import com.goliveira.spendingcontrol.model.Transaction;
import com.goliveira.spendingcontrol.model.TransactionType;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private final String TAG = "DashboardFragment";

    public void DisplayFirebaseData (View root, Fragment fragment) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid());
        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                double totalExpense = Transaction.calculateTotal(snapshot, TransactionType.EXPENSE);
                TextView expenseTodayValue =  root.findViewById(R.id.expenseTodayValue);
                expenseTodayValue.setText( fragment.getActivity().getString(R.string.currency) + totalExpense);

                double totalIncome = Transaction.calculateTotal(snapshot, TransactionType.INCOME);
                TextView todayIncomeValue =  root.findViewById(R.id.todayIncomeValue);
                todayIncomeValue.setText( fragment.getActivity().getString(R.string.currency) + + totalIncome);

                DrawChart(root, fragment, totalIncome, totalExpense);
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Error fetching database");
            }
        });
    }

    public ArrayList<Integer> ConfigurePieChartColors(Fragment fragment)
    {
        ArrayList<Integer> chartColors = new ArrayList<>();

        chartColors.add(ContextCompat.getColor(fragment.getContext(), R.color.income_color));
        chartColors.add(ContextCompat.getColor(fragment.getContext(), R.color.expense_color));

        return chartColors;
    }

    public void DrawChart(View root, Fragment fragment, double totalIncome, double totalExpense)
    {
        PieChart pieChartHome = root.findViewById(R.id.pieChartHome);
        List<PieEntry> chartData = new ArrayList<>();

        chartData.add(new PieEntry((int) totalIncome, "Income"));
        chartData.add(new PieEntry((int) totalExpense, "Expense"));

        PieDataSet pieDataSet = new PieDataSet(chartData, "Budget");

        pieDataSet.setColors(ConfigurePieChartColors(fragment));
        PieData pieData = new PieData(pieDataSet);

        pieChartHome.setData(pieData);
        pieChartHome.invalidate();
    }

}