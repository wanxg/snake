package snake;

import static snake.Constants.WINDOW_HEIGHT;
import static snake.Constants.WINDOW_WIDTH;

import javafx.application.Application;
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

		root.getChildren().add(snake.getSnakeBoneGroup());

		snake.goDirection(Direction.WEST);
		
		Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

		// scene.setFill(Color.DIMGRAY);

		scene.setFill(Color.BLACK);

		scene.setOnKeyPressed(event -> {

			KeyCode key = event.getCode();

			switch (key) {

			case UP:

				System.out.println("UP key pressed");
				if (snake.getDirection() == Direction.WEST || snake.getDirection() == Direction.EAST) {
					snake.goDirection(Direction.NORTH);
				}
				break;

			case DOWN:

				System.out.println("DOWN key pressed");
				if (snake.getDirection() == Direction.WEST || snake.getDirection() == Direction.EAST) {
					snake.goDirection(Direction.SOUTH);
				}
				break;

			case LEFT:

				System.out.println("LEFT key pressed");
				if (snake.getDirection() == Direction.NORTH || snake.getDirection() == Direction.SOUTH) {
					snake.goDirection(Direction.WEST);
				}
				break;

			case RIGHT:

				System.out.println("RIGHT key pressed");
				if (snake.getDirection() == Direction.NORTH || snake.getDirection() == Direction.SOUTH) {
					snake.goDirection(Direction.EAST);
				}
				break;

			default:
				break;

			}

		});

		primaryStage.setScene(scene);
		primaryStage.show();

	}
}
