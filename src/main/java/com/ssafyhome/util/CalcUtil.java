package com.ssafyhome.util;

import com.ssafyhome.util.object.Circle;
import com.ssafyhome.util.object.Point;
import org.geotools.referencing.GeodeticCalculator;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CalcUtil {

	public Circle getMinimumBoundedCircle(List<Point> dongBoundaryPoints) {

		return WelzlsAlgorithm.getCircle(ConvexHullAlgorithm.getPoints(dongBoundaryPoints));
	}
}

class ConvexHullAlgorithm {

	protected static List<Point> getPoints(List<Point> points) {

		Point base = points.get(0);
		for (Point point : points) {
			if (point.getLat() < base.getLat() || (point.getLat() == base.getLat() && point.getLng() < base.getLng())) {
				base = point;
			}
		}

		Point finalBase = base;
		Point[] sortedPoint = points.stream()
				.sorted((point1, point2) -> {
					if (point1 == finalBase) return -1;
					if (point2 == finalBase) return 1;

					int ccw = counterClockWise(finalBase, point1, point2);
					return ccw == 0 ? checkDistance(finalBase, point1, point2) : ccw;
				})
				.toArray(Point[]::new);

		LinkedList<Point> stack = new LinkedList<>();
		stack.addLast(sortedPoint[0]);
		stack.addLast(sortedPoint[1]);

		for (int i = 2; i < points.size(); i++) {
			while(stack.size() >= 2
					&& counterClockWise(stack.get(stack.size() - 2), stack.getLast(), sortedPoint[i]) >= 0
			) {
				stack.removeLast();
			}
			stack.addLast(sortedPoint[i]);
		}

		return new ArrayList<>(stack);
	}

	private static int counterClockWise(Point base, Point point1, Point point2) {

		double angle1 = getLeftAngle(base, point1);
		double angle2 = getLeftAngle(base, point2);
		return Double.compare(angle1, angle2);
	}

	private static double getLeftAngle(Point base, Point point) {

		return Math.atan2(point.getLat() - base.getLat(), point.getLng() - base.getLng());
	}

	private static int checkDistance(Point base, Point point1, Point point2) {

		double dist1 = getSquaredDistance(base, point1);
		double dist2 = getSquaredDistance(base, point2);
		return Double.compare(dist1, dist2);
	}

	private static double getSquaredDistance(Point point1, Point point2) {

		double dx = point1.getLat() - point2.getLat();
		double dy = point1.getLng() - point2.getLng();
		return dx * dx + dy * dy;
	}
}

class WelzlsAlgorithm {

	protected static final double EPSILON = 1e-9;

	public static Circle getCircle(List<Point> convexHullPoints) {

		List<Point> triPoints = new ArrayList<>();
		return welzlHelper(convexHullPoints, triPoints, convexHullPoints.size());
	}

	private static Circle welzlHelper(List<Point> requiredPoints, List<Point> circlePoints, int left) {

		if (left == 0 || circlePoints.size() == 3) {
			return trivial(circlePoints);
		}

		int i = new Random().nextInt(left);
		Point point = requiredPoints.get(i);
		Collections.swap(requiredPoints, i, left - 1);

		Circle D = welzlHelper(requiredPoints, circlePoints, left - 1);
		if (D != null && isInside(D, point)) {
			return D;
		}

		circlePoints.add(point);
		D = welzlHelper(requiredPoints, circlePoints, left - 1);
		circlePoints.remove(circlePoints.size() - 1);

		return D;
	}

	private static Circle trivial(List<Point> circlePoints) {
		switch (circlePoints.size()) {
			case 1 -> {
				return new Circle(circlePoints.get(0), 0);
			}
			case 2 -> {
				Point center = new Point(
						(circlePoints.get(0).getLat() + circlePoints.get(1).getLat()) / 2,
						(circlePoints.get(0).getLng() + circlePoints.get(1).getLng()) / 2
				);
				return new Circle(
						center,
						calcDistance(center, circlePoints.get(0))
				);
			}
			case 3 -> {
				Point point1 = circlePoints.get(0);
				Point point2 = circlePoints.get(1);
				Point point3 = circlePoints.get(2);
				double division = (
						point1.getLat() * (point2.getLng() - point3.getLng())
						+ point2.getLat() * (point3.getLng() - point1.getLng())
						+ point3.getLat() * (point1.getLng() - point2.getLng())
				) * 2;

				if (Math.abs(division) < EPSILON) {
					return null;
				}

				Point center = new Point(
						(
								(point1.getLat() * point1.getLat() + point1.getLng() * point1.getLng()) * (point2.getLng() - point3.getLng())
								+ (point2.getLat() * point2.getLat() + point2.getLng() * point2.getLng()) * (point3.getLng() - point1.getLng())
								+ (point3.getLat() * point3.getLat() + point3.getLng() * point3.getLng()) * (point1.getLng() - point2.getLng())
						) / division,
						(
								(point1.getLat() * point1.getLat() + point1.getLng() * point1.getLng()) * (point3.getLat() - point2.getLat())
								+ (point2.getLat() * point2.getLat() + point2.getLng() * point2.getLng()) * (point1.getLat() - point3.getLat())
								+ (point3.getLat() * point3.getLat() + point3.getLng() * point3.getLng()) * (point2.getLat() - point1.getLat())
						) / division
				);

				return new Circle(
						center,
						calcDistance(center, circlePoints.get(0))
				);
			}
			default -> {
				return null;
			}
		}
	}

	private static boolean isInside(Circle c, Point point) {

		return c.getRadius() >= calcDistance(c.getCenter(), point);
	}

	private static double calcDistance(Point point1, Point point2) {

		GeodeticCalculator calculator = new GeodeticCalculator(DefaultGeographicCRS.WGS84);
		calculator.setStartingGeographicPoint(point1.getLng(), point1.getLat());
		calculator.setDestinationGeographicPoint(point2.getLng(), point2.getLat());
		return calculator.getOrthodromicDistance();
	}
}
