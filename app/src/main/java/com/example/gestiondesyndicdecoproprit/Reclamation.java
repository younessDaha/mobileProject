package com.example.gestiondesyndicdecoproprit;


import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.content.Intent;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;


public class Reclamation extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = "ReclamationActivity";


    private EditText editTextReclamation;
    private Button buttonUploadImage;
    private ImageView imageViewUploaded;
    private Button buttonSubmit;
    private ImageButton backButton;


    private Uri imageUri;


    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reclamation);


        // Initialisation de Firestore
        db = FirebaseFirestore.getInstance();


        editTextReclamation = findViewById(R.id.editTextReclamation);
        buttonUploadImage = findViewById(R.id.buttonUploadImage);
        imageViewUploaded = findViewById(R.id.imageViewUploaded);
        buttonSubmit = findViewById(R.id.buttonSubmit);




        buttonUploadImage.setOnClickListener(v -> openFileChooser());
        buttonSubmit.setOnClickListener(v -> submitReclamation());


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_home) {


                    startActivity(new Intent(Reclamation.this,Acceuil.class));
                    return true;
                } else if (item.getItemId() == R.id.menu_contact) {
                    startActivity(new Intent(Reclamation.this, Contact.class));
                    return true;
                } else if (item.getItemId() == R.id.menu_signout) {
                    startActivity(new Intent(Reclamation.this, Signout.class));


                    return true;
                }
                return false;
            }
        });
    }








    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Sélectionner une image"), PICK_IMAGE_REQUEST);
    }


    private void submitReclamation() {
        // Récupérer le texte de la réclamation
        String reclamationText = editTextReclamation.getText().toString();


        // Créer un nouveau document dans la collection "reclamations" de Firestore
        Map<String, Object> reclamation = new HashMap<>();
        reclamation.put("texte", reclamationText);


        // Vérifier si une image a été sélectionnée
        if (imageUri != null) {
            reclamation.put("image", imageUri.toString());
        }


        db.collection("reclamations")
                .add(reclamation)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(Reclamation.this, "Réclamation envoyée avec succès!", Toast.LENGTH_SHORT).show();

                    editTextReclamation.setText("");
                    if (imageViewUploaded.getVisibility() == View.VISIBLE) {
                        imageViewUploaded.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Erreur lors de l'envoi de la réclamation", e));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageViewUploaded.setImageURI(imageUri);
            imageViewUploaded.setVisibility(View.VISIBLE);
        }
    }




}

