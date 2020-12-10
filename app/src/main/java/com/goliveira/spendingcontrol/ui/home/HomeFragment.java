package com.goliveira.spendingcontrol.ui.home;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.goliveira.spendingcontrol.R;
import com.goliveira.spendingcontrol.ui.dialogs.MonthYearPickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private Button btnAddIncome;
    private Button btnAddOutcome;
    private EditText txtMonthDate;
    private PieChart pieChartHome;
    SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");
    SimpleDateFormat dateInput = new SimpleDateFormat("yyyy-MM-dd");

    public void LoadFragmentViews(View root)
    {
        btnAddIncome = root.findViewById(R.id.btnAddIncome);
        btnAddOutcome = root.findViewById(R.id.btnAddOutcome);
        pieChartHome = root.findViewById(R.id.pieChartHome);
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

    public ArrayList<Integer> ConfigurePieChartColors()
    {
        ArrayList<Integer> chartColors = new ArrayList<>();

        chartColors.add(ContextCompat.getColor(this.getContext(), R.color.income_color));
        chartColors.add(ContextCompat.getColor(this.getContext(), R.color.expense_color));

        return chartColors;
    }

    public void DrawChart(View root)
    {
        List<PieEntry> chartData = new ArrayList<>();

        chartData.add(new PieEntry(80, "Income"));
        chartData.add(new PieEntry(20, "Expense"));

        PieDataSet pieDataSet = new PieDataSet(chartData, "Budget");

        pieDataSet.setColors(ConfigurePieChartColors());
        PieData pieData = new PieData(pieDataSet);

        pieChartHome.setData(pieData);
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

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        LoadFragmentViews(root);
        LoadFragmentButtonListeners();
        DrawChart(root);
        homeViewModel.DisplayFirebaseData(root);
        ConfigureDateTimePicker(root);

        return root;
    }


}