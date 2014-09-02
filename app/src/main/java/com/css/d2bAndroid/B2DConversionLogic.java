package com.css.d2bAndroid;

/**
 * B2DConversionLogic class.
 * <br><br>
 * This class contains the static method, {@link B2DConversionLogic#btod(String) btod}
 * which takes a binary string (1's and 0's) and returns
 * a string representing the given string in as a decimal number (string.)
 *
 * @author Christopher Sprague
 */
public class B2DConversionLogic {

    /**
     * TODO documentation
     *
     * @param message - the binary string
     * @return the binary string converted to decimal
     */
    static String btod(String message) {
        int res_value = 0;
        String res;
        for (int i = 0; i < message.length(); i++) {
            if (message.charAt(i) == '1') {
                res_value += Math.pow(2, message.length() - (i + 1));
            }

        }
        res = Integer.toString(res_value);
        return res;
    }
}
