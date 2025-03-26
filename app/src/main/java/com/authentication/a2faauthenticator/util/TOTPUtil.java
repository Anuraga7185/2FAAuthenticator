package com.authentication.a2faauthenticator.util;

import android.util.Log;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.binary.Base32;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
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
//            byte[] base64 = base64ToBytes(secret);
            byte[] base64 = base32ToBytes(secret);  // although it is base32 still name set base 64 so not to make some more changes
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
        if (extractedText == null || extractedText.isEmpty()) return details;

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

    public static String textToBase64(String text) {
        Base64.Encoder base64 = Base64.getEncoder();
        String encodedString = base64.encodeToString(text.getBytes());
        Log.d("TOTP UTIL  -> BASE64 ", encodedString);
        return encodedString;
    }

    public static String textToBase32(String text) {
        Base32 base32 = new Base32();
        byte[] encodedBytes = base32.encode(text.getBytes());
        String encodedString = new String(encodedBytes);

        Log.d("TOTP UTIL  -> ", "Encoded Base32: " + encodedString);
        return encodedString;
    }

    private static byte[] base32ToBytes(String base32) {
        Base32 base32Decoder = new Base32();
        return base32Decoder.decode(base32.replace(" ", "").toUpperCase());  // Ensure proper decoding
    }

    public static byte[] base64ToBytes(String base64) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decodedBytes = decoder.decode(base64);
        Log.d("TOTP UTIL  -> BASE64 ", Arrays.toString(decodedBytes) + " ");
        return decodedBytes;
    }
}
