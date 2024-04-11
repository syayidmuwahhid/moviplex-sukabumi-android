package com.mevy.myapplication.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FilmDetailData {
    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private Film film;

    public String getStatus() {
        return status;
    }

    public Film getFilm() {
        return film;
    }

    public class Film {
        @SerializedName("id")
        private int id;

        @SerializedName("judul")
        private String judul;

        @SerializedName("kategori")
        private String kategori;

        @SerializedName("durasi")
        private int durasi;

        @SerializedName("rating_usia")
        private int ratingUsia;

        @SerializedName("sinopsis")
        private String sinopsis;

        @SerializedName("produser")
        private String produser;

        @SerializedName("sutradara")
        private String sutradara;

        @SerializedName("penulis")
        private String penulis;

        @SerializedName("produksi")
        private String produksi;

        @SerializedName("cast")
        private String cast;

        @SerializedName("foto")
        private String foto;

        @SerializedName("trailer")
        private String trailer;

        @SerializedName("tanggal_awal")
        private String tanggalAwal;

        @SerializedName("tanggal_akhir")
        private String tanggalAkhir;

        @SerializedName("created_at")
        private String created_at;

        public int getId() {
            return id;
        }

        public String getJudul() {
            return judul;
        }

        public String getKategori() {
            return kategori;
        }

        public int getDurasi() {
            return durasi;
        }

        public int getRatingUsia() {
            return ratingUsia;
        }

        public String getSinopsis() {
            return sinopsis;
        }

        public String getProduser() {
            return produser;
        }

        public String getSutradara() {
            return sutradara;
        }

        public String getPenulis() {
            return penulis;
        }

        public String getProduksi() {
            return produksi;
        }

        public String getCast() {
            return cast;
        }

        public String getFoto() {
            return foto;
        }

        public String getTrailer() {
            return trailer;
        }

        public String getTanggalAwal() {
            return tanggalAwal;
        }

        public String getTanggalAkhir() {
            return tanggalAkhir;
        }

        public String getCreated_at() {
            return created_at;
        }
    }

}
