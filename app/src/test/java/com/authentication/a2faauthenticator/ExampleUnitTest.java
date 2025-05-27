package com.authentication.a2faauthenticator;

import org.junit.Test;

import static org.junit.Assert.*;

import com.authentication.a2faauthenticator.util.TOTPUtil;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testGenerateOTP_returnsValidOTP() {
        String secretKey = "JBSWY3DPEHPK3PXP";
        String otp = TOTPUtil.generateOTP(secretKey);
        assertNotNull(otp);
        assertEquals(6, otp.length()); // assuming 6-digit OTP
    }

    @Test
    public void generateOTP_returnsSixDigitNumeric() {
        String secretKey = "JBSWY3DPEHPK3PXP"; // Example base32 secret
        String otp = TOTPUtil.generateOTP(secretKey);

        assertNotNull("OTP should not be null", otp);
        assertEquals("OTP length should be 6", 6, otp.length());
        assertTrue("OTP should be numeric", otp.matches("\\d+"));
    }


}