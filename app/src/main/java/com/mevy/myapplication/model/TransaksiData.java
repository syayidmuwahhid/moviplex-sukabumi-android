package com.mevy.myapplication.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TransaksiData {
    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private List<Transaksi> transaksi;

    public String getStatus() {
        return status;
    }

    public List<Transaksi> getTransaksi() {
        return transaksi;
    }

    public class Transaksi {
        @SerializedName("id")
        private int id;

        @SerializedName("tanggal")
        private String tanggal;

        @SerializedName("id_jadwal")
        private int id_jadwal;

        @SerializedName("harga_tiket")
        private int harga_tiket;

        @SerializedName("harga_tambahan")
        private int harga_tambahan;

        @SerializedName("jumlah")
        private int jumlah;

        @SerializedName("total")
        private int total;

        @SerializedName("status_bayar")
        private String status_bayar;

        @SerializedName("tanggal_tayang")
        private String tanggal_tayang;

        @SerializedName("jam_tayang")
        private String jam_tayang;

        @SerializedName("judul")
        private String judul;

        @SerializedName("teater")
        private String teater;

        @SerializedName("kursi")
        private ArrayList<String> kursi;

        public int getId() {
            return id;
        }

        public String getTanggal() {
            return tanggal;
        }

        public int getId_jadwal() {
            return id_jadwal;
        }

        public int getHarga_tiket() {
            return harga_tiket;
        }

        public int getHarga_tambahan() {
            return harga_tambahan;
        }

        public int getJumlah() {
            return jumlah;
        }

        public int getTotal() {
            return total;
        }

        public String getStatus_bayar() {
            return status_bayar;
        }

        public String getTanggal_tayang() {
            return tanggal_tayang;
        }

        public String getJam_tayang() {
            return jam_tayang;
        }

        public String getJudul() {
            return judul;
        }

        public String getTeater() {
            return teater;
        }

        public ArrayList<String> getKursi() {
            return kursi;
        }
    }
}
