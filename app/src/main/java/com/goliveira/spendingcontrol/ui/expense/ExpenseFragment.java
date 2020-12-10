package com.goliveira.spendingcontrol.ui.expense;

import android.app.DatePickerDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.goliveira.spendingcontrol.R;
import com.goliveira.spendingcontrol.model.BudgetList;
import com.goliveira.spendingcontrol.model.Expense;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpenseFragment} factory method to
 * create an instance of this fragment.
 */
public class ExpenseFragment extends Fragment {

    private Calendar expenseCalendar;
    private FloatingActionButton btnAddExpense;
    private EditText expenseDate;
    private final String TAG = "Expense Fragment";

    public ExpenseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.show();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.expense_color)));
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
        expenseCalendar = Calendar.getInstance();
        expenseDate = (EditText) root.findViewById(R.id.expenseCreatedAt);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                expenseCalendar.set(Calendar.YEAR, year);
                expenseCalendar.set(Calendar.MONTH, monthOfYear);
                expenseCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                expenseDate.setText(dateFormat.format(expenseCalendar.getTime()));
            }
        };

        expenseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ExpenseFragment.this.getContext(), date, expenseCalendar.get(Calendar.YEAR), expenseCalendar.get(Calendar.MONTH), expenseCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_expense, container, false);

        btnAddExpense = root.findViewById(R.id.btnSubmitExpense);

        btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText expenseAmount = root.findViewById(R.id.expenseAmount);
                EditText expenseDate = root.findViewById(R.id.expenseCreatedAt);
                EditText expenseDescription = root.findViewById(R.id.expenseDescription);
                Spinner expenseCategory = root.findViewById(R.id.expenseCategory);
                // validate fields
                if(checkIsEmpty(expenseAmount) || checkIsEmpty(expenseDate) || checkSelect(expenseCategory) || checkIsEmpty(expenseDescription)){
                    return;
                }
                Expense expense = new Expense();
                expense.setDescription(expenseDescription.getText().toString());
                expense.setAmount(GetInt(expenseAmount.getText().toString()));
                expense.setCreatedAt(expenseDate.getText().toString());
                expense.setCategory(expenseCategory.getSelectedItem().toString());

                expense.save();

                // TODO: replace BudgetList with firebase data
                BudgetList.getInstance().budget.add(expense);

                Navigation.findNavController(view).popBackStack(); //goes to the previous fragment
            }
        });

        Spinner spinner = (Spinner) root.findViewById(R.id.expenseCategory);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(root.getContext(), R.array.expense_categories, android.R.layout.simple_spinner_item);
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
            Toast.makeText(ExpenseFragment.this.getContext(), "Category field is required", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }


    public int GetInt(String s){
        return Integer.parseInt(s.replaceAll("[\\D]", ""));
    }
}