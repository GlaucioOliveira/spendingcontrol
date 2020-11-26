package com.goliveira.spendingcontrol.model;

public class Income {

    private String description;

    private int amount;

    public Income()
    {
        description = "";
        amount = 0;
    }

    public Income(String description, int amount) {
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
}
