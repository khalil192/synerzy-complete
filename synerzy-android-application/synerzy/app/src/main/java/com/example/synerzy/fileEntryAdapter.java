package com.example.synerzy;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class fileEntryAdapter extends RecyclerView.Adapter<fileEntryAdapter.fileEntryViewHolder> {
    private ArrayList<fileEntry> fileList;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void OnItemCick(int position);
        void OnDownloadClick(int position, ImageButton download_button, ProgressBar download_progress_bar);
    }
    public void setOnItemClickListener(OnItemClickListener mlistener){
        listener = mlistener;
    }
    public fileEntryAdapter(ArrayList<fileEntry> files){
        fileList = files;
    }
    @NonNull
    @Override
    public fileEntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_entry_row,parent,false);
        return new fileEntryViewHolder(v , listener);
    }

    @Override
    public void onBindViewHolder(@NonNull fileEntryViewHolder holder, int position) {
        fileEntry currentFile = fileList.get(position);
        holder.fileNameView.setText(currentFile.getFileName());
        holder.fileTypeView.setText(currentFile.getFileType());
        holder.downloadProgressBar.setVisibility(View.GONE);
//        holder.fileImageView.setImageResource(currentFile.getFileImage());

    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public static class fileEntryViewHolder extends RecyclerView.ViewHolder{
        public TextView fileNameView , fileTypeView;
        public ImageView fileImageView;
        public ImageButton downloadButton;
        public ProgressBar downloadProgressBar;


        public fileEntryViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
//            fileImageView = itemView.findViewById(R.id.fileImageView);
            fileNameView = itemView.findViewById(R.id.fileNameView);
            fileTypeView = itemView.findViewById(R.id.fileTypeView);
            downloadButton = itemView.findViewById(R.id.downloadButton);
            downloadProgressBar = itemView.findViewById(R.id.downloadProgressBar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.OnItemCick(position);
                        }
                    }
                }
            });
            downloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.OnDownloadClick(position , downloadButton,downloadProgressBar);
                        }
                    }
                }
            });
        }
    }
}
