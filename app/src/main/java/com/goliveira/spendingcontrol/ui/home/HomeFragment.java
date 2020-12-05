package com.goliveira.spendingcontrol.ui.home;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.goliveira.spendingcontrol.model.Income;
import com.goliveira.spendingcontrol.adapter.ExpenditureAdapter;
import com.goliveira.spendingcontrol.R;
import com.goliveira.spendingcontrol.ui.income.IncomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import kotlin.reflect.KVariance;

public class HomeFragment extends Fragment {

    //region Private Fields
    private HomeViewModel homeViewModel;
    private Button btnAddIncome;
    private Button btnAddOutcome;
    private Calendar myCalendar;
    private EditText txtMonthDate;
    private PieChart pieChartHome;
    private ImageButton btnSelecionarData;
    //endregion

    public void LoadFragmentViews(View root)
    {
        btnAddIncome = root.findViewById(R.id.btnAddIncome);
        btnAddOutcome = root.findViewById(R.id.btnAddOutcome);
        pieChartHome = root.findViewById(R.id.pieChartHome);

        txtMonthDate = root.findViewById(R.id.txtMonthDate);
        btnSelecionarData = root.findViewById(R.id.btnSelecionarMes);
    }

    public void LoadFragmentButtonListeners()
    {
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

    }

    public ArrayList<Integer> ConfigurePieChartColors()
    {
        ArrayList<Integer> chartColors = new ArrayList<>();

        chartColors.add(ContextCompat.getColor(this.getContext(), R.color.income_color));
        chartColors.add(ContextCompat.getColor(this.getContext(), R.color.outcome_color));

        return chartColors;
    }

    public void DrawChart(View root)
    {
        List<PieEntry> chartData = new ArrayList<>();

        chartData.add(new PieEntry(80, "Income"));
        chartData.add(new PieEntry(20, "Outcome"));

        PieDataSet pieDataSet = new PieDataSet(chartData, "Budget");

        pieDataSet.setColors(ConfigurePieChartColors());
        PieData pieData = new PieData(pieDataSet);

        pieChartHome.setData(pieData);
    }

    public void ConfigureDateTimePicker()
    {
        myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                UpdateMonthLabel();
            }
        };

        btnSelecionarData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(HomeFragment.this.getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        LoadFragmentViews(root);

        LoadFragmentButtonListeners();

        DrawChart(root);

        ConfigureDateTimePicker();

        return root;
    }

    private void UpdateMonthLabel()
    {
        String myFormat = "MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, new Locale("pt","BR"));
        txtMonthDate.setText(dateFormat.format(myCalendar.getTime()));
    }
}