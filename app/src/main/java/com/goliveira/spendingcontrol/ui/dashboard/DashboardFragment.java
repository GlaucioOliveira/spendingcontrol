package com.goliveira.spendingcontrol.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.goliveira.spendingcontrol.R;
import com.goliveira.spendingcontrol.adapter.ExpenditureAdapter;
import com.goliveira.spendingcontrol.interfaces.IExpenditure;
import com.goliveira.spendingcontrol.model.BudgetList;
import com.goliveira.spendingcontrol.model.Income;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        RecyclerView listaRegistros = root.findViewById(R.id.listaRegistros);
        listaRegistros.setLayoutManager(new LinearLayoutManager(root.getContext()));

        ArrayList<IExpenditure> dados = BudgetList.getInstance().budget;

        listaRegistros.setHasFixedSize(true);
        listaRegistros.setAdapter(new ExpenditureAdapter(root.getContext(), 3, dados));

        return root;
    }
}