#include <stdio.h>
#include "Debug.c"
#include "Attacks.c"

void initializeAll(){
    initializeLeaperAttacks();
    initializeSliderAttacks(bishop);
    initializeSliderAttacks(rook);
}
int main(){
    initializeAll();
    parseFEN("rnbqkb1r/pp1p1pPp/8/2p1pP2/1P1P4/3P3P/P1P1P3/RNBQKBNR w KQkq e6 0 1");
    printBoard();
    return 1;
}