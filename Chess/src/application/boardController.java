package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;

public class boardController {
	@FXML
	GridPane board;

	//p.setBackground(new Background(Arrays.asList(bgfBlue), Arrays.asList(bgi)));
	
	List<Tuple<Integer, Integer>> legalMoves = new ArrayList<Tuple<Integer, Integer>>();

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

		BackgroundFill bgfBlack = new BackgroundFill(Paint.valueOf("#7e4c39"), new CornerRadii(0), new Insets(0));
		BackgroundFill bgfWhite = new BackgroundFill(Paint.valueOf("#f5f5f5"), new CornerRadii(0), new Insets(0));
		BackgroundFill bgfBlue = new BackgroundFill(Paint.valueOf("blue"), new CornerRadii(0), new Insets(0));

		BackgroundImage bgi = new BackgroundImage(new Image(new File("ChessPics/blackRook.png").toURI().toString()),
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
				new BackgroundSize(10, 10, false, false, true, true));

		// Sets the background color for all the tiles
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Pane p = new Pane();
				p.setMinHeight(100);
				p.setMinWidth(100);
				if ((i + j) % 2 == 0) {
					p.setBackground(new Background(bgfWhite));	
					p.setBackground(new Background(Arrays.asList(bgfBlue), Arrays.asList(bgi)));
					p.setId("white");
				} else {
					p.setBackground(new Background(bgfBlack));
					p.setId("black");
				}

				// Setting up drop and draggers
				p.setOnDragDetected(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						dragStart((Pane) event.getSource());
						event.consume();
					}
				});
				p.setOnDragDropped(new EventHandler<DragEvent>() {
					@Override
					public void handle(DragEvent event) {
						dragDropped((Pane) event.getGestureSource(), (Pane) event.getGestureTarget());
						event.consume();
					}
				});
				p.setOnDragEntered(new EventHandler<DragEvent>() {
					@Override
					public void handle(DragEvent event) {
						p.setBackground(new Background(bgfBlue));
						event.consume();
					}
				});
				p.setOnDragExited(new EventHandler<DragEvent>() {
					@Override
					public void handle(DragEvent event) {
						if (p.getId().equals("white"))
							p.setBackground(new Background(bgfWhite));
						else
							p.setBackground(new Background(bgfBlack));
						event.consume();
					}
				});
				p.setOnDragOver(new EventHandler<DragEvent>() {
					public void handle(DragEvent event) {
						event.acceptTransferModes(TransferMode.ANY);
						event.consume();
					}
				});
				board.add(p, i, j);	
			}
		}
	}

	/**
	 * ATM creates image views for each piece and puts it at their starting position
	 * ?? in future summarized with pane in own class
	 */
	private void settingUpPieces() {
		// Setting up whites figures
		board.add(new ImageView(new Image(new File("ChessPics/Rook.png").toURI().toString())), 0, 7);
		board.add(new ImageView(new Image(new File("ChessPics/Rook.png").toURI().toString())), 7, 7);
		board.add(new ImageView(new Image(new File("ChessPics/Knight.png").toURI().toString())), 1, 7);
		board.add(new ImageView(new Image(new File("ChessPics/Knight.png").toURI().toString())), 6, 7);
		board.add(new ImageView(new Image(new File("ChessPics/Bishop.png").toURI().toString())), 2, 7);
		board.add(new ImageView(new Image(new File("ChessPics/Bishop.png").toURI().toString())), 5, 7);
		board.add(new ImageView(new Image(new File("ChessPics/King.png").toURI().toString())), 4, 7);
		board.add(new ImageView(new Image(new File("ChessPics/Queen.png").toURI().toString())), 3, 7);
		// pawns
		for (int i = 0; i < 8; i++)
			board.add(new ImageView(new Image(new File("ChessPics/Pawn.png").toURI().toString())), i, 6);

		// Setting up blacks figures
		board.add(new ImageView(new Image(new File("ChessPics/blackRook.png").toURI().toString())), 0, 0);
		board.add(new ImageView(new Image(new File("ChessPics/blackRook.png").toURI().toString())), 7, 0);
		board.add(new ImageView(new Image(new File("ChessPics/blackKnight.png").toURI().toString())), 1, 0);
		board.add(new ImageView(new Image(new File("ChessPics/blackKnight.png").toURI().toString())), 6, 0);
		board.add(new ImageView(new Image(new File("ChessPics/blackBishop.png").toURI().toString())), 2, 0);
		board.add(new ImageView(new Image(new File("ChessPics/blackBishop.png").toURI().toString())), 5, 0);
		board.add(new ImageView(new Image(new File("ChessPics/blackKing.png").toURI().toString())), 4, 0);
		board.add(new ImageView(new Image(new File("ChessPics/blackQueen.png").toURI().toString())), 3, 0);
		// pawns
		for (int i = 0; i < 8; i++)
			board.add(new ImageView(new Image(new File("ChessPics/blackPawn.png").toURI().toString())), i, 1);

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
	private void dragStart(Pane source) {
		legalMoves(source);

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
	private void dragDropped(Pane source, Pane target) {
		if (checkLegalMove(target)) {
			move(source, target);
		}
		legalMoves.clear();
		System.out.println(GridPane.getColumnIndex(source) + " " + GridPane.getRowIndex(source) + " | "
				+ GridPane.getColumnIndex(target) + " " + GridPane.getRowIndex(target));
	}

	/**
	 * Adds all the legal moves from the piece on the given pane to the legalMoves
	 * list
	 * 
	 * @param p the Pane
	 */
	private void legalMoves(Pane source) {
		// TODO: add legal moves to List
	}

	/**
	 * checks if the move to the given tile is a legal one
	 * 
	 * @param target the tile that is being moved on
	 * @return true if move is legal
	 */
	private boolean checkLegalMove(Pane target) {
		// GridPane.getColumnIndex(target), GridPane.getRowIndex(target)
		// DONE: check if target is in legal move list
		if (legalMoves
				.contains(new Tuple<Integer, Integer>(GridPane.getColumnIndex(target), GridPane.getRowIndex(target)))) {
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
	private void move(Pane source, Pane target) {
		// TODO: move... Xd
	}
}
