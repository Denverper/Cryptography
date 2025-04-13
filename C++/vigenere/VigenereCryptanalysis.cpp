using namespace std;
#include <iostream>
#include <numeric>
#include <string>
#include <vector>
#include <fstream>
#include "../CaesarCryptanalysis/CaesarCryptanalysis.h"
#include "vig.h"

int getVigKeyLength(const string& ciphertext) {
    int keyLength = 0;
    unordered_map<string, int> tripletMap;
    vector<int> distances;

    for (int i = 0; i < ciphertext.length() - 2; ++i) {
        string triplet = ciphertext.substr(i, 3);
        if (tripletMap.find(triplet) == tripletMap.end()) { // not in map yet
            tripletMap[triplet] = i;
        } else { // found a repeat, calculate distance and update map
            int distance = i - tripletMap[triplet];
            distances.push_back(distance);
            tripletMap[triplet] = i;
        }
    }

    if (distances.empty()) {
        return 1; // No repeating triplets found, return 1 as key length
    }

    int window = 20;
    unordered_map<int, int> gcdMap;
    for (int i = 0; i < distances.size(); ++i) {
        for (int j = i + 1; j < min(i + window, static_cast<int>(distances.size())); ++j) {
            int g = gcd(distances[i], distances[j]);
            if (g > 1) {
                if (gcdMap.find(g) == gcdMap.end()) {
                    gcdMap[g] = 1;
                } else {
                    gcdMap[g]++;
                }
            }
        }
    }

    int maxCount = 0;
    for (const auto& pair : gcdMap) {
        if (pair.second > maxCount) {
            maxCount = pair.second;
            keyLength = pair.first;
        }
    }

    return keyLength;
}

vector<string> getCaesarStrings(const string& ciphertext, int keyLength) {
    vector<string> caesarStrings(keyLength);
    for (int i = 0; i < ciphertext.length(); ++i) {
        caesarStrings[i % keyLength] += ciphertext[i];
    }
    return caesarStrings;
}

string getVigKey(const string& ciphertext, bool verbose = false) {
    if (abs(0.065 - getMIC(ciphertext, false)) <= 0.005) { // looks like English in freq analysis, try to decrypt with single character Caesar
        return string(1, getKey(ciphertext));
    }

    int keyLength = getVigKeyLength(ciphertext);
    if (verbose) {
        cout << "Found Key Length: " << keyLength << endl;
    }
    vector<string> caesarStrings = getCaesarStrings(ciphertext, keyLength);
    string key = "";

    for (const auto& str : caesarStrings) {
        char k = getKey(str) + 'A';
        if (verbose) {
            string shiftedStr = _shiftText(str, (26 - (k - 'A'))%26);
            cout << "Substring: " << str << " -- " << "found key: " << k << " -- " << "shifted substring: " << shiftedStr << " -- " << "MIC for key" << " -- " << getMIC(shiftedStr, true) << endl;
        }
        key += k;
    }

    return key;
}

string decryptVigenere(const string& ciphertext, bool verbose = false) {
    string decryptedText = "";
    if (verbose) {
        cout << "Ciphertext: " << ciphertext << endl;
    }
    string key = getVigKey(ciphertext, verbose);
    if (verbose) {
        cout << "Probable Key: " << key << endl;
    }
    string plaintext = decryptVigenere(ciphertext, key); // use the decrypt function from my Vigenere library
    if (verbose) {
        cout << "Decrypted Text: " << plaintext << endl;
        float mic = getMIC(plaintext, true);
        cout << "MIC: " << mic << endl;
        if (abs(mic - 0.065) <= 0.005) {
            cout << "This is likely English text." << endl;
        } else {
            cout << "This is likely not English text." << endl;
        }
    }
    return plaintext;
}

int main(int argc, char* argv[]) {
    bool verbose = false;
    string USAGE = "VigenereCryptanalysis.py:\n\t[-v | -verbose] : Optional. Enables verbose mode to display detailed decryption steps.\n\t[ -u | -h | -usage | -help ] : Optional. output the usage\n\t<ciphertext>  : Required. The ciphertext to decrypt. Can be a string or a file path with one line containing the cipher.\n\tIf a file is passed, each line will be treated as a seperate cipher\n\tIf no args are provided, cipher must be given in the terminal and verbose will default to False.\n";
    try {
        for (int i = 1; i < argc; ++i) {
            string arg = argv[i];
            if (arg == "-v" || arg == "-verbose") {
                verbose = true;
                argv[i] = ""; // Remove the entry in the array
            } else if (arg == "-u" || arg == "-h" || arg == "-usage" || arg == "-help") {
                cout << USAGE;
                return 0;
            } 
        }

        string ciphertext = argv[argc - 1];
        ifstream file(ciphertext);
        if (file.is_open()) {
            cout << "Reading from file: " << ciphertext << endl;
            string line;
            while (getline(file, line)) {
                decryptVigenere(line, verbose);
                cout << "----------------------------------------" << endl;
            }
            file.close();
            return 0;
        } else {
            // if not a file, treat ciphertext as a string and decrypt it
            decryptVigenere(ciphertext, verbose);
        }
    } catch (const exception& e) {
        cerr << "Error: " << e.what() << endl;
        cout << USAGE << endl;
        return 1;
    } catch (...) { // only verbose or improper args
        cout << USAGE << endl;
        return 0;
    }
}