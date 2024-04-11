package com.mevy.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mevy.myapplication.api.ApiService;
import com.mevy.myapplication.api.RetrofitClient;
import com.mevy.myapplication.model.FilmDetailData;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailUpcomingActivity extends AppCompatActivity {
    ImageView dFoto;
    TextView dJudul, dGenre, dDurasi, dRating, dSinopsis, dSutradara, dProduser, dPenulis, dProduksi, dCast;
    Button trailerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_upcoming);

        dFoto = findViewById(R.id.dFoto);
        dJudul = findViewById(R.id.dJudul);
        dGenre = findViewById(R.id.dGenre);
        dDurasi = findViewById(R.id.dDurasi);
        dRating = findViewById(R.id.dRating);
        dSinopsis = findViewById(R.id.dSinopsis);
        dSutradara = findViewById(R.id.dSutradara);
        dProduser = findViewById(R.id.dProduser);
        dPenulis = findViewById(R.id.dPenulis);
        dProduksi = findViewById(R.id.dCast);
        dCast = findViewById(R.id.dCast);
        trailerBtn = findViewById(R.id.trailerBtn);

        Intent i = getIntent();
        int idFilm = i.getIntExtra("id_film", -1);
        Context mContext = this;

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        Call<FilmDetailData> call = apiService.getFilmDetail(idFilm);
        call.enqueue(new Callback<FilmDetailData>() {
            @Override
            public void onResponse(Call<FilmDetailData> call, Response<FilmDetailData> response) {

                if (response.isSuccessful()) {
                    FilmDetailData responseData = response.body();
                    if (responseData != null && responseData.getFilm() != null) {
                        FilmDetailData.Film film = responseData.getFilm(); // Mendapatkan objek Film dari respons

                        //menampilkan data ke widget
                        Picasso.get().load(RetrofitClient.BASE_URL+film.getFoto()).into(dFoto);
                        dJudul.setText(film.getJudul());
                        dGenre.setText(film.getKategori());
                        dDurasi.setText(film.getDurasi() + " Min");
                        dRating.setText(film.getRatingUsia() + "+");
                        dSinopsis.setText(film.getSinopsis());
                        dSutradara.setText(film.getSutradara());
                        dProduser.setText(film.getProduser());
                        dPenulis.setText(film.getPenulis());
                        dProduksi.setText(film.getProduksi());
                        dCast.setText(film.getCast());

                        trailerBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(film.getTrailer()));
                                startActivity(intent);
                            }
                        });

                    } else {
                        Toast.makeText(DetailUpcomingActivity.this, "Tidak ada data film", Toast.LENGTH_LONG).show();
                    finish();
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorBody);
                        String message = jsonObject.getString("message");
                        Toast.makeText(DetailUpcomingActivity.this, message, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<FilmDetailData> call, Throwable t) {
                Toast.makeText(DetailUpcomingActivity.this, "Koneksi Gagal!", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}