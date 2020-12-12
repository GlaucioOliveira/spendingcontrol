package com.goliveira.spendingcontrol.ui.expense;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
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
import com.goliveira.spendingcontrol.model.Transaction;
import com.goliveira.spendingcontrol.model.TransactionType;
import com.goliveira.spendingcontrol.model.User;
import com.goliveira.spendingcontrol.notification.PushNotificationAsync;
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

import static android.content.Context.MODE_PRIVATE;

public class ExpenseFragment extends Fragment {

    private Calendar expenseCalendar;
    private FloatingActionButton btnAddExpense;
    private EditText expenseDate;
    private final String TAG = "Expense Fragment";
    private User user;

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
        Log.i(TAG, "Back Button Pressed");
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.i(TAG, "home on back pressed");
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setDateTimePicker(View root) {
        expenseCalendar = Calendar.getInstance();
        expenseDate = root.findViewById(R.id.expenseCreatedAt);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        expenseDate.setText(sdf.format(expenseCalendar.getTime()));

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                expenseCalendar.set(Calendar.YEAR, year);
                expenseCalendar.set(Calendar.MONTH, monthOfYear);
                expenseCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                expenseDate.setText(dateFormat.format(expenseCalendar.getTime()));
            }
        };

        expenseDate.setOnClickListener(view -> new DatePickerDialog(ExpenseFragment.this.getContext(), date, expenseCalendar.get(Calendar.YEAR), expenseCalendar.get(Calendar.MONTH), expenseCalendar.get(Calendar.DAY_OF_MONTH)).show());
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_expense, container, false);

        btnAddExpense = root.findViewById(R.id.btnSubmitExpense);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);

                String[] arraySpinner = new String[]{user.getDisplayName(), user.getBuddyName()};
                Spinner expenseWallet = root.findViewById(R.id.expenseWallet);
                ArrayAdapter<String> adapterExpenseWallet = new ArrayAdapter<>(root.getContext(), android.R.layout.simple_spinner_item, arraySpinner);
                adapterExpenseWallet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                expenseWallet.setAdapter(adapterExpenseWallet);
                if (user.isHasBuddy()) {
                    expenseWallet.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Error fetching database");
            }
        });

        btnAddExpense.setOnClickListener(view -> {

            EditText expenseAmount = root.findViewById(R.id.expenseAmount);
            EditText expenseDate = root.findViewById(R.id.expenseCreatedAt);
            EditText expenseDescription = root.findViewById(R.id.expenseDescription);
            Spinner expenseCategory = root.findViewById(R.id.expenseCategory);
            Spinner expenseWallet = root.findViewById(R.id.expenseWallet);
            // validate fields
            if (checkIsEmpty(expenseAmount) || checkIsEmpty(expenseDate) || checkSelect(expenseCategory) || checkIsEmpty(expenseDescription)) {
                return;
            }
            Transaction expense = new Transaction();
            expense.setType(TransactionType.EXPENSE);
            expense.setDescription(expenseDescription.getText().toString());
            expense.setAmount(GetInt(expenseAmount.getText().toString()));
            expense.setDate(expenseDate.getText().toString());
            try {
                Date dateUnix = new SimpleDateFormat("dd-MM-yyyy").parse(expenseDate.getText().toString());
                expense.setDateUnix(dateUnix.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (user.isHasBuddy() && TextUtils.equals(expenseWallet.getSelectedItem().toString(), user.getBuddyName())) {
                expense.setWallet(user.getBuddy());
            } else {
                expense.setWallet(user.getUid());
            }

            expense.setCategory(expenseCategory.getSelectedItem().toString());

            expense.save();

            TryToNotifyBuddy(expense);

            Navigation.findNavController(view).popBackStack(); //goes to the previous fragment
        });

        Spinner spinner = root.findViewById(R.id.expenseCategory);
        ArrayAdapter<CharSequence> adapterExpenseCategory = ArrayAdapter.createFromResource(root.getContext(), R.array.expense_categories, android.R.layout.simple_spinner_item);
        adapterExpenseCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterExpenseCategory);
        //setting the date field
        setDateTimePicker(root);

        return root;
    }

    public void TryToNotifyBuddy(Transaction transaction) {
        String BUDDY_ID = "";

        SharedPreferences preferences = getActivity().getSharedPreferences("BUDDY", MODE_PRIVATE);
        if (preferences.contains("BUDDY_ID")) {
            BUDDY_ID = preferences.getString("BUDDY_ID", "");
        }

        if (TextUtils.isEmpty(BUDDY_ID) == false) {
            // Set up a new instance of our runnable object that will be run on the background thread
            PushNotificationAsync pushNotificationAsync = new PushNotificationAsync(BUDDY_ID, transaction.getMessageForBuddyNotification());

            // Set up the thread that will use our runnable object
            Thread thread = new Thread(pushNotificationAsync);

            thread.start();
        }
    }

    public boolean checkIsEmpty(TextView textView) {
        if (TextUtils.isEmpty(textView.getText().toString())) {
            textView.setError("Required field");
            return true;
        } else {
            return false;
        }
    }

    public boolean checkSelect(Spinner spinner) {
        if (TextUtils.equals(spinner.getSelectedItem().toString(), "-- Select category --")) {
            Toast.makeText(ExpenseFragment.this.getContext(), "Category field is required", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }


    public int GetInt(String s) {
        return Integer.parseInt(s.replaceAll("[\\D]", ""));
    }
}