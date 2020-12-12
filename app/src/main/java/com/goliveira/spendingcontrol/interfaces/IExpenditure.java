package com.goliveira.spendingcontrol.interfaces;

import com.goliveira.spendingcontrol.model.Transaction;
import com.goliveira.spendingcontrol.model.TransactionType;
import com.google.firebase.database.DataSnapshot;

public interface IExpenditure {

    public TransactionType getType();

    public void setType(TransactionType type);

    public double getAmount();

    public void setAmount(double amount);

    public String getDate();

    public void setDate(String date);

    public String getCategory();

    public void setCategory(String category);

    public String getDescription();

    public void setDescription(String description);

    public String getCreatedAt();

    public String getCreatedBy();

    public String getCreatedByName();

    void save();
}
