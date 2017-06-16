package snake;

import static snake.Constants.WINDOW_HEIGHT;
import static snake.Constants.WINDOW_WIDTH;

import java.util.concurrent.Callable;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

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

		root.getChildren().add(snake);

		Apple apple = new Apple();

		root.getChildren().add(apple);

		Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

		// scene.setFill(Color.DIMGRAY);

		scene.setFill(Color.BLACK);

		ObservableBooleanValue colliding = Bindings.createBooleanBinding(new Callable<Boolean>() {

			@Override
			public Boolean call() throws Exception {
				return snake.getSnakeHead().getBoundsInParent().intersects(apple.getBoundsInParent());
			}

		}, snake.getSnakeHead().boundsInParentProperty(), apple.boundsInParentProperty());

		colliding.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> obs, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					System.out.println("Colliding");

					root.getChildren().remove(apple);
					apple.placeToRandomLocation();
					root.getChildren().add(apple);
					

				} else {
					System.out.println("Not colliding");
					snake.grow();

				}
			}
		});

		scene.setOnKeyPressed(event -> {

			KeyCode key = event.getCode();

			switch (key) {

			case UP:

				if (snake.isRunning
						&& (snake.getDirection() == Direction.WEST || snake.getDirection() == Direction.EAST)) {
					snake.goDirection(Direction.NORTH);
				}
				break;

			case DOWN:

				if (snake.isRunning
						&& (snake.getDirection() == Direction.WEST || snake.getDirection() == Direction.EAST)) {
					snake.goDirection(Direction.SOUTH);
				}
				break;

			case LEFT:

				if (snake.isRunning
						&& (snake.getDirection() == Direction.NORTH || snake.getDirection() == Direction.SOUTH)) {
					snake.goDirection(Direction.WEST);
				}
				break;

			case RIGHT:

				if (snake.isRunning
						&& (snake.getDirection() == Direction.NORTH || snake.getDirection() == Direction.SOUTH)) {
					snake.goDirection(Direction.EAST);
				}
				break;

			case SPACE:

				if (snake.isRunning)
					snake.pauseAnimation();
				else
					snake.goDirection(snake.getDirection());

				break;

			default:
				break;

			}

		});

		primaryStage.setScene(scene);
		primaryStage.show();

	}
}
