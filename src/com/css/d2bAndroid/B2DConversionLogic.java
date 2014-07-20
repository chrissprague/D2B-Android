package com.css.d2bAndroid;

/**
 * B2DConversionLogic class.
 * <br><br>
 * The object which, when constructed given
 * a number to convert, contains the logic
 * necessary to convert a number in Binary format
 * to its corresponding representation in Decimal.
 * 
 * @author Christopher Sprague
 */
public class B2DConversionLogic {
	private final Integer BINARY_NUMBER;
	
	public B2DConversionLogic ( Integer number_to_convert )
	{
		this.BINARY_NUMBER = number_to_convert;
	}
	
	String btod ()
	{
		String num_in_string_form = String.valueOf(BINARY_NUMBER);
		int res_value=0;
		String res;
		int c = 0;
		for ( int i = ( num_in_string_form.length() - 1 ) ; i >=0 ; --i )
		{
			res_value += D2BConversionLogic.power(
					D2BConversionLogic.power(2,c),
					Integer.parseInt(Character.toString(num_in_string_form.charAt(i))));
			c++;
		}
		res = Integer.toString(res_value);
		return res;
	}
}
