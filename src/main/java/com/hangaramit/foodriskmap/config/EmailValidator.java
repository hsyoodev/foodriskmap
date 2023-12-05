package com.hangaramit.foodriskmap.config;

import org.springframework.context.annotation.Configuration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
public class EmailValidator {
    private String emailRegex = "^[a-z0-9]+@[a-z]+\\.[a-z]{2,3}$";
    private Pattern pattern = Pattern.compile(emailRegex);

    public boolean isValidEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}

