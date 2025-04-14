#ifndef CAESAR_CRYPTANALYSIS_H
#define CAESAR_CRYPTANALYSIS_H

#include <string>
#include <iostream>
#include <cmath>

using namespace std;

const float english_freq[26] = {			
    0.082, /* a */
    0.015, /* b */
    0.028, /* c */
    0.043, /* d */ 
    0.127, /* e */
    0.022, /* f */
    0.020, /* g */
    0.061, /* h */
    0.070, /* i */
    0.002, /* j */
    0.008, /* k */
    0.040, /* l */
    0.024, /* m */
    0.067, /* n */
    0.075, /* o */
    0.019, /* p */
    0.001, /* q */
    0.060, /* r */
    0.063, /* s */
    0.091, /* t */
    0.028, /* u */
    0.010, /* v */
    0.023, /* w */
    0.001, /* x */
    0.020, /* y */
    0.001  /* z */
};

// Function to calculate the Match Index Coefficient (MIC)
float getMIC(const string& text, const bool english = false);

// Function to shift text by a given key
string _shiftText(const string& text, int shift);

// Function to determine the key for decryption
char getKey(const string& text);

// Function to encrypt text using the Caesar cipher
string encryptCaesar(const string& text, int key);

// Function to decrypt text using the Caesar cipher
string decryptCaesar(const string& text);

#endif