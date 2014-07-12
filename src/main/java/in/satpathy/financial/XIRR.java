/*
 *  XIRR.java
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
import in.satpathy.math.GoalSeek ;
import in.satpathy.math.GoalSeekData ;
import in.satpathy.math.GoalSeekStatus ;

/**
 *  XIRR implementation
 *
 *  @author : gsatpath
 *  @version : 1.0.0 Date: Oct 19, 2005, Time: 12:09:58 PM
 */
public class XIRR {

	/*
	 *  Excel stores dates as sequential serial numbers so they can be used
	 *  in calculations. By default, January 1, 1900 is serial number 1, and
	 *  January 1, 2008 is serial number 39448 because it is 39,448 days
	 *  after January 1, 1900.
	 */

	/**
	 *  Calculate XIRR.
	 *
	 *  @param xirrData
	 *  @return
	 */
	public static double xirr( XIRRData xirrData ) 	{
		GoalSeekData    data ;
		GoalSeekStatus  status ;
		double          result ;
		double          rate0 ;
		int             n ;
		int             d_n ;

		data        = new GoalSeekData() ;
		GoalSeek.goal_seek_initialize( data ) ;
		data.xmin   = -1;
		data.xmax   = Math.min( 1000, data.xmax ) ;
		rate0       = xirrData.guess ; //argv[2] ? value_get_as_float (argv[2]) : 0.1;

		status = GoalSeek.goalSeekNewton(
		            new XIRRNPV(), null, data, xirrData, rate0 ) ;

		if (status.seekStatus == GoalSeekStatus.GOAL_SEEK_OK)  {
//			result = value_new_float(data.root);
			result = ((Double) status.returnData).doubleValue() ;    //data.root ;
		}
		else    {
//			result = value_new_error_NUM (ei.pos);
			result = Double.NaN ;
		}

		System.out.println( "XIRR Result - " + result ) ;
		return (Double.isNaN(result)) ? (result - 1) : result ;
	}

}   /*  End of the XIRR class. */
