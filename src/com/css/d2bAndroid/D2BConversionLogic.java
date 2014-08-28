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
public class D2BConversionLogic {

	private final int DECIMAL_NUMBER;
	
	/**
	 * constructor for D2BConversionLogic object
	 * @param i - the number to convert
	 */
	public D2BConversionLogic ( int i ) {
		DECIMAL_NUMBER = i;
	}
	
	/**
	* power function<BR><BR>
	* given an integer base and integer exponent,
	* return the value of (base)^(exponent)<BR><BR>
	* note that 0^0 is defined here as 1.<BR><BR>
	* Use {@link java.lang.Math#pow Math.pow}  instead of this
	* 
	* @see java.lang.Math#pow
	* 
	* @param base - the base
	* @param exponent - the power to raise the base to
	* @return the value of ( base ^ exponent )
	*/
	@Deprecated
	static int power ( int base, int exponent ) {
		if ( base == 0 && exponent == 0 ) {
			return 1; // definition here (redundant but w/e)
		}
		if ( exponent == 0 ) {
			return 1;
		}
		if ( exponent == 1 ) {
			return base;
		}
		int sum = base;
		for ( int i = 1 ; i < exponent ; i++ ) {
			sum = sum * base;
		}
		return sum;
	}
	
	/**
	 * given an integer value_remaining (think remainder)
	 * return the highest POWER to which 2 can be raised
	 * that does not EXCEED the value of value_remaining.
	 *
	 * in other words, this function is used determine
	 * where each '1' in the binary representation falls.
	 *
	 * if the 2^power = value_remaining, then this is the
	 * last '1' we need in the binary representation.
	 */
	int p2 ( int value_remaining ) {
		if ( value_remaining == 0 ) {
			return 0; // base case
		}
		int pow = 0;
		for ( int i = 0 ; power(2,i) < value_remaining ; i++ ) {
			pow++;
		}
		if ( power(2,pow) > value_remaining ) {
			return ( pow - 1 ) ;
		}
		return pow;
	}
	
	/**
	 * p2 function catered to float values.
	 * @param value_remaining
	 * @return something
	 */
	static int p2_float ( float value_remaining ) {
		if ( value_remaining == Float.valueOf(0) ) {
			return 0; // base case
		}
		int pow = 0;
		for ( int i = 0 ; Math.pow(2, i) < value_remaining ; i++ ) {
			pow++;
		}
		if ( Math.pow(2,pow) > value_remaining ) {
			return ( pow - 1 ) ;
		}
		return pow;
	}
	
	/**
	 * Run a sequence similar to dtob.cpp's main
	 * without working with stdin/stdout.
	 * 
	 * Main functionality of the program:
	 * create a D2BConversionLogic class and then
	 * call this function - dtob - to retrieve
	 * and integer representing the binary form of
	 * the number you gave.
	 * 
	 * @param number - number to turn into binary
	 * @return binary representation of that number
	 */
	String dtob ( ) {
		int num = DECIMAL_NUMBER;
		int size = p2(num)+1;
		int []bin = new int[size];
		for ( int i = 0 ; i < size ; i ++ ) {
			bin[i] = 0 ; // set default to 0, put in 1's later
		}
		int R = num - power(2,p2(num)); // R = value remaining
		bin[p2(num)] = 1;
		while ( R != 0 )
		{
			int nhp = p2(R); // next highest power
			bin[nhp] = 1;
			R = R - power(2, p2(R));
		}
		
		String strRep = new String();

		// print out the array backwards
		for ( int i = (size-1) ; i >= 0 ; i-- ) {
			strRep += bin[i];
		}
		return strRep;
	}
	
	/**
	 * Do decimal to binary conversion of numbers that would otherwise overflow
	 * java's primitive <code>int</code>.
	 * @param float_value - the float value to convert to string (decimal)
	 * @return the string representation of the float value, converted to binary.
	 */
	static String dtob_float (float float_value) {
		int size = p2_float(float_value)+1; // the length of the string.
		System.out.println(size);
		ArrayList<Float> bin = new ArrayList<Float>(50);
		for ( int i = 0 ; i < size ; ++i )
			bin.add(Float.valueOf(0)); // "Initialize" array
		
		for ( int i = 0 ; i < size ; i ++ ) {
			bin.set(i, Float.valueOf(0)); // set default to 0, put in 1's later
		}
		float R = (float) (float_value - Math.pow(2, p2_float(float_value)));
		bin.set(p2_float(float_value), Float.valueOf(1));
		while ( R != 0 )
		{
			int nhp = p2_float(R);
			bin.set(nhp, Float.valueOf(1));
			R=(float) (R-Math.pow(2, p2_float(R)));
		}
		String str = new String();
		
		// print out the array backwards
		for ( int i = (size-1) ; i >= 0 ; i-- ) {
			str += bin.get(i).toString().replaceAll("\\.?0*$", "");
		}
		return str;
	}
	
}
