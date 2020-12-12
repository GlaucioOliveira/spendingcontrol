package com.goliveira.spendingcontrol.ui.home;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.goliveira.spendingcontrol.R;
import com.goliveira.spendingcontrol.ui.dialogs.MonthYearPickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private Button btnAddIncome;
    private Button btnAddExpense;
    private EditText txtMonthDate;

    public void LoadFragmentViews(View root)
    {
        btnAddIncome = root.findViewById(R.id.btnAddIncome);
        btnAddExpense = root.findViewById(R.id.btnAddExpense);
        txtMonthDate = root.findViewById(R.id.txtMonthDate);
    }

    public void LoadFragmentButtonListeners()
    {
        btnAddIncome.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_navigation_income));
        btnAddExpense.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_navigation_outcome));
    }

    public void ConfigureDateTimePicker()
    {
        MonthYearPickerDialog pickerDialog = new MonthYearPickerDialog();
        txtMonthDate.setOnClickListener(view -> {
            pickerDialog.setListener((datePicker, year, month, i2) -> {
                String monthYearStr = year + "-" + (month + 1) + "-" + i2;
                txtMonthDate.setText(formatMonthYear(monthYearStr));
                Toast.makeText(HomeFragment.this.getContext(), year + "-" + month, Toast.LENGTH_SHORT).show();
            });
            pickerDialog.show(getFragmentManager(), "MonthYearPickerDialog");
        });

        Date dateNow = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");
        txtMonthDate.setText(sdf.format(dateNow));
    }

    private String formatMonthYear(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");
        SimpleDateFormat dateInput = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateInput.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdf.format(date);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
        actionBar.hide();

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        LoadFragmentViews(root);

        LoadFragmentButtonListeners();

        homeViewModel.setSharedPreferences(getActivity().getSharedPreferences("BUDDY", MODE_PRIVATE));
        homeViewModel.DisplayFirebaseData(root, this);

        ConfigureDateTimePicker();

        return root;
    }
}