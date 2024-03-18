package com.example.expensemanager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.expensemanager.databinding.ActivityAddExpenseBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Calendar;
import java.util.UUID;

public class AddExpenseActivity extends AppCompatActivity {
    ActivityAddExpenseBinding binding;
    private String type;
    private ExpenseModel expenseModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddExpenseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        type = getIntent().getStringExtra("type");
        expenseModel = (ExpenseModel) getIntent().getSerializableExtra("model");
        if (expenseModel != null) Log.d("TAG", expenseModel.getExpenseId());

        if (type == null) {
            assert expenseModel != null;
            type = expenseModel.getType();
            binding.amount.setText(String.valueOf(expenseModel.getAmount()));
            binding.category.setText(expenseModel.getCategory());
            binding.note.setText(expenseModel.getNote());
        }

        if (type.equals("Income")) {
            binding.incomeRadio.setChecked(true);
        } else {
            binding.expenseRadio.setChecked(true);
        }

        binding.incomeRadio.setOnClickListener(view -> type = "Income");
        binding.expenseRadio.setOnClickListener(view -> type = "Expense");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        if (expenseModel == null) {
            menuInflater.inflate(R.menu.add_menu, menu);
        } else {
            menuInflater.inflate(R.menu.update_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.saveExpense) {
            if (type != null) {
                createExpense();
            } else {
                updateExpense();
            }
            return true;
        }
        if (id == R.id.deleteExpense) {
            deleteExpense();
        }
        return false;
    }

    private void deleteExpense() {
        FirebaseFirestore
                .getInstance()
                .collection("expense")
                .document(expenseModel.getExpenseId())
                .delete();
        finish();
    }

    private void createExpense() {

        String expenseId = UUID.randomUUID().toString();
        String amount = binding.amount.getText().toString();
        String note = binding.note.getText().toString();
        String category = binding.category.getText().toString();

        boolean incomeChecked = binding.incomeRadio.isChecked();
        if (incomeChecked) {
            type = "Income";
        } else {
            type = "Expense";
        }

        if (amount.trim().isEmpty()) {
            binding.amount.setError("Empty");
            return;
        }
        ExpenseModel expenseModel = new ExpenseModel(expenseId, note, category, type, Long.parseLong(amount), Calendar.getInstance().getTimeInMillis(),
                FirebaseAuth.getInstance().getUid());

        FirebaseFirestore
                .getInstance()
                .collection("expenses")
                .document(expenseId)
                .set(expenseModel);
        finish();

    }

    private void updateExpense() {

        String expenseId = expenseModel.getExpenseId();
        Log.d("TAG", expenseId);
        String amount = binding.amount.getText().toString();
        String note = binding.note.getText().toString();
        String category = binding.category.getText().toString();

        expenseModel.setAmount(Long.parseLong(amount));
        expenseModel.setNote(note);
        expenseModel.setCategory(category);

        boolean incomeChecked = binding.incomeRadio.isChecked();
        if (incomeChecked) {
            type = "Income";
        } else {
            type = "Expense";
        }

        if (amount.trim().isEmpty()) {
            binding.amount.setError("Empty");
            return;
        }

        FirebaseFirestore
                .getInstance()
                .collection("expenses")
                .document(expenseModel.getExpenseId())
                .set(expenseModel, SetOptions.mergeFields());


        finish();
    }
}