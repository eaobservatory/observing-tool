/*
 * ( C ) 2008 STFC
 * Based on the C code by Russell Kackley ( JAC )
 * This code largely mirrors that code.
 * All comments are copied from the original code.
 */

package orac.jcmt.util ;

public class ComputePong
{
	private static final double sqrt2 = Math.sqrt( 2. ) ;
	private static final int MOST = 0 ;
	private static final int LEAST = 1 ;
	
	public static void main( String[] args )
	{
		String type = ComputePong.PongScan.CURVY ;
		double height = 180. ;
		double width = 180. ;
		double spacing = 30. ;
		double velocity = 120. ;

		if( args.length == 1 && "-test".equals( args[ 0 ] ) )
		{
			System.out.println( "Testing." ) ;
		}
		else if( args.length == 5 )
		{
			try
			{
				height = new Double( args[ 0 ] ) ;
				width = new Double( args[ 1 ] ) ;
				spacing = new Double( args[ 2 ] ) ;
				velocity = new Double( args[ 3 ] ) ;
				type = args[ 4 ] ;
			}
			catch( Exception e )
			{
				System.out.println( "Error detected in arguments : " + e ) ;
				System.exit( -1 ) ;
			}
		}
		else
		{
			System.out.println( "Arguments are : height width spacing velocity type[CURVY|ROUNDED|SQUARE|STRAIGHT]" ) ;
			System.exit( -1 ) ;
		}

		System.out.println( "Parameters are :" ) ;
		System.out.println( "Height : " + height ) ;
		System.out.println( "Width : " + width ) ;
		System.out.println( "Spacing : " + spacing ) ;
		System.out.println( "Velocity : " + velocity ) ;
		System.out.println( "Type : " + type ) ;

		ComputePong computePong = new ComputePong() ;
		double period = computePong.getPeriodForPong( height , width , spacing , velocity , type ) ;
		
		System.out.println( "Period : " + period ) ;
	}

	/**
	 * Convenience method for using the ComputePong class
	 * @param height
	 * @param width
	 * @param spacing
	 * @param velocity
	 * @param type - see String[] ComputePong.PongScan.types()
	 * @return period as a double
	 */
	public double getPeriodForPong( double height , double width , double spacing , double velocity , String type )
	{
		PongScan pongScan = new PongScan() ;
		pongScan.setHeight( height ) ;
		pongScan.setWidth( width ) ;
		pongScan.setSpacing( spacing ) ;
		pongScan.setVelocity( velocity ) ;
		pongScan.setType( type ) ;
		
		pongScan = computePongGrid( pongScan ) ;
		
		if( PongScan.ROUNDED.equals( type ) || PongScan.SQUARE.equals( type ) )
			pongScan = computePongPath( pongScan ) ;
		
		return pongScan.period() ;
	}
	
