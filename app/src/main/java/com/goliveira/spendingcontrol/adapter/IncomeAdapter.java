package com.goliveira.spendingcontrol.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.goliveira.spendingcontrol.R;
import com.goliveira.spendingcontrol.model.Income;
import java.util.ArrayList;
import java.util.List;

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.IncomeAdapterViewHolder> {

    List<Income> incomes;

    // ViewHolders cache the references to the views that will be modified in the adapter.
    public class IncomeAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView listItemNumberView = (TextView) itemView.findViewById(R.id.recycler_view_item_text);


        // Create a constructor that accepts a View called itemView as a parameter
        public IncomeAdapterViewHolder(View itemView) {
            super(itemView);
            listItemNumberView = (TextView) itemView.findViewById(R.id.recycler_view_item_text);
        }
    }

    private int mNumberItems;
    private Context context;


    public IncomeAdapter(Context context, int numberOfItems, ArrayList<Income> incomes)
    {
        mNumberItems = numberOfItems;
        this.incomes = incomes;
        this.context = context;
    }


    @Override
    public IncomeAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.v("onCreateViewHolder", "onCreateViewHolder is called !");
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recycler_view_item;

        // Inflate our new item view using a LayoutInflater. It takes the ID of layout in xml.
        // Then --> inflates or converts this collection of view groups and views.
        LayoutInflater inflater = LayoutInflater.from(context);


        // Set to false, so that the inflated layout will not be
        // immediately attached to its parent viewgroup.
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        IncomeAdapterViewHolder viewHolder = new IncomeAdapterViewHolder(view);

        return viewHolder;
    }

    //onBindViewHolder()
    @Override
    public void onBindViewHolder(IncomeAdapterViewHolder holder, int position) {

        // Get the data model based on position

        Income income = incomes.get(position);
        // Set item views based on your views and data model
        TextView textView = holder.listItemNumberView;
        textView.setText(income.getDescription());
    }

    //getItemCount() : returns the mNumberItems var
    @Override
    public int getItemCount() {
        mNumberItems = incomes.size();
        return mNumberItems;
    }
}