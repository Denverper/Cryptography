#ifndef VIG_H
#define VIG_H

#include <iostream>
#include <string>
#include <cctype>

using namespace std;

// Function to encrypt a plaintext message using the Vigenere cipher
string encryptVigenere(const string& plainText, const string& key);

// Function to decrypt a ciphertext message using the Vigenere cipher
string decryptVigenere(const string& cipherText, const string& key);

#endif // VIG_H