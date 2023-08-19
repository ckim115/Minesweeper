/**
 * @author Christina Kim
 * @version 3/25/22
 * Period 5
 * This lab is working
 *
 */
import java.util.Random;
import java.util.Scanner;

public class P5_Kim_Christina_MinesweeperModel implements P5_Kim_Christina_MSModelInterface {
	String[][] playerMS = new String[10][10];
	String[][] developerMS = new String[10][10]; // mine = "*", flag = "F"
	int numRows;
	int numCols;
	int numMines;
	int numFlags;
	int numRevealed;
	int moves;
	boolean isMine;
	
	public static void main(String[] args) {
		P5_Kim_Christina_MinesweeperModel test = new P5_Kim_Christina_MinesweeperModel(10, 10, 10);
		test.printBoard();
	}

	public P5_Kim_Christina_MinesweeperModel(int rows, int cols, int mines) {
		moves = 0;
		numRows = rows;
		numCols = cols;
		isMine = false;
		Random rand = new Random();
		moves = 0;
		numRevealed = 0;
		numMines = mines;
		playerMS = new String[rows][cols];
		developerMS = new String[rows][cols];
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				playerMS[i][j] = "-";
			}
		}
		
		for(int i = 0; i < mines; i++) {
			int r = rand.nextInt(rows);
			int c = rand.nextInt(cols);
			if (developerMS[r][c] == null) {
				developerMS[r][c] = "*";
			}
		}
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				if(developerMS[i][j] == null) {
					developerMS[i][j] = " ";
				}
			}
		}
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				if (!developerMS[i][j].equals("*")) {
					if(numAdjacentMines(i, j) != 0) {
						developerMS[i][j] = Integer.toString(numAdjacentMines(i, j));
					}
				}
			}
		}
	}
	
	public void printBoard() {
		Scanner in = new Scanner(System.in);
		
		System.out.println("  /\\/\\ (_)_ __   ___  _____      _____  ___ _ __   ___ _ __ \n"
				+ " /    \\| | '_ \\ / _ \\/ __\\ \\ /\\ / / _ \\/ _ \\ '_ \\ / _ \\ '__|\n"
				+ "/ /\\/\\ \\ | | | |  __/\\__ \\\\ V  V /  __/  __/ |_) |  __/ |   \n"
				+ "\\/    \\/_|_| |_|\\___||___/ \\_/\\_/ \\___|\\___| .__/ \\___|_|   \n"
				+ "                                           |_|              \n"
				+ "");
		while(!isGameOver()) {
			System.out.print("\t");
			for(int i = 0; i < playerMS[0].length; i++) {
				System.out.printf("%-3d", i);
			}
			System.out.println();
			for(int i = 0; i < playerMS.length; i++) {
				System.out.printf("%2s\t", i);
				for(int j = 0; j < playerMS[i].length; j++) {
					System.out.printf("%-3s", playerMS[i][j]);
				}
				System.out.println();
			}
			
			System.out.print("\t");
			for(int i = 0; i < developerMS[0].length; i++) {
				System.out.printf("%-3d", i);
			}
			System.out.println();
			for(int i = 0; i < developerMS.length; i++) {
				System.out.printf("%2s\t", i);
				for(int j = 0; j < developerMS[i].length; j++) {
					System.out.printf("%-3s", developerMS[i][j]);
				}
				System.out.println();
			}
			
			System.out.println("There are " + numMines + " mines left.");
			System.out.println("Would you like to flag a cell or reveal a cell?");
			System.out.print("Enter 'f' or 'r': ");
			String command = in.next();
			System.out.print("Enter row: ");
			int row = in.nextInt();
			System.out.print("Enter col: ");
			int col = in.nextInt();
			
			if(command.equals("f")) {
				setFlag(row, col);
			} else if (command.equals("r")) {
				reveal(row, col);
			}
			moves = 0;
		}
		
		if(isGameWon()) {
			System.out.println("You Win");
		} else if (isGameLost()) {
			System.out.println("You Lose");
		}
	}

	@Override
	public int numAdjacentMines(int row, int col) {
		int num = 0;
		if(row > 0 && developerMS[row-1][col].equals("*")) {
			num++;
		}
		if(row < developerMS.length - 1 && developerMS[row+1][col].equals("*")) {
			num++;
		}
		if(col > 0 && developerMS[row][col-1].equals("*")) {
			num++;
		}
		if(col < developerMS[row].length - 1 && developerMS[row][col+1].equals("*")) {
			num++;
		}
		if(row > 0 && col > 0 && developerMS[row-1][col-1].equals("*")) {
			num++;
		}
		if(row < developerMS.length - 1 && col > 0 && developerMS[row+1][col-1].equals("*")) {
			num++;
		}
		if(row > 0 && col < developerMS[row].length - 1 && developerMS[row-1][col+1].equals("*")) {
			num++;
		}
		if(row < developerMS.length - 1 && col < developerMS[row].length - 1 && developerMS[row+1][col+1].equals("*")) {
			num++;
		}
		return num;
	}

	@Override
	public boolean isFirstMove() {
		if(moves == 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void setFlag(int r, int c) {
		if(!playerMS[r][c].equals("F")) {
			playerMS[r][c] = "F";
		}
		numMines--;
	}
	
	public void removeFlag(int r, int c) {
		if(!playerMS[r][c].equals("-")) {
			playerMS[r][c] = "-";
			if(isMine(r, c)) {
				numMines++;
			}
		}
	}

	@Override
	public void reveal(int r, int c) {
		if(isFirstMove() && !developerMS[r][c].equals(" ")){
			Random rand = new Random();
			for(int i = 0; i < numRows; i++) {
				for(int j = 0; j < numCols; j++) {
					developerMS[i][j] = " ";
				}
			}
			
			int countM = 0;
			while(countM < numMines) {
				int ro = rand.nextInt(numRows);
				int co = rand.nextInt(numCols);
				if(ro != r && co != c && ro != r-1 && co != c-1 && ro != r+1 && co != c+1 && !developerMS[ro][co].equals("*")) {
					developerMS[ro][co] = "*";
					countM++;
				}
			}
			
			for(int i = 0; i < numRows; i++) {
				for(int j = 0; j < numCols; j++) {
					if (!developerMS[i][j].equals("*")) {
						if(numAdjacentMines(i, j) != 0) {
							developerMS[i][j] = Integer.toString(numAdjacentMines(i, j));
						}
					}
				}
			}
		}
		if(!isTileRevealed(r, c) && !developerMS[r][c].equals("*")) {
			if(!isFlag(r, c)) {
				playerMS[r][c] = developerMS[r][c];
			}
			moves++;
			numRevealed++;
			if(developerMS[r][c].equals(" ")) {
				if(c > 0) {
					reveal(r, c-1);
				}
				if(c < playerMS[0].length - 1) {
					reveal(r, c+1);
				}
				if(r > 0) {
					reveal(r-1, c);
				}
				if(r < playerMS.length - 1) {
					reveal(r+1, c);
				}
				if(c > 0 && r > 0) {
					reveal(r-1, c-1);
				}
				if(c < playerMS[0].length - 1 && r > 0) {
					reveal(r-1, c+1);
				}
				if(c > 0 && r < playerMS.length - 1) {
					reveal(r+1, c-1);
				}
				if(c < playerMS[0].length - 1 && r < playerMS.length - 1) {
					reveal(r+1, c+1);
				}
			} else if (!developerMS[r][c].equals("*") && !isFlag(r, c)) {
				playerMS[r][c] = developerMS[r][c];
			}
		} else {
			if(!isFlag(r, c)) {
				playerMS[r][c] = developerMS[r][c];
				if(developerMS[r][c].equals("*")) {
					isMine = true;
				}
			}
		}
	}

	@Override
	public boolean isGameOver() {
		if(isGameWon() || isGameLost()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isGameWon() {
		boolean mined = true;
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numCols; j++) {
				if(developerMS[i][j].equals("*") && !playerMS[i][j].equals("F")) {
					mined = false;
				}
			}
		}
		/*
		int numUnmineTiles = (numRows*numCols) - numMines;
		if(numRevealed == numUnmineTiles) {
			return true;
		} else {
			return false;
		}
		*/
		return mined;
	}

	@Override
	public boolean isGameLost() {
		if(isMine) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isTileRevealed(int row, int col) {
		if(playerMS[row][col].equals("-")) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean isMine(int row, int col) {
		if(developerMS[row][col].equals("*")) {
			isMine = true;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isFlag(int row, int col) {
		if(playerMS[row][col].equals("F")) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public int numMines() {
		return numMines;
	}

	@Override
	public int getNumFlags() {
		return numFlags;
	}

	@Override
	public int getNumRows() {
		return numRows;
	}

	@Override
	public int getNumCols() {
		return numCols;
	}

	@Override
	public void setNumMines(int m) {
		numMines = m;
		
	}

	@Override
	public String getTile(int r, int c) {
		return playerMS[r][c];
	}
}
