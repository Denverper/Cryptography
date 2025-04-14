#include <iostream>
using namespace std;

string encryptVigenere(const string& plainText, const string& key) {
    string encryptedText;
    int keyIndex = 0;
    for (char c : (plainText)) {
        if (isalpha(c)) { // only encrypt alphabetic characters
            c = tolower(c); 
            int shift = toupper(toupper(key[keyIndex]) - 'A');
            char encryptedChar = (c - 'a' + shift) % 26 + 'a';
            encryptedText += toupper(encryptedChar); // cipher text is uppercase
            keyIndex = (keyIndex + 1) % key.length(); // go to next key character, could wrap around
        }
    }
    return encryptedText;
}

string decryptVigenere(const string& cipherText, const string& key) {
    // use encryption function to decrypt
    // by creating a new key that is the inverse of the original key
    string newKey;
    for (int i = 0; i < key.length(); i++) { // create a new key that is the inverse of the original key
        newKey += (26 - (toupper(key[i]) - 'A')) + 'A';
    }

    string decryptedText = encryptVigenere(cipherText, newKey);
    for (char& c : decryptedText) { // lowercase for consistency in plaintext messages
        c = tolower(c);
    }
    return decryptedText;
}


// int main() {
//     // Example usage
//     // Encrypt and decrypt a message using the Vigenere cipher
//     string plainText, key;
//     plainText = "meetmeatmidnight";
//     key = "tryst";

//     cout << "Plain Text: " << plainText << endl;
//     cout << "Key: " << key << "\n" << endl;

//     string cipherText = encryptVigenere(plainText, key);
//     cout << "Cipher Text: " << cipherText << endl;
//     string decryptedText = decryptVigenere(cipherText, key);
//     cout << "Decrypted Text: " << decryptedText << endl;
//     return 0;
// }

