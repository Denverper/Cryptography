# Cryptography
Public work for COMP 3705. Each cipher and module written in C++, Java, and Python.

## Repository Structure
```
cryptography/
├── Assets
    ├── txts
        ├── files containing plaintext and ciphertexts from canvas course discussions

├── C++
    ├── Affine
        ├── Affine.cpp
        ├── Affine.h
        ├── AffineCryptanalysis.cpp
    ├── Caesar
        ├── CaesarCryptanalysis.h
        ├── CaesarCryptanalysis.cpp
    ├── Vigenere
        ├── Vigenere.cpp
        ├── Vigenere.h
        ├── VigenereCryptanalysis.cpp
    ├── Tools
        ├── numsToStrs.cpp

├── javaCryptography
    ├── src
        ├── affine
            ├── Affine.java
            ├── AffineCryptanalysis.java
        ├── caesar
            ├── CaesarCryptanalysis.java
        ├── vigenere
            ├── Vigenere.java
            ├── VigenereCryptanalysis.java
        ├── tools
            ├── numsToStrs.java

├── Python
    ├── Affine
        ├── affine.py
        ├── affine_cryptanalysis.py
    ├── Caesar
        ├── caesar_cryptanalysis.py
    ├── Vigenere
        ├── vigenere.py
        ├── vigenere_cryptanalysis.py
    ├── Tools
        ├── numsToStrs.py
        ├── gcds.py

├── Tools
    ├── getPosts.py
    ├── multtables.py
README.md
.gitignore
```