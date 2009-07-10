/*
 *  GoalSeek.java
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
import java.lang.Math  ;

/**
 *  A generic root finder.
 *
 *  @author : gsatpath
 *  @version : 1.0.0 Date: Oct 19, 2005, Time: 7:56:38 AM
 */
public class GoalSeek {

	private static final boolean DEBUG_GOAL_SEEK = false ;

	/**
	 *
	 *  @param x
	 *  @param y
	 *  @param data
	 *  @return
	 */
	public static boolean update_data( double x, double y, GoalSeekData data ){
		if (y > 0) {
			if (data.havexpos) {
				if (data.havexneg) {
					/*
					 *  When we have pos and neg, prefer the new point only
					 *  if it makes the pos-neg x-internal smaller.
					 */
					if (Math.abs(x - data.xneg) < Math.abs(data.xpos - data.xneg)) {
						data.xpos = x;
						data.ypos = y;
					}
				}
				else if (y < data.ypos) {
					/* We have pos only and our neg y is closer to zero.  */
					data.xpos = x;
					data.ypos = y;
				}
			}
			else {
				data.xpos = x;
				data.ypos = y;
				data.havexpos = true  ;
			}
			return false  ;
		}
		else if (y < 0) {
			if (data.havexneg) {
				if (data.havexpos) {
					/*
					 * When we have pos and neg, prefer the new point only
					 * if it makes the pos-neg x-internal smaller.
					 */
					if (Math.abs(x - data.xpos) < Math.abs(data.xpos - data.xneg)) {
						data.xneg = x;
						data.yneg = y;
					}
				}
				else if (-y < -data.yneg) {
					/* We have neg only and our neg y is closer to zero.  */
					data.xneg = x;
					data.yneg = y;
				}

			}
			else {
				data.xneg = x;
				data.yneg = y;
				data.havexneg = true;
			}
			return false  ;
		}
		else {
			/* Lucky guess...  */
			data.have_root = true  ;
			data.root = x  ;
			return true  ;
		}
	}


	/*
	 *  Calculate a reasonable approximation to the derivative of a function
     *  in a single point.
     */
	public static GoalSeekStatus fake_df( GoalSeekFunction f, double x,
	                                      double xstep, GoalSeekData data,
	                                      Object userData) {
		double          xl ;
		double          xr ;
		double          yl ;
		double          yr ;
		double          dfx ;
		GoalSeekStatus  status;

		if ( DEBUG_GOAL_SEEK ) {
			log( "fake_df (x = " + x +", xstep = " + xstep + ")" ) ;
		}

		xl = x - xstep;
		if (xl < data.xmin)
			xl = x;

		xr = x + xstep;
		if (xr > data.xmax)
			xr = x;

		if (xl == xr) {
			if ( DEBUG_GOAL_SEEK ) {
				log( "==> xl == xr" ) ;
			}
			return new GoalSeekStatus( GoalSeekStatus.GOAL_SEEK_ERROR, null ) ;
		}

		status = f.f( xl, userData ) ; //yl, userData ) ;
		if ( status.seekStatus != GoalSeekStatus.GOAL_SEEK_OK ) {
			if ( DEBUG_GOAL_SEEK ) {
				log("==> failure at xl\n") ;
			}
			return status;
		}
		yl = ((Double) status.returnData).doubleValue() ;
		if ( DEBUG_GOAL_SEEK ) {
			log( "==> xl = " + xl + " ; yl =" + yl ) ;
		}

		status = f.f( xr, userData ) ;  //yr, userData ) ;
		if (status.seekStatus != GoalSeekStatus.GOAL_SEEK_OK) {
			if ( DEBUG_GOAL_SEEK ) {
				log("==> failure at xr") ;
			}
			return status;
		}
		yr = ((Double) status.returnData).doubleValue() ;
		if ( DEBUG_GOAL_SEEK ) {
			log("==> xr = " + xr + " ; yr = " + yr ) ;
		}

		dfx = (yr - yl) / (xr - xl) ;
		if ( DEBUG_GOAL_SEEK ) {
			log("==> " + dfx) ;
		}

		return Double.isInfinite(dfx) ?
		       new GoalSeekStatus(GoalSeekStatus.GOAL_SEEK_ERROR, null) :
		       new GoalSeekStatus( GoalSeekStatus.GOAL_SEEK_OK, new Double(dfx) ) ;
		//return gnm_finite(*dfx) ? GoalSeekStatus.GOAL_SEEK_OK : GoalSeekStatus.GOAL_SEEK_ERROR;
	}

