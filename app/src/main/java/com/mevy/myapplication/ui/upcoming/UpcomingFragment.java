package com.mevy.myapplication.ui.upcoming;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.mevy.myapplication.DetailUpcomingActivity;
import com.mevy.myapplication.LoginActivity;
import com.mevy.myapplication.R;
import com.mevy.myapplication.adapter.FilmAdapter;
import com.mevy.myapplication.adapter.FilmUpcomingAdapter;
import com.mevy.myapplication.api.ApiService;
import com.mevy.myapplication.api.RetrofitClient;
import com.mevy.myapplication.databinding.FragmentNotificationsBinding;
import com.mevy.myapplication.databinding.FragmentUpcomingBinding;
import com.mevy.myapplication.model.FilmData;
import com.mevy.myapplication.model.LoginData;
import com.mevy.myapplication.ui.notifications.NotificationsViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpcomingFragment extends Fragment {
    private RecyclerView recyclerView;

    private FilmUpcomingAdapter gridAdapter;
    private FragmentUpcomingBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentUpcomingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        Call<FilmData> call = apiService.getFilmUpcoming();
        call.enqueue(new Callback<FilmData>() {
            @Override
            public void onResponse(Call<FilmData> call, Response<FilmData> response) {

                if (response.isSuccessful()) {
                    FilmData responseData = response.body();
                    List<FilmData.Film> filmList = responseData.getFilm();

                    recyclerView = root.findViewById(R.id.recyclerUpcoming);
                    recyclerView.setLayoutManager(new GridLayoutManager(root.getContext(), 2));

                    gridAdapter = new FilmUpcomingAdapter(filmList, new FilmUpcomingAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int itemId) {
                            Intent i = new Intent(root.getContext(), DetailUpcomingActivity.class);
                            i.putExtra("id_film", itemId);
                            startActivity(i);
                        }
                    });
                    recyclerView.setAdapter(gridAdapter);
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorBody);
                        String message = jsonObject.getString("message");
                        Toast.makeText(root.getContext(), message, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<FilmData> call, Throwable t) {
                System.out.println("gagal");
                Toast.makeText(root.getContext(), "Koneksi Gagal!", Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
