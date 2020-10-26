package org.gui.instruments;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import org.springframework.stereotype.Component;

@Component
public class Pencil  implements Draw {



	@Override
	public void draw(GraphicsContext gc, Canvas canvas, Image image) {
		
		canvas.setOnMousePressed(e -> {

			gc.beginPath();
			gc.moveTo(e.getX(), e.getY());

			gc.stroke();

		});

		canvas.setOnMouseDragged(e -> {
			if (e.getX() < image.getWidth() && e.getY()  < image.getHeight()) {
				gc.lineTo(e.getX(), e.getY());
				gc.stroke();
			}

		});

	}

}
