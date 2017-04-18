package snake;

import static snake.Constants.WINDOW_HEIGHT;
import static snake.Constants.WINDOW_WIDTH;

import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SnakeGame extends Application {

	public static void main(String[] args) {

		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("Snake");
		primaryStage.setResizable(false);

		Group root = new Group();

		Snake snake = new Snake();

		root.getChildren().add(snake.getSnakeBoneGroup());

		ParallelTransition pt = snake.goDirection(Direction.WEST);
		
		Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

		// scene.setFill(Color.DIMGRAY);

		scene.setFill(Color.BLACK);

		scene.setOnKeyPressed(event -> {

			KeyCode key = event.getCode();

			switch (key) {

			case UP:

				System.out.println("UP key pressed");
				if (snake.getDirection() == Direction.WEST || snake.getDirection() == Direction.EAST) {
					pt.stop();
					snake.goDirection(Direction.NORTH);
				}
				break;

			case DOWN:

				System.out.println("DOWN key pressed");
				if (snake.getDirection() == Direction.WEST || snake.getDirection() == Direction.EAST) {
					pt.stop();
					snake.goDirection(Direction.SOUTH);
				}
				break;

			case LEFT:

				System.out.println("LEFT key pressed");
				if (snake.getDirection() == Direction.NORTH || snake.getDirection() == Direction.SOUTH) {
					pt.stop();
					snake.goDirection(Direction.WEST);
				}
				break;

			case RIGHT:

				System.out.println("RIGHT key pressed");
				if (snake.getDirection() == Direction.NORTH || snake.getDirection() == Direction.SOUTH) {
					pt.stop();
					snake.goDirection(Direction.EAST);
				}
				break;

			default:
				break;

			}

			// snake.displaySnakeInfo();

		});

		final Rectangle rectPath = new Rectangle(0, 0, 40, 40);
		rectPath.setArcHeight(10);
		rectPath.setArcWidth(10);
		rectPath.setFill(Color.ORANGE);
		Path path = new Path();
		path.getElements().add(new MoveTo(20, 20));
		// path.getElements().add(new CubicCurveTo(380, 0, 380, 120, 200, 120));
		// path.getElements().add(new CubicCurveTo(0, 120, 0, 240, 380, 240));

		path.getElements().add(new LineTo(380, 20));
		path.getElements().add(new LineTo(380, 600));

		PathTransition pathTransition = new PathTransition();
		pathTransition.setDuration(Duration.millis(4000));
		pathTransition.setPath(path);
		pathTransition.setNode(rectPath);
		pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
		pathTransition.setCycleCount(Timeline.INDEFINITE);
		pathTransition.setAutoReverse(true);
		pathTransition.play();
		root.getChildren().add(rectPath);

		primaryStage.setScene(scene);
		primaryStage.show();

	}
}
