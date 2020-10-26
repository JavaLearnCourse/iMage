package org.gui.instruments;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.springframework.stereotype.Component;

@Component
public class Rubber implements Draw {


	@Override
	public void draw(GraphicsContext gc, Canvas canvas, Image image) {
		
		canvas.setOnMousePressed(e -> {

			gc.clearRect(e.getX() - 2, e.getY() - 2, 4, 4);
		});

		canvas.setOnMouseDragged(e -> {

			gc.clearRect(e.getX() - 2, e.getY() - 2, 4, 4);
		});

	}

}
