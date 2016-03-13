import java.io.*;
import java.util.*;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
import javafx.scene.layout.*;
public class Sudoku extends Application {
public static final int[] NUMBERS = {1, 2, 3, 4, 5, 6, 7, 8, 9};
public static int[][] board;
public static int[][] converter;
public static Button b = new Button("calculate");
public static Button c = new Button("clear");
public static Board sudokuBoard;
public static int backTrackCounter = 0;
public static Button[] buttons = new Button[81];
public static boolean controller = false;
public static void main(String[] args) throws Exception{ launch(args);}
public void start(Stage primaryStage){
    Scanner scan = new Scanner(System.in);
	board = new int[9][9];
    converter = new int[9][9];
    for(int i = 0; i < 9; i++){
        for(int j = 0; j < 9; j++){           
            board[i][j] = 0;
            converter[i][j] = 9*i + j;
        }
    }
    sudokuBoard = new Board(board);
	int boardSize = sudokuBoard.getBoard().length;
	int boxSize = sudokuBoard.getBoxSize();
	sudokuBoard.printBoard();
    for(int i = 0; i < 81; i++)
        buttons[i] = new Button("0");
    HBox[] hboxes = new HBox[27];
    for(int i = 0; i < 27; i++){
        hboxes[i] = new HBox(4);
        hboxes[i].getChildren().addAll(buttons[3*i], buttons[3*i+1], buttons[3*i+2]);
    }
    VBox[] vboxes = new VBox[9];
    int counter = 0;
    for(int i = 0; i < 9; i++)
        vboxes[i] = new VBox(4);
    vboxes[0].getChildren().addAll(hboxes[0],hboxes[3],hboxes[6]);
    vboxes[1].getChildren().addAll(hboxes[1],hboxes[4],hboxes[7]);                     
    vboxes[2].getChildren().addAll(hboxes[2],hboxes[5],hboxes[8]);                     
    vboxes[3].getChildren().addAll(hboxes[9],hboxes[12],hboxes[15]);                     
    vboxes[4].getChildren().addAll(hboxes[10],hboxes[13],hboxes[16]);                     
    vboxes[5].getChildren().addAll(hboxes[11],hboxes[14],hboxes[17]);                     
    vboxes[6].getChildren().addAll(hboxes[18],hboxes[21],hboxes[24]);                     
    vboxes[7].getChildren().addAll(hboxes[19],hboxes[22],hboxes[25]);                     
    vboxes[8].getChildren().addAll(hboxes[20],hboxes[23],hboxes[26]);                     
    HBox one  = new HBox(8);
    HBox two = new HBox(8);
    HBox three = new HBox(8);
    one.getChildren().addAll(vboxes[0],vboxes[1],vboxes[2]);
    two.getChildren().addAll(vboxes[3],vboxes[4],vboxes[5]);                     
    three.getChildren().addAll(vboxes[6],vboxes[7],vboxes[8]);                     
    HBox cont = new HBox(16);
    VBox totalboard = new VBox(8);
    totalboard.getChildren().addAll(one,two,three);
    for(int i = 0; i < 81; i++){
        final int a = i;
        buttons[a].setOnMouseClicked(e-> {buttonDoer(a);});
    }
    Button b = new Button("calculate");
    b.setOnMouseClicked(e -> { 
        //sudokuBoard = new Board(board);
        if(isSolvable()) recurseSolve(0,0);});
    Button c = new Button("clear");
    c.setOnMouseClicked(e -> {buttonClearer();});
    cont.getChildren().addAll(b,c);
    totalboard.getChildren().add(cont);
    Scene s = new Scene(totalboard);
    primaryStage.setScene(s);
    primaryStage.show();
}
public static void buttonDoer(int n){
    int x = (int)(n/9);
    int y = n%9;
    int a = (board[x][y] + 1) % 10;
    buttons[n].setText(""+a);
    board[x][y] = a;
    sudokuBoard.printBoard();   
}
public static void buttonShower(){
    for(int i = 0; i < 9; i++){
        for(int j = 0; j < 9; j++){
            buttons[9*i + j].setText(board[i][j]+""); 
 //           buttons[9*i + j].setDisable(true);
        }
    }
    b.setDisable(true);
}
public static void buttonClearer(){
    for(int i = 0; i < 9; i++){
        for(int j = 0; j < 9; j++){
            buttons[9*i + j].setText("0");
            board[i][j] = 0;
            buttons[9*i + j].setDisable(false);
        }
    }
    b.setDisable(false);
    sudokuBoard = new Board(board);
    controller = false;
}
public static boolean isSolvable(){
    boolean temp = false;
    boolean isEmpty = true;
    for(int i = 0; i < 9; i++){
        for(int j = 0; j < 9; j++){
           if(board[i][j] != 0){
                isEmpty = false;
                if(Rules.checkRules(board[i][j], new Index(i,j), new Board(board)))
                    temp = true;
           }      
        }
    }
    return (temp || isEmpty);
}
public static int tracker = 0;
public static void recurseSolve(int row, int col){
    if(!controller){
        if(row > 8){
		    System.out.println("Solution found");
		    sudokuBoard.printBoard();
		    System.out.println("backtrack counter: " + backTrackCounter);
            controller = true;
            buttonShower();
        }
	    else if(board[row][col] != 0){
		    if(col >= 8)
		    	recurseSolve(row+1, 0);
		    else
    			recurseSolve(row, col+1);
	    }
        else{	
            for(int k=0; k < NUMBERS.length; k++){
		        if(Rules.checkRules(NUMBERS[k], new Index(row,col), sudokuBoard)){
		    	    board[row][col] = NUMBERS[k];
		    	    if(col >= 8)
		    		    recurseSolve(row+1, 0);
		    	    else
		    		    recurseSolve(row, col+1);
		        }
	        }
		    board[row][col]=0;
		    backTrackCounter++;
        }
    }
}
}
class Index{
    private int _row;
    private int _col;
    public Index(int row, int col){
        _row = row;
        _col = col;
    }
    public int getRow(){
        return _row;
    }
    public int getCol(){
        return _col;
    }
    public boolean equals(Index other){
        return ((_row == other.getRow()) && (_col == other.getCol()));
    }
}
class Board{
	private int[][] _board;
	private int _boxSize;
	public Board(){
		_board = new int[9][9];
		_boxSize = 3;
	}
	public Board(int[][] b){
		_board = b;
		_boxSize = (int)Math.sqrt(b.length);
	}
	public int getBoxSize(){
		return _boxSize;
	}
	public int[][] getBoard(){
		return _board;
	}
	public void printBoard(){
		for(int i=0; i < _board.length; i++){
			for(int j=0; j < _board.length; j++){
				System.out.print(_board[i][j] + " ");
			}
			System.out.println("");
		}
	}
}
class Rules{
	public static boolean checkRowCol(int element, Index index, Board b){
		int[][] board = b.getBoard();
		for(int i=0; i < board.length; i++){
			if(i == index.getCol())
				continue;
			else{
				if(element == board[index.getRow()][i])
					return false;
			}
		}
		for(int i=0; i < board.length; i++){
			if(i == index.getRow())
				continue;
			else{
				if(element == board[i][index.getCol()])
					return false;
            }
        }
		return true;
    }
	public static boolean checkBox(int element, Index index, Board b){
		int[][] board = b.getBoard();
		int boxSize = b.getBoxSize();
		int boxRow = (index.getRow() / boxSize)*boxSize;
		int boxCol = (index.getCol() / boxSize)*boxSize;
		for(int i=boxRow; i < boxRow + boxSize; i++){
			for(int j=boxCol; j < boxCol + boxSize; j++){
				if((i == index.getRow()) && (j == index.getCol()))
					continue;
				else{
					if(element == board[i][j])
						return false;
				}
			}
		}
		return true;
	}
	public static boolean checkRules(int element, Index index, Board b){
		return (checkBox(element, index, b) && checkRowCol(element, index, b));
	}
}
