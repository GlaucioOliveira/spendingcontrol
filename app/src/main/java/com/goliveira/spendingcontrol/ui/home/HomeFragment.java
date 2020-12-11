package com.goliveira.spendingcontrol.ui.home;

import android.app.DatePickerDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.Date;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private Button btnAddIncome;
    private Button btnAddOutcome;
    private EditText txtMonthDate;
    SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");
    SimpleDateFormat dateInput = new SimpleDateFormat("yyyy-MM-dd");

    public void LoadFragmentViews(View root)
    {
        btnAddIncome = root.findViewById(R.id.btnAddIncome);
        btnAddOutcome = root.findViewById(R.id.btnAddExpense);
        txtMonthDate = root.findViewById(R.id.txtMonthDate);
    }

    public void LoadFragmentButtonListeners()
    {
        btnAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_navigation_income);
            }
        });

        btnAddOutcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_navigation_outcome);
            }
        });

    }

    public void ConfigureDateTimePicker(View root)
    {
        MonthYearPickerDialog pickerDialog = new MonthYearPickerDialog();

        txtMonthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickerDialog.setListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int i2) {
                        String monthYearStr = year + "-" + (month + 1) + "-" + i2;
                        txtMonthDate.setText(formatMonthYear(monthYearStr));
                        Toast.makeText(HomeFragment.this.getContext(), year + "-" + month, Toast.LENGTH_SHORT).show();
                    }
                });
                pickerDialog.show(getFragmentManager(), "MonthYearPickerDialog");
            }
        });
    }

    private String formatMonthYear(String str) {
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
        homeViewModel.DisplayFirebaseData(root, this);
        ConfigureDateTimePicker(root);

        return root;
    }


}