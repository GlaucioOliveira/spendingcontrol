package com.goliveira.spendingcontrol.model;

import com.goliveira.spendingcontrol.interfaces.IExpenditure;

import java.util.ArrayList;

public class BudgetList {
 private static BudgetList instance;
 public ArrayList<IExpenditure> budget;

 public BudgetList(){
     budget = new ArrayList<>();
 }

 public static BudgetList getInstance(){
    if(instance == null) instance = new BudgetList();
     return instance;
 }

}
