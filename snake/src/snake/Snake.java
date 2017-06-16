package snake;

import static snake.Constants.SNAKE_BONE_GAP;
import static snake.Constants.SNAKE_BONE_SIZE;
import static snake.Constants.SNAKE_INITIAL_DURATION;
import static snake.Constants.SNAKE_INITIAL_LENGTH;
import static snake.Constants.SNAKE_INITIAL_POSITION_X;
import static snake.Constants.SNAKE_INITIAL_POSITION_Y;
import static snake.Constants.WINDOW_HEIGHT;
import static snake.Constants.WINDOW_WIDTH;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

enum BoneType {

	HEAD, TAIL, BODY

}

class SnakeBone extends Rectangle {

	PathTransition transition;

	BoneType boneType;
	
	SnakeBone(){
		super();
	}
	
	SnakeBone (double x, double y){
		
		super();
	
		this.setArcHeight(10);
		this.setArcWidth(0);
		this.setWidth(SNAKE_BONE_SIZE);
		this.setHeight(SNAKE_BONE_SIZE);
		this.setX(x);
		this.setY(y);
		this.setFill(Color.WHITESMOKE);
		//bone.setEffect(new Lighting());

		Path path = new Path();

		PathTransition pathTransition = new PathTransition();
		pathTransition.setDuration(Duration.millis(SNAKE_INITIAL_DURATION));
		pathTransition.setPath(path);
		pathTransition.setNode(this);
		pathTransition.setOrientation(PathTransition.OrientationType.NONE);
		pathTransition.setCycleCount(1);
		pathTransition.setAutoReverse(false);
		pathTransition.setInterpolator(new SnakeInterpolator());

		this.transition = pathTransition;
		
	}
	
}

enum Direction {

	WEST, EAST, SOUTH, NORTH
}

class SnakeInterpolator extends Interpolator {

	private double timeFraction;

	@Override
	protected double curve(double t) {

		this.timeFraction = t;
		return t;
	}

	public double getTimeFraction() {

		return this.timeFraction;
	}

}

/**
 * 
 * @author WanX
 * 
 *         A snake class which contains a list of snakeBone, length, speed and
 *         direction.
 *
 */
public class Snake extends Group {
	
	boolean isRunning = false;

	private double speed = SNAKE_INITIAL_POSITION_X / SNAKE_INITIAL_DURATION;

	private double length = SNAKE_INITIAL_LENGTH;

	private Direction direction = Direction.WEST;

	private List<PathTransition> snakeTransition = new ArrayList<PathTransition>();

	public Snake() {
		
		super();

		double x = SNAKE_INITIAL_POSITION_X;
		double y = SNAKE_INITIAL_POSITION_Y;

		for (int i = 0; i < SNAKE_INITIAL_LENGTH + ((WINDOW_WIDTH*WINDOW_HEIGHT)/(SNAKE_BONE_SIZE*SNAKE_BONE_SIZE)/20); i++) {

			SnakeBone sb = new SnakeBone(x,y);

			if (i == 0) {
				sb.setId("HEAD");
				sb.boneType = BoneType.HEAD;
			} else if (i == SNAKE_INITIAL_LENGTH - 1) {
				sb.setId("TAIL");
				sb.boneType = BoneType.TAIL;
			} else {
				sb.setId("BODY");
				sb.boneType = BoneType.BODY;
			}
			
			if(i>=SNAKE_INITIAL_LENGTH)
				sb.setVisible(false);

			this.getChildren().add(sb);
			this.snakeTransition.add(sb.transition);

			x = x + SNAKE_BONE_SIZE + SNAKE_BONE_GAP;
		}

	}

	protected double getLength() {
		return length;
	}

	protected void setLength(double length) {
		this.length = length;
	}

	protected Direction getDirection() {
		return direction;
	}

	
	
	public SnakeBone getSnakeHead(){
		
		return (SnakeBone) this.getChildren().get(0);
	}

	/**
	 * 
	 * start the animation for going to specified direction
	 * 
	 * @param direction
	 * @return
	 */

