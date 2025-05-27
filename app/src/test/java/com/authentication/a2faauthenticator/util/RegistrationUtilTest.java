package com.authentication.a2faauthenticator.util;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

public class RegistrationUtilTest {
    RegistrationUtil registrationUtil = new RegistrationUtil();

    @Test
    public void valid_registration() {
        // Test with valid, unique username, matching passwords of sufficient length.
        // TODO implement test

        boolean res = registrationUtil.validRegistrationInput("RahulDas", "password", "password");
        assertThat(res).isTrue();
    }

    @Test
    public void empty_username() {
        // Test with an empty username string.
        // TODO implement test
        boolean res = registrationUtil.validRegistrationInput("", "password", "password");
        assertThat(res).isFalse();
    }

    @Test
    public void empty_password() {
        // Test with an empty password string.
        // TODO implement test
        boolean res = registrationUtil.validRegistrationInput("Rahul", "", "password");
        assertThat(res).isFalse();    }

    @Test
    public void empty_confirm_password() {
        // Test with an empty confirm password string.
        // TODO implement test
        boolean res = registrationUtil.validRegistrationInput("Rahul", "password", "");
        assertThat(res).isFalse();
    }

    @Test
    public void all_fields_empty() {
        // Test with all input fields (username, password, confirmPassword) being empty.
        // TODO implement test
        boolean res = registrationUtil.validRegistrationInput("", "", "");
        assertThat(res).isFalse();
    }

    @Test
    public void existing_username() {
        // Test with a username that already exists in 'existingUsers' (e.g., 'Rahul').
        // TODO implement test
        boolean res = registrationUtil.validRegistrationInput("Rahul", "password", "password");
        assertThat(res).isFalse();
    }

    @Test
    public void existing_username_case_sensitive() {

        // TODO implement test
        boolean res = registrationUtil.validRegistrationInput("Rahul", "password", "password");
        assertThat(res).isFalse();
    }

    @Test
    public void passwords_do_not_match() {
        // Test with password and confirmPassword being different.
        // TODO implement test
        boolean res = registrationUtil.validRegistrationInput("Rahul", "password1", "password");
        assertThat(res).isFalse();
    }

    @Test
    public void password_too_short__less_than_2_chars_() {
        // Test with a password that is shorter than the minimum required length (e.g., 1 character).
        // TODO implement test
        boolean res = registrationUtil.validRegistrationInput("Rahul", "p", "p");
        assertThat(res).isFalse();
    }

    @Test
    public void password_minimum_length__exactly_2_chars_() {
        // Test with a password that is exactly the minimum required length (2 characters).
        // TODO implement test
        boolean res = registrationUtil.validRegistrationInput("RahulRam", "pa", "pa");
        assertThat(res).isTrue();
    }

    @Test
    public void password_longer_than_minimum_length() {
        // Test with a password that is longer than the minimum required length (e.g., 10 characters).
        // TODO implement test
        boolean res = registrationUtil.validRegistrationInput("RahulRam", "pass", "pass");
        assertThat(res).isTrue();
    }

    @Test
    public void username_with_special_characters() {
        // Test with a username containing special characters (e.g., 'user@name').
        // Although not explicitly handled, it's good to see behavior.
        // TODO implement test
        boolean res = registrationUtil.validRegistrationInput("Rahul@", "pass", "pass");
        assertThat(res).isTrue();
    }

    @Test
    public void password_with_special_characters() {
        // Test with a password containing special characters (e.g., 'p@$$wOrd').
        // TODO implement test
        boolean res = registrationUtil.validRegistrationInput("RahulRam", "pass@", "pass@");
        assertThat(res).isTrue();
    }

    @Test
    public void username_with_leading_trailing_spaces() {
        // Test with a username that has leading or trailing spaces (e.g., '  newUser  ').
        // The current implementation would treat this as a unique user if not trimmed before check.
        // TODO implement test
        boolean res = registrationUtil.validRegistrationInput("Rahul", "pass ", "pass ");
        assertThat(res).isFalse();
    }
}