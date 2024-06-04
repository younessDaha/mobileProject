package com.example.gestiondesyndicdecoproprit;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Acceuil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceuil);


        CardView cardDocument = findViewById(R.id.cardDocument);
        CardView cardReclamation = findViewById(R.id.cardReclamation);
        CardView messagerie = findViewById(R.id.messagerie);
        CardView facture = findViewById(R.id.Facture);
        CardView maps = findViewById(R.id.maps);
        CardView calendrier = findViewById(R.id.calendrier);
        CardView historique = findViewById(R.id.Historique);
        CardView meteo =findViewById(R.id.cardMeteo);


       cardDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Acceuil.this, Document.class);
                startActivity(intent);
            }
        });

        cardReclamation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Acceuil.this, Reclamation.class);
                startActivity(intent);
            }
        });

        messagerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Acceuil.this, messagerie.class);
                startActivity(intent);
            }
        });

        facture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Acceuil.this, Facture.class);
                startActivity(intent);
            }
        });

        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Acceuil.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        calendrier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Acceuil.this, Calendrier.class);
                startActivity(intent);
            }
        });

        historique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Acceuil.this, Historique.class);
                startActivity(intent);
            }
        });

        meteo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Acceuil.this, WeatherActivity.class);
                startActivity(intent);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_home) {

                    startActivity(new Intent(Acceuil.this,Acceuil.class));
                    return true;
                } else if (item.getItemId() == R.id.menu_contact) {
                    startActivity(new Intent(Acceuil.this, Contact.class));
                    return true;
                } else if (item.getItemId() == R.id.menu_signout) {
                    startActivity(new Intent(Acceuil.this, Signout.class));

                    return true;
                }
                return false;
            }
        });
    }
}
