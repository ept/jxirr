/*
 *  GoalSeekData.java
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

/*
 *  Imports
 */


/**
 *  Data structure used by the GoalSeek algo.
 *
 * @author : gsatpath
 * @version : 1.0.0 Date: Oct 19, 2005, Time: 7:51:55 AM
 */
public class GoalSeekData {

	public double   xmin;             /* Minimum allowed values for x.  */
	public double   xmax;             /* Maximum allowed values for x.  */
	public double   precision;        /* Desired relative precision.  */

	public boolean  havexpos;        /* Do we have a valid xpos?  */
	public double   xpos;             /* Value for which f(xpos) > 0.  */
	public double   ypos;             /* f(xpos).  */

	public boolean  havexneg;        /* Do we have a valid xneg?  */
	public double   xneg;             /* Value for which f(xneg) < 0.  */
	public double   yneg;             /* f(xneg).  */

	public boolean  have_root;       /* Do we have a valid root?  */
	public double   root;             /* Value for which f(root) == 0.  */


}   /*  End of the GoalSeekData class. */