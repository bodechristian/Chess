package application;

import java.io.File;
import java.util.Arrays;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

public class Tile extends Pane{
	private BackgroundFill bgf;
	private BackgroundImage bgi;
	private Pieces piece;
	private Tuple<Integer, Integer> coord;
	private boolean isPieceOn;

	public Tile(Tuple<Integer, Integer> coord) {
		this.coord = coord;
		this.piece = Pieces.EMPTY;
		this.isPieceOn = false;
		
		this.setMinHeight(100);
		this.setMinWidth(100);
		//#f5f5f5 = white | #7e4c39 = brown
		if ((coord.x + coord.y) % 2 == 0)
			bgf = new BackgroundFill(Paint.valueOf("#f5f5f5"), new CornerRadii(0), new Insets(0));
		else
			bgf = new BackgroundFill(Paint.valueOf("#7e4c39"), new CornerRadii(0), new Insets(0));
		this.setBackground(new Background(bgf));
	}

	/**
	 * sets the background to its saved color (white/black) and piece standing on it
	 * or just the background if no piece is standing here
	 * called when mouse is leaving tile and the color should go back to normal
	 */
	public void resetBackground() {
		if (isPieceOn)
			this.setBackground(new Background(Arrays.asList(bgf), Arrays.asList(bgi)));
		else
			this.setBackground(new Background(bgf));
	}
	
	public void colorBackground(Paint paint) {
		BackgroundFill bgfColor = new BackgroundFill(paint, new CornerRadii(0), new Insets(0));
		if (isPieceOn)
			this.setBackground(new Background(Arrays.asList(bgfColor), Arrays.asList(bgi)));
		else
			this.setBackground(new Background(bgfColor));
	}

	public void setPiece(Pieces piece) {
		this.piece = piece;
		bgi = new BackgroundImage(new Image(new File("ChessPics/" + piece.toString() + ".png").toURI().toString()),
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
				new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, false, false));
		isPieceOn = true;
		resetBackground();
	}
	
	public void removePiece() {
		this.piece = Pieces.EMPTY;
		isPieceOn = false;
		resetBackground();
	}
	
	public Pieces getPiece() {
		if(isPieceOn)
			return piece;
		return Pieces.EMPTY;
	}
	
	public boolean isPieceOn() {
		return isPieceOn;
	}
	
	public Tuple<Integer, Integer> getCoord() {
		return coord;
	}
	
}
