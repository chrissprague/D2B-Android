/**
 * conversion logic for converting a number in decimal
 * to its binary representation.
 *
 * Code taken or slightly modified from my C++
 * project, "decimal-to-binary".
 *
 * @author Christopher Sprague
 */
package com.css.d2bAndroid;

import java.util.ArrayList;

/**
 * D2BConversionLogic class.
 * <br><br>
 * The object which, when constructed given
 * a number to convert, contains the logic
 * necessary to convert a number in Decimal format
 * to its corresponding representation in Binary.
 *
 * @author Christopher Sprague
 */
class D2BConversionLogic {

    /**
     * given an integer value_remaining (think remainder)
     * return the highest POWER to which 2 can be raised
     * that does not EXCEED the value of value_remaining.
     * <p/>
     * in other words, this function is used determine
     * where each '1' in the binary representation falls.
     * <p/>
     * if the 2^power = value_remaining, then this is the
     * last '1' we need in the binary representation.
     */
    private static int p2(int value_remaining) {
        if (value_remaining == 0) {
            return 0; // base case
        }
        int pow = 0;
        for (int i = 0; Math.pow(2, i) < value_remaining; i++) {
            pow++;
        }
        if (Math.pow(2, pow) > value_remaining) {
            return (pow - 1);
        }
        return pow;
    }

    /**
     * p2 function catered to long values.
     *
     * @param value_remaining - the remaining unaccounted-for value of the input number.
     * @return something
     */
    private static int p2_long(long value_remaining) {
        if (value_remaining == 0) {
            return 0; // base case
        }
        int pow = 0;
        for (int i = 0; Math.pow(2, i) < value_remaining; i++) {
            pow++;
        }
        if (Math.pow(2, pow) > value_remaining) {
            return (pow - 1);
        }
        return pow;
    }

    /**
     * Run a sequence similar to dtob.cpp's main
     * without working with stdin/stdout.
     * <p/>
     * Main functionality of the program:
     * create a D2BConversionLogic class and then
     * call this function - dtob - to retrieve
     * and integer representing the binary form of
     * the number you gave.
     * <p/>
     *
     * @param number - number to turn into binary
     * @return binary representation of that number
     */
    static String dtob(int number) {
        if ( number == 0 )
            return "0";
        int size = p2(number) + 1;
        int[] bin = new int[size];
        for (int i = 0; i < size; i++) {
            bin[i] = 0; // set default to 0, put in 1's later
        }
        int R = (int) (number - Math.pow(2, p2(number))); // R = value remaining
        bin[p2(number)] = 1;
        while (R != 0) {
            int nhp = p2(R); // next highest power
            bin[nhp] = 1;
            R = (int) (R - Math.pow(2, p2(R)));
        }

        StringBuilder strRep=new StringBuilder();

        // print out the array backwards
        for (int i = (size - 1); i >= 0; i--) {
            strRep.append(bin[i]);
        }

        return strRep.toString();
    }

    /**
     * Do decimal to binary conversion of numbers that would otherwise overflow
     * java's primitive <code>int</code>.
     *
     * @param long_value - the long value to convert to string (decimal)
     * @return the string representation of the long value, converted to binary.
     */
    static String dtob_long(long long_value) {
        if ( long_value == 0 )
        {
            return "0";
        }
        int size = p2_long(long_value) + 1; // the length of the string.
        ArrayList<Long> bin = new ArrayList<>(50);
        for (int i = 0; i < size; ++i)
            bin.add(0L); // "Initialize" array

        for (int i = 0; i < size; i++) {
            bin.set(i, 0L); // set default to 0, put in 1's later
        }
        long R = (long) (long_value - Math.pow(2, p2_long(long_value)));
        bin.set(p2_long(long_value), 1L);
        while (R != 0) {
            int nhp = p2_long(R);
            bin.set(nhp, 1L);
            R = (long) (R - Math.pow(2, p2_long(R)));
        }
        StringBuilder str = new StringBuilder();

        // print out the array backwards
        for (int i = (size - 1); i >= 0; i--) {
            str.append(bin.get(i));
        }
        return str.toString();
    }

}
