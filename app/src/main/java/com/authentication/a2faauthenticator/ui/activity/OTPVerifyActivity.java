package com.authentication.a2faauthenticator.ui.activity;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.authentication.a2faauthenticator.R;
import com.authentication.a2faauthenticator.databinding.OtpVerifyActivityBinding;
import com.authentication.a2faauthenticator.util.TOTPUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;


public class OTPVerifyActivity extends AppCompatActivity {
    OtpVerifyActivityBinding otpVerifyActivity;
    public static String SECRET_KEY_VERIFY = "t/nrXLOxTR1bhLTh/fg+KW";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        otpVerifyActivity = OtpVerifyActivityBinding.inflate(getLayoutInflater());
        setContentView(otpVerifyActivity.getRoot());
        otpVerifyActivity.submit.setOnClickListener(this::verify);
        otpVerifyActivity.generateQR.setOnClickListener(this::openQR);
    }


    private void openQR(View view) {
        SECRET_KEY_VERIFY = otpVerifyActivity.editBoxSecretKey.getText().toString();
        generateQRCode();
    }

    private void generateQRCode() {
        Log.d("Generated Secret Key", SECRET_KEY_VERIFY);
        SECRET_KEY_VERIFY = TOTPUtil.textToBase32(otpVerifyActivity.editBoxSecretKey.getText().toString());  // text to base32 or base64
        if (SECRET_KEY_VERIFY.isEmpty()) {
            Toast.makeText(this, "Empty Secret Key", Toast.LENGTH_SHORT).show();
            return;
        }
        String otpFormat = TOTPUtil.constructUriFormat(otpVerifyActivity.editBoxIssuer.getText().toString(), otpVerifyActivity.editBoxDisplayName.getText().toString(), SECRET_KEY_VERIFY);
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        try {
            Bitmap bitmap = barcodeEncoder.encodeBitmap(otpFormat, BarcodeFormat.QR_CODE, 400, 400);
            otpVerifyActivity.qrImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.d("QR GENERATION ->", "Unable to Generate QR Code ");
        }
    }

    private void verify(View view) {
        String otpEntered = otpVerifyActivity.editBox.getText().toString();
        boolean isSuccessful = TOTPUtil.verifyOTP(otpEntered, SECRET_KEY_VERIFY);
        if (isSuccessful) {
            Log.d("VERIFICATION ->", "VERIFIED SUCCSSFULLY");
            Toast.makeText(this, "SUCCESSFULL", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "WRONG OTP", Toast.LENGTH_SHORT).show();
            Log.d("VERIFICATION ->", "WRONG OTP ");
        }
    }
}
