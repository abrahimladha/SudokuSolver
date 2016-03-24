public class Rules{
	public static boolean checkRowCol(int element, Index index, Board b){
		int[][] board = b.getBoard();
		
		//Checking if the element is equal to any of the other elements in the same row
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
				if((i == index.getRow()) && (j == index.getCol()))	//Don't compare it with itself
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
