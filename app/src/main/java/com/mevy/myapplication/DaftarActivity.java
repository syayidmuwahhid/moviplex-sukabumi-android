package com.mevy.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mevy.myapplication.api.ApiService;
import com.mevy.myapplication.api.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DaftarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        EditText edEmail = findViewById(R.id.editTextNewEmail);
        EditText edUsername = findViewById(R.id.editTextNewUsername);
        EditText edPassword = findViewById(R.id.editTextNewPassword);
        Button daftar = findViewById(R.id.buttonRegister);
        TextView login = findViewById(R.id.textViewLogin);

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daftar.setEnabled(false);
                String email = edEmail.getText().toString();
                String password = edPassword.getText().toString();
                String username = edUsername.getText().toString();

                Call<ResponseBody> call = apiService.submitDaftar(email, password, username);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if (response.isSuccessful()) {
                            Toast.makeText(DaftarActivity.this, "Daftar Berhasil, Silakan Login!", Toast.LENGTH_LONG).show();

                            //pindah login activity
                            finish(); // Menutup Activity asal setelah ActivityTujuan dimulai
                        } else {
                            daftar.setEnabled(true);
                            try {
                                String errorBody = response.errorBody().string();
                                JSONObject jsonObject = new JSONObject(errorBody);
                                String message = jsonObject.getString("message");
                                Toast.makeText(DaftarActivity.this, message, Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        daftar.setEnabled(true);
                        Toast.makeText(DaftarActivity.this, "Koneksi Gagal!", Toast.LENGTH_LONG);
                    }
                });
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}