	/**
	 * Compute the vertices (i.e., reflection points) of the PONG pattern
	 */
	public PongScan computePongGrid( PongScan pongScan )
	{
		int most = 0 ;
		int least = 0 ;

		/*
		 * Determine number of vertices (reflection points) along each side
		 * of the box which satisfies the common-factors criterion and the
		 * requested size / spacing. The algorithm to determine the vertices
		 * is based on pseudocode provided in SC2/ANA/S210/008 "The Impact
		 * of Scanning Pattern Strategies on Uniform Sky Coverage of Large Maps".
		 */
	
		// Find out how far apart the vertices are along the axes
		double vertSpacing = ( 2. * pongScan.spacing() ) / sqrt2 ;

		/*
		* Determine how many vertices (minimum) there must be
		* along each axis to cover the required area
		*/
		int xNumVert = ( int )Math.ceil( pongScan.width() / vertSpacing ) ;
		int yNumVert = ( int )Math.ceil( pongScan.height() / vertSpacing ) ;
	
		/*
		* Determine which is lower and check to make sure that 
		* one is even while the other is odd
		*/
		int[] result = setMostLeast( xNumVert , yNumVert ) ;
		most = result[ MOST ] ;
		least = result[ LEAST ] ;

		/*
		* If both are odd or both are even,
		* increment the lesser of the two, and update which is least
		*/
		if( ( xNumVert % 2 ) == ( yNumVert % 2 ) )
		{
			least++ ;
			result = setMostLeast( most , least ) ;
			most = result[ MOST ] ;
			least = result[ LEAST ] ;
		}

		/*
		 * Check for common factors between the two, 
		 * and adjust as necessary until x_numvert and y_numvert 
		 * do not share any common factors 
		 */
		for( int i = 3 ; i <= least ; i++ ) 
		{
			if ( ( ( least % i ) == 0 ) && ( ( most % i ) == 0 ) ) 
			{
				// Found a common factor, increment most and start over
				most += 2 ;
				i = 3 ;
			}
		}

		/*
		 * At the end of this step, we are left with two values, x_numvert
		 * and y_numvert, of which one is even and one is odd, and which
		 * share no common factors.
		 */

		xNumVert = most ;
		yNumVert = least ;

		/*
		 * The entire pattern is defined on a grid with points spaced half
		 * the distance between adjacent vertices as calculated above along
		 * the same side. Calculate spacing between these grid points and
		 * the length along each side in units of grid_space sized segments.
		 */
		double gridSpace = vertSpacing / 2. ;
		int xNGridSeg = xNumVert * 2 ;
		int yNGridSeg = yNumVert * 2 ;

		/*
		 * Calculate the total number of straight line segments in the
		 * pattern, and allocate an array to store the x and y tanplane
		 * offsets of the endpoints in order
		 */
		int nSeg = xNGridSeg + yNGridSeg ;

		double[] xGrid = new double[ xNGridSeg + 1 ] ;
		double[] yGrid = new double[ yNGridSeg + 1 ] ;

		for( int i = 0 ; i <= xNGridSeg ; i++ )
			xGrid[ i ] = ( i - xNGridSeg / 2 ) * gridSpace ;
		for( int i = 0 ; i <= yNGridSeg ; i++ )
			yGrid[ i ] = ( i - yNGridSeg / 2 ) * gridSpace ;

		double[] xVertex = new double[ nSeg + 1 ] ;
		double[] yVertex = new double[ nSeg + 1 ] ;

		// Initialization of the scan pattern
		// starting grid coordinates
		int xInit = 0 ;
		int yInit = 1 ;
		// current grid offsets
		int xOff = xInit ;
		int yOff = yInit ;

		// starting tanplane offsets of the scan
		xVertex[ 0 ] = xGrid[ xInit ] ;
		yVertex[ 0 ] = yGrid[ yInit ] ;

		/*
		 * Loop over line segments and calculate list of endpoints 
		 * for each reflection in order 
		 */
		int steps = 0 ;
		for( int i = 1 ; i <= nSeg ; i++ ) 
		{
			int xRefl ;
			int yRefl ;

			/*
			 * increment steps to the next boundary reflection 
			 * (whichever comes first - along the sides or top/bottom 
			 * of the rectangle) 
			 */
			// if then side reflection next
			// else top/bottom reflection next
			if( ( xNGridSeg - xOff ) <= ( yNGridSeg - yOff ) )
				steps += xNGridSeg - xOff ;
			else
				steps += yNGridSeg - yOff ;

			/*
			 * Number of steps divided by number of grid segments along each
			 * side tells us how many reflections we've been through.
			 */
			xRefl = ( int )Math.floor( ( steps + xInit ) / xNGridSeg ) ;
			yRefl = ( int )Math.floor( ( steps + yInit ) / yNGridSeg ) ;

			// The remainder is the grid offset
			xOff = ( steps + xInit ) % xNGridSeg ;
			yOff = ( steps + yInit ) % yNGridSeg ;

			/*
			 * If the number of reflections is even, the offset is in the
			 * positive direction from the start of the lookup table.
			 * If it is odd, the offset is in the negative direction
			 * from the end of the lookup table
			 */
			if( ( xRefl % 2 ) == 0 )
				xVertex[ i ] = xGrid[ xOff ] ; // even reflections
			else
				xVertex[ i ] = xGrid[ xNGridSeg - xOff ] ; // odd reflections

			if( ( yRefl % 2 ) == 0 )
				yVertex[ i ] = yGrid[ yOff ] ; // even reflections
			else
				yVertex[ i ] = yGrid[ yNGridSeg - yOff ] ; // odd reflections
		}

		pongScan.setNSeg( nSeg ) ;
		pongScan.setGridSpace( gridSpace ) ;
		pongScan.setXNGridSeg( xNGridSeg ) ;
		pongScan.setYNGridSeg( yNGridSeg ) ;
		pongScan.setXVertex( xVertex ) ;
		pongScan.setYVertex( yVertex ) ;

		// Compute the coefficients for the CURVY_PONG path
		pongScan.setAlphaX( xNumVert * vertSpacing / 2. ) ;
		pongScan.setAlphaY( yNumVert * vertSpacing / 2. ) ;
		double v2 = pongScan.velocity() * sqrt2 / 2. ;
		pongScan.setBetaX( xNumVert * vertSpacing * 2. / v2 ) ;
		pongScan.setBetaY( yNumVert * vertSpacing * 2. / v2 ) ;

		/*
		 * Compute the time required to complete
		 * one circuit of the CURVY_PONG path
		 */
		pongScan.setPeriod( xNumVert * yNumVert * vertSpacing * 2. / v2 ) ;

		return pongScan ;
	}

