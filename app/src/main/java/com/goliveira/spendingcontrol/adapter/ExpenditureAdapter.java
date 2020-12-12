package com.goliveira.spendingcontrol.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.goliveira.spendingcontrol.R;
import com.goliveira.spendingcontrol.interfaces.IExpenditure;
import com.goliveira.spendingcontrol.model.TransactionType;

import java.util.ArrayList;

public class ExpenditureAdapter extends RecyclerView.Adapter<ExpenditureAdapter.ExpenditureAdapterViewHolder> {

    ArrayList<IExpenditure> expenditures;
    Context context;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    // ViewHolders cache the references to the views that will be modified in the adapter.
    public class ExpenditureAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView transactionDescription;
        TextView transactionAmount;
        TextView secondaryDescription;

        // Create a constructor that accepts a View called itemView as a parameter
        public ExpenditureAdapterViewHolder(View itemView) {
            super(itemView);
            transactionDescription = itemView.findViewById(R.id.transactionDescription);
            transactionAmount = itemView.findViewById(R.id.transactionAmount);
            secondaryDescription = itemView.findViewById(R.id.secondaryDescription);
        }
    }

    private int mNumberItems;

    public ExpenditureAdapter(Context context, int numberOfItems, ArrayList<IExpenditure> expenditures)
    {
        mNumberItems = numberOfItems;
        this.expenditures = expenditures;
    }


    @Override
    public ExpenditureAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.v("onCreateViewHolder", "onCreateViewHolder is called !");
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recycler_view_item;
        // Inflate our new item view using a LayoutInflater. It takes the ID of layout in xml.
        // Then --> inflates or converts this collection of view groups and views.
        LayoutInflater inflater = LayoutInflater.from(context);
        // Set to false, so that the inflated layout will not be
        // immediately attached to its parent viewgroup.
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        ExpenditureAdapterViewHolder viewHolder = new ExpenditureAdapterViewHolder(view);

        return viewHolder;
    }

    //onBindViewHolder()
    @Override
    public void onBindViewHolder(ExpenditureAdapterViewHolder holder, int position) {
        // Get the data model based on position
        IExpenditure expenditure = expenditures.get(position);
        // Set item views based on your views and data model
        TextView description = holder.transactionDescription;
        TextView amount = holder.transactionAmount;
        TextView secondaryDescription = holder.secondaryDescription;
        description.setText(expenditure.getDescription());
        amount.setText("$ "+expenditure.getAmount());
        if(expenditure.getType() == TransactionType.INCOME) {
            amount.setTextColor(ContextCompat.getColor(context, R.color.income_color));
        }
        secondaryDescription.setText(expenditure.getCategory()+" | "+expenditure.getCreatedByName()+" | "+expenditure.getDate());
    }

    //getItemCount() : returns the mNumberItems var
    @Override
    public int getItemCount() {
        mNumberItems = expenditures.size();
        return mNumberItems;
    }
}