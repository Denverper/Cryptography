# RSA Project (Part 2)

## Overview
I implemented 2048 bit RSA encryption, decryption, and key generation in Java. 

## How to Run

The code can be run in many different ways by changing the below variables in the `RSA.java` file. The first group of variables are the file paths for the key files to write to and read from, the second group is for super secret keys that will never be written to. The third group of variables controls whether to generate new keys and settings for the key generation process, and the last group controls whether to encrypt or decrypt messages or both.

### Format of Input Files:
- `plainText.txt`: Contains the plaintext to be encrypted, one long ASCII string on one line.
- `publicKeys.txt`: Contains the public keys. `n` is on the first line, and `e` is on the second line. `n` is the mod and `e` is the public exponent.
- `privateKeys.txt`: Contains the private exponent `d` on one line.
- `encryptedText.txt`: Contains the encrypted text, each line is a separate encrypted block to be independently decrypted with the private key.
- `supersecretmode`: Set to `true` to use the super secret keys from `supersecretkeys/publicKeys.txt` and/or `supersecretkeys/privateKeys.txt`, or `false` to use the regular keys from `publicKeys.txt` and `privateKeys.txt`.
- `supersecretkeys/publicKeys.txt`: Contains the super secret public keys, formatted like `publicKeys.txt`.
- `supersecretkeys/privateKeys.txt`: Contains the super secret private keys, formatted like `privateKeys.txt`.

### Variables influencing key generation:

For key generation, you can adjust the following variables:
- `generateKeys`: Set to `true` to generate new keys, or `false` to use existing keys in the files above. If we generate new keys and return, stopping the program
- `REPS_A`: The number of values of `a` to test for primality in the Solvay-Strassen test. Higher values increase the certainty that the number is prime, the certainty of which will be told if verbose is set to true.
- `verbose`: Set to `true` to print out details of the key generation process, such as the number of primes tested, the ceartinty of a prime, or `false` to keep it quiet.
- `writeToFiles`: Set to `true` to write the generated keys to files in the `inputFiles` directory, or `false` to do nothing.

### Variables influencing encryption/decryption:
- `encrypt`: Set to `true` to encrypt the plaintext from `plainText.txt` using the public key from `publicKeys.txt`.
- `decrypt`: Set to `true` to decrypt the encrypted text from `encryptedText.txt` using the private key from `privateKeys.txt`.

Code containing the blocks below show all the variables that can be set to control the program's behavior, they exist in the main function of the `RSA.java` file. 

```java
String projectRoot = System.getProperty("user.dir");
String plainTextFile = projectRoot + "/inputFiles/plainText.txt";
String publicKeyFile = projectRoot + "/inputFiles/publicKeys.txt";
String privateKeyFile = projectRoot + "/inputFiles/privateKeys.txt";
String encryptedTextFile = projectRoot + "/inputFiles/encryptedText.txt";

// if you have personal keys you want to use, set the supersecretmode to true and put them in a supersecretkeys directory
String superSecretPublicKeys = projectRoot + "/supersecretkeys/publicKeys.txt";
String superSecretPrivateKeys = projectRoot + "/supersecretkeys/privateKeys.txt";
boolean supersecretmode = false; 

boolean generateKeys = false; // set to true to generate new keys, false to use existing keys
final int REPS_A = 55; // change this to change the number of values of a we test in SS. higher number means more certain prime.
boolean verbose = true; // set to true to print out key generation details, such as amount of primes tested, false to keep it quiet
boolean writeToFiles = true; // set to true to write the keys to files (in the inputFiles directory), false to just print them out

boolean encrypt = true; // set to true to encrypt the message
boolean decrypt = true; // set to true to decrypt the message
```