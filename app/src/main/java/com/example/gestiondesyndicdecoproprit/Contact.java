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


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;


public class Contact extends AppCompatActivity {


    private EditText editTextName, editTextEmail, editTextMessage;
    private Button buttonSendMessage;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);


        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSendMessage = findViewById(R.id.buttonSendMessage);


        db = FirebaseFirestore.getInstance();


        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_home) {


                    startActivity(new Intent(Contact.this,Acceuil.class));
                    return true;
                } else if (item.getItemId() == R.id.menu_contact) {
                    startActivity(new Intent(Contact.this, Contact.class));
                    return true;
                } else if (item.getItemId() == R.id.menu_signout) {
                    startActivity(new Intent(Contact.this, Signout.class));


                    return true;
                }
                return false;
            }
        });

    }


    private void sendMessage() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String message = editTextMessage.getText().toString().trim();


        // Vérifier si tous les champs sont remplis
        if (name.isEmpty() || email.isEmpty() || message.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }


        // Créer un objet Map avec les données du formulaire
        Map<String, Object> contactData = new HashMap<>();
        contactData.put("name", name);
        contactData.put("email", email);
        contactData.put("message", message);


        // Enregistrer les données dans Firestore
        db.collection("contacts")
                .add(contactData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(Contact.this, "Message envoyé avec succès", Toast.LENGTH_SHORT).show();
                        // Effacer les champs après l'envoi
                        editTextName.setText("");
                        editTextEmail.setText("");
                        editTextMessage.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Contact.this, "Erreur : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

