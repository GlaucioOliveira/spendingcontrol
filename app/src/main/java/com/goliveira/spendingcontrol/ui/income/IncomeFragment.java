package com.goliveira.spendingcontrol.ui.income;

import android.app.DatePickerDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.goliveira.spendingcontrol.R;
import com.goliveira.spendingcontrol.model.BudgetList;
import com.goliveira.spendingcontrol.model.Income;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class IncomeFragment extends Fragment {

    private Calendar incomeCalendar;
    private EditText incomeDate;
    private FloatingActionButton btnAddIncome;
    private final String TAG = "Income Fragment";

    public IncomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.show();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.income_color)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
        actionBar.hide();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG,"Back Button Pressed");
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.i(TAG,"home on back pressed");
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setDateTimePicker(View root)
    {
        incomeCalendar = Calendar.getInstance();
        incomeDate = (EditText) root.findViewById(R.id.incomeCreatedAt);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                incomeCalendar.set(Calendar.YEAR, year);
                incomeCalendar.set(Calendar.MONTH, monthOfYear);
                incomeCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                incomeDate.setText(dateFormat.format(incomeCalendar.getTime()));
            }
        };

        incomeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(IncomeFragment.this.getContext(), date, incomeCalendar.get(Calendar.YEAR), incomeCalendar.get(Calendar.MONTH), incomeCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_income, container, false);

        btnAddIncome = root.findViewById(R.id.btnSubmitIncome);

        btnAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText incomeAmount = root.findViewById(R.id.incomeAmount);
                EditText incomeDate = root.findViewById(R.id.incomeCreatedAt);
                EditText incomeDescription = root.findViewById(R.id.incomeDescription);
                Spinner incomeCategory = root.findViewById(R.id.incomeCategory);
                // validate fields
                if(checkIsEmpty(incomeAmount) || checkIsEmpty(incomeDate) || checkSelect(incomeCategory) || checkIsEmpty(incomeDescription)){
                    return;
                }
                Income income = new Income();
                income.setDescription(incomeDescription.getText().toString());
                income.setAmount(GetInt(incomeAmount.getText().toString()));
                income.setCreatedAt(incomeDate.getText().toString());
                income.setCategory(incomeCategory.getSelectedItem().toString());

                income.save();

                // TODO: replace BudgetList with firebase data
                BudgetList.getInstance().budget.add(income);

                Navigation.findNavController(view).popBackStack(); //goes to the previous fragment
            }
        });

        Spinner spinner = root.findViewById(R.id.incomeCategory);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(root.getContext(), R.array.income_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //setting the date field
        setDateTimePicker(root);

        return root;
    }

    public boolean checkIsEmpty (TextView textView) {
        if(TextUtils.isEmpty(textView.getText().toString())){
            textView.setError("Required field");
            return true;
        } else {
            return false;
        }
    }

    public boolean checkSelect (Spinner spinner){
        if(TextUtils.equals(spinner.getSelectedItem().toString(), "-- Select category --")) {
            Toast.makeText(IncomeFragment.this.getContext(), "Category field is required", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }

    public int GetInt(String s){
        return Integer.parseInt(s.replaceAll("[\\D]", ""));
    }
}