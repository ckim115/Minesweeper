import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * @author Christina Kim
 * @version 3/25/22
 * Period 5
 * This lab is working.
 */
public class P5_Kim_Christina_MinesweeperController extends Application {
	P5_Kim_Christina_MinesweeperModel MSModel = new P5_Kim_Christina_MinesweeperModel(10, 10, 10);
	GridPane MSpane;
	MenuItem newBeginnerGame;
	MenuItem exit;
	MenuItem setNumMines;
	MenuItem howToPlay;
	MenuItem about;
	Image image;
	Label timerLabel;
	Label mines;
	Button goBack;
	Button enter;
	TextField mineText;
	long startTime;
	long elapsedMillis;
	Scene scene;
	Stage stage;
	
	FileChooser fc = new FileChooser();
	
	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		
		VBox root = new VBox();
		
		MenuBar menubar = new MenuBar();
		Menu menu = new Menu("File");
		newBeginnerGame = new MenuItem("New Beginner Game");
		exit = new MenuItem("Exit");
		setNumMines = new MenuItem("Set Number of Mines");
		howToPlay = new MenuItem("How To Play");
		about = new MenuItem("About");
		menu.getItems().addAll(newBeginnerGame, exit, setNumMines, howToPlay, about);
		menubar.getMenus().add(menu);
		newBeginnerGame.setOnAction(new MenuHandler());
		exit.setOnAction(new MenuHandler());
		setNumMines.setOnAction(new MenuHandler());
		howToPlay.setOnAction(new MenuHandler());
		about.setOnAction(new MenuHandler());
		
		image = new Image(getClass().getClassLoader().getResource("resources/blank.gif").toString());
		MSpane = new GridPane();
		for(int i = 0; i < MSModel.getNumRows(); i++) {
			for(int j = 0; j < MSModel.getNumCols(); j++) {
				ImageView b = new ImageView(image);
				MSpane.add(b, j, i);
			}
		}
		MSpane.setOnMousePressed(new MouseListener());
		
		HBox bottomRow = new HBox();
		bottomRow.setSpacing(10);
		bottomRow.setAlignment(Pos.CENTER);
		timerLabel = new Label(" ");
		
		startTime = System.currentTimeMillis();
		new AnimationTimer() {
            @Override
            public void handle(long now) {
                long elapsedMillis = System.currentTimeMillis() - startTime;
                timerLabel.setText("Time Elapsed: " + Long.toString(elapsedMillis / 1000));
            }
        }.start();
        
		mines = new Label("Mines Remaining: " + MSModel.numMines());
		bottomRow.getChildren().addAll(timerLabel, mines);
		
