package com.goliveira.spendingcontrol.model;

import java.util.ArrayList;

public class BudgetList {
 private static BudgetList instance;
 public ArrayList<Income> budget;

 public BudgetList(){
     budget = new ArrayList<>();
 }

 public static BudgetList getInstance(){
    if(instance == null) instance = new BudgetList();
     return instance;
 }


}
