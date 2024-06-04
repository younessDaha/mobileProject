package com.example.gestiondesyndicdecoproprit;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;


public class AjoutEvenement extends AppCompatActivity {


    private FirebaseFirestore db;
    private EditText editTextNomReunion;
    private EditText editTextDescriptionReunion;
    private EditText editTextHeureReunion;
    private Button btnAjouterEvenement;
    private Button btnAnnuler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_evenement);



        db = FirebaseFirestore.getInstance();
        editTextNomReunion = findViewById(R.id.editTextNomReunion);
        editTextDescriptionReunion = findViewById(R.id.editTextDescriptionReunion);
        editTextHeureReunion = findViewById(R.id.editTextHeureReunion);
        btnAjouterEvenement = findViewById(R.id.btnAjouterEvenement);
        btnAnnuler = findViewById(R.id.btnAnnuler);



        btnAjouterEvenement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nomReunion = editTextNomReunion.getText().toString();
                String descriptionReunion = editTextDescriptionReunion.getText().toString();
                String heureReunion = editTextHeureReunion.getText().toString();



                if (!nomReunion.isEmpty() && !descriptionReunion.isEmpty() && !heureReunion.isEmpty()) {

                    ajouterEvenement(nomReunion, descriptionReunion, heureReunion);
                } else {
                    Toast.makeText(AjoutEvenement.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                }
            }
        });



        btnAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextNomReunion.setText("");
                editTextDescriptionReunion.setText("");
                editTextHeureReunion.setText("");


                Intent intent = new Intent(AjoutEvenement.this, Calendrier.class);
                startActivity(intent);
            }
        });



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_home) {
                    startActivity(new Intent(AjoutEvenement.this,Acceuil.class));
                    return true;
                } else if (item.getItemId() == R.id.menu_contact) {
                    startActivity(new Intent(AjoutEvenement.this, Contact.class));
                    return true;
                } else if (item.getItemId() == R.id.menu_signout) {
                    startActivity(new Intent(AjoutEvenement.this, Signout.class));


                    return true;
                }
                return false;
            }
        });
    }



    private void ajouterEvenement(String nomReunion, String descriptionReunion, String heureReunion) {

        Map<String, Object> evenement = new HashMap<>();
        evenement.put("nom", nomReunion);
        evenement.put("description", descriptionReunion);
        evenement.put("heure", heureReunion);

        db.collection("evenements")
                .add(evenement)
                .addOnSuccessListener(documentReference -> {

                    Toast.makeText(AjoutEvenement.this, "Événement ajouté avec succès!", Toast.LENGTH_SHORT).show();

                    editTextNomReunion.setText("");
                    editTextDescriptionReunion.setText("");
                    editTextHeureReunion.setText("");
                })
                .addOnFailureListener(e -> {

                    Toast.makeText(AjoutEvenement.this, "Erreur lors de l'ajout de l'événement", Toast.LENGTH_SHORT).show();
                });
    }
}
