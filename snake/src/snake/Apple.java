package snake;

import static snake.Constants.SNAKE_BONE_SIZE;
import static snake.Constants.WINDOW_HEIGHT;
import static snake.Constants.WINDOW_WIDTH;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Apple extends Rectangle {
	
	private Image image;
	
	Apple(double x, double y){
		super(15,15);
		try {
			
			this.image = new Image(new FileInputStream("apple_15x15.png"));
			this.setFill(new ImagePattern(this.image));
			this.setX(x);
			this.setY(y);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public void placeTo(double x, double y){
		
		this.setX(x);
		this.setY(y);
	}
	
	public void placeToRandomLocation(){
		
		double minX = SNAKE_BONE_SIZE / 2;
		double maxX = WINDOW_WIDTH  - SNAKE_BONE_SIZE / 2;
		
		double minY = SNAKE_BONE_SIZE / 2;
		double maxY = WINDOW_HEIGHT - SNAKE_BONE_SIZE / 2;
		
		this.setX(2);
		
		Random randomGenerator = new Random();
		
		int x = randomGenerator.nextInt(((int)maxX - (int)minX) + 1) + (int)minX;
		int y = randomGenerator.nextInt(((int)maxY - (int)minY) + 1) + (int)minY;
		
		this.setX(x);
		this.setY(y);
	}
}
