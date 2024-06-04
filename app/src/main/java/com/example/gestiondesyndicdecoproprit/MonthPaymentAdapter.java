package com.example.gestiondesyndicdecoproprit;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MonthPaymentAdapter extends RecyclerView.Adapter<MonthPaymentAdapter.MonthPaymentViewHolder> {

    private final List<Map.Entry<String, String>> payments;

    public MonthPaymentAdapter(Map<String, String> payments) {
        this.payments = new ArrayList<>(payments.entrySet());
    }

    @NonNull
    @Override
    public MonthPaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_month_payment, parent, false);
        return new MonthPaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonthPaymentViewHolder holder, int position) {
        Map.Entry<String, String> payment = payments.get(position);
        holder.textViewMonth.setText(payment.getKey());
        holder.textViewStatus.setText(payment.getValue());

        // Change text color based on payment status
        if ("paid".equals(payment.getValue())) {
            holder.textViewStatus.setTextColor(Color.GREEN);
        } else {
            holder.textViewStatus.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return payments.size();
    }

    static class MonthPaymentViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMonth;
        TextView textViewStatus;

        MonthPaymentViewHolder(View itemView) {
            super(itemView);
            textViewMonth = itemView.findViewById(R.id.textViewMonth);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
        }
    }
}
