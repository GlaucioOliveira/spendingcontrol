package com.goliveira.spendingcontrol.ui.income;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toolbar;

import com.goliveira.spendingcontrol.R;
import com.goliveira.spendingcontrol.model.BudgetList;
import com.goliveira.spendingcontrol.model.Income;

import java.util.Calendar;
import java.util.Date;

public class IncomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button btnAddIncome;

    public IncomeFragment() {
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
    public static IncomeFragment newInstance(String param1, String param2) {
        IncomeFragment fragment = new IncomeFragment();
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

        View root = inflater.inflate(R.layout.fragment_income, container, false);

        btnAddIncome = root.findViewById(R.id.btnAddIncome_fragment_income);

        btnAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Income newIncome = new Income();

                EditText txtIncomeAmount = root.findViewById(R.id.txtIncomeAmount);
                EditText txtIncomeDescription = root.findViewById(R.id.txtIncomeDescription);
                Spinner cmbIncomeCategory = root.findViewById(R.id.cmbIncomeCategory);

                newIncome.setDescription(txtIncomeDescription.getText().toString());
                newIncome.setAmount(GetInt(txtIncomeAmount.getText().toString()));
                newIncome.setCategory(cmbIncomeCategory.getSelectedItem().toString());
                newIncome.save();

                BudgetList.getInstance().budget.add(newIncome);

                Navigation.findNavController(view).popBackStack(); //goes to the previous fragment
            }
        });

        //TODO: ver como fazer o back button funcionar...
//        Toolbar toolbar = root.findViewById(R.id.nav_men);
//        toolbar.setNavigationIcon(R.drawable.ic_back_button);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().onBackPressed();
//            }
//        });

        //fulfill the combobox...
        Spinner spinner = (Spinner) root.findViewById(R.id.cmbIncomeCategory);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                root.getContext(), R.array.income_categories, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        EditText fragment_income_txtDate = root.findViewById(R.id.fragment_income_txtDate);
        Date currentTime = Calendar.getInstance().getTime();

        fragment_income_txtDate.setText(currentTime.toString());
        return root;
    }

    public int GetInt(String s){
        return Integer.parseInt(s.replaceAll("[\\D]", ""));
    }
}