package application;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import javax.swing.GroupLayout.Alignment;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class boardController {
	@FXML
	GridPane board;

	Pane[][] playingField = new Pane[8][8];

	@FXML
	public void initialize() throws IOException {
		settingUpTiles();
		settingUpPieces();
		settingUpCellIdentifiers();
	}
	
	private void settingUpTiles() {
		BackgroundFill bgfBlack = new BackgroundFill(Paint.valueOf("#7e4c39"), new CornerRadii(0), new Insets(0));
		BackgroundFill bgfWhite = new BackgroundFill(Paint.valueOf("#f5f5f5"), new CornerRadii(0), new Insets(0));
		BackgroundFill bgfBlue = new BackgroundFill(Paint.valueOf("blue"), new CornerRadii(0), new Insets(0));

		// Sets the background color for all the tiles
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Pane p = new Pane();
				p.setMinHeight(100);
				p.setMinWidth(100);
				if ((i + j) % 2 == 0) {
					p.setBackground(new Background(bgfWhite));
					p.setId("white");
				} else {
					p.setBackground(new Background(bgfBlack));
					p.setId("black");
				}
				
				// Setting up drop and draggers
				p.setOnDragDetected(new EventHandler <MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						dragStart((Pane)event.getSource());
						event.consume();
					}			
				});
				p.setOnDragDropped(new EventHandler <DragEvent>() {
					@Override
					public void handle(DragEvent event) {
						dragDropped((Pane)event.getGestureSource(), (Pane)event.getGestureTarget());
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
						if(p.getId().equals("white"))
							p.setBackground(new Background(bgfWhite));
						else
							p.setBackground(new Background(bgfBlack));
						event.consume();
					}				
				});
				p.setOnDragOver(new EventHandler <DragEvent>() {
				    public void handle(DragEvent event) {
				        event.acceptTransferModes(TransferMode.ANY);
				        event.consume();
				    }
				});
				board.add(p, i, j);
			}
		}
	}
	private void settingUpCellIdentifiers() {
		//Numberrow
		for ( int i = 1; i <9; i++) {
			StackPane sp = new StackPane();
			sp.setMinWidth(100);
			sp.setMinHeight(15);
			Label l = new Label(i+"");
			sp.getChildren().add(l);			
			board.add(sp, i-1, 8);
		}
		//Alphabetcolumn
		int i = 0;
		for(alphabet a : alphabet.values()) {
			StackPane sp = new StackPane();
			sp.setMinHeight(100);
			sp.setMinWidth(15);
			Label l = new Label(a.toString());
			sp.getChildren().add(l);			
			board.add(sp, 8, i);
			i++;
		}
	}
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
				//pawns
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

	private void dragStart(Pane p) {
		Dragboard db = p.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        content.putString("");
        db.setContent(content);
	}
	private void dragDropped(Pane p, Pane pp) {
		System.out.println(board.getColumnIndex(p) + " " + board.getRowIndex(p) + " | " + board.getColumnIndex(pp) + " " + board.getRowIndex(pp));
	}
}

enum alphabet{
	A,B,C,D,E,F,G,H;
}
