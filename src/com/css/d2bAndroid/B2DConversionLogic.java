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
	
	private final String MESSAGE;
	
	public B2DConversionLogic ( String message )
	{
		this.MESSAGE=message;
	}
	
	// TODO just make this a static method, jeez
	String btod ()
	{
		int res_value=0;
		String res;
		for(int i = 0; i < MESSAGE.length();i++){
			if (MESSAGE.charAt(i) == '1'){
				res_value+= D2BConversionLogic.power(2, MESSAGE.length() - (i+1));
				
			}
			
		}
		res = Integer.toString(res_value);
		return res;
	}
}
