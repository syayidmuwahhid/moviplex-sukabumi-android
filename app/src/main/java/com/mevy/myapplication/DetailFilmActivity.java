package com.mevy.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mevy.myapplication.adapter.FilmAdapter;
import com.mevy.myapplication.api.ApiService;
import com.mevy.myapplication.api.RetrofitClient;
import com.mevy.myapplication.model.FilmData;
import com.mevy.myapplication.model.FilmDetailData;
import com.mevy.myapplication.model.LoginData;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailFilmActivity extends AppCompatActivity {
    ImageView dFoto;
    TextView dJudul, dGenre, dDurasi, dRating, dSinopsis, dSutradara, dProduser, dPenulis, dProduksi, dCast;
    Button trailerBtn, ubahTanggal;
    EditText dTanggal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_film);

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
        dTanggal = findViewById(R.id.dTanggal);
        ubahTanggal = findViewById(R.id.ubahTanggal);

        Intent i = getIntent();
        int idFilm = i.getIntExtra("id_film", -1);
        Context mContext = this;

        dTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        //ambil tanggal hari ini
        Date today = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-dd");
        String tanggalToday = dateFormat.format(today);
        dTanggal.setText(tanggalToday);

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

                        getJadwal(film.getId(), film.getJudul(), mContext);

                        ubahTanggal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getJadwal(film.getId(), film.getJudul(), mContext);
                            }
                        });
                    } else {
                        Toast.makeText(DetailFilmActivity.this, "Tidak ada data film", Toast.LENGTH_LONG).show();
                        finish();
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorBody);
                        String message = jsonObject.getString("message");
                        Toast.makeText(DetailFilmActivity.this, message, Toast.LENGTH_LONG).show();
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
                Toast.makeText(DetailFilmActivity.this, "Koneksi Gagal!", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    void getJadwal(int idFilm, String judul, Context mContext) {
        // Mendapatkan layout tempat Anda ingin menambahkan TextView
        LinearLayout layout = findViewById(R.id.jadwalKontainer);
        layout.removeAllViews(); //menghapus isi layout

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<ResponseBody> call = apiService.getJadwal(idFilm, dTanggal.getText().toString());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        try {
                            String responseData = responseBody.string();
                            JSONObject jsonObject = new JSONObject(responseData);
                            JSONArray dataArray = jsonObject.getJSONArray("data");

                            if (dataArray.length() == 0) {
                                TextView tv = new TextView(mContext);
                                tv.setText("Tidak Ada Jadwal Tersedia");
                                layout.addView(tv);
                            }

                            // Loop melalui setiap elemen array jika perlu
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject data = dataArray.getJSONObject(i);

                                // Membuat objek Button baru
                                Button btn = new Button(mContext);
                                btn.setText(data.getString("jam_mulai"));
                                btn.setId(data.getInt("id"));

                                btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        int id_jadwal = ((Button) v).getId();
                                        Intent i = new Intent(mContext, BeliTiketActivity.class);
                                        i.putExtra("id_jadwal", id_jadwal);
                                        i.putExtra("tanggal", dTanggal.getText().toString());
                                        i.putExtra("judul", judul);
                                        i.putExtra("jam", ((Button) v).getText());
                                        startActivity(i);
                                    }
                                });

                                // Menambahkan TextView ke dalam layout
                                layout.addView(btn);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // Tangani jika body response null
                        Toast.makeText(DetailFilmActivity.this, "Tidak Ada Data", Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorBody);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        Toast.makeText(DetailFilmActivity.this, message, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(DetailFilmActivity.this, "Koneksi Gagal!", Toast.LENGTH_LONG);
            }
        });
    }

    private void showDatePickerDialog() {
        // Mendapatkan tanggal sekarang
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Membuat dialog pemilih tanggal
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = year + "-" + (month + 1) + "-" + dayOfMonth;
                        dTanggal.setText(date);
                    }
                },
                year, month, dayOfMonth);

        datePickerDialog.show();
    }
}