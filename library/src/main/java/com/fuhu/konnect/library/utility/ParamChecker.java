package com.fuhu.konnect.library.utility;

/**
 * Created by jacktseng on 2015/7/28.
 */
public class ParamChecker {

    public static final boolean isString(String str) {
        boolean isError = true;
        do {
            if(str == null) break;
            if(str.isEmpty()) break;
            if(str.trim().equals("")) break;

            isError = false;
        } while(false);

        return !isError;
    }

}
