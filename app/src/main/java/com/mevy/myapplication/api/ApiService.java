package com.mevy.myapplication.api;

import android.util.Log;

import com.mevy.myapplication.model.FilmData;
import com.mevy.myapplication.model.FilmDetailData;
import com.mevy.myapplication.model.TransaksiData;

import java.lang.reflect.Array;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @FormUrlEncoded
    @POST("api/login") // Sesuaikan dengan endpoint POST Anda
    Call<ResponseBody> submitLogin(@Field("email") String email,
                                 @Field("password") String password);

    @FormUrlEncoded
    @POST("api/daftar") // Sesuaikan dengan endpoint POST Anda
    Call<ResponseBody> submitDaftar(@Field("email") String email,
                                    @Field("password") String password,
                                    @Field("name") String name);

    @GET("api/film/playing")
    Call<FilmData> getFilm();

    @GET("api/film/upcoming")
    Call<FilmData> getFilmUpcoming();

    @FormUrlEncoded
    @POST("api/film/detail") // Sesuaikan dengan endpoint POST Anda
    Call<FilmDetailData> getFilmDetail(@Field("id") int id);

    @FormUrlEncoded
    @POST("api/jadwal") // Sesuaikan dengan endpoint POST Anda
    Call<ResponseBody> getJadwal(@Field("id_film") int id,
                                 @Field("tanggal") String tanggal);

    @FormUrlEncoded
    @POST("api/cek-kursi") // Sesuaikan dengan endpoint POST Anda
    Call<ResponseBody> getKursi(@Field("id_jadwal") int id,
                                 @Field("tanggal") String tanggal);

    @FormUrlEncoded
    @POST("api/beli-tiket") // Sesuaikan dengan endpoint POST Anda
    Call<ResponseBody> submitTiket(@Field("id_jadwal") int id,
                                 @Field("harga_tiket") int harga_tiket,
                                 @Field("harga_tambahan") int harga_tambahan,
                                 @Field("jumlah") int jumlah,
                                 @Field("total") int total,
                                 @Field("user_id") int user_id,
                                 @Field("kursi[]") ArrayList<String> kursi
    );

    @FormUrlEncoded
    @POST("api/detail-transaksi") // Sesuaikan dengan endpoint POST Anda
    Call<ResponseBody> getDetailTransaksi(@Field("id_transaksi") int id);

    @FormUrlEncoded
    @POST("api/riwayat-transaksi")
    Call<TransaksiData> getRiwayatTransaksi(@Field("user_id") int id);

    @FormUrlEncoded
    @POST("api/bayar")
    Call<ResponseBody> submitBayar(@Field("id") int id);
}
