package com.atharva.erp_telecom.utils;

import java.util.function.Consumer;

public class CrudUtils {
    // Methods for setting not null values for PATCH calls

    /**
     * Name: updateIfNotNull <br>
     * Purpose: To update not null values from payload
     */
    public static <T> void updateIfNotNull(Consumer<T> setter, T value){
        if(value != null){
            setter.accept(value);
        }
    }

    // Null checker for Boolean values since the above method will default the value if sent as null
    public static void updateIfNotNull(Consumer<Boolean> setter, Boolean value) {
        if (value != null) {
            setter.accept(value);
        }
    }

}
