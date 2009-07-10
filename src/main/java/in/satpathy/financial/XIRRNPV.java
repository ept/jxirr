/*
 *  XIRRNPV.java
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
import in.satpathy.math.GoalSeekStatus ;
import in.satpathy.math.GoalSeekFunction ;

/**
 * @author : gsatpath
 * @version : 1.0.0 Date: Oct 19, 2005, Time: 9:32:57 AM
 */
public class XIRRNPV implements GoalSeekFunction {

	/**
	 *  Default Constructor.
	 */
	public XIRRNPV() {
	}

	/**
	 *
	 *  @param rate
	 *  @param y
	 *  @param userData
	 *  @return
	 */
	public GoalSeekStatus f( double rate, Object userData ) {
		XIRRData    p ;
		double[]    values ;
		double[]    dates ;
		double      sum ;
		int         n ;

		p       = (XIRRData) userData ;
		values  = p.values ;
		dates   = p.dates ;
		n       = p.n ;
		sum     = 0 ;
		for ( int i = 0; i < n; i++ ) {
			double d = dates[i] - dates[0];
			if ( d < 0 )  {
				return new GoalSeekStatus( GoalSeekStatus.GOAL_SEEK_ERROR, null) ;
			}
		    sum += values[i] / Math.pow(rate, d / 365.0) ; //pow1p( rate, d / 365.0 ) ;
		}

//		GoalSeekStatus.returnData = new Double( sum ) ;
		return new GoalSeekStatus( GoalSeekStatus.GOAL_SEEK_OK, new Double(sum) ) ;
	}

}   /*  End of the XIRRNPV class. */