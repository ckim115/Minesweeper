/**
 * @author Christina Kim
 * @version 3/22/22
 * Period 5
 * This lab is working
 *
 */
public interface P5_Kim_Christina_MSModelInterface {
	int getNumRows();
	
	int getNumCols();
	
	int numAdjacentMines(int row, int col); // numAdjacentMines
	
	boolean isGameOver();
	
	boolean isGameWon();
	
	boolean isGameLost();
	
	boolean isTileRevealed(int row, int col);
	
	boolean isMine(int row, int col);
	
	boolean isFlag(int row, int col);
	
	int numMines();
	
	boolean isFirstMove();
	
	void setFlag(int r, int c);
	
	public void removeFlag(int r, int c);
	
	void setNumMines(int m);
	
	void reveal(int r, int c);
	
	int getNumFlags();
	
	String getTile(int r, int c);
}