	public void goDirection(Direction direction) {

		SnakeBone head = getSnakeHead();

//		if (!isAllowedToGo(head))
//			return;

		stopAnimation();

		this.direction = direction;

		double breakPointX = head.getTranslateX() + head.getX() + SNAKE_BONE_SIZE / 2;
		double breakPointY = head.getTranslateY() + head.getY() + SNAKE_BONE_SIZE / 2;

		// double roof = SNAKE_BONE_SIZE / 2;

		double edge = -(SNAKE_INITIAL_LENGTH * SNAKE_BONE_SIZE) + SNAKE_BONE_SIZE / 2;

		double distance = -1;
		double offset = 0;

		double oldDuration = SNAKE_INITIAL_DURATION;
		double oldPathLength = SNAKE_INITIAL_POSITION_X;
		double traveledPathLength = 0;

		double newPathLength = breakPointX - edge;
		double newDuration = newPathLength * speed;

		for (Node node : this.getChildren()) {
			
			SnakeBone sb = (SnakeBone)node;

			ObservableList<PathElement> pList = ((Path) sb.transition.getPath()).getElements();

			// path is empty
			if (pList.size() == 0)
				pList.add(new MoveTo(sb.getTranslateX() + sb.getX() + SNAKE_BONE_SIZE / 2,
						sb.getTranslateY() + sb.getY() + SNAKE_BONE_SIZE / 2));

			oldDuration = sb.transition.getDuration().toMillis();
			oldPathLength = calculatePathLength(pList);
			traveledPathLength = ((SnakeInterpolator) sb.transition.getInterpolator()).getTimeFraction()
					* oldPathLength;

			PathElement endPoint = pList.get(pList.size() - 1);

			switch (direction) {

			case NORTH:

				if (endPoint instanceof LineTo) {
					LineTo lastLineTo = (LineTo) endPoint;
					lastLineTo.setX(breakPointX);
					lastLineTo.setY(breakPointY);
				}
				pList.add(new LineTo(breakPointX, edge));
				break;

			case EAST:
				if (endPoint instanceof LineTo) {
					LineTo lastLineTo = (LineTo) endPoint;
					lastLineTo.setX(breakPointX);
					lastLineTo.setY(breakPointY);
				}
				pList.add(new LineTo(WINDOW_WIDTH + 10 - edge, breakPointY));
				break;

			case SOUTH:
				if (endPoint instanceof LineTo) {
					LineTo lastLineTo = (LineTo) endPoint;
					lastLineTo.setX(breakPointX);
					lastLineTo.setY(breakPointY);
				}
				pList.add(new LineTo(breakPointX, WINDOW_HEIGHT + 10 - edge));
				break;

			case WEST:
				if (endPoint instanceof LineTo) {
					LineTo lastLineTo = (LineTo) endPoint;
					lastLineTo.setX(breakPointX);
					lastLineTo.setY(breakPointY);
				}
				pList.add(new LineTo(edge, breakPointY));
				break;

			default:
				break;

			}

			if (traveledPathLength != 0) {
				newPathLength = calculatePathLength(pList);
				newDuration = newPathLength / oldPathLength * oldDuration;
			}

			/*
			System.out.println("Travled : " + traveledPathLength);
			System.out.println("New path length : " + newPathLength);
			System.out.println("New duration: " + newDuration);
			System.out.println("Jump to: " + newDuration * traveledPathLength / newPathLength);
			*/
			sb.transition.jumpTo(Duration.millis(newDuration * traveledPathLength / newPathLength));

			if (distance < 0)
				distance = newPathLength;

			sb.transition.setDuration(Duration.millis(distance / speed));

			offset = SNAKE_BONE_SIZE + SNAKE_BONE_GAP;
			edge = edge + offset;
		}

		playAnimation();
		isRunning = true;

	}
	
	
	public void grow(){
		
		
		for (Node node : this.getChildren()) {
			
			SnakeBone sb = (SnakeBone)node;
			
			if(sb.isVisible())
				continue;
			else{
				sb.setVisible(true);
				break;
			}
		}
		
		
		
//		//pauseAnimation();
//		
//		SnakeBone oldTail = (SnakeBone)this.getChildren().get(this.getChildren().size()-1);
//		
//		//MoveTo start = (MoveTo)((Path)oldTail.transition.getPath()).getElements().get(0);    
//				
//		oldTail.setId("BODY");
//		oldTail.boneType = BoneType.BODY;
//		
//		double x = oldTail.getX() + SNAKE_BONE_SIZE + SNAKE_BONE_GAP;
//		double y = oldTail.getY();
//		
//		SnakeBone newTail = new SnakeBone(x,y);
//		newTail.setId("TAIL");
//		newTail.boneType = BoneType.TAIL;
//		
//		newTail.transition.setInterpolator(oldTail.transition.getInterpolator());
//		
//		ObservableList<PathElement> pList = ((Path)newTail.transition.getPath()).getElements();
//		
//		for(PathElement pe : ((Path)oldTail.transition.getPath()).getElements()){
//			
//			if(pe instanceof MoveTo){
//				
//				MoveTo newStart = new MoveTo();
//				
//				newStart.setX(newTail.getX() + SNAKE_BONE_SIZE / 2);
//				newStart.setY(newTail.getY() + SNAKE_BONE_SIZE / 2);
//				
//				pList.add(newStart);
//				
//			}
//			
//			else 
//				pList.add(pe);
//		}
//
//		this.getChildren().add(newTail);
//		this.snakeTransition.add(newTail.transition);
//		
//		//this.getChildren().add(newTail);
//		
//		goDirection(this.direction);
		
	}
	
