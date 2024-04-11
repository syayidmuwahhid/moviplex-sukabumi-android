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
import com.mevy.myapplication.model.TransaksiData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TransaksiAdapter extends RecyclerView.Adapter<TransaksiAdapter.ViewHolder>{
    private List<TransaksiData.Transaksi> dataList;
    private TransaksiAdapter.OnItemClickListener listener;

    public TransaksiAdapter(List<TransaksiData.Transaksi> dataList, TransaksiAdapter.OnItemClickListener listener) {
        this.dataList = dataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TransaksiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_transaksi, parent, false);
        return new TransaksiAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransaksiAdapter.ViewHolder holder, int position) {
        TransaksiData.Transaksi data = dataList.get(position);
        holder.bind(data, listener);
//        holder.textViewName.setText(data); // Atur data ke dalam TextView atau elemen lainnya
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView judul, tanggal, status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            judul = itemView.findViewById(R.id.g_judul); // Sesuaikan dengan ID dari layout item grid
            tanggal = itemView.findViewById(R.id.g_tanggal); // Sesuaikan dengan ID dari layout item grid
            status = itemView.findViewById(R.id.g_status); // Sesuaikan dengan ID dari layout item grid
        }

        public void bind(final TransaksiData.Transaksi data, final TransaksiAdapter.OnItemClickListener listener) {
            judul.setText(data.getJudul());
            tanggal.setText(data.getTanggal());
            status.setText(data.getStatus_bayar());

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
