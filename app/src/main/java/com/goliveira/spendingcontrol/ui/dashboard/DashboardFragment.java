package com.goliveira.spendingcontrol.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.goliveira.spendingcontrol.R;
import com.goliveira.spendingcontrol.adapter.ExpenditureAdapter;
import com.goliveira.spendingcontrol.interfaces.IExpenditure;
import com.goliveira.spendingcontrol.model.BudgetList;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        RecyclerView transactionsList = root.findViewById(R.id.transactionsList);

        transactionsList.setLayoutManager(new LinearLayoutManager(root.getContext()));

        ArrayList<IExpenditure> expenditures = BudgetList.getInstance().budget;

        transactionsList.setHasFixedSize(true);
        transactionsList.setAdapter(new ExpenditureAdapter(root.getContext(), 3, expenditures));

        return root;
    }
}