	public void speedUp(){
		
		this.speed = speed*2;
	}
	

	private boolean isAllowedToGo(SnakeBone head) {

		ObservableList<PathElement> pList = ((Path) head.transition.getPath()).getElements();

		if (pList.size() < 3)
			return true;

		LineTo lastSecondPoint = (LineTo) pList.get(pList.size() - 2);

		double breakPointX = head.getTranslateX() + head.getX() + SNAKE_BONE_SIZE / 2;
		double breakPointY = head.getTranslateY() + head.getY() + SNAKE_BONE_SIZE / 2;

		double traveledPathLength = Math.abs(breakPointX - lastSecondPoint.getX())
				+ Math.abs(breakPointY - lastSecondPoint.getY());

		if (traveledPathLength > SNAKE_BONE_SIZE)
			return true;

		return false;
	}

	public void playAnimation() {
		for (PathTransition pt : snakeTransition)
			pt.play();

	}

	public void stopAnimation() {
		isRunning=false;
		for (PathTransition pt : snakeTransition)
			pt.stop();
	}
	
	public void pauseAnimation() {
		isRunning=false;
		for (PathTransition pt : snakeTransition)
			pt.pause();
	}

	/**
	 * calculate the total length of the path
	 * 
	 * @param pList
	 * @return
	 */
	private double calculatePathLength(ObservableList<PathElement> pList) {

		double totalLength = 0;

		if (pList.size() > 1) {

			MoveTo firstPoint = (MoveTo) pList.get(0);

			Point2D from = new Point2D(firstPoint.getX(), firstPoint.getY());

			for (int i = 1; i < pList.size(); i++) {

				Point2D to = new Point2D(((LineTo) pList.get(i)).getX(), ((LineTo) pList.get(i)).getY());

				totalLength = totalLength + from.distance(to);

				from = to;

			}

		}
		return totalLength;
	}

	protected void displaySnakeInfo() {

		System.out.println("Snake info ---------------------> ");

		System.out.println("Snake length : " + getLength());

		System.out.println("Snake speed : " + speed);

		System.out.println("Snake moving direction : " + getDirection());

		for (Node node: this.getChildren()) {
			
			SnakeBone sb = (SnakeBone)node;
			System.out.println("Bone type : " + sb.boneType);
			System.out.println(sb.getTranslateX() + ", " + sb.getTranslateY());
		}

		System.out.println("<--------------------- Snake Info");

	}

	protected void displaySnakeBoneGap() {

		SnakeBone previous = null;

		int count = 1;

		for (Node node : this.getChildren()) {

			SnakeBone sb = (SnakeBone)node;
			if (previous == null)
				previous = sb;

			if (previous != null) {

				System.out.println(
						"Bone gap" + count + " on x " + (sb.getTranslateX() - previous.getTranslateX()));
				System.out.println(
						"Bone gap" + count + " on y " + (sb.getTranslateY() - previous.getTranslateY()));
				count++;
				previous = sb;
			}

		}

	}

}
