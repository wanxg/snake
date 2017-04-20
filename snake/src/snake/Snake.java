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
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.effect.Lighting;
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

class SnakeBone {

	Rectangle bone;

	PathTransition transition;

	BoneType boneType;

}

enum Direction {

	WEST, EAST, SOUTH, NORTH
}

class SnakeInterpolator extends Interpolator {
	
	private double timeFraction;
	
	@Override
	protected double curve(double t) {
		
		this.timeFraction=t;
		return t;
	}
	
	public double getTimeFraction(){
		
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
public class Snake {

	private List<SnakeBone> body = new ArrayList<SnakeBone>();

	private double speed = SNAKE_INITIAL_POSITION_X / SNAKE_INITIAL_DURATION;

	private double length = SNAKE_INITIAL_LENGTH;

	private Direction direction = Direction.WEST;

	private List<PathTransition> snakeTransition = new ArrayList<PathTransition>();

	public Snake() {

		double x = SNAKE_INITIAL_POSITION_X;
		double y = SNAKE_INITIAL_POSITION_Y;

		for (int i = 0; i < SNAKE_INITIAL_LENGTH; i++) {

			SnakeBone sb = new SnakeBone();

			Rectangle bone = new Rectangle();

			bone.setEffect(new Lighting());

			if (i == 0) {
				bone.setId("HEAD");
				sb.boneType = BoneType.HEAD;
			} else if (i == SNAKE_INITIAL_LENGTH - 1) {
				bone.setId("TAIL");
				sb.boneType = BoneType.TAIL;
			} else {
				bone.setId("BODY");
				sb.boneType = BoneType.BODY;
			}

			bone.setArcHeight(10);
			bone.setArcWidth(10);
			bone.setWidth(SNAKE_BONE_SIZE);
			bone.setHeight(SNAKE_BONE_SIZE);
			bone.setX(x);
			x = x + SNAKE_BONE_SIZE + SNAKE_BONE_GAP;
			bone.setY(y);
			bone.setFill(Color.WHITESMOKE);

			sb.bone = bone;

			Path path = new Path();

			PathTransition pathTransition = new PathTransition();
			pathTransition.setDuration(Duration.millis(SNAKE_INITIAL_DURATION));
			pathTransition.setPath(path);
			pathTransition.setNode(sb.bone);
			pathTransition.setOrientation(PathTransition.OrientationType.NONE);
			pathTransition.setCycleCount(1);
			pathTransition.setAutoReverse(false);
			pathTransition.setInterpolator(new SnakeInterpolator());

			sb.transition = pathTransition;

			this.body.add(sb);
			this.snakeTransition.add(pathTransition);

		}

	}

	protected List<SnakeBone> getBody() {
		return body;
	}

	protected void setBody(List<SnakeBone> body) {
		this.body = body;
	}

	protected double getSpeed() {
		return speed;
	}

	protected void setSpeed(double speed) {
		this.speed = speed;
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

	protected void setDirection(Direction direction) {
		this.direction = direction;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	protected Group getSnakeBoneGroup() {

		Group group = new Group();

		if (!this.body.isEmpty())

			for (SnakeBone sb : body)

				group.getChildren().add(sb.bone);

		return group;
	}
	
	/**
	 * 
	 *  start the animation for going to specified direction
	 * 
	 * @param direction
	 * @return
	 */

	public void goDirection(Direction direction) {

		stop();
		
		this.direction = direction;

		double breakPointX = body.get(0).bone.getTranslateX() + body.get(0).bone.getX() + SNAKE_BONE_SIZE / 2;
		double breakPointY = body.get(0).bone.getTranslateY() + body.get(0).bone.getY() + SNAKE_BONE_SIZE / 2;

		double roof = SNAKE_BONE_SIZE / 2;
		
		//double roof = -1000;
		
		double distance = -1;
		double offset = 0;
		
		double oldDuration=SNAKE_INITIAL_DURATION;
		double oldPathLength=SNAKE_INITIAL_POSITION_X;
		double traveledPathLength=0;

		for (SnakeBone sb : body) {

			Path path = (Path) sb.transition.getPath();

			ObservableList<PathElement> pList = path.getElements();

			if (pList.size() == 0)
				pList.add(new MoveTo(sb.bone.getTranslateX() + sb.bone.getX() + SNAKE_BONE_SIZE / 2,
						sb.bone.getTranslateY() + sb.bone.getY() + SNAKE_BONE_SIZE / 2));

			oldDuration = sb.transition.getDuration().toMillis();
			oldPathLength = calculatePathLength(pList);
			traveledPathLength = ((SnakeInterpolator)sb.transition.getInterpolator()).getTimeFraction()*oldPathLength;
			
			PathElement pe = pList.get(pList.size() - 1);

			
			switch (direction) {

			case NORTH:
				if (pe instanceof LineTo) {
					LineTo lastLineTo = (LineTo) pe;
					lastLineTo.setX(breakPointX);
				}
				pList.add(new LineTo(breakPointX, roof));
				break;
				
			case EAST:
				if (pe instanceof LineTo) {
					LineTo lastLineTo = (LineTo) pe;
					lastLineTo.setY(breakPointY);
				}
				pList.add(new LineTo(WINDOW_WIDTH + 10 - roof, breakPointY));
				break;
				
			case SOUTH:
				if (pe instanceof LineTo) {
					LineTo lastLineTo = (LineTo) pe;
					lastLineTo.setX(breakPointX);
				}
				pList.add(new LineTo(breakPointX, WINDOW_HEIGHT + 10 - roof));
				break;
				
			case WEST:
				if (pe instanceof LineTo) {
					LineTo lastLineTo = (LineTo) pe;
					lastLineTo.setY(breakPointY);
				}
				pList.add(new LineTo(roof, breakPointY));
				break;
				
			default:
				break;

			}
			
			if(traveledPathLength!=0){
				double newPathLength = calculatePathLength(pList);
				double newDuration = newPathLength / oldPathLength * oldDuration;
				
				System.out.println("Travled : " + traveledPathLength);
				System.out.println("New path length : " + newPathLength);
				System.out.println("New duration: " + newDuration);
				System.out.println("Jump to: " + newDuration * traveledPathLength / newPathLength);
				
				sb.transition.jumpTo(Duration.millis(newDuration * traveledPathLength / newPathLength));
			
				if (distance < 0)
					distance = newPathLength;
				
				sb.transition.setDuration(Duration.millis(distance / speed));
			}

			offset = SNAKE_BONE_SIZE + SNAKE_BONE_GAP;
			roof = roof + offset;

			

		}

		play();

	}
	
	private void play() {
		for(PathTransition pt : snakeTransition)
			pt.play();
		
	}

	private void stop() {
		for(PathTransition pt : snakeTransition)
			pt.stop();
	}

	/**
	 *  calculate the total length of the path
	 * 
	 * @param pList
	 * @return
	 */
	private double calculatePathLength(ObservableList<PathElement> pList){
		
		double totalLength=0;
		
		if(pList.size()>1){
			
			MoveTo firstPoint = (MoveTo)pList.get(0);
			
			Point2D from = new Point2D(firstPoint.getX(),firstPoint.getY());
			
			for(int i = 1; i<pList.size();i++){
				
				Point2D to = new Point2D(((LineTo)pList.get(i)).getX(), ((LineTo)pList.get(i)).getY());
				
				totalLength = totalLength + from.distance(to);
				
				from = to;
				
			}
			
		}
		return totalLength;
	}
	

	protected void displaySnakeInfo() {

		System.out.println("Snake info ---------------------> ");

		System.out.println("Snake length : " + getLength());

		System.out.println("Snake speed : " + getSpeed());

		System.out.println("Snake moving direction : " + getDirection());

		for (SnakeBone sb : body) {
			System.out.println("Bone type : " + sb.boneType);
			System.out.println(sb.bone.getTranslateX() + ", " + sb.bone.getTranslateY());
		}

		System.out.println("<--------------------- Snake Info");

	}

	protected void displaySnakeBoneGap() {

		SnakeBone previous = null;

		int count = 1;

		for (SnakeBone sb : body) {

			if (previous == null)
				previous = sb;

			if (previous != null) {

				System.out.println(
						"Bone gap" + count + " on x " + (sb.bone.getTranslateX() - previous.bone.getTranslateX()));
				System.out.println(
						"Bone gap" + count + " on y " + (sb.bone.getTranslateY() - previous.bone.getTranslateY()));
				count++;
				previous = sb;
			}

		}

	}

}
