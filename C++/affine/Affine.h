#ifndef AFFINE_H
#define AFFINE_H

#include <iostream>
#include <numeric>
#include <string>
#include <vector>
#include <unordered_map>

using namespace std;

extern unordered_map<int, int> inverses;

// Function to initialize the modular inverses for the affine cipher
void initializeInverses();

// Function to encrypt a plaintext string using the affine cipher
string encryptAffine(const string& plaintext, int a, int b);

// Function to decrypt a ciphertext string using the affine cipher
string decryptAffine(const string& ciphertext, int a, int b);

#endif // AFFINE_H