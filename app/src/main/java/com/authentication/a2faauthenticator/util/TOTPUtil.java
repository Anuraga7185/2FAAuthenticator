package com.authentication.a2faauthenticator.util;


import android.util.Log;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import dev.turingcomplete.kotlinonetimepassword.HmacAlgorithm;
import dev.turingcomplete.kotlinonetimepassword.TimeBasedOneTimePasswordConfig;
import dev.turingcomplete.kotlinonetimepassword.TimeBasedOneTimePasswordGenerator;

public class TOTPUtil {
    public static long TIME_STEP = 30;
    public static int CODE_DIGITS = 6;
    public static HmacAlgorithm ALGORITHM = HmacAlgorithm.SHA1;

    public static boolean verifyOTP(String otpEntered, String secretKey) {
        if (otpEntered == null || secretKey == null) return false;
        String generatedOtp = generateOTP(secretKey);
        boolean isValid = otpEntered.equals(generatedOtp);
        Log.d("TOTP UTIL  -> VERIFICATION ", "OTP Verification: " + isValid);
        return isValid;
    }

    public static String generateOTP(String secret) {
        Log.d("TOTP UTIL  -> GENERATE OTP ", secret);
        try {
            byte[] base64 = Base64.getDecoder().decode(secret);  // min sdk-> 26
            TimeBasedOneTimePasswordConfig timeBasedOneTimePasswordConfig = new TimeBasedOneTimePasswordConfig(TIME_STEP, TimeUnit.SECONDS, CODE_DIGITS, ALGORITHM);
            TimeBasedOneTimePasswordGenerator oneTimePasswordGenerator = new TimeBasedOneTimePasswordGenerator(base64, timeBasedOneTimePasswordConfig);
            return oneTimePasswordGenerator.generate(Instant.now());
        } catch (RuntimeException e) {
            Log.e("TOTP UTIL  ->", "Error generating OTP: " + e.getMessage());
        }
        return "";
    }

    public static Map<String, String> infoExtract(String extractedText) {
        Map<String, String> details = new HashMap<>();
        if (extractedText == null || extractedText.isEmpty())
            return details;

        try {
            URI uri = new URI(extractedText);
            String path = uri.getPath();
            String query = uri.getQuery();   //Google:Joe (path)  ---------------- (query)   secret=5xv95timqqmmxxTMsuTcqg==&issuer=Google
            Log.d("TOTP UTIL  -> URI PATH ", path + "   ----------------   " + query);

            details.put("issuer", path.split(":")[0]);
            details.put("displayName", path.substring(1));

            String[] split = query.split("&");
            String[] secret = split[0].split("=", 2);

            details.put("secretKey", secret[1]);

            return details;
        } catch (URISyntaxException e) {
            Log.e("TOTP UTIL  ->", "Invalid OTP URI format: " + e.getMessage());
        }
        return details;
    }

    public static String constructUriFormat(String issuer, String displayName, String secretKey) {
        return String.format("otpauth://%s/%s:%s?secret=%s&issuer=%s", "totp", issuer, displayName, secretKey, issuer);

    }

    //TODO: Generating Secret Key not working perfectly for some reason
    public static String generateSecretKey(String userInput) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] hash = digest.digest(userInput.getBytes(StandardCharsets.UTF_8));
            StringBuilder base64Encoded = new StringBuilder(Base64.getEncoder().encodeToString(hash));
            while (base64Encoded.length() < 32) {
                base64Encoded.append("A");
            }
            return base64Encoded.substring(0, 32);

        } catch (Exception e) {
            Log.e("TOTP UTIL  ->", "Error generating secret key: " + e.getMessage());
        }
        return "";
    }
}
