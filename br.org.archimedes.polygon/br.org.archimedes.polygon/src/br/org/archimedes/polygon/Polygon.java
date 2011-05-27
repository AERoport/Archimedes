package br.org.archimedes.polygon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.org.archimedes.exceptions.InvalidArgumentException;
import br.org.archimedes.exceptions.NullArgumentException;
import br.org.archimedes.gui.opengl.OpenGLWrapper;
import br.org.archimedes.line.Line;
import br.org.archimedes.model.Element;
import br.org.archimedes.model.Point;
import br.org.archimedes.model.Rectangle;
import br.org.archimedes.model.ReferencePoint;
import br.org.archimedes.model.references.CirclePoint;
import br.org.archimedes.model.references.RhombusPoint;

public class Polygon extends Element {

	private Point center;
	private Point initialPoint;
	private int sides;

	public Polygon(Point center, Point initialPoint, int sides)
			throws NullArgumentException, InvalidArgumentException {

		if ((center == null) || (initialPoint == null)) {
			throw new NullArgumentException();
		} else {
			this.center = center;
			this.initialPoint = initialPoint;
		}

		if (sides < 3) {
			throw new InvalidArgumentException();
		} else {
			this.sides = sides;
		}
	}

	public Point getCenter() {
		return center;
	}

	public Point getInitialPoint() {
		return initialPoint;
	}

	public int getSides() {
		return sides;
	}

	public List<Point> getVertexPoints() {

		List<Point> vertexPoints = new ArrayList<Point>();
		Point auxiliarPoint = getInitialPoint().clone();
		for (int i = 0; i < getSides(); i++) {
			try {
				auxiliarPoint.rotate(getCenter(), 2 * Math.PI / getSides());
				vertexPoints.add(auxiliarPoint.clone());
			} catch (NullArgumentException e) {
				e.printStackTrace();
			}
		}

		return vertexPoints;
	}

	@Override
	public Element clone() {

		Polygon clone = null;
		try {
			clone = new Polygon(this.center, this.initialPoint, this.sides);
		} catch (NullArgumentException e) {
			e.printStackTrace();
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
		}

		return clone;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Polygon other = (Polygon) obj;
		if (center == null) {
			if (other.center != null)
				return false;
		} else if (!center.equals(other.center))
			return false;
		if (initialPoint == null) {
			if (other.initialPoint != null)
				return false;
		} else if (!initialPoint.equals(other.initialPoint))
			return false;
		if (sides != other.sides)
			return false;
		return true;
	}

	@Override
	public Rectangle getBoundaryRectangle() {
		double radius = initialPoint.calculateDistance(center);
		double left = center.getX() - radius;
		double right = center.getX() + radius;
		double bottom = center.getY() - radius;
		double top = center.getY() + radius;
		return new Rectangle(left, bottom, right, top);
	}

	@Override
	public Collection<? extends ReferencePoint> getReferencePoints(
			Rectangle area) {

		Collection<ReferencePoint> references = new ArrayList<ReferencePoint>();

		try {
			ReferencePoint referencePoint = new CirclePoint(getCenter());
			references.add(referencePoint);

			for (Point point : getVertexPoints()) {
				referencePoint = new RhombusPoint(point);
				references.add(referencePoint);
			}

		} catch (NullArgumentException e) {

			e.printStackTrace();
		}

		return references;
	}

	@Override
	public Point getProjectionOf(Point point) throws NullArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean contains(Point point) throws NullArgumentException {

		boolean isInside = false;
		List<Point> vertexPoints = getVertexPoints();
		for (int i = 0, j = getSides() - 1; i < getSides(); j = i++) {
			Point vertexI = vertexPoints.get(i);
			Point vertexJ = vertexPoints.get(j);
			
			if (((vertexI.getY() > point.getY()) != (vertexJ.getY() > point.getY()))
					&& (point.getX() < (vertexJ.getX() - vertexI.getX()) * (point.getY() - vertexI.getY())
							/ (vertexJ.getY() - vertexI.getY()) + vertexI.getX()))
				isInside = !isInside;
		}
		return isInside;

	}

	@Override
	public List<Point> getPoints() {
		List<Point> points = new ArrayList<Point>();
		points.add(center);
		points.add(initialPoint);
		return points;
	}
	
	@Override
	public void draw(OpenGLWrapper wrapper) {
		List<Line> lines = getLines();
		for (Line line : lines) {
            line.draw(wrapper);
        }
	}

	public List<Line> getLines () {

		List<Point> vertexPoints = getVertexPoints();
        List<Line> lines = new ArrayList<Line>();
        
        Point p1;
        Point p2;
        int lastPosition = vertexPoints.size() -1;
        for (int i = 0; i < lastPosition; i++) {
            p1 = vertexPoints.get(i);
            p2 = vertexPoints.get(i + 1);
            try {
                lines.add(new Line(p1, p2));
            }
            catch (Exception e) {
                // Should never reach this code
                e.printStackTrace();
            }
        }
        p1 = vertexPoints.get(0);
        p2 = vertexPoints.get(lastPosition);
        try {
            lines.add(new Line(p1, p2));
        }
        catch (Exception e) {
            // Should never reach this code
            e.printStackTrace();
        }
        
        return lines;
    }
	
}