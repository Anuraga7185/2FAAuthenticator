package com.authentication.a2faauthenticator.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.authentication.a2faauthenticator.adapter.AuthAdapter;
import com.authentication.a2faauthenticator.databinding.ActivityOtpGenerateBinding;
import com.authentication.a2faauthenticator.entity.AuthenticationDetail;
import com.authentication.a2faauthenticator.ui.viewmodel.TOTPViewModel;
import com.authentication.a2faauthenticator.util.TOTPUtil;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.List;
import java.util.Map;

public class OTPGenerateActivity extends AppCompatActivity {
    private ActivityOtpGenerateBinding binding;
    private TOTPViewModel viewModel;
    private AuthAdapter adapter;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpGenerateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(TOTPViewModel.class);

        setupRecyclerView();
        observeLiveData();
        viewModel.startTimer();

        binding.floatingBtn.setOnClickListener(this::openQRScanner);
    }

    private void setupRecyclerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        adapter = new AuthAdapter();
        binding.recyclerView.setAdapter(adapter);
    }

    private void observeLiveData() {
        viewModel.getAuthDetails().observe(this, this::updateRecyclerView);
        viewModel.getRemainingTime().observe(this, timeRemaining -> adapter.notifyItems());
    }

    private void updateRecyclerView(List<AuthenticationDetail> authenticationDetail) {
        binding.recyclerView.setVisibility(View.GONE);
        binding.noData.setVisibility(View.GONE);
        if (authenticationDetail == null || authenticationDetail.isEmpty()) {
            binding.noData.setVisibility(View.VISIBLE);
        } else {
            binding.recyclerView.setVisibility(View.VISIBLE);
            adapter.submitList(authenticationDetail);
        }
    }


    private void openQRScanner(View view) {
        barcodeLauncher.launch(new ScanOptions());
    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            Map<String, String> authenticationDetails = TOTPUtil.infoExtract(result.getContents());

            AuthenticationDetail authDetail = new AuthenticationDetail();
            authDetail.displayName = authenticationDetails.getOrDefault("displayName", "");
            authDetail.secretKey = authenticationDetails.getOrDefault("secretKey", "");
            authDetail.issuer = authenticationDetails.getOrDefault("issuer", "");
            authDetail.type = authenticationDetails.getOrDefault("type", "totp");

            if (authDetail.secretKey.isEmpty()) {
                Log.d("OTP GENERATE ACTIVITY", "EMPTY SECRET KEY    ");
                return;
            }
            g
            viewModel.insertAuthDetail(authDetail);
        } else {
            Log.d("OTP GENERATE ACTIVITY", "NO DATA");
        }
    });
}