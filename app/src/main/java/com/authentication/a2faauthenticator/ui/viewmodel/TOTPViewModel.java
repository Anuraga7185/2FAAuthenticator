package com.authentication.a2faauthenticator.ui.viewmodel;


import android.app.Application;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.authentication.a2faauthenticator.db.AuthDatabase;
import com.authentication.a2faauthenticator.entity.AuthenticationDetail;


import java.util.List;
import java.util.concurrent.Executors;

public class TOTPViewModel extends AndroidViewModel {
    private final MutableLiveData<List<AuthenticationDetail>> authDetails = new MutableLiveData<>();
    private final MutableLiveData<Long> remainingTime = new MutableLiveData<>(30L);
    private CountDownTimer timer;
    private final AuthDatabase database;

    public TOTPViewModel(@NonNull Application application) {
        super(application);
        database = AuthDatabase.getInstance(application);
        fetchAuthDetails();
    }

    private void fetchAuthDetails() {
        database.authDao().getAllAccounts().observeForever(authDetails::setValue);
    }

    public MutableLiveData<List<AuthenticationDetail>> getAuthDetails() {  //TODO: Use this instead of finding list from db
        return authDetails;
    }

    public LiveData<Long> getRemainingTime() {
        return remainingTime;
    }

    public void setAuthDetails(List<AuthenticationDetail> authDetails) {
        this.authDetails.setValue(authDetails);
    }

    public void setRemainingTime(Long remainingTime) {
        this.remainingTime.setValue(remainingTime);
    }

    public void insertAuthDetail(AuthenticationDetail authenticationDetail) {
        Executors.newSingleThreadExecutor().execute(() -> {
            AuthenticationDetail existingAccount = database.authDao().checkExistence(authenticationDetail.secretKey);
            if (existingAccount == null) {
                database.authDao().insert(authenticationDetail);
            }
        });
    }

    public void startTimer() {
        if (timer != null) {  // Cancel existing timer if already running
            timer.cancel();
        }
        timer = new CountDownTimer(30 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                setRemainingTime(millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                startTimer();
            }

        }.start();
    }

}
