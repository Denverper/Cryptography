using namespace std;
#include <iostream>

string intsToString(const vector<int>& ints) {
    string result;
    for (int i=0; i < ints.size(); i++) {
        result += char((ints[i]%26) + 'a');
    }
    return result;
}

vector<int> strToInts(const string& s) {
    vector<int> result;
    for (char c : s) {
        if (isalpha(c)) {
            result.push_back(c - 'a');
        }
    }
    return result;
}

int main() {
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