	/**
	 * Compute the details of the SQUARE_PONG or ROUNDED_PONG path
	 */
	public PongScan computePongPath( PongScan pongScan )
	{
		// 45 deg arc at start and end of straight-line segment for ROUNDED_PONG
		final double ARC_LENGTH_NOM = 45. ;
		// EPSILON is used to determine if a path is basically zero-length
		final double EPSILON = 1.e-6 ;

		int nSeg = pongScan.nSeg() ;
		PongSegment[] pathData = new PongSegment[ 3 * nSeg ] ;
		for( int i = 0 ; i < pathData.length ; i++ )
			pathData[ i ] = new PongSegment() ;

		// TSTART for the first path segment is 0.0
		pathData[ 0 ].setTStart( 0. ) ;

		double[] xVertex = pongScan.xVertex() ;
		double[] yVertex = pongScan.yVertex() ;

		double xStart ;
		double yStart ;
		double xEnd ;
		double yEnd ;
		double dx ;
		double dy ;
		double length ;

		double xStartNom ;
		double yStartNom ;
	
		double xEndNom ;
		double yEndNom ;
	
		double dxNom ;
		double dyNom ;
		double lengthNom ;
		double mx ;
		double my ;
		
		double gridSpace = pongScan.gridSpace() ;
		int xNGridSeg = pongScan.xNGridSeg() ;
		int yNGridSeg = pongScan.yNGridSeg() ;
		double posXBoundary = gridSpace * xNGridSeg / 2 ;
		double negXBoundary = -posXBoundary ;
		double posYBoundary = gridSpace * yNGridSeg / 2 ;
		double negYBoundary = -posYBoundary ;
		
		double curveRadius = pongScan.spacing() / 2. ;
		
		int pathSegmentIndex = -1 ;
		
		double pathLength = 0. ;
		double segLength ;

		final double D2PI = 6.2831853071795864769252867665590057683943387987502 ;
		double arcLength = ARC_LENGTH_NOM ;
		
		for( int i = 0 ; i < nSeg ; i++ )
		{
			double xStartArcCenter = 0. ;
			double yStartArcCenter = 0. ;
			double startThetaInit = 0. ;
			double startThetaFin= 0. ;
			double startRotDir = 0. ;
			double xEndArcCenter = 0. ;
			double yEndArcCenter = 0. ;
			double endThetaInit = 0. ;
			double endThetaFin = 0. ;
			double endRotDir = 0. ;
		
			// Get the nominal start and end points for this row
			xStartNom = xVertex[ i ] ;
			yStartNom = yVertex[ i ] ;
		
			xEndNom = xVertex[ i + 1 ] ;
			yEndNom = yVertex[ i + 1 ] ;
		
			// Compute the length and direction vector (mx, my) of the row
			dxNom = xEndNom - xStartNom ;
			dyNom = yEndNom - yStartNom ;
			lengthNom = Math.sqrt( dxNom * dxNom + dyNom * dyNom ) ;
			mx = dxNom / lengthNom ;
			my = dyNom / lengthNom ;
		
			/*
			* For SQUARE_PONG, we use the nominal start and end points for
			* the actual path segment. For ROUNDED_PONG, we modify the path
			* segment by shortening the straight part of the path and putting
			* arcs at the beginning and end of the path segment.
			*/
			if( PongScan.SQUARE.equals( pongScan.type() ) )
			{
				xStart = xStartNom ;
				yStart = yStartNom ;
				xEnd = xEndNom ;
				yEnd = yEndNom ;
				dx = xEnd - xStart ;
				dy = yEnd - yStart ;
				length = Math.sqrt( dx * dx + dy * dy ) ;
			}
			else
			{	
				/*
				* For ROUNDED_PONG, the actual start and end points are computed
				* from the nominal start and end points, modified by the arcs
				* placed at the start and end to round off the sharp corners.
				*/
				xStart = xStartNom + mx * curveRadius ;
				yStart = yStartNom + my * curveRadius ;
				xEnd = xEndNom - mx * curveRadius ;
				yEnd = yEndNom - my * curveRadius ;
				dx = xEnd - xStart ;
				dy = yEnd - yStart ;
				length = Math.sqrt( dx * dx + dy * dy ) ;
		
				/*
				* If the straight-line segment has a non-zero length, where
				* zero is defined as less than EPSILON, use the nominal arc
				* length. If the straight-line segment is basically zero, then
				* we don't even use a straight-line segment and combine the
				* start and end arcs into a single arc of twice the nominal arc length
				*/
				// does this code match the comment ?
				arcLength = ARC_LENGTH_NOM ;
				if( length <= EPSILON ) 
					arcLength *= 2. ;
		
				/*
				* For ROUNDED_PONG, the arcs at the start and end of the
				* straight segment are the most complicated parts of the
				* path. We need to figure out the angular position at which the
				* arc starts and the direction of motion while in the
				* arc. rot_dir == 1 => CCW ; rot_dir == -1 => CW. These values
				* depend on whether the start/end points are on the +X, -X, +Y,
				* or -Y boundaries, and the direction of the path
				* leaving/approaching the boundary.
				*/
				if( Math.abs( xStartNom - posXBoundary ) < EPSILON )
				{
					xStartArcCenter = xStartNom - curveRadius * sqrt2 ;
					yStartArcCenter = yStartNom ;
					startThetaInit = 0. ;
					startRotDir = ( dyNom > 0 ) ? 1. : -1. ;
				} 
				else if( Math.abs( xStartNom - negXBoundary ) < EPSILON )
				{
					xStartArcCenter = xStartNom + curveRadius * sqrt2 ;
					yStartArcCenter = yStartNom ;
					startThetaInit = 180. ;
					startRotDir = ( dyNom > 0 )? -1. : 1. ;
				} 
				else if( Math.abs( yStartNom - posYBoundary ) < EPSILON )
				{
					xStartArcCenter = xStartNom ;
					yStartArcCenter = yStartNom - curveRadius * sqrt2 ;
					startThetaInit = 90. ;
					startRotDir = ( dxNom > 0 ) ? -1. : 1. ;
				} 
				else if( Math.abs( yStartNom - negYBoundary ) < EPSILON )
				{
					xStartArcCenter = xStartNom ;
					yStartArcCenter = yStartNom + curveRadius * sqrt2 ;
					startThetaInit = 270. ;
					startRotDir = ( dxNom > 0 ) ? 1. : -1. ;
				}
				else
				{
//					status = PTCS__GERROR ;
				}
		
				startThetaFin = startThetaInit + startRotDir * arcLength ;
		
				if( Math.abs( xEndNom - posXBoundary ) < EPSILON ) 
				{
					xEndArcCenter = xEndNom - curveRadius * sqrt2 ;
					yEndArcCenter = yEndNom ;
					endRotDir = ( dyNom > 0 ) ? 1. : -1. ;
					endThetaInit = 0. - endRotDir * arcLength ;
				}
				else if( Math.abs( xEndNom - negXBoundary ) < EPSILON ) 
				{
					xEndArcCenter = xEndNom + curveRadius * sqrt2 ;
					yEndArcCenter = yEndNom ;
					endRotDir = ( dyNom > 0 ) ? -1. : 1. ;
					endThetaInit = 180. - endRotDir * arcLength ;
				}
				else if( Math.abs( yEndNom - posYBoundary ) < EPSILON )
				{
					xEndArcCenter = xEndNom ;
					yEndArcCenter = yEndNom - curveRadius * sqrt2 ;
					endRotDir = ( dxNom > 0 ) ? -1. : 1. ;
					endThetaInit = 90. - endRotDir * arcLength ;
				}
				else if( Math.abs( yEndNom - negYBoundary ) < EPSILON )
				{
					xEndArcCenter = xEndNom ;
					yEndArcCenter = yEndNom + curveRadius * sqrt2 ;
					endRotDir = ( dxNom > 0 ) ? 1. : -1. ;
					endThetaInit = 270. - endRotDir * arcLength ;
				}
				else
				{
//					status = PTCS__GERROR ;
				}
		
				endThetaFin = endThetaInit + endRotDir * arcLength ;
			}
		
			// For ROUNDED_PONG, put an arc at the start of the segment
			if( PongScan.ROUNDED.equals( pongScan.type() ) )
			{
				pathSegmentIndex++ ;
				PongSegment segment = pathData[ pathSegmentIndex ] ;
				segment.setType( PongSegment.ARC ) ;
				PongArcSegment arc = segment.arc() ;
				arc.setXCenter( xStartArcCenter ) ;
				arc.setYCenter( yStartArcCenter ) ;
				arc.setRadius( curveRadius ) ;
				arc.setThetaInit( startThetaInit ) ;
				arc.setThetaFin( startThetaFin ) ;
				arc.setRotDir( startRotDir ) ;
				segLength = D2PI * curveRadius * arcLength / 360. ;
				pathLength += segLength ;
				// TSTART for path segments after the first is the TEND from the previous segment
				if( pathSegmentIndex > 0 )
					segment.setTStart( pathData[ pathSegmentIndex - 1 ].tEnd() ) ;
		
				segment.setTEnd( segment.tStart() + segLength / pongScan.velocity() ) ;
			}
		
			/*
			* The straight part of the segment.
			* Skip any segments that are basically zero-length.
			*/
			if( length > EPSILON )
			{
				pathSegmentIndex++ ;
				PongSegment segment = pathData[ pathSegmentIndex ] ;
				segment.setType( PongSegment.STRAIGHT ) ;
				PongStraightSegment straight = segment.straight() ;
				straight.setXStart( xStart ) ;
				straight.setYStart( yStart ) ;
				straight.setXEnd( xEnd ) ;
				straight.setYEnd( yEnd ) ;
				straight.setMx( mx ) ;
				straight.setMy( my ) ;
				pathLength += length ;
				// TSTART for path segments after the first is the TEND from the previous segment
				if( pathSegmentIndex > 0 )
					segment.tstart = pathData[ pathSegmentIndex - 1 ].tEnd() ;
		
				segment.setTEnd( segment.tStart() + length / pongScan.velocity() ) ;
			}
		
			/*
			* For ROUNDED_PONG where the straight-line segment is non-zero,
			* put an arc at the end of the segment
			*/
			if( PongScan.ROUNDED.equals( pongScan.type() ) && length > EPSILON )
			{
				pathSegmentIndex++ ;
				PongSegment segment = pathData[ pathSegmentIndex ] ;
				segment.setType( PongSegment.ARC ) ;
				PongArcSegment arc = segment.arc() ;
				arc.setXCenter( xEndArcCenter ) ;
				arc.setYCenter( yEndArcCenter ) ;
				arc.setRadius( curveRadius ) ;
				arc.setThetaInit( endThetaInit ) ;
				arc.setThetaFin( endThetaFin ) ;
				arc.setRotDir( endRotDir ) ;
				segLength = D2PI * curveRadius * arcLength / 360. ;
				pathLength += segLength ;
				segment.setTStart( pathData[ pathSegmentIndex - 1 ].tEnd() ) ;
				segment.setTEnd( segment.tStart() + segLength / pongScan.velocity() ) ;
			}
		}
		// Save the number of path segments and the pointer to the path_data array
  		pongScan.setNumPathSegments( pathSegmentIndex + 1 ) ;
  		pongScan.setPathData( pathData ) ;

		// Compute the time required to complete one complete circuit of the SQUARE or ROUNDED PONG path
		pongScan.setPeriod( pathLength / pongScan.velocity() ) ;

		return pongScan ;
	}

