package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;

public class boardController {
	@FXML
	GridPane board;
	
	List<Tuple<Integer, Integer>> legalMoves = new ArrayList<Tuple<Integer, Integer>>();
	List<Pieces> killedPieces = new ArrayList<Pieces>();
	Tile[][] tileArray = new Tile[8][8];
	Tile clickedSource;
	
	String hoverColor = "blue", clickedColor = "green";

	@FXML
	public void initialize() throws IOException {
		settingUpTiles();
		settingUpPieces();
		settingUpCellIdentifiers();
	}

	/**
	 * creates the tiles, including background color and all the drag&drop events
	 */
	private void settingUpTiles() {
		// Sets up all the tiles
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Tile t = new Tile(new Tuple<Integer, Integer>(i,j));
				
				// Setting up drop and draggers
				t.setOnDragDetected(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						dragStart((Tile) event.getSource());
						event.consume();
					}
				});
				t.setOnDragDropped(new EventHandler<DragEvent>() {
					@Override
					public void handle(DragEvent event) {
						dragDropped((Tile) event.getGestureSource(), (Tile) event.getGestureTarget());
						event.consume();
					}
				});
				t.setOnDragEntered(new EventHandler<DragEvent>() {
					@Override
					public void handle(DragEvent event) {
						if(!t.equals(clickedSource))
							t.colorBackground(Paint.valueOf(hoverColor));
						event.consume();
					}
				});
				t.setOnDragExited(new EventHandler<DragEvent>() {
					@Override
					public void handle(DragEvent event) {
						if(!t.equals(clickedSource))
							t.resetBackground();
						event.consume();
					}
				});
				t.setOnDragOver(new EventHandler<DragEvent>() {
					public void handle(DragEvent event) {
						event.acceptTransferModes(TransferMode.ANY);
						event.consume();
					}
				});
				t.setOnMouseEntered(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						if(!t.equals(clickedSource))
							t.colorBackground(Paint.valueOf(hoverColor));
						event.consume();
					}					
				});
				t.setOnMouseExited(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						if(!t.equals(clickedSource))
							t.resetBackground();
						event.consume();
					}	
				});
				tileArray[i][j] = t;
				board.add(t, i, j);	
			}
		}
	}

	/**
	 * Puts the piece in the starting positions
	 */
	private void settingUpPieces() {
		// Setting up whites figures
		tileArray[0][7].setPiece(Pieces.whiteRook);
		tileArray[7][7].setPiece(Pieces.whiteRook);
		tileArray[1][7].setPiece(Pieces.whiteKnight);
		tileArray[6][7].setPiece(Pieces.whiteKnight);
		tileArray[2][7].setPiece(Pieces.whiteBishop);
		tileArray[5][7].setPiece(Pieces.whiteBishop);
		tileArray[4][7].setPiece(Pieces.whiteKing);
		tileArray[3][7].setPiece(Pieces.whiteQueen);
		// pawns
		for (int i = 0; i < 8; i++)
			tileArray[i][6].setPiece(Pieces.whitePawn);
		
		// Setting up blacks figures
		tileArray[0][0].setPiece(Pieces.blackRook);
		tileArray[7][0].setPiece(Pieces.blackRook);
		tileArray[1][0].setPiece(Pieces.blackKnight);
		tileArray[6][0].setPiece(Pieces.blackKnight);
		tileArray[2][0].setPiece(Pieces.blackBishop);
		tileArray[5][0].setPiece(Pieces.blackBishop);
		tileArray[4][0].setPiece(Pieces.blackKing);
		tileArray[3][0].setPiece(Pieces.blackQueen);
		// pawns
		for (int i = 0; i < 8; i++)
			tileArray[i][1].setPiece(Pieces.blackPawn);
	}

	/**
	 * creates the identifiers at the side of the chess board (1..8, A..H)
	 */
	private void settingUpCellIdentifiers() {
		List<String> alphabets = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H");

		for (int i = 1; i < 9; i++) {
			// Numberrow
			StackPane sp = new StackPane();
			sp.setMinWidth(100);
			sp.setMinHeight(15);
			Label l = new Label(i + "");
			sp.getChildren().add(l);
			board.add(sp, i - 1, 8);

			// AlphabetColumn
			StackPane sps = new StackPane();
			sps.setMinHeight(100);
			sps.setMinWidth(15);
			Label ls = new Label(alphabets.get(i - 1));
			sps.getChildren().add(ls);
			board.add(sps, 8, i - 1);
		}
	}

	/**
	 * Triggers on a tile when a mouse is dragging on it (Drag&Drop)
	 * 
	 * @param source the tile on which the dragging is started
	 */
	private void dragStart(Tile source) {
		legalMoves(source);
		clickedSource = source;
		source.colorBackground(Paint.valueOf(clickedColor));
		// Drag&Drop stuff
		Dragboard db = source.startDragAndDrop(TransferMode.ANY);
		ClipboardContent content = new ClipboardContent();
		content.putString("");
		db.setContent(content);
	}

	/**
	 * Triggers on a tile when a mouse is dropped on it (Drag&Drop)
	 * 
	 * @param source the tile from where its dragged
	 * @param target the tile on which its dropped
	 */
	private void dragDropped(Tile source, Tile target) {
		if (checkLegalMove(target)) {
			move(source, target);
		}
		source.resetBackground();
		target.colorBackground(Paint.valueOf(clickedColor));
		legalMoves.clear();
		System.out.println(source.getCoord().x + " " + source.getCoord().y + " | "
				+ target.getCoord().x + " " + target.getCoord().y);
	}

	/**
	 * Adds all the legal moves from the piece on the given pane to the legalMoves
	 * list
	 * 
	 * @param p the Pane
	 */
	private void legalMoves(Tile source) {
		// TODO: add legal moves to List
	}

	/**
	 * checks if the move to the given tile is a legal one
	 * 
	 * @param target the tile that is being moved on
	 * @return true if move is legal
	 */
	private boolean checkLegalMove(Tile target) {
		// GridPane.getColumnIndex(target), GridPane.getRowIndex(target)
		// DONE: check if target is in legal move list
		if (legalMoves
				.contains(new Tuple<Integer, Integer>(target.getCoord().x, target.getCoord().y))) {
			return true;
		}
		return false;
	}

	/**
	 * moves a piece from tile to tile is called after legality is checked and true
	 * 
	 * @param source moves from this
	 * @param target to this
	 */
	private void move(Tile source, Tile target) {
		// DONE: move... Xd
		// DONE: Make List of killed pieces
		// TODO: Do something with the list of killed enemies
		Pieces p = source.getPiece();
		target.setPiece(p);
		killedPieces.add(p);
		source.setPiece(Pieces.EMPTY);
	}
}
