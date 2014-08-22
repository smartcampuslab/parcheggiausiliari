package eu.trentorise.smartcampus.parcheggiausiliari.util;

import java.util.ArrayList;
import java.util.List;

import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import smartcampus.vas.parcheggiausiliari.android.R;
import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Street;

/**
 * subclass of {@link Polyline} which adds the possibility to check if the click
 * made may be referring to the line ( by checking the distance from it) and the
 * possibility to create a new Polyline from a <a href="https://developers.google.com/maps/documentation/utilities/polylinealgorithm">encoded polyline</a>
 * 
 * @author Michele Armellini
 * 
 */
public class MyPolyline extends Polyline {

	private Street obj;
	private SinglePopup iPopup;

	public MyPolyline(Context ctx, Street obj, SinglePopup iPopup) {
		super(ctx);
		// TODO Auto-generated constructor stub
		this.obj = obj;
		this.iPopup = iPopup;
	}

	public MyPolyline(ResourceProxy resourceProxy, Street obj,
			SinglePopup iPopup) {
		super(resourceProxy);
		// TODO Auto-generated constructor stub
		this.obj = obj;
		this.iPopup = iPopup;
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e, MapView mapView) {
		// TODO Auto-generated method stub

		if (isOnLine(mapView.getProjection().fromPixels((int) e.getX(),
				(int) e.getY()))) {
			iPopup.openPopup(obj);
		}
		return super.onLongPress(e, mapView);
	}

	/**
	 * method to check if the touch happened near the line
	 * @param Geopoint containing the coordinates of where the user touched
	 * @return true if the point is near the line, otherwhise false
	 */
	public boolean isOnLine(IGeoPoint point) {
		boolean toRtn = false;
		ArrayList<GeoPoint> mPoints = (ArrayList<GeoPoint>) getPoints();
		for (int i = 0; i < getNumberOfPoints() - 1; i++) {
			GeoPoint a = mPoints.get(i);
			GeoPoint b = mPoints.get(i + 1);
			toRtn = toRtn || (linePointDist(a, b, point, true) <= 0.00035);
		}
		return toRtn;
	}

	// Compute the dot product AB x AC
	private double dot(IGeoPoint A, IGeoPoint B, IGeoPoint C) {
		double[] AB = new double[2];
		double[] BC = new double[2];
		AB[0] = B.getLongitude() - A.getLongitude();
		AB[1] = B.getLatitude() - A.getLatitude();
		BC[0] = C.getLongitude() - B.getLongitude();
		BC[1] = C.getLatitude() - B.getLatitude();
		double dot = AB[0] * BC[0] + AB[1] * BC[1];
		return dot;
	}

	// Compute the cross product AB x AC
	private double cross(IGeoPoint A, IGeoPoint B, IGeoPoint C) {
		double[] AB = new double[2];
		double[] AC = new double[2];
		AB[0] = B.getLongitude() - A.getLongitude();
		AB[1] = B.getLatitude() - A.getLatitude();
		AC[0] = C.getLongitude() - A.getLongitude();
		AC[1] = C.getLatitude() - A.getLatitude();
		double cross = AB[0] * AC[1] - AB[1] * AC[0];
		return cross;
	}

	// Compute the distance from A to B
	private double distance(IGeoPoint A, IGeoPoint B) {
		double d1 = A.getLongitude() - B.getLongitude();
		double d2 = A.getLatitude() - B.getLatitude();
		return Math.sqrt(d1 * d1 + d2 * d2);
	}

	// Compute the distance from AB to C
	// if isSegment is true, AB is a segment, not a line.
	private double linePointDist(IGeoPoint A, IGeoPoint B, IGeoPoint C,
			boolean isSegment) {
		double dist = cross(A, B, C) / distance(A, B);
		if (isSegment) {
			double dot1 = dot(A, B, C);
			if (dot1 > 0)
				return distance(B, C);
			double dot2 = dot(B, A, C);
			if (dot2 > 0)
				return distance(A, C);
		}
		return Math.abs(dist);
	}

	/**
	 * method to decode an encoded polyline into a list of geopoint on which the line passes on
	 * @param encoded the encoded polyline
	 * @return a {@link List} of {@link GeoPoint} the line passes on
	 */
	private static List<GeoPoint> decodePoly(String encoded) {
		List<GeoPoint> poly = new ArrayList<GeoPoint>();

		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;

		while (index < len) {
			int b, shift = 0, result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;

			shift = 0;
			result = 0;
			if (index >= len) {
				break;
			}
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			GeoPoint p = new GeoPoint(lat * 10, lng * 10);
			poly.add(p);
		}

		return poly;
	}

	/**
	 * method who creates a polyline from an encoded string
	 * @param ctx
	 * @param encoded the encoded polyline
	 * @param obj the Street related to the line
	 * @param iPopup Interface used to keep only one popup open at a time
	 * @return a {@link MyPolyline} created from the encoded points
	 */
	public static MyPolyline decode(Context ctx, String encoded, Street obj,
			SinglePopup iPopup) {
		MyPolyline toRtn = new MyPolyline(ctx, obj, iPopup);
		toRtn.setPoints(decodePoly(encoded));
		toRtn.setWidth(5.5f);
		int c = ctx.getResources().getColor(R.color.action_bar);
		toRtn.setColor(Color.argb(100, Color.red(c), Color.green(c),
				Color.blue(c)));
		return toRtn;
	}

}
