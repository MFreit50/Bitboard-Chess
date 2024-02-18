#include <stdio.h>
#include "definitions.h"

const U64 NOT_A_FILE = 0xFEFEFEFEFEFEFEFEULL;
const U64 NOT_H_FILE = 0x7F7F7F7F7F7F7F7FULL;
const U64 NOT_HG_FILE= 0x3F3F3F3F3F3F3F3FULL;
const U64 NOT_AB_FILE= 0xFCFCFCFCFCFCFCFCULL;

enum{
    white, black
};
//attacks table [side][square]
U64 pawnAttacks[2][64];
U64 knightAttacks[64];
U64 kingAttacks[64];

U64 maskPawnAttacks(int side, int square){
    //piece bitboard
    U64 board = 0ULL;

    //attack bitboard result
    U64 attacks = 0ULL;

    //set piece
    setBit(board, square);
    printBoard(board);

    if(!side){  //white pawns
        if((board >> 7) & NOT_A_FILE) attacks |= (board >> 7);
        if((board >> 9) & NOT_H_FILE) attacks |= (board >> 9);
    }else{      //black pawns
        if((board << 7) & NOT_H_FILE) attacks |= (board << 7);
        if((board << 9) & NOT_A_FILE) attacks |= (board << 9);
    }
    //return attack map
    return attacks;
}

//generate knight attacks
U64 maskKnightAttacks(int square){
    //result attacks
    U64 attacks = 0ULL;

    //piece bitboard
    U64 board = 0ULL;

    setBit(board, square);
 
    //generate knight attacks
    if((board >> 17) & NOT_H_FILE) attacks |= (board >> 17);
    if((board >> 15) & NOT_A_FILE) attacks |= (board >> 15);
    if((board >> 10) & NOT_HG_FILE)attacks |= (board >> 10);
    if((board >> 6 ) & NOT_AB_FILE)attacks |= (board >> 6 );

    if((board << 17) & NOT_A_FILE) attacks |= (board << 17);
    if((board << 15) & NOT_H_FILE) attacks |= (board << 15);
    if((board << 10) & NOT_AB_FILE)attacks |= (board << 10);
    if((board << 6 ) & NOT_HG_FILE)attacks |= (board << 6 );

    return attacks;
}

U64 maskKingAttacks(int square){
    //piece bitboard
    U64 board = 0ULL;

    //attack bitboard result
    U64 attacks = 0ULL;

    //set piece
    setBit(board, square);

    if(board >> 8) attacks |= (board >> 8);
    if(board << 8) attacks |= (board << 8);
    if((board << 1) & NOT_A_FILE){
     attacks |= (board << 9);
     attacks |= (board >> 7);
     attacks |= (board << 1);
    }
    if((board >> 1) & NOT_H_FILE){
        attacks |= (board >> 9);
        attacks |= (board << 7);
        attacks |= (board >> 1);
    }
    //return attack map
    return attacks;
}

//initialize leaper pieces attacks
void initializeLeaperAttacks(){
    //loop over 64 board squares
    for(int square = 0; square < 64; square++){
        //pawns 
        pawnAttacks[white][square] = maskPawnAttacks(white, square);
        pawnAttacks[black][square] = maskPawnAttacks(black, square);
        //knight
        knightAttacks[square] = maskKnightAttacks(square);
        //king
        kingAttacks[square] = maskKingAttacks(square);
    }
}