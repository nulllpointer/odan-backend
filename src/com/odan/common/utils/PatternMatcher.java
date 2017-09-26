package com.odan.common.utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternMatcher {
    public static Matcher numberRangeMatch(String input) {
        Pattern pattern = Pattern.compile("\\[(\\d+|\\d+.\\d+),(\\d+|\\d+.\\d+)\\]$");
        Matcher matcher = pattern.matcher(input);
        return matcher;
    }

    public static Matcher dateRangeMatch(String input) {
        Pattern pattern = Pattern.compile("\\[(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3}-\\d{2}:\\d{2}),(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3}-\\d{2}:\\d{2})\\]$");
        Matcher matcher = pattern.matcher(input);
        return matcher;
    }



    public static Matcher dateFormatMatch(String input) {
        Pattern pattern = Pattern.compile("(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3}-\\d{2}:\\d{2})$");
        Matcher matcher = pattern.matcher(input);
        return matcher;
    }

    public static Matcher keyMatch(String input) {
        Pattern pattern = Pattern.compile("([a-zA-z-]+)-(lt-eq|gt-eq|lt|gt)$");
        Matcher matcher = pattern.matcher(input);
        return matcher;
    }
    public static Matcher startsWithMatch(String input) {
        Pattern pattern = Pattern.compile("([a-zA-z-]+)-(starts-with)$");
        Matcher matcher = pattern.matcher(input);
        return matcher;
    }
    public static Matcher endsWithMatch(String input) {
        Pattern pattern = Pattern.compile("([a-zA-z-]+)-(ends-with)$");
        Matcher matcher = pattern.matcher(input);
        return matcher;
    }
    public static Matcher containsMatch(String input) {
        Pattern pattern = Pattern.compile("(^[a-zA-Z]+)-(contains)$");
        Matcher matcher = pattern.matcher(input);
        return matcher;
    }
    public static Matcher notContainMatch(String input) {
        Pattern pattern = Pattern.compile("([a-zA-z-]+)(-not-contains)$");
        Matcher matcher = pattern.matcher(input);
        return matcher;
    }
    public static Matcher emptyMatch(String input) {
        Pattern pattern = Pattern.compile("^([a-zA-z]+)-(empty)$");
        Matcher matcher = pattern.matcher(input);
        return matcher;
    }
    public static Matcher nonEmptyMatch(String input) {
        Pattern pattern = Pattern.compile("([a-zA-z]+)-(non-empty)$");
        Matcher matcher = pattern.matcher(input);
        return matcher;
    }
    public static Matcher specificDateMatch(String input) {
        Pattern pattern = Pattern.compile("(last_month|last_week|yesterday|today|tomorrow|next_week|next_month|this_month|this_week)$");
        Matcher matcher = pattern.matcher(input);
        return matcher;
    }
    public static Matcher isNotEqualMatch(String input) {
        Pattern pattern = Pattern.compile("([a-zA-z-]+)-is-not=([a-zA-z-]+)$");
        Matcher matcher = pattern.matcher(input);
        return matcher;
    }


}