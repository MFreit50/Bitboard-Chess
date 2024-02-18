#define U64 unsigned long long

//bit manipulation
#define getBit(board, square) (board & (1ULL << square))
#define setBit(board, square) (board |= (1ULL << square))
#define popBit(board, square) (getBit(board, square) ? board ^= (1ULL << square) : 0)

//constants
/*
#ifndef CONSTANTS_H
#define CONSTANTS_H
extern const U64 NOT_A_FILE;
extern const U64 NOT_H_FILE;
extern const U64 NOT_HG_FILE;
extern const U64 NOT_AB_FILE;
#endif
*/