	/**
	 *  Initialize a GoalSeekData object.
	 */
	public static void goal_seek_initialize( GoalSeekData data ) 	{
		data.havexpos = data.havexneg = data.have_root = false;
		data.xpos = data.xneg = data.root = Double.NaN ; //gnm_nan;
		data.ypos = data.yneg = Double.NaN ; //gnm_nan ;
		data.xmin = -1e10;
		data.xmax = +1e10;
		data.precision = 1e-10;
	}

	/**
	 *  Seek a goal (root) using Newton's iterative method.
	 *
	 *  The supplied function must (should) be continously differentiable in
	 *  the supplied interval.  If NULL is used for `df', this function will
	 *  estimate the derivative.
	 *
	 *  This method will find a root rapidly provided the initial guess, x0,
	 *  is sufficiently close to the root.  (The number of significant digits
	 *  (asympotically) goes like i^2 unless the root is a multiple root in
	 *  which case it is only like c*i.)
	 */
	public static GoalSeekStatus goalSeekNewton(
                                        GoalSeekFunction f,
	                                    GoalSeekFunction df,
	                                    GoalSeekData data,
	                                    Object userData, double x0 ) {
		int iterations;
		double precision = data.precision / 2;

		if ( data.have_root )   {
			return new GoalSeekStatus( GoalSeekStatus.GOAL_SEEK_OK,
			                           new Double(data.root) ) ;
		}

		if ( DEBUG_GOAL_SEEK ) {
			log( "goalSeekNewton" ) ;
		}

		for (iterations = 0; iterations < 20; iterations++) {
			double x1 ;
            double y0 ;
            double df0 ;
            double stepsize ;
			GoalSeekStatus status;
        	if ( DEBUG_GOAL_SEEK ) {
				log("goalSeekNewton - x0 = " + x0 + ", (i = " + iterations + " )" ) ;
			}
			//  Check whether we have left the valid interval.
			if ( x0 < data.xmin || x0 > data.xmax ) {
				return new GoalSeekStatus( GoalSeekStatus.GOAL_SEEK_ERROR, null );
			}
			status = f.f(x0, userData ) ; //y0, userData) ;
			if ( status.seekStatus != GoalSeekStatus.GOAL_SEEK_OK )   {
				return status ;
			}

			y0 = ((Double) status.returnData).doubleValue() ;
			if ( DEBUG_GOAL_SEEK ) {
				log("   y0 = " + y0 ) ;
			}
			if (update_data (x0, y0, data) )    {
				return new GoalSeekStatus( GoalSeekStatus.GOAL_SEEK_OK,
				                           new Double(data.root) ) ;
            }

			if ( df != null ) {
				status = df.f( x0, userData ) ;
			}
			else {
				double xstep;
				if ( Math.abs(x0) < 1e-10 ) {
					if (data.havexneg && data.havexpos)
						xstep = Math.abs(data.xpos - data.xneg) / 1e6;
					else
						xstep = (data.xmax - data.xmin) / 1e6;
				}
				else    {
					xstep = Math.abs(x0) / 1e6;
				}
				status = fake_df(f, x0, xstep, data, userData) ;
			}
			if ( status.seekStatus != GoalSeekStatus.GOAL_SEEK_OK )    {
				return status;
			}

			df0 = ((Double) status.returnData).doubleValue() ;
			//  If we hit a flat spot, we are in trouble.
			if ( df0 == 0 ) {
				return new GoalSeekStatus(GoalSeekStatus.GOAL_SEEK_ERROR, null);
			}

			/*
			 * Overshoot slightly to prevent us from staying on
			 * just one side of the root.
			 */
			x1 = x0 - 1.000001 * y0 / df0;
			stepsize = Math.abs(x1 - x0) / (Math.abs(x0) + Math.abs(x1)) ;
			if ( DEBUG_GOAL_SEEK ) {
				log("   df0 = " + df0 ) ;
				log("   ss = " + stepsize ) ;
			}

			x0 = x1;

			if ( stepsize < precision ) {
				data.root = x0;
				data.have_root = true;
				return new GoalSeekStatus( GoalSeekStatus.GOAL_SEEK_OK,
				                           new Double(data.root) ) ;
			}
		}

		return new GoalSeekStatus( GoalSeekStatus.GOAL_SEEK_ERROR, null ) ;
	}

