package com.goliveira.spendingcontrol.model;

import com.goliveira.spendingcontrol.interfaces.IExpenditure;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Expense implements IExpenditure {

    private String description;
    private int amount;
    private final String createdBy;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseRef = database.getReference();
    private String category;

    public Expense()
    {
        description = "";
        amount = 0;
        category = "";
        createdBy = FirebaseAuth.getInstance().getUid();
    }

    public Expense(String _description, int _amount, String _category, String _createdBy) {
        this.description = _description;
        this.amount = _amount;
        this.category = _category;
        this.createdBy = _createdBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCreatedBy() { return createdBy; }

    public String getRecyclerViewDescription(){
        NumberFormat formatter = new DecimalFormat("#,###");
        double myNumber = this.amount;
        String formattedAmount = formatter.format(myNumber);
        return formattedAmount + " - " + category + " - " + description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void save() {
        databaseRef.child("users/"+this.getCreatedBy()+"/expenses").push().setValue(this);
    }

}