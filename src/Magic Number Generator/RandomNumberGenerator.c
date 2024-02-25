#include "definitions.h"
//generate 32-bit pseudo legal numbers
unsigned int state = 1804289383;
unsigned int randomNumberU32(){
    //current state
    unsigned int number = state;
    //XOR shift algorithm
    number ^= number << 13;
    number ^= number >> 17;
    number ^= number << 5;

    //update random number
    state = number;
    return number;
}
//generate 54-bit pseudo legal numbers
U64 randomNumberU64(){
    //define 4 random numbers
    U64 n1, n2, n3, n4;

    //initialize random numbers
    n1 = (U64)(randomNumberU32() & 0xFFFF);
    n2 = (U64)(randomNumberU32() & 0xFFFF);
    n3 = (U64)(randomNumberU32() & 0xFFFF);
    n4 = (U64)(randomNumberU32() & 0xFFFF);

    return n1 | (n2 << 16) | (n3 << 32) | (n4 << 48);
}

U64 generateMagicNumber(){
    return randomNumberU64() & randomNumberU64() & randomNumberU64();
}