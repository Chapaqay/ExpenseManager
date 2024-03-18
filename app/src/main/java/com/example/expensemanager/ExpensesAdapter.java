package com.example.expensemanager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.MyViewHolder> {
    private final OnItemsClick onItemsClick;
    private final List<ExpenseModel> expenseModelList;

    public ExpensesAdapter(Context context, OnItemsClick onItemsClick) {
        expenseModelList = new ArrayList<>();
        this.onItemsClick = onItemsClick;
    }
    public void add(ExpenseModel expenseModel){
        expenseModelList.add(expenseModel);
        notifyDataSetChanged();
    }
    public void clear(){
        expenseModelList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ExpenseModel expenseModel = expenseModelList.get(position);
        holder.note.setText(expenseModel.getNote());
        holder.category.setText(expenseModel.getCategory());
        holder.amount.setText(String.valueOf(expenseModel.getAmount()));


        if (Objects.equals(expenseModel.getType(), "Expense")) {
            holder.card.setCardBackgroundColor(Color.RED);
        }else {
            holder.card.setCardBackgroundColor(Color.GREEN);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", expenseModel.getExpenseId());
                onItemsClick.onClick(expenseModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return expenseModelList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private final TextView note;
        private final TextView date;
        private final TextView amount;
        private final TextView category;
        private final CardView card;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            note = itemView.findViewById(R.id.note);
            category = itemView.findViewById(R.id.category);
            date = itemView.findViewById(R.id.date);
            amount = itemView.findViewById(R.id.amount);
            card = itemView.findViewById(R.id.item_card);
        }

        public TextView getDate() {
            return date;
        }
    }
}
