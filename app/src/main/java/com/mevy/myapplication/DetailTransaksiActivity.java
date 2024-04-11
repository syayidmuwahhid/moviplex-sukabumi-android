package com.mevy.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mevy.myapplication.api.ApiService;
import com.mevy.myapplication.api.RetrofitClient;
import com.mevy.myapplication.receiver.AlarmReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailTransaksiActivity extends AppCompatActivity {
    TextView id_transaksi, tanggal_transaksi, judul_film, tanggal_tayang, jam_tayang, teater,
            harga_tiket, biaya_tambahan, jumlah_tiket, kursi_dipesan, total, status_bayar, tanggal_pembayaran;
    Button bayarBtn;
    String tanggalTayang, jamMulai, namaFilm;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaksi);

        id_transaksi = findViewById(R.id.r_id_transaksi);
        tanggal_transaksi = findViewById(R.id.r_tanggal);
        judul_film = findViewById(R.id.r_judul);
        tanggal_tayang = findViewById(R.id.r_tanggal_tayang);
        jam_tayang = findViewById(R.id.r_jam_tayang);
        teater = findViewById(R.id.r_teater);
        harga_tiket = findViewById(R.id.r_harga);
        biaya_tambahan = findViewById(R.id.r_harga_tambahan);
        jumlah_tiket = findViewById(R.id.r_jumlah_tiket);
        kursi_dipesan = findViewById(R.id.r_kursi);
        total = findViewById(R.id.r_total);
        status_bayar = findViewById(R.id.r_status);
        tanggal_pembayaran = findViewById(R.id.r_tanggal_pembayaran);
        bayarBtn = findViewById(R.id.r_bayar);

        bayarBtn.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        int idTransaksi = intent.getIntExtra("id_transaksi", -1);

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<ResponseBody> call = apiService.getDetailTransaksi(idTransaksi);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        try {
                            String responseData = responseBody.string();
                            JSONObject jsonObject = new JSONObject(responseData);
                            JSONObject data = jsonObject.getJSONObject("data");
                            id_transaksi.setText(": " + data.getString("id"));
                            tanggal_transaksi.setText(": " + data.getString("tanggal"));
                            judul_film.setText(": " + data.getString("judul"));
                            tanggal_tayang.setText(": " + data.getString("tanggal_tayang"));
                            jam_tayang.setText(": " + data.getString("jam_tayang"));
                            teater.setText(": " + data.getString("teater"));
                            harga_tiket.setText(": Rp " + data.getString("harga_tiket"));
                            biaya_tambahan.setText(": Rp " + data.getString("harga_tambahan"));
                            jumlah_tiket.setText(": " + data.getString("jumlah"));
                            kursi_dipesan.setText(": " + data.getString("kursi"));
                            total.setText(": Rp " + data.getString("total"));
                            status_bayar.setText(": " + data.getString("status_bayar"));
                            tanggal_pembayaran.setText(": " + data.getString("tanggal_pembayaran"));
                            tanggalTayang = data.getString("tanggal_tayang");
                            jamMulai = data.getString("jam_mulai");
                            namaFilm = data.getString("judul");

                            //button bayar
                            if (data.getString("status_bayar").equalsIgnoreCase("belum bayar")) {
                                bayarBtn.setVisibility(View.VISIBLE);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // Tangani jika body response null
                        Toast.makeText(DetailTransaksiActivity.this, "Tidak Ada Data", Toast.LENGTH_LONG).show();
                        finish();
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorBody);
                        String message = jsonObject.getString("message");
                        Toast.makeText(DetailTransaksiActivity.this, message, Toast.LENGTH_LONG).show();
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
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(DetailTransaksiActivity.this, "Koneksi Gagal!", Toast.LENGTH_LONG);
                finish();
            }
        });

        bayarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailTransaksiActivity.this);
                builder.setTitle("Konfirmasi");
                builder.setMessage("Apakah Anda yakin ingin melakukan pembayaran?");

                // Menambahkan tombol "Ya"
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Tambahkan logika untuk pembayaran di sini
                        // Misalnya, memanggil metode untuk melakukan pembayaran
                        // misalnya, doPayment();
                        doBayar(idTransaksi);
                    }
                });

                // Menambahkan tombol "Tidak"
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Kosongkan jika tidak ingin melakukan apa-apa saat tombol "Tidak" diklik
                        dialog.dismiss(); // Menutup dialog
                    }
                });

                // Membuat dan menampilkan dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void doBayar(int idTransaksi) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<ResponseBody> call = apiService.submitBayar(idTransaksi);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        try {
                            String responseData = responseBody.string();
                            JSONObject jsonObject = new JSONObject(responseData);
                            Toast.makeText(DetailTransaksiActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            setAlarm();

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // Tangani jika body response null
                        Toast.makeText(DetailTransaksiActivity.this, "Tidak Ada Data", Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorBody);
                        String message = jsonObject.getString("message");
                        Toast.makeText(DetailTransaksiActivity.this, message, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(DetailTransaksiActivity.this, "Koneksi Gagal!", Toast.LENGTH_LONG);
            }
        });

        //merefresh page
        Intent intent = new Intent(DetailTransaksiActivity.this, DetailTransaksiActivity.class);
        intent.putExtra("id_transaksi", idTransaksi);
        startActivity(intent);
        finish();
    }

    private void setAlarm() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date tanggal = null, waktu = null;

        try {
            tanggal = dateFormat.parse(tanggalTayang);
            waktu = timeFormat.parse(jamMulai);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();

        // Mengatur waktu dalam format 24 jam
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
        calendar.setTime(tanggal); // Mengatur tanggal
        calendar.set(Calendar.HOUR_OF_DAY, waktu.getHours());
        calendar.set(Calendar.MINUTE, waktu.getMinutes());
        calendar.set(Calendar.SECOND, waktu.getSeconds());
        calendar.add(Calendar.MINUTE, -15); // mengurangi 15 menit lebih awal

        // Menggunakan intent untuk memanggil AlarmReceiver
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("nama_film", namaFilm);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long time = calendar.getTimeInMillis();

        // Memeriksa apakah waktu yang ditetapkan telah berlalu, jika ya, atur untuk keesokan harinya
        if (System.currentTimeMillis() > time) {
            // Tambahkan satu hari
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        // Mengatur alarm dengan menggunakan setRepeating agar berulang pada waktu yang ditentukan
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, 1000, pendingIntent);
        SimpleDateFormat alarmDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Toast.makeText(DetailTransaksiActivity.this, "Alarm dijadwalkan 15 menit sebelum film dimulai : " + alarmDateTime.format(calendar.getTime()) , Toast.LENGTH_LONG).show();
    }
}