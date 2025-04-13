#include <tuple>
#include "Affine.h"
#include "../caesarcryptanalysis/CaesarCryptanalysis.h"

using namespace std;

tuple<int, int> getKey(string& ciphertext) {
    int a = 0;
    int b = 0;
    tuple<float, tuple<int,int>> best = make_tuple(1000, tuple<int, int>(0,0));
    for (const auto& pair : inverses) {
        int i = pair.first; // Access the key from the map
        for (int j = 0; j < 26; j++) {
            string curr = decryptAffine(ciphertext, i, j);
            float dist = 0.065 - getMIC(curr, true);
            if (dist < 0) {
                dist = -dist;
            }
            if (dist < get<0>(best)) {
                best = make_tuple(dist, tuple<int, int>(i,j));
            }
        }
    }
    return get<1>(best);
}

string decryptAffine(string& ciphertext) {
    tuple<int, int> key = getKey(ciphertext);
    return decryptAffine(ciphertext, get<0>(key), get<1>(key));
}

int main() {
    initializeInverses();
    string ciphertext = "PFIMKZZIXWQIDFWJIMDJWPPEQAARWVWXIZITAXAPSXTWJMPKXTPFWUKPFNWFIXTIPKPPIUWMNSPIZWWRNKTNWQKSMWRKMPPIUWKRAPAZDWADRWTITKRAPAZPWBPKXTIZWWRRIOWITITXPTAKRAPAZPWBPMAXAYIUTAIXCKRAPPAUKOWMSJWIPMWXASCFNWQKSMWEASMKITIPMMSDDAMWTPANWKQASDRWFSXTJWTQFKJKQPWMYFIQFUKOWMMWXMWNWQKSMWEASDJANKNREXWWTKRAPAZQFKJKQPWJMIXAJTWJPANWKNRWPATWQJEDPIPWMDWIQKRREYIPFKXWYQIDFWJIPMDJANKNRENWPPWJPAFKVWYKEUAJWQFKJKQPWJMKXTUKOWIPWKMIWJAXYFAWVWJIMKPPWUDPIXCPATWQJEDPIPNWQKSMWIPMKYFARWXWYQIDFWJKXTIPQKXNWJWKRREQAXZSMIXCKPPFWMPKJPPAKPPWUDPPAZICSJWASPYFKPPAWVWXTAKXTYFWJWPAWVWXNWCIX";
    string decrypted = decryptAffine(ciphertext);
    cout << "Decrypted text: " << decrypted << endl;
    cout << "Key: " << get<0>(getKey(ciphertext)) << ", " << get<1>(getKey(ciphertext)) << endl;
    return 0;
}