using namespace std;
#include <iostream>

string intsToString(const vector<int>& ints) {
    // Convert a vector of integers to a string
    // Each integer is converted to a character by taking the modulo 26
    // and adding 'a' to get the corresponding lowercase letter
    string result;
    for (int i=0; i < ints.size(); i++) {
        result += char((ints[i]%26) + 'a');
    }
    return result;
}

vector<int> strToInts(const string& s) {
    // Convert a string to a vector of integers
    // Each character is converted to an integer by subtracting 'a'
    vector<int> result;
    for (char c : s) {
        if (isalpha(c)) {
            result.push_back(c - 'a');
        }
    }
    return result;
}

int main() {
    // Example usage
    string s = "don't do it by hand";
    vector<int> ints = strToInts(s);
    cout << "String to Ints: ";
    for (int i : ints) {
        cout << i << " ";
    }
    cout << endl;

    string str = intsToString(ints);
    cout << "Ints to String: " << str << endl;

    return 0;
}