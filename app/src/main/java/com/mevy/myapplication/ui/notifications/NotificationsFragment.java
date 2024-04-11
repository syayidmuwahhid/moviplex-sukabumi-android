package com.mevy.myapplication.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mevy.myapplication.LoginActivity;
import com.mevy.myapplication.MainActivity;
import com.mevy.myapplication.R;
import com.mevy.myapplication.databinding.FragmentNotificationsBinding;
import com.mevy.myapplication.model.LoginData;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        LoginData loginData = new LoginData(root.getContext());

        TextView username = root.findViewById(R.id.loginUsername);
        TextView email = root.findViewById(R.id.loginEmail);
        Button logoutBtn = root.findViewById(R.id.logoutBtn);

        username.setText(loginData.getName());
        email.setText(loginData.getEmail());

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginData.logout();
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
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