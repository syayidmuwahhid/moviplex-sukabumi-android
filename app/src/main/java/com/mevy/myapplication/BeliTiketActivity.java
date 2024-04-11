package com.mevy.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mevy.myapplication.api.ApiService;
import com.mevy.myapplication.api.RetrofitClient;
import com.mevy.myapplication.model.LoginData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BeliTiketActivity extends AppCompatActivity {

    int hargaTiket, hargaTambahan, totalBayar, jumlahTiket;
    private List<Spinner> spinnerList;

    TextView Tvjudul, Tvjam, TvHarga, TvHargaAF, Tvtotal;
    EditText edJmlTiket;
    Button btnKursi, btnCheckout;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beli_tiket);

        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        LinearLayout layout = findViewById(R.id.kursiKontainer);
        layout.removeAllViews(); //menghapus isi layout

        String Kursi[] = {
                "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "A10", "A11",
                "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8", "B9", "B10", "B11",
                "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "C10", "C11",
                "D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9", "D10", "D11",
                "E1", "E2", "E3", "E4", "E5", "E6", "E7", "E8", "E9", "E10", "E11"
        };

        // Konversi array ke ArrayList
        ArrayList<String> listKursi = new ArrayList<>(Arrays.asList(Kursi));
        spinnerList = new ArrayList<>();

        Intent intent = getIntent();

        int id_jadwal = intent.getIntExtra("id_jadwal", -1);
        String tanggal = intent.getStringExtra("tanggal");
        String judul = intent.getStringExtra("judul");
        String jam = intent.getStringExtra("jam");

        Tvjudul = findViewById(R.id.tiket_judul);
        Tvjam = findViewById(R.id.tiket_jam);
        edJmlTiket = findViewById(R.id.jml_tiket);
        btnKursi = findViewById(R.id.pilihKursi);
        TvHarga = findViewById(R.id.tiket_harga);
        TvHargaAF = findViewById(R.id.tiket_hargaAF);
        btnCheckout = findViewById(R.id.btnCheckout);
        Tvtotal = findViewById(R.id.totalBayar);

        Tvjudul.setText(judul);
        Tvjam.setText(tanggal + " " +jam);

        Call<ResponseBody> call = apiService.getKursi(id_jadwal, tanggal);
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
                            hargaTiket = Integer.parseInt(data.getString("harga_tiket"));
                            hargaTambahan = Integer.parseInt(data.getString("harga_tambahan"));
                            JSONArray kursiDipesan = data.getJSONArray("kursi_dipesan");

                            TvHarga.setText("Harga per tiket: IDR " + hargaTiket);
                            TvHargaAF.setText("Additional Fee: IDR " + hargaTambahan);

                            //menghapus kursi yang sudah di pesan
                            for (int j = 0; j < kursiDipesan.length(); j++) {
                                try {
                                    listKursi.remove(kursiDipesan.getString(j));
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                            btnKursi.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    edJmlTiket.setEnabled(false);
                                    btnKursi.setEnabled(false);
                                    btnCheckout.setEnabled(true);

                                    jumlahTiket = Integer.parseInt(edJmlTiket.getText().toString());
                                    totalBayar = (hargaTiket * jumlahTiket) + hargaTambahan;
                                    Tvtotal.setText("Total Bayar : IDR " + totalBayar);

                                    for (int i = 0; i < jumlahTiket; i++) {

                                        Spinner newSpinner = new Spinner(BeliTiketActivity.this);
                                        ArrayAdapter<String> adapter = new ArrayAdapter<>(BeliTiketActivity.this, android.R.layout.simple_spinner_item, listKursi);
                                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        newSpinner.setAdapter(adapter);

                                        int spinnerId = View.generateViewId();
                                        newSpinner.setId(spinnerId);
                                        layout.addView(newSpinner);
                                        spinnerList.add(newSpinner);
                                    }

                                    btnCheckout.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //validasi duplikasi spinner
                                            Set<String> selectedSeats = new HashSet<>();
                                            boolean hasDuplicate = false;

                                            // Memeriksa kursi yang dipilih dalam setiap spinner
                                            for (Spinner spinner : spinnerList) {
                                                String selectedSeat = spinner.getSelectedItem().toString();

                                                // Memeriksa apakah kursi telah dipilih sebelumnya
                                                if (!selectedSeats.add(selectedSeat)) {
                                                    hasDuplicate = true;
                                                    break; // Ada duplikat, keluar dari loop
                                                }
                                            }

                                            if (hasDuplicate) {
                                                // Menampilkan pesan bahwa ada kursi yang duplikat
                                                Toast.makeText(BeliTiketActivity.this, "Ada kursi yang duplikat, harap pilih kursi yang berbeda.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                // Lanjutkan dengan proses checkout karena tidak ada kursi yang duplikat
                                                submitBeliTiket(id_jadwal, tanggal, selectedSeats);
                                            }
                                        }
                                    });
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // Tangani jika body response null
                        Toast.makeText(BeliTiketActivity.this, "Tidak Ada Data", Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorBody);
                        String message = jsonObject.getString("message");
                        Toast.makeText(BeliTiketActivity.this, message, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(BeliTiketActivity.this, "Koneksi Gagal!", Toast.LENGTH_LONG);
            }
        });

    }

    public void submitBeliTiket(int id_jadwal, String tanggalTiket, Set<String> selectedSeats) {
        LoginData loginData = new LoginData(this);
        Call<ResponseBody> call = apiService.submitTiket(id_jadwal, hargaTiket, hargaTambahan, jumlahTiket, totalBayar, Integer.parseInt(loginData.getUserID()), new ArrayList<>(selectedSeats));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        try {
                            String responseData = responseBody.string();
                            JSONObject jsonObject = new JSONObject(responseData);
                            String message = jsonObject.getString("message");
                            int id_transaksi = Integer.parseInt(jsonObject.getString("id_transaksi"));

                            Toast.makeText(BeliTiketActivity.this, message, Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(BeliTiketActivity.this, DetailTransaksiActivity.class);
                            intent.putExtra("id_transaksi", id_transaksi);

                            startActivity(intent);
                            finish();

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // Tangani jika body response null
                        Toast.makeText(BeliTiketActivity.this, "Tidak Ada Data", Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorBody);
                        String message = jsonObject.getString("message");
                        Toast.makeText(BeliTiketActivity.this, message, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(BeliTiketActivity.this, "Koneksi Gagal!", Toast.LENGTH_LONG);
            }
        });
    }
}