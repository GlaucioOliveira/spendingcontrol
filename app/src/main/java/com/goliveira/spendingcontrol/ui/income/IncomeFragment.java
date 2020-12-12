package com.goliveira.spendingcontrol.ui.income;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.goliveira.spendingcontrol.R;
import com.goliveira.spendingcontrol.model.Transaction;
import com.goliveira.spendingcontrol.model.TransactionType;
import com.goliveira.spendingcontrol.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class IncomeFragment extends Fragment {

    private Calendar incomeCalendar;
    private EditText incomeDate;
    private FloatingActionButton btnAddIncome;
    private User user;
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
        incomeDate = root.findViewById(R.id.incomeCreatedAt);

        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            incomeCalendar.set(Calendar.YEAR, year);
            incomeCalendar.set(Calendar.MONTH, monthOfYear);
            incomeCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            incomeDate.setText(dateFormat.format(incomeCalendar.getTime()));
        };

        incomeDate.setOnClickListener(view -> new DatePickerDialog(IncomeFragment.this.getContext(), date, incomeCalendar.get(Calendar.YEAR), incomeCalendar.get(Calendar.MONTH), incomeCalendar.get(Calendar.DAY_OF_MONTH)).show());
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_income, container, false);
        btnAddIncome = root.findViewById(R.id.btnSubmitIncome);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);

                String[] arraySpinner = new String[]{user.getDisplayName(), user.getBuddyName()};
                Spinner incomeWallet = root.findViewById(R.id.incomeWallet);
                ArrayAdapter<String> adapterExpenseWallet = new ArrayAdapter<>(root.getContext(), android.R.layout.simple_spinner_item, arraySpinner);
                adapterExpenseWallet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                incomeWallet.setAdapter(adapterExpenseWallet);
                if(user.isHasBuddy()){
                    incomeWallet.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Error fetching database");
            }
        });

        btnAddIncome.setOnClickListener(view -> {
            EditText incomeAmount = root.findViewById(R.id.incomeAmount);
            EditText incomeDate = root.findViewById(R.id.incomeCreatedAt);
            EditText incomeDescription = root.findViewById(R.id.incomeDescription);
            Spinner incomeCategory = root.findViewById(R.id.incomeCategory);
            Spinner incomeWallet = root.findViewById(R.id.incomeWallet);
            // validate fields
            if(checkIsEmpty(incomeAmount) || checkIsEmpty(incomeDate) || checkSelect(incomeCategory) || checkIsEmpty(incomeDescription)){
                return;
            }
            Transaction income = new Transaction();
            income.setType(TransactionType.INCOME);
            income.setDescription(incomeDescription.getText().toString());
            income.setAmount(GetInt(incomeAmount.getText().toString()));
            income.setDate(incomeDate.getText().toString());
            try {
                Date dateUnix = new SimpleDateFormat("dd-MM-yyyy").parse(incomeDate.getText().toString());
                income.setDateUnix(dateUnix.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(user.isHasBuddy() && TextUtils.equals(incomeWallet.getSelectedItem().toString(), user.getBuddyName())){
                income.setWallet(user.getBuddy());
            } else {
                income.setWallet(user.getUid());
            }
            income.setCategory(incomeCategory.getSelectedItem().toString());
            income.save();

            Navigation.findNavController(view).popBackStack(); //goes to the previous fragment
        });

        Spinner spinner = root.findViewById(R.id.incomeCategory);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(root.getContext(), R.array.income_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);;
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