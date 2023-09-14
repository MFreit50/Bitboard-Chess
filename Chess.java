import java.util.Scanner;
public class Chess{
	public static final int[][] board = new int[8][8];
	public static boolean player = true;
	public static void main(String[] args){
		initiate(); //puts pieces on board;
		printBoard();
	}
	public static void initiate(){
		/*
		empty space = 0

		white pieces:
		pawn = 1
		king = 2
		queen = 3
		bishop = 4
		knight = 5
		rook = 6

		black pieces
		pawn = 10
		king = 20
		queen = 30
		bishop = 40
		knight = 50
		rook = 60
		*/

		//places pawns
		for(int i = 0; i < 8; i++){
			board[i][1] = 10; //black pawns;
			board[i][6] = 1; //white pawns
		}
		//black rooks
		board[0][0] = 60;
		board[7][0] = 60;
		//white rooks
		board[0][7] = 6;
		board[7][7] = 6;
		//black knights
		board[1][0] = 50;
		board[6][0] = 50;
		//white knights
		board[1][7] = 5;
		board[6][7] = 5;
		//black bishops
		board[2][0] = 40;
		board[5][0] = 40;
		//white bishops
		board[2][7] = 4;
		board[5][7] = 4;
		//black king and queen
		board[3][0] = 30; //queen
		board[4][0] = 20; //king
		//white king and queen
		board[3][7] = 3;
		board[4][7] = 2;
	}
	public static void printBoard(){
		for(int i = 0; i < board.length; i++){
			System.out.print((i + 1) + " ");
			for(int j = 0; j < board[0].length; j++){
				if(board[j][i] < 10){
					System.out.print("| " + board[j][i] + " ");
				}else{
					System.out.print("|" + board[j][i]);
				}
				if(j == 7)
					System.out.println("|");
			}
		}
	}
	public static void move(){
		Scanner sc = new Scanner(System.in);
		System.out.println("make a move");
		while(true){
			String s = sc.nextLine();
			if(validate(s)){
				player = !player;
				break;
			}
		}
	}
	private static boolean validate(String s, boolean player){
		s = s.toLowerCase();
		int[] pos = {s.charAt(1),columnToNumber(s.charAt(2))};
		if(pos[1] == -1){//error checking
			return false;
		}
		int x = pos[1], y = pos[2];
		char piece = s.charAt(0);
		if(piece = 'p'){
			int target = player ? 1:10;

			if(board[y][x] != 0){//prevents pawn from overwriting piece
				if(target == 10){//black pawn
					if(y - 1 >= 0 && x - 1 >= 0){//diagonal captures
						if(board[y-1][x-1] == 10 && board[y][x] < 10){ //searchCheck() will need pos given by search() (fix later)
							if(searchCheck(int[] arr = {y-1,x-1}, false)){return false;}//think this through
							board[y-1][x-1] = 0;
							board[y][x] = 10;
							return true;
						}
					}else if(y - 1 >= 0 && x + 1 < 8){
						if(board[y+1][x-1] != 0 && board[y][x] < 10){
							board[y-1][x+1] = 0;
							board[y][x] = 10;
							return true;
						}//make new else if statements for en passant
					}else{
						return false;
					}
				}
				if(target == 1){//white pawn
					if(y + 1 < 8 && x - 1 >= 0){
						if(board[y+1][x-1] == 1 && board[y][x] > 9){
							board[y+1][x-1] = 0;
							board[y][x] = 1;
							return true;
						}
					}else if(y + 1 < 8 && x + 1 < 8){
						if(board[y+1][x+1] == 1 && board[y][x] > 9){
							board[y+1][x+1] = 0;
							board[y][x] = 1;
							return true;
						}
					}else{
						return false;
					}
				}//checks for diagonal captures
			}
		}
	}
	private static int[] search(int[] pos, int id){
		boolean c1 = true, c2 = true; //collumns
		boolean r1 = true, r2 = true; //rows
		boolean rd1 = true, rd2 = true, ld1 = true ld2 = true; //diagonals
		boolean k = true //knight movements
		int d = 8; //distance
		int x = pos[1];
		int y = pos[0];
		switch(id){
		case 1://pawn
			c1 = c2 = rd1 = rd2 = ld1 = ld2 = k = false;
			d = (pos[1]==3 || pos[1]==4) ? 2:1; 
		break;
		case 2://king (needs revision)
			break;
		case 3://queen
			k = false;
			break;
		case 4://bishop
			c1 = c2 = r1 = r2 = k = false;
			break;
		case 5://knight
			c1 = c2 = r1 = r2 = rd1 = rd2 = ld1 = ld2 = false;
			break;
		case 6://rook
			rd1 = rd2 = ld1 = ld2 = k = false;
		}
		//idea: search for knights outside of for loop
		for(int i = 0; i < d && (c1&&c2&&r1&&r2&&rd1&&rd2&&ld1&&ld2&&k); i++){
			if(c1 && x - i >= 0){
				if(board[y][x-i] == 0){
					break;
				}else if(board[y][x-i] != id){
					c1 = false;
				}else{
					return board[y][x-i];
				}
			}else{
				c1 = false;
			}

			if(c2 && x + i < 8){
				if(board[y][x+i] == 0){
					break;
				}else if(board[y][x+i] != id){
					c2 = false;
				}else{
					return board[y][x+i];
				}
			}else{
				c2 = false;
			}

			if(r1 && y - i >= 0){
				if(board[y-i][x] == 0){
					break;
				}else if(board[y-i][x] != id){
					r1 = false;
				}else{
					return board[y-i][x];
				}
			}else{
				r1 = false;
			}

			if(r2 && y + i < 8){
				if(board[y+i][x] == 0){
					break;
				}else if(board[y+i][x] != id){
					c2 = false;
				}else{
					return board[y+i][x];
				}
			}else{
				c2 = false;
			}

			if(rd1 && x + i < 8 && y - i >= 0){
				if(board[y-i][x+i] == 0){
					break;
				}else if(board[y-i][x+i] != id){
					rd1 = false;
				}else{
					return board[y-i][x+i];
				}
			}else{
				rd1 = false;
			}

			if(rd2 && x - i >= 0 && y + 1 < 8){
				if(board[y+i][x-i] == 0){
					break;
				}else if(board[y+i][x-i] != id){
					rd2 = false;
				}else{
					return board[y+i][x-i];
				}
			}else{
				rd2 = false;
			}

			if(ld1 && x - i >= 0 && y - i >= 0){
				if(board[x-i][y-i] == 0){
					break;
				}else if(board[y-i][x-i] != id){
					ld1 = false;
				}else{
					return board[y-i][x-i];
				}
			}else{
				ld1 = false;
			}

			if(ld2 && x + i <= 8 && y + i <= 8){
				if(board[y+i][x+i] == 0){
					break;
				}else if(board[y+i][x+i] != id){
					ld2 = false;
				}else{
					return board[y+i][x+i];
				}
			}else{
				ld2 = false;
			}

		}
	}
	private static boolean searchCheck(int[] pos, boolean color){
		target = color ? 2:20;
		boolean c1 = true, c2 = true; //collumns
		boolean r1 = true, r2 = true; //rows
		boolean rd1 = true, rd2 = true, ld1 = true ld2 = true; //diagonals
		int x = pos[1];
		int y = pos[0];

		for(int i = 0; i < 8 && (c1&&c2&&r1&&r2&&rd1&&rd2&&ld1&&ld2); i++){
			if(c1 && x - i >= 0){
				if(board[y][x-i] == 0){
					break;
				}else if(board[y][x-i] != target){
					c1 = false;
				}else{
					return true;
				}
			}else{
				c1 = false;
			}

			if(c2 && x + i <= 8){
				if(board[y][x+i] == 0){
					break;
				}else if(board[y][x+i] != target){
					c2 = false;
				}else{
					return true;
				}
			}else{
				c2 = false;
			}

			if(r1 && y - i >= 0){
				if(board[y-i][x] == 0){
					break;
				}else if(board[y-i][x] != target){
					r1 = false;
				}else{
					return true;
				}
			}else{
				r1 = false;
			}

			if(r2 && y + i <= 8){
				if(board[y+1][x] == 0){
					break;
				}else if(board[y+1][x] != target){
					c2 = false;
				}else{
					return true;
				}
			}else{
				c2 = false;
			}

			if(rd1 && x + i <= 8 && y - i >= 0){
				if(board[y-i][x+i] == 0){
					break;
				}else if(board[y-i][x+i] != target){
					rd1 = false;
				}else{
					return true;
				}
			}else{
				rd1 = false;
			}

			if(rd2 && x - i >= 0 && y + 1 <= 8){
				if(board[y+i][x-i] == 0){
					break;
				}else if(board[y+i][x-i] != target){
					rd2 = false;
				}else{
					return true;
				}
			}else{
				rd2 = false;
			}

			if(ld1 && x - i >= 0 && y - i >= 0){
				if(board[x-i][y-i] == 0){
					break;
				}else if(board[y-i][x-i] != target){
					ld1 = false;
				}else{
					return true;
				}
			}else{
				ld1 = false;
			}

			if(ld2 && x + i <= 8 && y + i <= 8){
				if(board[y+i][x+i] == 0){
					break;
				}else if(board[y+i][x+i] != target){
					ld2 = false;
				}else{
					return true;
				}
			}else{
				ld2 = false;
			}

		}
		return false;

	}
	private static int columnToNumber(char c){
		if(c >= 'a' && c <= 'h'){
			return c -'a';
		}else{
			return {-1,-1}; //error
		}
	}
}