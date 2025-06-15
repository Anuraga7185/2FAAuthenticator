package com.authentication.a2faauthenticator.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;


public class RegistrationUtil {

    String[] existingUsers = {"Rahul", "Ram"};

    public boolean validRegistrationInput(String userName, String password, String confirmPassword) {
        if (userName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) return false;
        String user = Arrays.stream(existingUsers).filter(s -> s.equals(userName)).findAny().orElse(null);
        if (user != null) return false;
        if (!password.equals(confirmPassword)) return false;
        return password.length() >= 2;
    }
}
