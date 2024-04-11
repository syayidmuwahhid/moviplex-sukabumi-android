package com.mevy.myapplication.ui.dashboard;

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

import com.mevy.myapplication.DetailTransaksiActivity;
import com.mevy.myapplication.R;
import com.mevy.myapplication.adapter.TransaksiAdapter;
import com.mevy.myapplication.api.ApiService;
import com.mevy.myapplication.api.RetrofitClient;
import com.mevy.myapplication.databinding.FragmentDashboardBinding;
import com.mevy.myapplication.model.LoginData;
import com.mevy.myapplication.model.TransaksiData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {
    private RecyclerView recyclerView;
    private TransaksiAdapter adapter;
    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        LoginData loginData = new LoginData(root.getContext());

        Call<TransaksiData> call = apiService.getRiwayatTransaksi(Integer.parseInt(loginData.getUserID()));
        call.enqueue(new Callback<TransaksiData>() {
            @Override
            public void onResponse(Call<TransaksiData> call, Response<TransaksiData> response) {

                if (response.isSuccessful()) {
                    TransaksiData responseData = response.body();
                    List<TransaksiData.Transaksi> transaksiList = responseData.getTransaksi();
                    System.out.println(responseData.getStatus());
                    recyclerView = root.findViewById(R.id.recyclerTransaksi);

                    adapter = new TransaksiAdapter(transaksiList, new TransaksiAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int itemId) {
                            Intent i = new Intent(root.getContext(), DetailTransaksiActivity.class);
                            i.putExtra("id_transaksi", itemId);
                            startActivity(i);
                        }
                    });
                    recyclerView.setAdapter(adapter);
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
            public void onFailure(Call<TransaksiData> call, Throwable t) {
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