	/**
	 *
	 *  @param f
	 *  @param xmin
	 *  @param xmax
	 *  @param points
	 *  @return
	 */
//	GoalSeekStatus goal_seek_trawl_uniformly( GoalSeekFunction f,
//	                                          GoalSeekData data,
//	                                          Object userData, double xmin,
//	                                          double xmax, int points ) {
//		int i;
//
//		if (data.have_root)
//			return GoalSeekStatus.GOAL_SEEK_OK;
//
//		if (xmin > xmax || xmin < data.xmin || xmax > data.xmax)
//			return GoalSeekStatus.GOAL_SEEK_ERROR;
//
//		for (i = 0; i < points; i++) {
//			double x ;
//			double y ;
//			GoalSeekStatus status;
//
//			y = 0 ;
//			if (data.havexpos && data.havexneg) {
//				break;
//			}
//
//			x = xmin + (xmax - xmin) * Math.random() ;
//			//x = xmin + (xmax - xmin) * random_01() ;
//			status = f.f( x, y, userData ) ;
//			if ( status != GoalSeekStatus.GOAL_SEEK_OK )  {
//				/* We are not depending on the result, so go on.  */
//				continue ;
//			}
//
//			if ( DEBUG_GOAL_SEEK ) {
//				log( "x = " + x) ;
//				log( "y = " + y) ;
//			}
//
//			if (update_data (x, y, data))
//				return GoalSeekStatus.GOAL_SEEK_OK;
//		}
//
//		/* We were not (extremely) lucky, so we did not actually hit the
//		   root. We report this as an error.  */
//		return GoalSeekStatus.GOAL_SEEK_ERROR;
//	}

	/**
	 *
	 *  @param f
	 *  @param data
	 *  @param userData
	 *  @param mu
	 *  @param sigma
	 *  @param points
	 *  @return
	 */
//	GoalSeekStatus goal_seek_trawl_normally ( GoalSeekFunction f,
//	                                          GoalSeekData data,
//	                                          Object userData,
//	                                          double mu, double sigma,
//	                                          int points ) {
//		int i;
//
//		if (data.have_root)
//			return GoalSeekStatus.GOAL_SEEK_OK;
//
//		if (sigma <= 0 || mu < data.xmin || mu > data.xmax)
//			return GoalSeekStatus.GOAL_SEEK_ERROR;
//
//		for (i = 0; i < points; i++) {
//			double x, y;
//			GoalSeekStatus status;
//
//			y = 0 ;
//			if ( data.havexpos && data.havexneg ) {
//				break;
//			}
//
//			x = mu + sigma * Math.random() ;
//			//x = mu + sigma * random_normal () ;
//			if ( x < data.xmin || x > data.xmax )   {
//				continue;
//			}
//
//			status = f.f(x, y, userData) ;
//			if (status != GoalSeekStatus.GOAL_SEEK_OK)
//				/* We are not depending on the result, so go on.  */
//				continue;
//
//			if ( DEBUG_GOAL_SEEK ) {
//				log("x = " + x) ;
//				log("y = " + y) ;
//			}
//
//			if ( update_data (x, y, data) ) {
//				return GoalSeekStatus.GOAL_SEEK_OK;
//			}
//		}
//
//		/* We were not (extremely) lucky, so we did not actually hit the
//		   root.  We report this as an error.  */
//		return GoalSeekStatus.GOAL_SEEK_ERROR;
//	}

	/**
	 *  Log a message to the console.
	 *
	 *  @param message
	 */
	private static void log( String message ) {
		System.out.println( message ) ;
	}

}   /*  End of the GoalSeek class. */