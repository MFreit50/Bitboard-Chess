#include <stdio.h>
#include "definitions.h"
#include "RandomNumberGenerator.c"
#include "Bitboard.h"

const int bishopRelevantBits[] = {
    6, 5, 5, 5, 5, 5, 5, 6,
    5, 5, 5, 5, 5, 5, 5, 5,
    5, 5, 7, 7, 7, 7, 5, 5,
    5, 5, 7, 9, 9, 7, 5, 5,
    5, 5, 7, 9, 9, 7, 5, 5,
    5, 5, 7, 7, 7, 7, 5, 5,
    5, 5, 5, 5, 5, 5, 5, 5,
    6, 5, 5, 5, 5, 5, 5, 6
};

const int rookRelevantBits[] = {
    12, 11, 11, 11, 11, 11, 11, 12,
    11, 10, 10, 10, 10, 10, 10, 11,
    11, 10, 10, 10, 10, 10, 10, 11,
    11, 10, 10, 10, 10, 10, 10, 11,
    11, 10, 10, 10, 10, 10, 10, 11,
    11, 10, 10, 10, 10, 10, 10, 11,
    11, 10, 10, 10, 10, 10, 10, 11,
    12, 11, 11, 11, 11, 11, 11, 12
};

U64 findMagicNumbers(int square, int relevantBits, int bishop){
    //initialize occupancies
    U64 occupancies[4096];

    //initialize attack tables
    U64 attacks[4096];

    //initialize used attacks
    U64 usedAttacks[4096];

    //initialize mask for a current piece
    U64 attackMask = bishop ? maskBishopAttacks(square) : maskRookAttacks(square);

    //initialize occupancy indicies
    int occupancyIndicies = 1 << relevantBits;

    //loop over occupancy indicies
    for(int index = 0; index < occupancyIndicies; index++){
        //initialize occupancies
        occupancies[index] = setOccupancy(index, relevantBits, attackMask);
        //initialize attacks
        attacks[index] = bishop ? bishopMoves(square, occupancies[index]) : rookMoves(square, occupancies[index]);
    }
    //test magic numbers loop
    for(int randomCount = 0; randomCount < 100000000; randomCount++){
        //generate magic number candidate
        U64 magicNumber = generateMagicNumber();
        //skip inappropriate magic numbers
        if(countBits((attackMask*magicNumber) & 0xFF00000000000000) < 6) continue;
        //initialize used attacks array
        memset(usedAttacks, 0ULL, sizeof(usedAttacks));
        //initialize index and fail flag
        int index, fail;

        //test magic index loop
        for(index = 0, fail = 0; !fail && index < occupancyIndicies; index++){
            //initialize magic index
            int magicIndex = (int)((occupancies[index] * magicNumber) >> (64 - relevantBits));

            //if magic index works
            if(usedAttacks[magicIndex] == 0ULL){
                //initialize used attacks
                usedAttacks[magicIndex] = attacks[index];
            }else if(usedAttacks[magicIndex] != attacks[index]){
                //magic index doesn't work
                fail = 1;
            }
        }
        if(!fail){
            return magicNumber;
        }
    }
    printf("Magic number fails");
        return 0ULL;
}
//initialize magic numbers
void initializeMagicNumbers(){
    //loop over 64 board squares
    printf("\n\nRook Magic Numbers:\n\n");
    for(int square = 0; square < 64; square++){
        //initialize rook magic numbers
        printf("0x%llxULL,\n", findMagicNumbers(square, rookRelevantBits[square], rook));
    }
    printf("Bishop Magic Numbers:\n\n");
    for(int square = 0; square < 64; square++){
        //initialize rook magic numbers
        printf("0x%llxULL,\n", findMagicNumbers(square, bishopRelevantBits[square], bishop));
    }
    printf("\n");
}