package com.rikyahmadfathoni.test.weatherapp.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UtilsInteger {

    public static boolean equals(int a, int b) {
        return (a == b);
    }

    public static int validateNull(Integer value) {
        return value != null ? value : 0;
    }

    @Nullable
    public static Integer parseInt(Object value) {
        try {
            if (value instanceof String) {
                return Integer.parseInt((String) value);
            } else if (value instanceof Integer) {
                return (Integer) value;
            }
            return (int) value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static Double parseDouble(Object value) {
        try {
            if (value instanceof String) {
                return Double.parseDouble((String) value);
            } else if (value instanceof Double) {
                return (Double) value;
            }
            return (double) value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static Long parseLong(Object value) {
        try {
            if (value instanceof String) {
                value = ((String) value).replace(".", "");
                return Long.parseLong((String) value);
            } else if (value instanceof Long) {
                return (Long) value;
            }
            return (long) value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
