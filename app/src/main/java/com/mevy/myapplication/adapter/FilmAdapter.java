package com.mevy.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mevy.myapplication.R;
import com.mevy.myapplication.api.RetrofitClient;
import com.mevy.myapplication.model.FilmData;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.ViewHolder> {
    private List<FilmData.Film> dataList;
    private OnItemClickListener listener;

    public FilmAdapter(List<FilmData.Film> dataList, OnItemClickListener listener) {
        this.dataList = dataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_film, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FilmData.Film data = dataList.get(position);
        holder.bind(data, listener);
//        holder.textViewName.setText(data); // Atur data ke dalam TextView atau elemen lainnya
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView judul, kategori, durasi, rating_usia;
        ImageView foto;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            judul = itemView.findViewById(R.id.judul); // Sesuaikan dengan ID dari layout item grid
            foto = itemView.findViewById(R.id.foto); // Sesuaikan dengan ID dari layout item grid
            kategori = itemView.findViewById(R.id.kategori); // Sesuaikan dengan ID dari layout item grid
            durasi = itemView.findViewById(R.id.durasi); // Sesuaikan dengan ID dari layout item grid
            rating_usia = itemView.findViewById(R.id.rating_usia); // Sesuaikan dengan ID dari layout item grid
        }

        public void bind(final FilmData.Film data, final OnItemClickListener listener) {
            Picasso.get().load(RetrofitClient.BASE_URL + data.getFoto()).into(foto);
            judul.setText(data.getJudul());
            kategori.setText(data.getKategori());
            durasi.setText(data.getDurasi()+" Menit");
            rating_usia.setText(data.getRatingUsia()+"+");
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(data.getId()); // Mengirimkan ID saat item diklik
                }
            });
        }

    }

    // Interface untuk menangani klik pada item grid
    public interface OnItemClickListener {
        void onItemClick(int itemId);
    }

}
