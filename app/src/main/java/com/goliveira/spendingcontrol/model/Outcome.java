package com.goliveira.spendingcontrol.model;

import com.goliveira.spendingcontrol.interfaces.IExpenditure;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Outcome implements IExpenditure {

    private String description;

    private int amount;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private String category;

    public Outcome()
    {
        description = "";
        amount = 0;
        category = "";
    }

    public Outcome(String description, int amount) {
        this.description = description;
        this.amount = amount;
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

    public String getRecyclerViewDescription(){
        NumberFormat formatter = new DecimalFormat("#,###");
        double myNumber = this.amount;
        String formattedAmount = formatter.format(myNumber);

        return formattedAmount + " - " + category + " - " + description;
    }
}
