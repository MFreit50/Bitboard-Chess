//#include "definitions.h"
#pragma once
//used for readable bit manipulations
#define U64 unsigned long long
//bit manipulation
#define getBit(board, square) ((board) & (1ULL << (square)))
#define setBit(board, square) ((board) |= (1ULL << (square)))
#define popBit(board, square) (getBit(board, square) ? board ^= (1ULL << square) : 0)
#define countBits(board) __builtin_popcountll(board)
#define lsbIndex(board) (board ? __builtin_ctzll(board) : -1)

//variables
int side;               //side to move
typedef const enum{
    a8, b8, c8, d8, e8, f8, g8, h8,
    a7, b7, c7, d7, e7, f7, g7, h7,
    a6, b6, c6, d6, e6, f6, g6, h6,
    a5, b5, c5, d5, e5, f5, g5, h5,
    a4, b4, c4, d4, e4, f4, g4, h4,
    a3, b3, c3, d3, e3, f3, g3, h3,
    a2, b2, c2, d2, e2, f2, g2, h2,
    a1, b1, c1, d1, e1, f1, g1, h1, noSquare
}squares;
//define bitboards
U64 bitboards[12];

//enums board squares
enum{rook, bishop};

//en passant square
int enPassant = noSquare;

//define sides
enum{white, black, both};
/*
0001 1 white king can castle king side
0010 2 white king can castle queen side
0100 4 black king can castle king side
1000 8 black king can castle queen side
*/
//castle permissions enum
enum{whiteKingCastle = 1, whiteQueenCastle = 2, blackKingCastle = 4, blackQueenCastle = 8};
//castling rights
int castle;