		root.getChildren().addAll(menubar, MSpane, bottomRow);
		root.setAlignment(Pos.CENTER);
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}	
	
	private class MenuHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			MenuItem m = (MenuItem)event.getSource();
			if(m == newBeginnerGame) {
				MSModel = new P5_Kim_Christina_MinesweeperModel(10, 10, 10);
				startTime = System.currentTimeMillis();
				image = new Image(getClass().getClassLoader().getResource("resources/blank.gif").toString());
				for(int i = 0; i < MSModel.getNumRows(); i++) {
					for(int j = 0; j < MSModel.getNumCols(); j++) {
						ImageView b = new ImageView(image);
						MSpane.add(b, j, i);
					}
				}
		    } else if (m == exit) {
		    	stage.close(); 
			} else if (m == setNumMines) {
				HBox mineRow = new HBox();
				BorderPane root = new BorderPane();
				Label mineLabel = new Label("Number of Mines");
				mineText = new TextField();
				mineText.setPromptText("Enter number of mines");
				
				mineRow.getChildren().addAll(mineLabel, mineText);
				
				enter = new Button("Enter");
				enter.setOnAction(new InputHandler());
				
				root.setCenter(mineRow);
				root.setBottom(enter);
				stage.setScene(new Scene(root));
			} else if (m == howToPlay) {
				BorderPane root = new BorderPane();
				root.setCenter(new Label("Left-click an empty square to reveal it.\n"
						+ "Right-click (or Ctrl+click) an empty square to flag it.\n"
						+ "Midde-click (or left+right click) a number to reveal\n"
						+ "its adjacent squares.\n"
						+ "Press space bar while hovering over a square to flag\n"
						+ "it or reveal its adjacent squares.\n"
						+ "Press F2 or click the smiley face to start a new game"));
				goBack = new Button("Go Back");
				goBack.setOnAction(new InputHandler());
				root.setBottom(goBack);
				stage.setScene(new Scene(root));
			} else if (m == about) {
				BorderPane root = new BorderPane();
				root.setCenter(new Label("Minesweeper\n"
						+ "Christina Kim\n"
						+ "P5\n"
						+ "4/1/22\n"
						+ "APCS"));
				goBack = new Button("Go Back");
				goBack.setOnAction(new InputHandler());
				root.setBottom(goBack);
				stage.setScene(new Scene(root));
			}
		}
	}
	
	private class InputHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent event) {
			Button src = (Button)event.getSource();
			if (src == goBack) {
				stage.setScene(scene);
			} else if (src == enter) {
				int m = Integer.parseInt(mineText.getText());
				stage.setScene(scene);
				MSModel = new P5_Kim_Christina_MinesweeperModel(10, 10, m);
				startTime = System.currentTimeMillis();
				image = new Image(getClass().getClassLoader().getResource("resources/blank.gif").toString());
				for(int i = 0; i < MSModel.getNumRows(); i++) {
					for(int j = 0; j < MSModel.getNumCols(); j++) {
						ImageView b = new ImageView(image);
						MSpane.add(b, j, i);
					}
				}
			}
		}
		
	}
	
	private class MouseListener implements EventHandler<MouseEvent> {
		//this is writing in the Controller (because responding to user input)
		@Override
		public void handle(MouseEvent event) {
			int row = (int) (event.getX() / image.getWidth());
			int col = (int) (event.getY() / image.getHeight());
			
			if (event.getButton() == MouseButton.PRIMARY) {
				if(!MSModel.isFlag(row, col)) {
					MSModel.reveal(row, col);
				}
				
				for(int i = 0; i < MSModel.getNumRows(); i++) {
					for(int j = 0; j < MSModel.getNumCols(); j++) {
						if(MSModel.isTileRevealed(i, j)) {
							String t = MSModel.getTile(i, j);
							if (!MSModel.isFlag(i, j)) {
								if(t.equals(" ")) {
									image = new Image(getClass().getClassLoader().getResource("resources/num_0.gif").toString());
									ImageView c = new ImageView(image);
									MSpane.add(c, i, j);
								} else if (t.equals("*")) {
									image = new Image(getClass().getClassLoader().getResource("resources/bomb_revealed.gif").toString());
									ImageView b = new ImageView(image);
									MSpane.add(b, i, j);
									break;
								} else {
									String num = "resources/num_" + t + ".gif";
									image = new Image(getClass().getClassLoader().getResource(num).toString());
									ImageView c = new ImageView(image);
									MSpane.add(c, i, j);
								}
							}
						}
					}
				}
				
				if(MSModel.isGameLost()) {
					BorderPane root = new BorderPane();
					root.setCenter(new Label("You lost lol"));
					goBack = new Button("Go Back");
					goBack.setOnAction(new InputHandler());
					root.setBottom(goBack);
					stage.setScene(new Scene(root));
				}
			} else if (event.getButton() == MouseButton.SECONDARY) {
				if(MSModel.getTile(row, col).equals("-")) {
					image = new Image(getClass().getClassLoader().getResource("resources/bomb_flagged.gif").toString());
					ImageView c = new ImageView(image);
					MSpane.add(c, row, col);
					MSModel.setFlag(row, col);
					if (MSModel.isGameWon()) {
						BorderPane root = new BorderPane();
						root.setCenter(new Label("You Won!"));
						goBack = new Button("Go Back");
						goBack.setOnAction(new InputHandler());
						root.setBottom(goBack);
						stage.setScene(new Scene(root));
					}
				} else if (MSModel.getTile(row, col).equals("F")) {
					image = new Image(getClass().getClassLoader().getResource("resources/blank.gif").toString());
					ImageView c = new ImageView(image);
					MSpane.add(c, row, col);
					MSModel.removeFlag(row, col);
				}
				mines.setText("Mines Remaining: " + MSModel.numMines());
			}
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
