package com.mevy.myapplication.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mevy.myapplication.DetailFilmActivity;
import com.mevy.myapplication.R;
import com.mevy.myapplication.adapter.FilmAdapter;
import com.mevy.myapplication.api.ApiService;
import com.mevy.myapplication.api.RetrofitClient;
import com.mevy.myapplication.databinding.FragmentHomeBinding;
import com.mevy.myapplication.model.FilmData;
import com.mevy.myapplication.model.LoginData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private FilmAdapter gridAdapter;

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        Call<FilmData> call = apiService.getFilm();
        call.enqueue(new Callback<FilmData>() {
            @Override
            public void onResponse(Call<FilmData> call, Response<FilmData> response) {

                if (response.isSuccessful()) {
                    FilmData responseData = response.body();
                    List<FilmData.Film> filmList = responseData.getFilm();

                    recyclerView = root.findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new GridLayoutManager(root.getContext(), 2));

                    gridAdapter = new FilmAdapter(filmList, new FilmAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int itemId) {
                            Intent i = new Intent(root.getContext(), DetailFilmActivity.class);
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