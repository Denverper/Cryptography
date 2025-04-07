#include <string>
#include <iostream>

using namespace std;

float english_freq[26] = {			
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

float getMIC(const string& text, const bool english=false) {
    int freq[26] = {0};
    int len = text.length();
    for (int i = 0; i < len; i++) {
        char c = tolower(text[i]);
        if (c >= 'a' && c <= 'z') {
            freq[c - 'a']++;
        }
    }

    float mic = 0.0;
    for (int i = 0; i < 26; i++) {
        if (freq[i] != 0) {
            float f = static_cast<float>(freq[i]) / len;
            if (english) {
                mic += f * english_freq[i];
            } else {
                mic += f * f;
            }
        }
    }
    return mic;
}


string _shiftText(const string& text, int shift) {
    string shiftedText = "";
    for (char c : text) {
        if (isalpha(c)) {
            char cTemp = tolower(c);
            char shiftedChar = (cTemp - 'a' + shift) % 26 + 'a';
            shiftedText += shiftedChar;
        } 
    }
    return shiftedText;
}

char getKey(const string& text) {
    float bestMIC = 1000.0;
    char bestKey = 0;
    for (int i = 0; i < 26; i++) {
        string shiftedText = _shiftText(text, i);
        float mic = getMIC(shiftedText, true);
        float dist = abs(mic - 0.065);
        if (dist < bestMIC) {
            bestMIC = dist;
            bestKey = i;
        }
    }
        
    return bestKey;
}

string encryptCeaser(const string& text, int key) {
    return _shiftText(text, key);
}

string decryptCeaser(const string& text) {
    char key = getKey(text);
    return _shiftText(text, key);
}

int main() {
    string text = "this is a test to see if the ceaser cipher decryption works automatically";
    int key = 3;
    cout << "Original Text: " << text << endl;

    string encryptedText = encryptCeaser(text, key);
    cout << "Encrypted Text: " << encryptedText << endl;

    string decryptedText = decryptCeaser(encryptedText);
    cout << "Decrypted Text: " << decryptedText << endl;

    return 0;
}

