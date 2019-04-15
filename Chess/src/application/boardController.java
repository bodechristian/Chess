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
	boolean whitesTurn = true;

	String hoverColor = "blue", clickedColor = "green", invalidColor = "red";

	@FXML
	public void initialize() throws IOException {
		settingUpTiles();
		settingUpPieces();
		settingUpCellIdentifiers();
		move(tileArray[3][1], tileArray[0][5]);
	}

	/**
	 * creates the tiles, including background color and all the drag&drop events
	 */
	private void settingUpTiles() {
		// Sets up all the tiles
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Tile t = new Tile(new Tuple<Integer, Integer>(i, j));

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
						if (!t.equals(clickedSource))
							t.colorBackground(Paint.valueOf(hoverColor));
						event.consume();
					}
				});
				t.setOnDragExited(new EventHandler<DragEvent>() {
					@Override
					public void handle(DragEvent event) {
						if (!t.equals(clickedSource))
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
						if (!t.equals(clickedSource))
							t.colorBackground(Paint.valueOf(hoverColor));
						event.consume();
					}
				});
				t.setOnMouseExited(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						if (!t.equals(clickedSource))
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
		if(source.getPiece().toString().startsWith("w") && whitesTurn
				|| source.getPiece().toString().startsWith("b") && !whitesTurn) {
			source.colorBackground(Paint.valueOf(clickedColor));
		} else {
			source.colorBackground(Paint.valueOf(invalidColor));
		}
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
		legalMoves.clear();
	}

	/**
	 * Adds all the legal moves from the piece on the given pane to the legalMoves
	 * list
	 * 
	 * @param p the Pane
	 */
	private void legalMoves(Tile source) {
		switch (source.getPiece()) {
		case EMPTY:
			break;
		case blackBishop:
			moveBishop(source.getCoord(), false);
			break;
		case whiteBishop:
			moveBishop(source.getCoord(), true);
			break;
		case blackKing:
			moveKing(source.getCoord(), false);
			break;
		case whiteKing:
			moveKing(source.getCoord(), true);
			break;
		case blackKnight:
			moveKnight(source.getCoord(), false);
			break;
		case whiteKnight:
			moveKnight(source.getCoord(), true);
			break;
		case blackPawn:
			movePawn(source.getCoord(), false);
			break;
		case whitePawn:
			movePawn(source.getCoord(), true);
			break;
		case blackQueen:
			moveQueen(source.getCoord(), false);
			break;
		case whiteQueen:
			moveQueen(source.getCoord(), true);
			break;
		case blackRook:
			moveRook(source.getCoord(), false);
			break;
		case whiteRook:
			moveRook(source.getCoord(), true);
			break;
		}
	}

	private void moveBishop(Tuple<Integer, Integer> coord, boolean whitePiece) {
		boolean stop = false;

		// check towards the top left
		int x = coord.x - 1;
		int y = coord.y - 1;
		while (x >= 0 && y >= 0 && !stop) {
			Pieces p = tileArray[x][y].getPiece();
			stop = nextTileCheck(p, whitePiece, x, y);
			x--;
			y--;
		}

		// check towards the top right
		stop = false;
		x = coord.x + 1;
		y = coord.y - 1;
		while (x < 8 && y >= 0 && !stop) {
			Pieces p = tileArray[x][y].getPiece();
			stop = nextTileCheck(p, whitePiece, x, y);
			x++;
			y--;
		}

		// check towards bottom left
		stop = false;
		x = coord.x - 1;
		y = coord.y + 1;
		while (x >= 0 && y < 8 && !stop) {
			Pieces p = tileArray[x][y].getPiece();
			stop = nextTileCheck(p, whitePiece, x, y);
			x--;
			y++;
		}

		// check towards bottom right
		stop = false;
		x = coord.x + 1;
		y = coord.y + 1;
		while (x < 8 && y < 8 && !stop) {
			Pieces p = tileArray[x][y].getPiece();
			stop = nextTileCheck(p, whitePiece, x, y);
			x++;
			y++;
		}
	}

	private void moveKing(Tuple<Integer, Integer> coord, boolean whitePiece) {
		if ( coord.x >0 && coord.y >0 )
			nextTileCheck(tileArray[coord.x - 1][coord.y - 1].getPiece(), whitePiece, coord.x - 1, coord.y - 1);
		if (coord.y >0 )
			nextTileCheck(tileArray[coord.x][coord.y - 1].getPiece(), whitePiece, coord.x, coord.y - 1);
		if ( coord.x <7 && coord.y >0)
			nextTileCheck(tileArray[coord.x + 1][coord.y - 1].getPiece(), whitePiece, coord.x + 1, coord.y - 1);
		if (coord.x <7 )
			nextTileCheck(tileArray[coord.x + 1][coord.y].getPiece(), whitePiece, coord.x + 1, coord.y);
		if ( coord.x <7 && coord.y <7)
			nextTileCheck(tileArray[coord.x + 1][coord.y + 1].getPiece(), whitePiece, coord.x + 1, coord.y + 1);
		if ( coord.y <7)
			nextTileCheck(tileArray[coord.x][coord.y + 1].getPiece(), whitePiece, coord.x, coord.y + 1);
		if ( coord.x >0 && coord.y <7)
			nextTileCheck(tileArray[coord.x - 1][coord.y + 1].getPiece(), whitePiece, coord.x - 1, coord.y + 1);
		if ( coord.x >0)
			nextTileCheck(tileArray[coord.x - 1][coord.y].getPiece(), whitePiece, coord.x - 1, coord.y);
	}

	private void moveKnight(Tuple<Integer, Integer> coord, boolean whitePiece) {
		int x = coord.x - 2;
		int y = coord.y + 1;
		if (x >= 0 && y < 8)
			nextTileCheck(tileArray[x][y].getPiece(), whitePiece, x, y);
		x = coord.x - 2;
		y = coord.y - 1;
		if (x >= 0 && y >= 0)
			nextTileCheck(tileArray[x][y].getPiece(), whitePiece, x, y);
		x = coord.x - 1;
		y = coord.y - 2;
		if (x >= 0 && y >= 0)
			nextTileCheck(tileArray[x][y].getPiece(), whitePiece, x, y);
		x = coord.x + 1;
		y = coord.y - 2;
		if (x < 8 && y >= 0)
			nextTileCheck(tileArray[x][y].getPiece(), whitePiece, x, y);
		x = coord.x + 2;
		y = coord.y - 1;
		if (x < 8 && y >= 0)
			nextTileCheck(tileArray[x][y].getPiece(), whitePiece, x, y);
		x = coord.x + 2;
		y = coord.y + 1;
		if (x < 8 && y < 8)
			nextTileCheck(tileArray[x][y].getPiece(), whitePiece, x, y);
		x = coord.x + 1;
		y = coord.y + 2;
		if (x < 8 && y < 8)
			nextTileCheck(tileArray[x][y].getPiece(), whitePiece, x, y);
		x = coord.x - 1;
		y = coord.y + 2;
		if (x >= 0 && y < 8)
			nextTileCheck(tileArray[x][y].getPiece(), whitePiece, x, y);
	}

	private void movePawn(Tuple<Integer, Integer> coord, boolean whitePiece) {
		// TODO:en passant
		if (tileArray[coord.x][coord.y].getPiece().toString().startsWith("w")) {
			if (coord.x > 0 && coord.y > 0 && !tileArray[coord.x - 1][coord.y - 1].getPiece().equals(Pieces.EMPTY))
				nextTileCheck(tileArray[coord.x - 1][coord.y - 1].getPiece(), whitePiece, coord.x - 1, coord.y - 1);
			if (coord.x < 7 && coord.y > 0 && !tileArray[coord.x + 1][coord.y - 1].getPiece().equals(Pieces.EMPTY))
				nextTileCheck(tileArray[coord.x + 1][coord.y - 1].getPiece(), whitePiece, coord.x + 1, coord.y - 1);
			if (coord.y == 6)
				nextTileCheck(tileArray[coord.x][coord.y - 2].getPiece(), whitePiece, coord.x, coord.y - 2);
			if (coord.y > 0) {
				nextTileCheck(tileArray[coord.x][coord.y - 1].getPiece(), whitePiece, coord.x, coord.y - 1);
			}
		} else if (tileArray[coord.x][coord.y].getPiece().toString().startsWith("b")) {
			if (coord.x > 0 && coord.y < 7 && !tileArray[coord.x - 1][coord.y + 1].getPiece().equals(Pieces.EMPTY))
				nextTileCheck(tileArray[coord.x - 1][coord.y + 1].getPiece(), whitePiece, coord.x - 1, coord.y + 1);
			if (coord.x < 7 && coord.y < 7 && !tileArray[coord.x + 1][coord.y + 1].getPiece().equals(Pieces.EMPTY))
				nextTileCheck(tileArray[coord.x + 1][coord.y + 1].getPiece(), whitePiece, coord.x + 1, coord.y + 1);
			if (coord.y == 1)
				nextTileCheck(tileArray[coord.x][coord.y + 2].getPiece(), whitePiece, coord.x, coord.y + 2);
			if (coord.y < 7) {
				nextTileCheck(tileArray[coord.x][coord.y + 1].getPiece(), whitePiece, coord.x, coord.y + 1);
			}
		}
	}

	private void moveQueen(Tuple<Integer, Integer> coord, boolean whitePiece) {
		boolean stop = false;

		// check towards the left
		int x = coord.x - 1;
		int y = coord.y;
		while (x >= 0 && !stop) {
			Pieces p = tileArray[x][y].getPiece();
			stop = nextTileCheck(p, whitePiece, x, y);
			x--;
		}

		// check towards the right
		stop = false;
		x = coord.x + 1;
		y = coord.y;
		while (x < 8 && !stop) {
			Pieces p = tileArray[x][y].getPiece();
			stop = nextTileCheck(p, whitePiece, x, y);
			x++;
		}

		// check towards top
		stop = false;
		x = coord.x;
		y = coord.y - 1;
		while (y >= 0 && !stop) {
			Pieces p = tileArray[x][y].getPiece();
			stop = nextTileCheck(p, whitePiece, x, y);
			y--;
		}

		// check towards bottom
		stop = false;
		x = coord.x;
		y = coord.y + 1;
		while (y < 8 && !stop) {
			Pieces p = tileArray[x][y].getPiece();
			stop = nextTileCheck(p, whitePiece, x, y);
			y++;
		}

		stop = false; // check towards the top left
		x = coord.x - 1;
		y = coord.y - 1;
		while (x >= 0 && y >= 0 && !stop) {
			Pieces p = tileArray[x][y].getPiece();
			stop = nextTileCheck(p, whitePiece, x, y);
			x--;
			y--;
		}

		// check towards the top right
		stop = false;
		x = coord.x + 1;
		y = coord.y - 1;
		while (x < 8 && y >= 0 && !stop) {
			Pieces p = tileArray[x][y].getPiece();
			stop = nextTileCheck(p, whitePiece, x, y);
			x++;
			y--;
		}

		// check towards bottom left
		stop = false;
		x = coord.x - 1;
		y = coord.y + 1;
		while (x >= 0 && y < 8 && !stop) {
			Pieces p = tileArray[x][y].getPiece();
			stop = nextTileCheck(p, whitePiece, x, y);
			x--;
			y++;
		}

		// check towards bottom right
		stop = false;
		x = coord.x + 1;
		y = coord.y + 1;
		while (x < 8 && y < 8 && !stop) {
			Pieces p = tileArray[x][y].getPiece();
			stop = nextTileCheck(p, whitePiece, x, y);
			x++;
			y++;
		}
	}

	private void moveRook(Tuple<Integer, Integer> coord, boolean whitePiece) {
		boolean stop = false;

		// check towards the left
		int x = coord.x - 1;
		int y = coord.y;
		while (x >= 0 && !stop) {
			Pieces p = tileArray[x][y].getPiece();
			stop = nextTileCheck(p, whitePiece, x, y);
			x--;
		}

		// check towards the right
		stop = false;
		x = coord.x + 1;
		y = coord.y;
		while (x < 8 && !stop) {
			Pieces p = tileArray[x][y].getPiece();
			stop = nextTileCheck(p, whitePiece, x, y);
			x++;
		}

		// check towards top
		stop = false;
		x = coord.x;
		y = coord.y - 1;
		while (y >= 0 && !stop) {
			Pieces p = tileArray[x][y].getPiece();
			stop = nextTileCheck(p, whitePiece, x, y);
			y--;
		}

		// check towards bottom
		stop = false;
		x = coord.x;
		y = coord.y + 1;
		while (y < 8 && !stop) {
			Pieces p = tileArray[x][y].getPiece();
			stop = nextTileCheck(p, whitePiece, x, y);
			y++;
		}
	}

	/**
	 * checks if given tile is legal and if it can go on
	 * 
	 * @param p          the Piece on the next tile that has to be checked
	 * @param whitePiece true if source tile has white piece on it, false -> black
	 * @param x          x-coordinate of the tile to check
	 * @param y          y-coordinate of the tile to check
	 * @return true, if it theres a piece on the next tile (and it shouldnt go any
	 *         further)
	 */
	private boolean nextTileCheck(Pieces p, boolean whitePiece, int x, int y) {
		// There is a piece on the next tile
		if (!p.equals(Pieces.EMPTY)) {
			// It's a black piece And we are moving a white piece ||
			// It's a white piece And we are moving a black piece
			if ((p.toString().startsWith("b") && whitePiece) || (p.toString().startsWith("w") && !whitePiece))
				legalMoves.add(new Tuple<Integer, Integer>(x, y));
			return true;
		}
		// There is no piece on the next tile
		legalMoves.add(new Tuple<Integer, Integer>(x, y));
		return false;
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
		if (legalMoves.contains(new Tuple<Integer, Integer>(target.getCoord().x, target.getCoord().y))) {
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
		// TODO: check if pawn goes on last
		// TODO: check if king is taken

		Pieces sourcePiece = source.getPiece();
		Pieces targetPiece = target.getPiece();

		if (sourcePiece.toString().startsWith("w") && whitesTurn
				|| sourcePiece.toString().startsWith("b") && !whitesTurn) {
			if (!targetPiece.equals(Pieces.EMPTY))
				killedPieces.add(targetPiece);
			target.setPiece(sourcePiece);
			source.removePiece();

			whitesTurn = !whitesTurn;
		}
	}
}
