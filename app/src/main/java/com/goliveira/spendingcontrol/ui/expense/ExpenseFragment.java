package com.goliveira.spendingcontrol.ui.expense;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.goliveira.spendingcontrol.R;
import com.goliveira.spendingcontrol.model.BudgetList;
import com.goliveira.spendingcontrol.model.Expense;

import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpenseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpenseFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button btnAddOutcome;

    public ExpenseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentInput.
     */
    // TODO: Rename and change types and number of parameters
    public static ExpenseFragment newInstance(String param1, String param2) {
        ExpenseFragment fragment = new ExpenseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_input, container, false);
//    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_outcome, container, false);

        btnAddOutcome = root.findViewById(R.id.btnAddOutcome_fragment_outcome);

        btnAddOutcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Expense newExpense = new Expense();

                EditText txtOutcomeAmount = root.findViewById(R.id.txtOutcomeAmount);
                EditText txtOutcomeDescription = root.findViewById(R.id.txtOutcomeDescription);
                Spinner cmbOutcomeCategory = root.findViewById(R.id.cmbOutcomeCategory);

                newExpense.setDescription(txtOutcomeDescription.getText().toString());
                newExpense.setAmount(GetInt(txtOutcomeAmount.getText().toString()));
                newExpense.setCategory(cmbOutcomeCategory.getSelectedItem().toString());

                newExpense.save();
                BudgetList.getInstance().budget.add(newExpense);

                Navigation.findNavController(view).popBackStack(); //goes to the previous fragment
            }
        });


        //fulfill the combobox...
        Spinner spinner = (Spinner) root.findViewById(R.id.cmbOutcomeCategory);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                root.getContext(), R.array.outcome_categories, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        //setting the date field
        EditText fragment_outcome_txtDate = root.findViewById(R.id.fragment_outcome_txtDate);
        Date currentTime = Calendar.getInstance().getTime();

        fragment_outcome_txtDate.setText(currentTime.toString());

        return root;
    }


    public int GetInt(String s){
        return Integer.parseInt(s.replaceAll("[\\D]", ""));
    }
}