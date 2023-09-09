package com.example.simplestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

//imports suggested by ChatGPT to add code snippets below
import android.content.Intent;
import android.net.Uri;
import androidx.core.content.FileProvider;
import java.io.File;
import android.os.Environment;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db;
    RecyclerView mRecyclerView;
    ArrayList<DownModel> downModelArrayList = new ArrayList<>();
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpRV();
        setUpFB();
        dataFromFirebase();

    }

    // ChatGPT suggested to add this method for downloading and opening APKs
    private void downloadAndOpenApk(String apkLink) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference apkRef = storage.getReferenceFromUrl(apkLink);

        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File localFile = new File(downloadsDir, "downloaded_apk.apk");

        apkRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
            // File downloaded successfully
            openDownloadedApk(localFile);
        }).addOnFailureListener(e -> {
            // Handle download failure
            Toast.makeText(MainActivity.this, "Download failed", Toast.LENGTH_SHORT).show();
        });
    }
    // ChatGPT suggested to add this method for downloading and opening APKs
    private void openDownloadedApk(File localFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri apkUri = Uri.fromFile(localFile);
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void dataFromFirebase(){
        if (downModelArrayList.size()>0)
            downModelArrayList.clear();

        //COULD BE A CULPRIT
        //FirebaseStorage storage;
        //StorageReference storageRef = storage.getReference();

        //ALREADY INITIALIZED ON LINE 78
        //db=FirebaseFirestore.getInstance();

        db.collection("apks")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for(DocumentSnapshot documentSnapshot: task.getResult()) {

                            DownModel downModel= new DownModel(documentSnapshot.getString("name"),
                                    documentSnapshot.getString("link"));
                            downModelArrayList.add(downModel);

                        }

                        myAdapter= new MyAdapter(MainActivity.this,downModelArrayList);
                        mRecyclerView.setAdapter(myAdapter);
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error ;-.-;", Toast.LENGTH_SHORT).show();
                    }
                })
        ;
        
    }

    private void setUpFB(){
        db=FirebaseFirestore.getInstance();
    }

    private void setUpRV(){
        mRecyclerView= findViewById(R.id.recycle);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}