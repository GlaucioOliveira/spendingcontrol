package com.goliveira.spendingcontrol.ui.expense;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.goliveira.spendingcontrol.R;
import com.goliveira.spendingcontrol.model.BudgetList;
import com.goliveira.spendingcontrol.model.Expense;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpenseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpenseFragment extends Fragment {

    private Calendar expenseCalendar;
    private Button btnAddExpense;
    private EditText expenseDate;

    public ExpenseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        btnAddExpense = root.findViewById(R.id.submitExpense);

        btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Expense expense = new Expense();

                EditText expenseAmount = root.findViewById(R.id.expenseAmount);
                EditText expenseDate = root.findViewById(R.id.expenseCreatedAt);
                EditText expenseDescription = root.findViewById(R.id.expenseDescription);
                Spinner expenseCategory = root.findViewById(R.id.expenseCategory);

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
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(root.getContext(), R.array.outcome_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //setting the date field
        setDateTimePicker(root);

        return root;
    }


    public int GetInt(String s){
        return Integer.parseInt(s.replaceAll("[\\D]", ""));
    }
}