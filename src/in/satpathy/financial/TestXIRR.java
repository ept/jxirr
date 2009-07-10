/*
 *  TestXIRR.java
 *  Copyright (C) 2005 Gautam Satpathy
 *  gautam@satpathy.in
 *  www.satpathy.in
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package in.satpathy.financial;

/*
 *  Imports
 */
import java.util.GregorianCalendar;

/**
 * @author : gsatpath
 * @version : 1.0.0 Date: Oct 20, 2005, Time: 4:39:25 AM
 */
public class TestXIRR {

	/**
	 *
	 *  @param args
	 */
	public static void main( String[] args ) {
		log( "Testing XIRR..." ) ;

//		GregorianCalendar dateStart = new GregorianCalendar( 1899, 11, 30 ) ;
		GregorianCalendar dateEnd = new GregorianCalendar( 2005, 9, 20 ) ;
        int daysBetween = XIRRData.getExcelDateValue( dateEnd ) ;
		log( "Days Between = " + daysBetween ) ;

//		"Let us assume that the cells A1:A5 contain the numbers -6000, "
//		"2134, 1422, 1933, and 1422, and the cells B1:B5 contain the "
//		"dates \"1999-01-15\", \"1999-04-04\", \"1999-05-09\", "
//		"\"2000-03-12\", and \"2000-05-1\". Then\n"
//		"XIRR(A1:A5,B1:B5) returns 0.224838. "
		double[]    values  = new double[5] ;
		double[]    dates   = new double[5] ;
		values[0]           = -6000 ;
		values[1]           = 2134 ;
		values[2]           = 1422 ;
		values[3]           = 1933 ;
		values[4]           = 1422 ;
		dates[0]            = XIRRData.getExcelDateValue( new GregorianCalendar(1999, 0, 15) ) ;
		dates[1]            = XIRRData.getExcelDateValue( new GregorianCalendar(1999, 3, 4) ) ;
		dates[2]            = XIRRData.getExcelDateValue( new GregorianCalendar(1999, 4, 9) ) ;
		dates[3]            = XIRRData.getExcelDateValue( new GregorianCalendar(2000, 2, 12) ) ;
		dates[4]            = XIRRData.getExcelDateValue( new GregorianCalendar(2000, 4, 1) ) ;
		XIRRData data       = new XIRRData( 5, 0.3, values, dates ) ;
		double xirrValue = XIRR.xirr( data ) ;
        log( "XIRR = " + xirrValue ) ;

		log( "XIRR Test Completed..." ) ;
	}


	/**
	 *
	 * @param message
	 */
	public static void log( String message ) {
		System.out.println( message ) ;
	}

}   /*  End of the TestXIRR class. */