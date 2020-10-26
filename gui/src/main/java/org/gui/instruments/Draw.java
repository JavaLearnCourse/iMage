package org.gui.instruments;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public interface Draw {

	void draw(GraphicsContext gc, Canvas canvas, Image image);
}
