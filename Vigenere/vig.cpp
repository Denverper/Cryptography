#include <iostream>
using namespace std;

string encryptVigenere(const string& plainText, const string& key) {
    string encryptedText;
    int keyIndex = 0;
    for (char c : (plainText)) {
        if (isalpha(c)) {
            c = tolower(c); 
            int shift = toupper(toupper(key[keyIndex]) - 'A');
            char encryptedChar = (c - 'a' + shift) % 26 + 'a';
            encryptedText += toupper(encryptedChar);
            keyIndex = (keyIndex + 1) % key.length();
        }
    }
    return encryptedText;
}

string decryptVigenere(const string& cipherText, const string& key) {
    string newKey;
    for (int i = 0; i < key.length(); i++) { // create a new key that is the inverse of the original key
        newKey += (26 - (toupper(key[i]) - 'A')) + 'A';
    }

    string decryptedText = encryptVigenere(cipherText, newKey);
    for (char& c : decryptedText) { // lowercase for consistency
        c = tolower(c);
    }
    return decryptedText;
}


int main() {
    string plainText, key;
    plainText = "meetmeatmidnight";
    key = "tryst";

    cout << "Plain Text: " << plainText << endl;
    cout << "Key: " << key << "\n" << endl;

    string cipherText = encryptVigenere(plainText, key);
    cout << "Cipher Text: " << cipherText << endl;
    string decryptedText = decryptVigenere(cipherText, key);
    cout << "Decrypted Text: " << decryptedText << endl;
    return 0;
}