	public class PongScan
	{
		public final static String CURVY = "CURVY" ;
		public final static String ROUNDED = "ROUNDED" ;
		public final static String SQUARE = "SQUARE" ;
		public final static String STRAIGHT = "STRAIGHT" ;
		public final String[] types = { CURVY , ROUNDED , SQUARE , STRAIGHT } ;
		
		private double height ;
		private double width ;
		private double spacing ;
		private double velocity ;
		private int nSeg ;
		private double gridSpace ;
		private int xNGridSeg ;
		private int yNGridSeg ;
		private double[] xVertex ;
		private double[] yVertex ;
		private double alphaX ;
		private double alphaY ;
		private double betaX ;
		private double betaY ;
		private double period ;
		private int numPathSegments ;
		private String type ;
		private PongSegment[] pathData ;

		public double height(){ return height ; }
		public double width(){ return width ; }
		public double spacing(){ return spacing ; }
		public double velocity(){ return velocity ; }
		public int nSeg(){ return nSeg ; }
		public double gridSpace(){ return gridSpace ; }
		public int xNGridSeg(){ return xNGridSeg ; }
		public int yNGridSeg(){ return yNGridSeg ; }
		public double[] xVertex(){ return xVertex ; }
		public double[] yVertex(){ return yVertex ; }
		public double alphaX(){ return alphaX ; }
		public double alphaY(){ return alphaY ; }
		public double betaX(){ return betaX ; }
		public double betaY(){ return betaY ; }
		public double period(){ return period ; }
		public int numPathSegments(){ return numPathSegments ; }
		public String type(){ return type ; }
		public String[] types(){ return types ; }
		public PongSegment[] pathData(){ return pathData ; }

