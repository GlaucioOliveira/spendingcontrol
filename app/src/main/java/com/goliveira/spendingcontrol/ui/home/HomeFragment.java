package com.goliveira.spendingcontrol.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.goliveira.spendingcontrol.model.Income;
import com.goliveira.spendingcontrol.adapter.IncomeAdapter;
import com.goliveira.spendingcontrol.R;
import com.goliveira.spendingcontrol.ui.income.IncomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import kotlin.reflect.KVariance;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private Button btnAddIncome;
    private Button btnAddOutcome;
    private CalendarView calendario;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        btnAddIncome = root.findViewById(R.id.btnAddIncome);

        btnAddOutcome = root.findViewById(R.id.btnAddOutcome);

        btnAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view)
                        .navigate(R.id.action_navigation_home_to_navigation_income);
            }
        });

        btnAddOutcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view)
                        .navigate(R.id.action_navigation_home_to_navigation_outcome);
            }
        });

        FloatingActionButton fab =  getActivity().findViewById(R.id.floatingActionButton);

        if(fab != null)
        {
            fab.hide();
        }

        return root;
    }
}