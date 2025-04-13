using namespace std;
#include <iostream>
#include <numeric>
#include <string>
#include <vector>
#include <unordered_map>

unordered_map<int, int> inverses;

void initializeInverses() {
    inverses.insert({1, 1});   inverses.insert({3, 9});   inverses.insert({5, 21});
    inverses.insert({7, 15});  inverses.insert({9, 3});   inverses.insert({11, 19});
    inverses.insert({15, 7});  inverses.insert({17, 23}); inverses.insert({19, 11});
    inverses.insert({21, 5});  inverses.insert({23, 17}); inverses.insert({25, 25});
}

string encryptAffine(const string& plaintext, int a, int b) {
    string ciphertext = "";
    for (char c : plaintext) {
        c = tolower(c); 
        if (isalpha(c)) {
            char encryptedChar = ((a * (c - 'a') + b) % 26) + 'A';
            ciphertext += encryptedChar;
        }
    }
    return ciphertext;
}

string decryptAffine(const string& chiphertext, int a, int b) {
    string plaintext = "";
    for (char c : chiphertext) {
        c = toupper(c); 
        if (isalpha(c)) {
            int decoded = (c - 'A' - b);
            if (decoded < 0) {
                decoded += 26 * ((abs(decoded) / 26) + 1);
            }
            int invA = inverses[a];
            char encryptedChar = ((invA * decoded) % 26) + 'a';
            plaintext += encryptedChar;
        }
    }
    return plaintext;
}