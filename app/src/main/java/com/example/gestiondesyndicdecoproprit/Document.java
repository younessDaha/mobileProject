package com.example.gestiondesyndicdecoproprit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class Document extends AppCompatActivity {

    private static final String TAG = "DocumentActivity";
    private static final int PICK_FILE_REQUEST = 1;

    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private ListView documentsListView;
    private List<String> documentsList;
    private ArrayAdapter<String> adapter;
    private Button addDocumentButton;
    private List<DocumentData> documentDataList; // List to hold document data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        documentsListView = findViewById(R.id.documentsListView);
        addDocumentButton = findViewById(R.id.addDocumentButton);
        documentsList = new ArrayList<>();
        documentDataList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, documentsList);
        documentsListView.setAdapter(adapter);

        loadDocuments();

        addDocumentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        // Set item click listener to open document
        documentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = documentDataList.get(position).getUrl();
                openDocument(url);
            }
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_home) {
                    startActivity(new Intent(Document.this, Acceuil.class));
                    return true;
                } else if (item.getItemId() == R.id.menu_contact) {
                    startActivity(new Intent(Document.this, Contact.class));
                    return true;
                } else if (item.getItemId() == R.id.menu_signout) {
                    startActivity(new Intent(Document.this, Signout.class));
                    return true;
                }
                return false;
            }
        });
    }

    private void loadDocuments() {
        db.collection("documents")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            documentsList.clear();  // Clear the list to avoid duplicates
                            documentDataList.clear();  // Clear the document data list
                            for (DocumentSnapshot document : task.getResult()) {
                                String documentName = document.getString("name");
                                String documentUrl = document.getString("url");
                                if (documentName != null && documentUrl != null) {
                                    documentsList.add(documentName);
                                    documentDataList.add(new DocumentData(documentName, documentUrl));
                                } else {
                                    Log.d(TAG, "Document does not contain 'name' or 'url' field");
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            Toast.makeText(Document.this, "Error getting documents.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri fileUri = data.getData();
            Log.d(TAG, "File selected: " + fileUri.toString());  // Log file URI
            uploadFile(fileUri);
        }
    }

    private void uploadFile(Uri fileUri) {
        if (fileUri != null) {
            StorageReference storageRef = storage.getReference();
            StorageReference fileRef = storageRef.child("documents/" + System.currentTimeMillis() + "-" + fileUri.getLastPathSegment());
            fileRef.putFile(fileUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();
                                    Log.d(TAG, "File uploaded successfully, URL: " + url);  // Log file URL
                                    addDocumentToFirestore(fileUri.getLastPathSegment(), url);  // Pass the file name and URL
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "File upload failed: " + e.getMessage(), e);  // Log error
                            Toast.makeText(Document.this, "File upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void addDocumentToFirestore(String name, String url) {
        DocumentData documentData = new DocumentData(name, url);  // Creating an instance with name and URL
        db.collection("documents")
                .add(documentData)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Document added successfully to Firestore");  // Log success
                            loadDocuments();
                            Toast.makeText(Document.this, "Document added successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "Failed to add document to Firestore", task.getException());  // Log error
                            Toast.makeText(Document.this, "Failed to add document", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void openDocument(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    // Define a new model class for documents
    public static class DocumentData {
        private String name;
        private String url;

        public DocumentData() {

        }

        public DocumentData(String name, String url) {
            this.name = name;
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