		public void setHeight( double value ){ height = value ; }
		public void setWidth( double value ){ width = value ; }
		public void setSpacing( double value ){ spacing = value ; }
		public void setVelocity( double value ){ velocity = value ; }		
		public void setNSeg( int value ){ nSeg = value ; }
		public void setGridSpace( double value ){ gridSpace = value ; }
		public void setXNGridSeg( int value ){ xNGridSeg = value ; }
		public void setYNGridSeg( int value ){ yNGridSeg = value ; }
		public void setXVertex( double[] value ){ xVertex = value ; }
		public void setYVertex( double[] value ){ yVertex = value ; }
		public void setAlphaX( double value ){ alphaX = value ; }
		public void setAlphaY( double value ){ alphaY = value ; }
		public void setBetaX( double value ){ betaX = value ; }
		public void setBetaY( double value ){ betaY = value ; }
		public void setPeriod( double value ){ period = value ; }
		public void setNumPathSegments( int value ){ numPathSegments = value ; }
		public void setType( String value )
		{
			for( String candidate : types )
			{
				if( candidate.equals( value ) )
				{
					type = value ;
					break ;
				}
			}
			if( type == null )
				throw new RuntimeException( value + " is not a valid type." ) ;
		}
		public void setPathData( PongSegment[] value ){ pathData = value ; }
	}
	
