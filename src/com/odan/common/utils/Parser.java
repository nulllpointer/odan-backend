package com.odan.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Parser {
    public static Timestamp convertObjectToTimestamp(Object input) throws ParseException {
        Timestamp timestamp = null;
        if (input != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = (Date) dateFormat.parse((String) input);
            timestamp = new Timestamp(parsedDate.getTime());
        }
        return timestamp;
    }

    public static Date convertObjectToDate(Object input) throws ParseException {
        Date parsedDate = null;
        if (input != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            parsedDate = (Date) dateFormat.parse((String) input);

        }
        return parsedDate;
    }

    public static BigInteger convertObjectToBigInteger(Object input) {
        BigInteger number = null;
        if (input != null) {
            String str = input.toString();
            number = new BigInteger(str);
        }
        return number;
    }


    public static Integer convertObjectToInteger(Object input) {
        Integer number = null;
        if (input != null) {
            String str = input.toString();
            number = Integer.parseInt(str);
        }
        return number;
    }

    public static Long convertObjectToLong(Object input) {
        Long number = null;
        if (input != null) {
            String str = input.toString();
            number = Long.parseLong(str);
        }
        return number;
    }

    public static Boolean convertObjectToBoolean(Object input) {
        Boolean val = null;
        if (input != null) {
            String str = input.toString();
            val = Boolean.parseBoolean(str);
        }
        return val;
    }

    public static List<Long> convertObjectToLongList(Object input) {
        List<Long> array = new ArrayList<Long>();
        if (input != null) {
            System.out.println("TE");
            System.out.println(input);
            List<Object> inputList = (List<Object>) input;
            System.out.println("D");
            System.out.println(inputList.size());
            System.out.println("------");
            for (Object inputItem : inputList) {
                array.add(Long.parseLong(inputItem.toString()));
            }
        }
        return array;
    }

    public static Double convertObjectToDouble(Object input) {
        Double number = null;
        if (input != null) {
            String str = input.toString();
            number = Double.parseDouble(str);
        }
        return number;
    }

    public static Byte convertObjectToByte(Object input) {
        Byte number = null;
        if (input != null) {
            String str = input.toString();
            number = Byte.parseByte(str);
        }
        return number;
    }

    public static List<String> csvToList(Object input) {
        if (input == null) return null;

        String str = (String) input;
        List<String> output = Arrays.asList(str.split(","));
        return output;
    }

    public static String satos(Object input) {
        String str = null;
        String[] strArr = (String[]) input;
        if (strArr.length > 0) {
            str = strArr[0];
        }
        System.out.println(str);
        return str;
    }

    public static List<String> csvToArrayList(String input) {
        List<String> items = Arrays.asList(input.split("\\s*,\\s*"));
        return items;
    }


    public static String getOperator(String operator) {
        if (operator.equals("gt-eq")) {
            return ">=";
        } else if (operator.equals("lt-eq")) {
            return "<=";
        } else if (operator.equals("gt")) {
            return ">";
        } else if (operator.equals("lt")) {
            return "<";
        } else return "";
    }

    //remove createdAt,deletedAt in json response so as to match actual rew3 response
    public static Object convert(List<Object> o) {
        String s = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            s = mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Object maps = null;
        try {
            maps = mapper.readValue(s, new TypeReference<List<HashMap<String, Object>>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return maps;
    }

    public static Object convert(Object o) {
        String s = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            s = mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Object map = null;
        try {
            map = mapper.readValue(s, new TypeReference<HashMap<String, Object>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }



}