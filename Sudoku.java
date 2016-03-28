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
public static Button b = new Button("SOLVE");
public static Button c = new Button("CLEAR");
public static Board sudokuBoard;
public static Button[] buttons = new Button[81];
public static boolean controller = false;
public static void main(String[] args) throws Exception{ launch(args);}
public void start(Stage primaryStage){

	board = new int[9][9];
    for(int i = 0; i < 9; i++){
        for(int j = 0; j < 9; j++){           
            board[i][j] = 0;
            buttons[9*i + j] = new Button("0");
        }
    }
    sudokuBoard = new Board(board);
	int boardSize = sudokuBoard.getBoard().length;
	int boxSize = sudokuBoard.getBoxSize();

    //declare our hboxes and add the buttons appropriately
    HBox[] hboxes = new HBox[27];
    for(int i = 0; i < 27; i++){
        hboxes[i] = new HBox(4);
        hboxes[i].getChildren().addAll(buttons[3*i], buttons[3*i+1], buttons[3*i+2]);
    }
    //declare our vertical boxes
    VBox[] vboxes = new VBox[9];
    for(int i = 0; i < 9; i++)
        vboxes[i] = new VBox(4);
    //add their respective hboxes. could implement with a for loop
    vboxes[0].getChildren().addAll(hboxes[0],hboxes[3],hboxes[6]);
    vboxes[1].getChildren().addAll(hboxes[1],hboxes[4],hboxes[7]);                     
    vboxes[2].getChildren().addAll(hboxes[2],hboxes[5],hboxes[8]);                     
    vboxes[3].getChildren().addAll(hboxes[9],hboxes[12],hboxes[15]);                     
    vboxes[4].getChildren().addAll(hboxes[10],hboxes[13],hboxes[16]);                     
    vboxes[5].getChildren().addAll(hboxes[11],hboxes[14],hboxes[17]);                     
    vboxes[6].getChildren().addAll(hboxes[18],hboxes[21],hboxes[24]);                     
    vboxes[7].getChildren().addAll(hboxes[19],hboxes[22],hboxes[25]);                     
    vboxes[8].getChildren().addAll(hboxes[20],hboxes[23],hboxes[26]);                     
    //our final columns
    HBox one  = new HBox(8);
    HBox two = new HBox(8);
    HBox three = new HBox(8);
    one.getChildren().addAll(vboxes[0],vboxes[1],vboxes[2]);
    two.getChildren().addAll(vboxes[3],vboxes[4],vboxes[5]);                     
    three.getChildren().addAll(vboxes[6],vboxes[7],vboxes[8]);                     
    //separate the buttons from the board
    HBox buttonBox = new HBox(2);
    
    VBox totalboard = new VBox(8);
    totalboard.getChildren().addAll(one,two,three);
    //lambda expr handlers for each button
    for(int i = 0; i < 81; i++){
        final int a = i;
        buttons[a].setOnMouseClicked(e-> {buttonDoer(a);});
    }
    //handler for the calculate button
    b.setOnMouseClicked(e -> { 
        if(isSolvable()) recurseSolve(0,0);
    });
    //handler for the clear button
    c.setOnMouseClicked(e -> {buttonClearer();});
    Button easy = new Button("EASY");
    easy.setOnAction(e -> {chooseBoard("e");});
    Button medium = new Button("MEDIUM");
    medium.setOnAction(e -> {chooseBoard("m");});
    Button hard = new Button("HARD");
    hard.setOnAction(e -> {chooseBoard("h");});
    buttonBox.getChildren().addAll(b,c,easy,medium,hard);
    totalboard.getChildren().add(buttonBox);
    Scene s = new Scene(totalboard);
    primaryStage.setScene(s);
    primaryStage.show();
}
public static void chooseBoard(String s){
    String file = "";
    if(s.equals("e")){
        file = "./easy.txt";
    }
    else if(s.equals("m")){
        file = "./medium.txt";
    }
    else if(s.equals("h")){
        file = "./hard.txt";
    }    
    int trash;
    try{
        Scanner scan = new Scanner(new File(file));
        int a = (int)(Math.random()*50);
        for(int x = 0; x < a*81; x++)
            trash = scan.nextInt();
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                board[i][j] = scan.nextInt();   
            }
        }
    }
    catch (FileNotFoundException e){
    }
    buttonShower();
}
public static void buttonDoer(int n){
    int x = (int)(n/9);
    int y = n%9;
    int a = (board[x][y] + 1) % 10;
    buttons[n].setText(""+a);
    board[x][y] = a; 
}
//sets the textfields of the buttons from the board[][]
public static void buttonShower(){
    for(int i = 0; i < 9; i++){
        for(int j = 0; j < 9; j++)
            buttons[9*i + j].setText(board[i][j]+""); 
    }
    //b.setDisable(true);
}
//sets the buttons and board back to all zeroes.
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
//will determine if board is solvable before recursing
public static boolean isSolvable(){
    boolean isSolvable = false;
    boolean isEmpty = true;
    for(int i = 0; i < 9; i++){
        for(int j = 0; j < 9; j++){
           if(board[i][j] != 0){
                isEmpty = false;
                if(Rules.checkRules(board[i][j], new Index(i,j), new Board(board)))
                    isSolvable = true;
                else return false;
           }      
        }
    }
    //return true if no collisions or its empty
    return (isSolvable || isEmpty);
}
//the backtracking recursive method
public static void recurseSolve(int row, int col){
    if(!controller){
        //solution found!
        if(row > 8){
            controller = true;
            buttonShower();
            b.setDisable(true);
        }
	    //if the square is empty, do I go right a square or down a row
        else if(board[row][col] != 0){
		    if(col >= 8)
		    	recurseSolve(row+1, 0);
		    else
    			recurseSolve(row, col+1);
	    }
        //if the square is empty
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
        }
    }
}
//check if it conflicts its row and column and box
public static boolean checkRules(int element, Index index, Board b){
	int[][] board = b.getBoard();
    //checking column
    for(int i=0; i < board.length; i++){
		if(i == index.getCol())
			continue;
		else{
			if(element == board[index.getRow()][i])
				return false;
		}
	}
    //checking row
	for(int i=0; i < board.length; i++){
		if(i == index.getRow())
			continue;
        else{
			if(element == board[i][index.getCol()])
				return false;
        }
    }
    //checking box
	int boxSize = b.getBoxSize();
	int boxRow = (index.getRow() / boxSize)*boxSize;
	int boxCol = (index.getCol() / boxSize)*boxSize;
	for(int i = boxRow; i < boxRow + boxSize; i++){
		for(int j = boxCol; j < boxCol + boxSize; j++){
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
}
class Index{
    private int row;
    private int col;
    public Index(int r, int c){
        row = r;
        col = c;
    }
    public int getRow(){
        return row;
    }
    public int getCol(){
        return col;
    }
}
class Board{
	private int[][] _board;
	private int _boxSize;
	//allows for construction of non 9x9 sudokus
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
    public void printboard(){
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                System.out.print(_board[i][j]);
            }
            System.out.println();
        }
    }
}
