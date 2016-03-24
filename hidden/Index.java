public class Index{
	private int _row;
	private int _col;	
	public Index(int row, int col){
		_row = row;
		_col = col;
	}
	//Getters
	public int getRow(){
		return _row;
	}
	public int getCol(){
		return _col;
	}
	//End getters
	public boolean equals(Index other){
		return ((_row == other.getRow()) && (_col == other.getCol()));
	}
}
