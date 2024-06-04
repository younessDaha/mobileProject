package com.example.gestiondesyndicdecoproprit;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;


public class Historique extends AppCompatActivity {


    private static final String TAG = "HistoriqueActivity";
    private FirebaseFirestore db;
    private ListView historiqueListView;
    private List<String> historiqueList;
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);


        db = FirebaseFirestore.getInstance();
        historiqueListView = findViewById(R.id.historiqueListView);
        historiqueList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, historiqueList);
        historiqueListView.setAdapter(adapter);


        loadHistorique();


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_home) {


                    startActivity(new Intent(Historique.this,Acceuil.class));
                    return true;
                } else if (item.getItemId() == R.id.menu_contact) {
                    startActivity(new Intent(Historique.this, Contact.class));
                    return true;
                } else if (item.getItemId() == R.id.menu_signout) {
                    startActivity(new Intent(Historique.this, Signout.class));


                    return true;
                }
                return false;
            }
        });
    }


    private void loadHistorique() {
        db.collection("reclamations")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            String reclamationText = document.getString("texte");
                            String imageUrl = document.getString("image");
                            if (reclamationText != null) {
                                if (imageUrl != null) {
                                    historiqueList.add("Réclamation: " + reclamationText + "\nImage: " + imageUrl);
                                } else {
                                    historiqueList.add("Réclamation: " + reclamationText);
                                }
                            } else {
                                Log.d(TAG, "Document does not contain 'texte' field");
                            }
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                        Toast.makeText(Historique.this, "Error getting documents.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
