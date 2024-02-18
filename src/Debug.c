#include <stdio.h>
#include "definitions.h"
void printBoard(U64 board){
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