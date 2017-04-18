package snake;

import static snake.Constants.*;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.effect.Lighting;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

enum BoneType {

	HEAD, TAIL, BODY

}

class SnakeBone {

	Rectangle bone;

	Transition transition;

	BoneType boneType;

}

enum Direction {
	WEST, EAST, SOUTH, NORTH
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
	
	private ParallelTransition snakeTransition = new ParallelTransition();

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

			/*
			TranslateTransition tt = new TranslateTransition(Duration.millis(SNAKE_INITIAL_DURATION), bone);
			tt.setCycleCount(1);
			tt.setAutoReverse(false);
			tt.setInterpolator(Interpolator.LINEAR);

			tt.setToX(-SNAKE_INITIAL_POSITION_X);

			sb.transition = tt;
			*/

			this.body.add(sb);

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
	 * @return
	 */
	protected ParallelTransition getAnimation() {

		ParallelTransition pt = new ParallelTransition();

		if (!this.body.isEmpty())

			for (SnakeBone sb : body)

				pt.getChildren().add(sb.transition);

		return pt;
	}

	public ParallelTransition goDirection(Direction direction) {

		// displaySnakeBoneGap();

		snakeTransition.getChildren().clear();
		this.direction = direction;

		double breakPointX = body.get(0).bone.getTranslateX() + body.get(0).bone.getX() + SNAKE_BONE_SIZE / 2;
		double breakPointY = body.get(0).bone.getTranslateY() + body.get(0).bone.getY() + SNAKE_BONE_SIZE / 2;

		//System.out.println("breakPointX:" + breakPointX);
		//System.out.println("breakPointY:" + breakPointY);

		double roof = SNAKE_BONE_SIZE / 2;

		double distance = -1;

		double offset = 0;

		for (SnakeBone sb : body) {

			Path path = new Path();

			path.getElements().add(new MoveTo(sb.bone.getTranslateX() + sb.bone.getX() + SNAKE_BONE_SIZE / 2,
					sb.bone.getTranslateY() + sb.bone.getY() + SNAKE_BONE_SIZE / 2));

			if (sb.boneType != BoneType.HEAD)
				path.getElements().add(new LineTo(breakPointX, breakPointY));

			switch (direction) {

			case NORTH:
				path.getElements().add(new LineTo(breakPointX, roof));
				System.out.println("LineTo:" + breakPointX + "," + roof);
				if(distance<0)
					distance = breakPointY;
				break;
			case EAST:
				path.getElements().add(new LineTo(WINDOW_WIDTH+10 - roof, breakPointY));
				System.out.println("LineTo:" + (WINDOW_WIDTH+10 - roof) + "," + breakPointY);
				if(distance<0)
					distance = WINDOW_WIDTH-breakPointX;
				break;
			case SOUTH:
				path.getElements().add(new LineTo(breakPointX, WINDOW_HEIGHT+10 - roof));
				System.out.println("LineTo:" + breakPointX + "," + (WINDOW_HEIGHT+10 - roof));
				if(distance<0)
					distance = WINDOW_HEIGHT-breakPointY;
				break;
			case WEST:
				path.getElements().add(new LineTo(roof, breakPointY));
				System.out.println("LineTo:" + roof + "," + breakPointY);
				if(distance<0)
					distance = breakPointX;
				break;
			default:
				break;

			}

			offset = SNAKE_BONE_SIZE + SNAKE_BONE_GAP;
			roof = roof + offset;

			PathTransition pathTransition = new PathTransition();
			pathTransition.setDuration(Duration.millis(distance/speed));
			pathTransition.setPath(path);
			pathTransition.setNode(sb.bone);
			pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
			pathTransition.setCycleCount(1);
			pathTransition.setAutoReverse(false);
			pathTransition.setInterpolator(Interpolator.LINEAR);

			sb.transition = pathTransition;
			
			snakeTransition.getChildren().add(pathTransition);

		}

		snakeTransition.play();
		
		return snakeTransition;

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