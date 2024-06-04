package com.example.gestiondesyndicdecoproprit;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;




public class Calendrier extends AppCompatActivity {
    private FirebaseFirestore db;
    private CalendarView calendarView;
    private Button btnAjouterEvenementPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendrier);

        db = FirebaseFirestore.getInstance();
        calendarView = findViewById(R.id.calendarView);
        btnAjouterEvenementPage= findViewById(R.id.btnAjouterEvenement);
        btnAjouterEvenementPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Calendrier.this, AjoutEvenement.class);
                startActivity(intent);
            }
        });
    }

    private void ajouterEvenement(String date) {

        Map<String, Object> evenement = new HashMap<>();
        evenement.put("date", date);
        db.collection("evenements")
                .add(evenement)
                .addOnSuccessListener(documentReference -> {

                    Toast.makeText(Calendrier.this, "Événement ajouté avec succès!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Calendrier.this, "Erreur lors de l'ajout de l'événement", Toast.LENGTH_SHORT).show();
                });




        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_home) {
                    startActivity(new Intent(Calendrier.this,Acceuil.class));
                    return true;
                } else if (item.getItemId() == R.id.menu_contact) {
                    startActivity(new Intent(Calendrier.this, Contact.class));
                    return true;
                } else if (item.getItemId() == R.id.menu_signout) {
                    startActivity(new Intent(Calendrier.this, Signout.class));


                    return true;
                }
                return false;
            }
        });
    }
}
