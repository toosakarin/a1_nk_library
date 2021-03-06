package com.fuhu.konnect.library.utility;

/**
 * This class is a useful tool for params basic checking likes null point, be 0 or empty value etc.
 * <p/>
 * Author: Jack Tseng
 */
public class ParamChecker {

    /**
     * Checks string is valid or not
     *
     * @param str
     * @return true if string is valid
     */
    public static final boolean isValid(String str) {
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
