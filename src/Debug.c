#include <stdio.h>
#include "Bitboard.h"
#include <string.h>
//encode pieces
enum{P, N, B, R, Q, K, p, n, b, r, q, k};
//define occupancy bitboards
U64 occupancies[3];
//character to encoded constants
int charPieces[] = {
    ['P'] = P,
    ['N'] = N,
    ['B'] = B,
    ['R'] = R,
    ['Q'] = Q,
    ['K'] = K,
    ['p'] = p,
    ['n'] = n,
    ['b'] = b,
    ['r'] = r,
    ['q'] = q,
    ['k'] = k
};
//Unicode pieces
char *unicodePieces[12] = {"♙", "♘", "♗", "♖", "♕", "♔", "♟︎", "♞", "♝", "♜", "♛", "♚"};
//ASCII pieces
char asciiPieces[12] = "PNBRQKpnbrqk";
U64 bitboards[12];

const char *squareToCoordinates[] = {
"a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
"a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
"a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
"a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
"a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
"a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
"a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
"a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"
};

void printBitboard(U64 board){
    //ranks
    for(int rank = 0; rank < 8; rank++){
        for(int file = 0; file < 8; file++){
            int square = rank*8 + file;
            if(!file){//print board ranks
                printf("%d  ", 8 - rank);
            }
            printf(" %d", getBit(board, square) ? 1 : 0);
        }
        printf("\n");
    }
    //print board files
    printf("\n    a b c d e f g h\n\n");
    //print bitboard value
    printf("    Bitboard: %llud\n\n", board);
}

void parseFEN(char *fen){
    //reset bitboard position
    memset(bitboards, 0ULL, sizeof(bitboards));
    //reset occupancies
    memset(occupancies, 0ULL, sizeof(occupancies));
    //reset game state variable
    side = 0;
    enPassant = noSquare;
    castle = 0;

    //ranks
    for(int rank = 0; rank < 8; rank++){
        for(int file = 0; file < 8; file++){
            int square = rank*8 + file;

            if((*fen >= 'b' && *fen <= 'r') || (*fen >= 'B' && *fen <= 'R')){
                //initialize piece type
                int piece = charPieces[*fen];
                //set piece on corresponding bitboard
                setBit(bitboards[piece], square);
                //increment pointer to FEN string
                fen++;
            }
            if(*fen >= '0' && *fen <= '9'){
                //convert char number to integer number
                int offset = *fen - '0';
                //define piece variable
                int piece = -1;
                for(int bitboardPiece = P; bitboardPiece <= k; bitboardPiece++){
                    //if there is a piece on current square
                    if(getBit(bitboards[bitboardPiece], square)){
                    //get piece code
                        piece = bitboardPiece;
                    }
                }

                //if no piece on current square
                if(piece == -1){
                    //decrement file
                    file--;
                }
                //adjust file counuter
                file += offset;
                //increment pointer to FEN string
                fen++;
            }
            //match rank separator
            if(*fen == '/'){
                fen++;
            }
        }
    }
    //parse side to move
    fen++;
    (*fen == 'w') ? (side = white) : (side = black);

    //parse castling rights
    fen += 2;
    while(*fen != ' '){
        switch (*fen){
            case 'K': castle |= whiteKingCastle; break;
            case 'Q': castle |= whiteQueenCastle; break;
            case 'k': castle |= blackKingCastle; break;
            case 'q': castle |= blackQueenCastle; break;
            case '-': break;
        }
        //increment pointer
        fen++;
    }

    //parse en passant square
    fen++;
    if(*fen != '-'){
        //parse enpassant file & rank
        int file = fen[0] - 'a';
        int rank = 8 - (fen[1] - '0');
        //initialize en passant square
        enPassant = rank * 8 + file;
    }else{
        enPassant = noSquare;
    }
    //loop over white piece bitboards
    for(int piece = P; piece <= K; piece++){
        //populate white occupancy bitboard
        occupancies[white] |= bitboards[piece];
    }
    //loop over black piece bitboards
    for(int piece = p; piece <= k; piece++){
        //populate black occupancy bitboard
        occupancies[black] |= bitboards[piece];
    }
    //initialize the occupancies of both
    occupancies[both] |= occupancies[white];
    occupancies[both] |= occupancies[black];
    printf("no square%d", noSquare);
    printf("enPassant = %d\n",enPassant);
}

void printBoard(){
    printf("\n");
    for(int rank = 0; rank < 8; rank++){
        for(int file = 0; file < 8; file++){
            int square = rank*8 + file;
            if(!file){//print board ranks
                printf("%d  ", 8 - rank);
            }
            //define piece variable
            int piece = -1;
            //loop over all pieces bitboards
            for(int bitboardPiece = P; bitboardPiece <= k; bitboardPiece++){
                if(getBit(bitboards[bitboardPiece], square))
                    piece = bitboardPiece;
            }
            printf(" %s", (piece == -1) ? "." : unicodePieces[piece]);
        }
        printf("\n");
    }
    //print board files
    printf("\n    a b c d e f g h\n\n");
    //print side to move
    printf("Side to move: %s\n", !side ? "white" : "black");
    //print en passant square
    printf("enPassant = %d\n",enPassant);
    printf("En passant: %s\n", (enPassant != noSquare) ? squareToCoordinates[enPassant] : "none");

    //print castling rights
    printf("Castling: %c%c%c%c\n\n", 
    (castle & whiteKingCastle) ? 'K' : '.',(castle & whiteQueenCastle) ? 'Q' : '.',
    (castle & blackKingCastle) ? 'k' : '.',(castle & blackQueenCastle) ? 'q' : '.');
}