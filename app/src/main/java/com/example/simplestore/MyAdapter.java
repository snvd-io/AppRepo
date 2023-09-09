package com.example.simplestore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//COULD BE A CULPRIT
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import static android.os.Environment.DIRECTORY_DOWNLOADS;


public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
//<VH> maybe????
    MainActivity mainActivity;
    ArrayList<DownModel> downModels;

    public MyAdapter(MainActivity mainActivity, ArrayList<DownModel> downModels) {
        this.mainActivity = mainActivity;
        this.downModels = downModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity.getBaseContext());
        View view = layoutInflater.inflate(R.layout.elements, null, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

        myViewHolder.mName.setText(downModels.get(i).getName());
        myViewHolder.mLink.setText(downModels.get(i).getLink());
        myViewHolder.mDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadFile(myViewHolder.mName.getContext(),downModels.get(i).getName(),".apk",DIRECTORY_DOWNLOADS,downModels.get(i).getLink());
            }
        });
    };

    public void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {

        DownloadManager downloadmanager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);

        downloadmanager.enqueue(request);
    }

    @Override
    public int getItemCount() {
        return downModels.size();
    }
}
