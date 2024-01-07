package com.example.gachabro.imageLoader;

import android.content.Context;
import android.util.Log;
import android.widget.Adapter;

import androidx.annotation.NonNull;

import com.example.gachabro.Adapters.Char_active_adapter;
import com.example.gachabro.model.Character_items;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MaterialLoader {

    private final StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    private final Context context;


    public MaterialLoader(Context context) {
        this.context = context;
    }

    public void listFiles() {
        // replace listRef with your reference
        StorageReference listRef = storageRef.child("/dataset/api-mistress/assets/images/materials");
        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        // Get the list of file references
                        for (StorageReference prefix : listResult.getPrefixes()) {
                            prefix.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                                @Override
                                public void onSuccess(ListResult listResult) {
                                    for (StorageReference item : listResult.getItems()) {
                                        String prefix_name = prefix.getName();
                                        File dir = new File(context.getFilesDir(), prefix_name);
                                        if (!dir.exists()) {
                                            boolean wasSuccessful = dir.mkdirs();
                                            if (!wasSuccessful) {
                                                Log.e("CreateDir", "Failed to create directory");
                                                return;
                                            }

                                            File file = new File(dir, prefix_name + ".txt");
                                            Log.d("CreateFile1", "Successfully downloaded: " + file.getAbsolutePath());
                                            try {
                                                FileOutputStream fos = new FileOutputStream(file);
                                                fos.write(prefix_name.getBytes());
                                                fos.close();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            downloadFile(prefix_name, item, item.getName());
                                        }
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle any errors
                                    Log.e("ImageError", "onFailure: Fail to download Image", e);
                                }
                            });


                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors
                        Log.e("FolderError", "onFailure: Fail to find folder", e);
                    }
                });
    }
    public void listAndDownloadFilesRecursively(StorageReference ref) {
        ref.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        // Iterate over the files
                        for (StorageReference fileRef : listResult.getItems()) {
                            // Download the file
                            downloadFile(ref.getName(), fileRef, fileRef.getName());
                        }

                        // Iterate over the subdirectories
                        for (StorageReference prefix : listResult.getPrefixes()) {
                            // Recursive call
                            listAndDownloadFilesRecursively(prefix);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors
                        Log.e("FolderError", "onFailure: Failed to list files", e);
                    }
                });
    }

    public void downloadFile(String prefix, StorageReference fileRef, String Filename) {

        File directory = new File(context.getFilesDir(), prefix);
        if (!directory.exists()) {
            boolean wasSuccessful = directory.mkdirs();
            if (!wasSuccessful) {
                Log.e("DownloadFile", "Failed to create directory");
                return;
            }
        }
//        File localFile = new File(directory, prefix + "_" + Filename + ".png");
        File localFile = new File(directory, Filename + ".png");

        if (localFile.exists()) {
            Log.d("DownloadFile", "File already exists: " + localFile.getAbsolutePath());
            return;
        }

        fileRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Local temp file has been written
                        Log.d("DownloadFile", "Successfully downloaded: " + localFile.getAbsolutePath());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        Log.w("DownloadFile", "Error downloading: ", exception);
                    }
                });
    }
}
