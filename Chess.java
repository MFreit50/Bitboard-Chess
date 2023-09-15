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
		board[0][7] = 60;
		//white rooks
		board[7][0] = 6;
		board[7][7] = 6;
		//black knights
		board[0][1] = 50;
		board[0][6] = 50;
		//white knights
		board[7][1] = 5;
		board[7][6] = 5;
		//black bishops
		board[0][2] = 40;
		board[0][5] = 40;
		//white bishops
		board[7][2] = 4;
		board[7][5] = 4;
		//black king and queen
		board[0][3] = 30; //queen
		board[0][4] = 20; //king
		//white king and queen
		board[7][3] = 3;
		board[7][4] = 2;
	}
	public static void printBoard(){
		for(int i = 0; i < board.length; i++){
			System.out.print((i + 1) + " ");
			for(int j = 0; j < board[0].length; j++){
				if(board[i][j] < 10){
					System.out.print("| " + board[i][j] + " ");
				}else{
					System.out.print("|" + board[i][j]);
				}
			}
			System.out.println("|");
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
			break;
		}
		int t = 0;
		//idea: search for knights outside of for loop
		for(int i = 0; i < d && (c1&&c2&&r1&&r2&&rd1&&rd2&&ld1&&ld2&&k); i++){
			if(c1 && t=search(x-i,y,target)){
				if(t == 1){return board[y][x-i];}
				c1 = false;
			}
			if(c2 && t=search(x+i,y,target)){
				if(t == 1){return board[y][x+i];}
				c2 = false;
			}
			if(r1 && t=search(x,y-i,target)){
				if(t == 1){return board[y-i][x];}
				r1 = false;
			}
			if(r2 && t=search(x,y+i,target)){
				if(t == 1){return board[y+i][x];}
				r2 = false;
			}
			if(rd1 && t=search(x+i,y-i,target)){
				if(t == 1){return board[y-i][x+i];}
				rd1 = false;
			}
			if(rd2 && t=search(x-i,y+i,target)){
				if(t == 1){return board[y+i][x-i];}
				rd2 = false;
			}
			if(ld1 && t=search(x-i,y-i,target)){
				if(t == 1){return board[y-i][x-i];}
				ld1 = false;
			}
			if(ld2 && t=search(x+i,y+i,target)){
				if(t == 1){return board[y+i][x+i];}
				ld2 = false;
			}
		}
	}
	private static int search(int x, int y, int target){
		if(x >= 0 && x < 8 && y >= 0 && y < 8){
			if(board[y][x] == 0){
				return 0;
			}else if(board[y][x] != target){
				return -1;
			}else{
				return 1;
			}
		}
		return -1;
	}
	private static boolean searchKnight(int x, int y){
		target = player ? 5:50;
		boolean up1 = true, up2 = true;
		boolean l1 = true, l2 = true;
		boolean r1 = true, r2 = true;
		boolean d1 = true, d2 = true;

		switch(x){
		case:7
			up2 = r1 = r2 = d2 = false;
			break;
		case:6
			r1 = r2 = false;
			break;
		case:0
			up1 = l1 = l2 = d1 = false;
			break;
		case:1
			l1 = l2 = false;
			break;}
		switch(y){
		case:7
			up1 = up2 = l2 = r2 = false;
			break;
		case:6
			up1 = up2 = false;
			break;
		case:0
			d1 = d2 = l1 = r1 = false;
			break;
		case:1
			d1 = d2 = false;
			break;}
		if(up1 && board[y-2][x-1] == target){return true;}
		if(up2 && board[y-2][x+1] == target){return true;}
		if(l1 && board[y-1][x-2] == target){return true;}
		if(l2 && board[y+1][x-2] == target){return true;}
		if(r1 && board[y-1][x+2] == target){return true;}
		if(r2 && board[y+1][x+2] == target){return true;}
		if(d1 && board[y+2][x-1] == target){return true;}
		if(d2 && board[y+2][x+1] == target){return true;}
		return false;
	}
	private static boolean searchCheck(int[] pos, int id){
		target = player ? 2:20;
		boolean c1 = true, c2 = true; //collumns
		boolean r1 = true, r2 = true; //rows
		boolean rd1 = true, rd2 = true, ld1 = true ld2 = true; //diagonals
		int x = pos[1];
		int y = pos[0];

		//capture ids 
		switch(id){
		case:0//knight
			break;
		case:1//forward
			r1=r2=false;
			break;
		case:2//sideways
			c1=c2=false;
			break;
		case:3//right diagonal
			rd1=rd2=false;
			break;
		case:4//left diagonal
			ld1=ld2=false;
			break;
		}
		//temporary variable
		int t = 0;
		for(int i = 0; i < 8 && (c1&&c2&&r1&&r2&&rd1&&rd2&&ld1&&ld2); i++){
			if(c1 && t=search(x-i,y,target)){
				if(t == 1){return true;}
				c1 = false;
			}
			if(c2 && t=search(x+i,y,target)){
				if(t == 1){return true;}
				c2 = false;
			}
			if(r1 && t=search(x,y-i,target)){
				if(t == 1){return true;}
				r1 = false;
			}
			if(r2 && t=search(x,y+i,target)){
				if(t == 1){return true;}
				r2 = false;
			}
			if(rd1 && t=search(x+i,y-i,target)){
				if(t == 1){return true;}
				rd1 = false;
			}
			if(rd2 && t=search(x-i,y+i,target)){
				if(t == 1){return true;}
				rd2 = false;
			}
			if(ld1 && t=search(x-i,y-i,target)){
				if(t == 1){return true;}
				ld1 = false;
			}
			if(ld2 && t=search(x+i,y+i,target)){
				if(t == 1){return true;}
				ld2 = false;
			}
		}
		return false;

	}
	private static boolean searchThreat(){

	}
	private static int columnToNumber(char c){
		if(c >= 'a' && c <= 'h'){
			return c -'a';
		}else{
			return {-1,-1}; //error
		}
	}
}