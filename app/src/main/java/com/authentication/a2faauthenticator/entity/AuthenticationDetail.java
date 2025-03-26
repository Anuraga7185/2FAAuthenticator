package com.authentication.a2faauthenticator.entity;

import android.os.CountDownTimer;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.authentication.a2faauthenticator.databinding.AuthenticatedItemsLayoutBinding;

import java.util.Objects;

@Entity(tableName = "auth_creds")
public class AuthenticationDetail {
    @PrimaryKey(autoGenerate = true)
    public long primaryKey;
    public String displayName;
    public String secretKey;
    public String issuer;
    public String type;

    @Ignore
    private CountDownTimer countDownTimer = null;

    public void actionForUpdatingTOTPNTiming(AuthenticatedItemsLayoutBinding itemsLayoutBinding) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        long currSecond = System.currentTimeMillis() / 1000;
        long remainingSeconds = 30 - (currSecond % 30);

        itemsLayoutBinding.circularProgressIndicator.setMax(30);
        itemsLayoutBinding.circularProgressIndicator.setProgress((int) remainingSeconds, true);

        countDownTimer = new CountDownTimer(remainingSeconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsLeft = millisUntilFinished / 1000;
                itemsLayoutBinding.circularProgressIndicator.setProgress((int) (30 - secondsLeft), true);
                itemsLayoutBinding.issuer.post(() -> itemsLayoutBinding.issuer.setText(displayName));
            }

            @Override
            public void onFinish() {
                actionForUpdatingTOTPNTiming(itemsLayoutBinding);  // Restart timer
            }
        };

        countDownTimer.start();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthenticationDetail that = (AuthenticationDetail) o;
        return primaryKey == that.primaryKey &&
                Objects.equals(displayName, that.displayName) &&
                Objects.equals(secretKey, that.secretKey) &&
                Objects.equals(issuer, that.issuer) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(primaryKey, displayName, secretKey, issuer, type);
    }

}
