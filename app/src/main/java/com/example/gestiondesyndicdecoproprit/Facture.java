package com.example.gestiondesyndicdecoproprit;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

public class Facture extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MonthPaymentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facture);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Map<String, String> userPayments = new HashMap<>();
        userPayments.put("January", "paid");
        userPayments.put("February", "unpaid");
        userPayments.put("March", "paid");
        userPayments.put("April", "unpaid");
        userPayments.put("May", "paid");
        userPayments.put("June", "unpaid");
        userPayments.put("July", "paid");
        userPayments.put("August", "unpaid");
        userPayments.put("September", "paid");
        userPayments.put("October", "unpaid");
        userPayments.put("November", "paid");
        userPayments.put("December", "unpaid");

        adapter = new MonthPaymentAdapter(userPayments);
        recyclerView.setAdapter(adapter);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_home) {
                    startActivity(new Intent(Facture.this, Acceuil.class));
                    return true;
                } else if (item.getItemId() == R.id.menu_contact) {
                    startActivity(new Intent(Facture.this, Contact.class));
                    return true;
                } else if (item.getItemId() == R.id.menu_signout) {
                    startActivity(new Intent(Facture.this, Signout.class));
                    return true;
                }
                return false;
            }
        });
    }
}
