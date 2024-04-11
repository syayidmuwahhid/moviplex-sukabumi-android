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
import com.mevy.myapplication.model.LoginData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        LoginData loginData = new LoginData(this);

        EditText emailED = findViewById(R.id.editTextEmail);
        EditText passwordED = findViewById(R.id.editTextPassword);
        Button loginButton = findViewById(R.id.buttonLogin);
        TextView register = findViewById(R.id.textViewRegister);

        //cek sudah login
        if (loginData.isLogin()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setEnabled(false);
                String email = emailED.getText().toString();
                String password = passwordED.getText().toString();

                Call<ResponseBody> call = apiService.submitLogin(email, password);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if (response.isSuccessful()) {
                            ResponseBody responseBody = response.body();
                            if (responseBody != null) {
                                try {
                                    Toast.makeText(LoginActivity.this, "Berhasil Login", Toast.LENGTH_LONG).show();
                                    String responseData = responseBody.string();
                                    JSONObject jsonObject = new JSONObject(responseData);
                                    JSONObject data = jsonObject.getJSONObject("data");
                                    String name = data.getString("name");
                                    loginData.saveString(LoginData.KEY_USERNAME, name);
                                    loginData.saveString(LoginData.KEY_EMAIL, email);
                                    loginData.saveString(LoginData.KEY_USERID, data.getString("id"));
                                    loginData.saveBoolean(LoginData.KEY_LOGIN, true);

                                    //pindah main activity
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish(); // Menutup Activity asal setelah ActivityTujuan dimulai
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                loginButton.setEnabled(true);
                                // Tangani jika body response null
                                Toast.makeText(LoginActivity.this, "Tidak Ada Data", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            loginButton.setEnabled(true);
                            try {
                                String errorBody = response.errorBody().string();
                                JSONObject jsonObject = new JSONObject(errorBody);
                                String status = jsonObject.getString("status");
                                String message = jsonObject.getString("message");
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loginButton.setEnabled(true);
                        Toast.makeText(LoginActivity.this, "Koneksi Gagal!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, DaftarActivity.class);
                startActivity(intent);
            }
        });

    }
}