	public int[] setMostLeast( int a , int b )
	{
		int most = b ;
		int least = a ;
		if( a >= b )
		{
			most = a ;
			least = b ;
		}
		return new int[]{ most , least } ;
	}
	
	// P R I V A T E   C L A S S E S
	
	private class PongSegment
	{
		public static final String STRAIGHT = "STRAIGHT" ;
		public static final String ARC = "ARC" ;
		private String[] types = { STRAIGHT , ARC } ;
		
		private double tstart ;
		private double tend ;
		private PongArcSegment arc = new PongArcSegment() ;
		private PongStraightSegment straight = new PongStraightSegment() ;
		private String type ;

		public double tStart(){ return tstart ; }
		public double tEnd(){ return tend ; }
		public PongArcSegment arc(){ return arc ; }
		public PongStraightSegment straight(){ return straight ; }
        public String type(){ return type ; }

		public void setTStart( double value ){ tstart = value ; }
		public void setTEnd( double value ){ tend = value ; }
        public void setArc( PongArcSegment value ){ arc = value ; }
        public void setStraight( PongStraightSegment value ){ straight = value ; }
		public void setType( String value )
		{
			for( String candidate : types )
			{
				if( candidate.equals( value ) )
				{
					type = value ;
					break ;
				}
			}
		}
	}

