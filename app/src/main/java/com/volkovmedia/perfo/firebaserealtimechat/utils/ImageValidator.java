package com.volkovmedia.perfo.firebaserealtimechat.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageValidator {

    private Pattern iPattern;
    private Matcher iMatcher;

    private static final String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";

    public ImageValidator() {
        iPattern = Pattern.compile(IMAGE_PATTERN);
    }

    public boolean validate(final String text) {
        iMatcher = iPattern.matcher(text);
        return iMatcher.matches();
    }
}