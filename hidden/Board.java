public class Board{
	private int[][] _board;
	private int _boxSize;
	public Board(){
		_board = new int[9][9];
		_boxSize = 3;
	}
    /*
	public Board(int order){
		if(order == 4){
			_board = new int[order][order];
			_boxSize = 4;	
		}
		else{
			System.out.println("Unable to handle board sizes greater than 4x4 or lesser than 3x3");
			System.exit(1);
		}	
	}*/
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
