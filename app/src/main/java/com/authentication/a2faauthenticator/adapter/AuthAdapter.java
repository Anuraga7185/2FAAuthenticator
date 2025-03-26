package com.authentication.a2faauthenticator.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.authentication.a2faauthenticator.databinding.AuthenticatedItemsLayoutBinding;
import com.authentication.a2faauthenticator.entity.AuthenticationDetail;
import com.authentication.a2faauthenticator.util.TOTPUtil;

public class AuthAdapter extends ListAdapter<AuthenticationDetail, AuthAdapter.AuthViewHolder> {

    public AuthAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public AuthViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AuthViewHolder(AuthenticatedItemsLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AuthViewHolder holder, int position) {
        AuthenticationDetail authenticationDetail = getItem(position);
        holder.layoutBinding.totp.setText(TOTPUtil.generateOTP(authenticationDetail.secretKey));
        holder.layoutBinding.issuer.setText(authenticationDetail.displayName);
        authenticationDetail.actionForUpdatingTOTPNTiming(holder.layoutBinding);
    }


    public static class AuthViewHolder extends RecyclerView.ViewHolder {
        public AuthenticatedItemsLayoutBinding layoutBinding;

        public AuthViewHolder(AuthenticatedItemsLayoutBinding binding) {
            super(binding.getRoot());
            layoutBinding = binding;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyItems() {
        notifyDataSetChanged();
    }

    static final DiffUtil.ItemCallback<AuthenticationDetail> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        public boolean areItemsTheSame(@NonNull AuthenticationDetail oldItem, @NonNull AuthenticationDetail newItem) {
            return oldItem.secretKey.equals(newItem.secretKey);
        }

        public boolean areContentsTheSame(@NonNull AuthenticationDetail oldItem, @NonNull AuthenticationDetail newItem) {
            return oldItem.equals(newItem);
        }
    };
}