	private class PongArcSegment
	{
		private double xCenter ;
		private double yCenter ;
		private double radius ;
		private double thetaInit ;
		private double thetaFin ;
		private double rotDir ;

        public double xCenter(){ return xCenter ; }
        public double yCenter(){ return yCenter ; }
        public double radius(){ return radius ; }
        public double thetaInit(){ return thetaInit ; }
        public double thetaFin(){ return thetaFin ; }
        public double rotDir(){ return rotDir ; }

		public void setXCenter( double value ){ xCenter = value ; }
		public void setYCenter( double value ){ yCenter = value ; }
		public void setRadius( double value ){ radius = value ; }
		public void setThetaInit( double value ){ thetaInit = value ; }
		public void setThetaFin( double value ){ thetaFin = value ; }
		public void setRotDir( double value ){ rotDir = value ; }
	}

	private class PongStraightSegment
	{
		private double xStart ;
		private double yStart ;
		private double xEnd ;
		private double yEnd ;
		private double mx ;
		private double my ;

        public double xStart(){ return xStart ; }
        public double yStart(){ return yStart ; }
        public double xEnd(){ return xEnd ; }
        public double yEnd(){ return yEnd ; }
        public double mx(){ return mx ; }
        public double my(){ return my ; }

		public void setXStart( double value ){ xStart = value ; }
		public void setYStart( double value ){ yStart = value ; }
		public void setXEnd( double value ){ xEnd = value ; }
		public void setYEnd( double value ){ yEnd = value ; }
		public void setMx( double value ){ mx = value ; }
		public void setMy( double value ){ my = value ; }
	}
}
