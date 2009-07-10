/*
 *  GoalSeekStatus.java
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
package in.satpathy.math;

/**
 *
 *
 *  @author : gsatpath
 *  @version : 1.0.0
 *  Date: Oct 19, 2005, Time: 7:54:37 AM
 */
public class GoalSeekStatus {

	public static final int GOAL_SEEK_OK     = 0 ;
	public static final int GOAL_SEEK_ERROR  = 1 ;

	public int      seekStatus ;
	public Object   returnData ;

	/**
	 *  Constuctor
	 *
	 *  @param pStatus
	 *  @param retData
	 */
	public GoalSeekStatus( int pStatus, Object retData ) {
		this.seekStatus = pStatus ;
		this.returnData = retData ;
	}

	/**
	 *
	 *  @return
	 */
	public int getSeekStatus() {
		return seekStatus;
	}

	/**
	 *
	 *  @param seekStatus
	 */
	public void setSeekStatus( int seekStatus ) {
		this.seekStatus = seekStatus;
	}

	/**
	 *
	 *  @return
	 */
	public Object getReturnData() {
		return returnData;
	}

	/**
	 *
	 *  @param returnData
	 */
	public void setReturnData( Object returnData ) {
		this.returnData = returnData;
	}

	/**
	 *
	 * @return
	 */
	public String toString() {
		return "Status - " + seekStatus + ", Return Data - " + returnData ;
	}

}   /*  End of the GoalSeekStatus class. */