package com.keg.kegutils.View;

import com.keg.kegutils.R;

import java.util.regex.Pattern;

/**
 * Created by gamer on 11/07/2017.
 */

public class Validator {

    public interface IValidator<T> {
        boolean validate(T value);
        int getError();
    }

//    public static IValidator not(final IValidator validator) {
//        return new IValidator<String>() {
//
//            private IValidator _validator = validator;
//
//            @Override
//            public boolean validate(String value) {
//                return !_validator.validate(value);
//            }
//
//            @Override
//            public int getError() {
//                return 0;
//            }
//        };
//    }

    public static IValidator notEmpty() {
        return new IValidator<String>() {

            private final String EMPTY_STRING = "";

            @Override
            public boolean validate(String value) {
                return !EMPTY_STRING.equals(value.trim());
            }

            @Override
            public int getError() {
                return R.string.validator_empty_field;
            }
        };
    }

    public static IValidator atLeast(final int minSize) {
        return new IValidator<String>() {

            private final int minimumSize = minSize;

            @Override
            public boolean validate(String value) {
                return value.trim().length() >= minimumSize;
            }

            @Override
            public int getError() {
                return R.string.validator_character_length_bellow_minimum;
            }
        };
    }

    public static IValidator atMost(final int maxSize) {
        return new IValidator<String>() {

            private final int maximumSize = maxSize;

            @Override
            public boolean validate(String value) {
                return value.trim().length() <= maximumSize;
            }

            @Override
            public int getError() {
                return R.string.validator_character_length_over_maximum;
            }
        };
    }

    public static IValidator between(final int minSize, final int maxSize) {
        return new IValidator<String>() {

            private IValidator atLeast = Validator.atLeast(minSize);
            private IValidator atMost = Validator.atMost(maxSize);

            @Override
            public boolean validate(String value) {
                return atLeast.validate(value) && atMost.validate(value);
            }

            @Override
            public int getError() {
                return R.string.validator_character_length_invalid;
            }
        };
    }

    public static IValidator contains(final String patternRegex, final int err_message) {
        return new IValidator<String>() {

            private int _err_message = err_message;
            private String _patternRegex = patternRegex;

            @Override
            public boolean validate(String value) {
                return Pattern.compile(_patternRegex).matcher(value).find();
            }

            @Override
            public int getError() {
                return _err_message;
            }
        };
    }

    public static IValidator containsDigit() {
        return contains("[0-9]", R.string.validator_must_contain_digit);
    }

    public static IValidator containsLowerCaseLetter() {
        return contains("[a-z]", R.string.validator_must_contain_lowercase_letter);
    }

    public static IValidator containsUpperCaseLetter() {
        return contains("[A-Z]", R.string.validator_must_contain_capital_letter);
    }

    public static IValidator containsLetter() {
        return contains("[A-Za-z]", R.string.validator_must_contain_letter);
    }

    public static IValidator containsSpecialCharacter() {
        return contains("[^a-z0-9 ]", R.string.validator_must_contain_special_character);
    }

}
