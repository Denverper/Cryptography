import math
import os
import sys
from typing import Counter
sys.path.append('./CeaserCryptanalysis')
import CeaserCryptanalysis
sys.path.append('./Vigenere')
import vig


def getVigKeyLength(text):
    """
    Find the probable length of the key using the first method learned in class (Kasiski):
    1. find the distance between pairs of matching triplets
    2. find the gcds of those distances and choose the one that leads to the length of the key most likely

    Args:
        text (string): the ciphertext to find the key length of

    Returns:
        int: the length of the key
    """
    text = text.lower() ## init
    tripMap = {}
    distances = []

    for i in range(len(text) - 2): #slide a window to find all triplets
        triplet = text[i:i+3] #triplet starting at i
        if triplet in tripMap:
            distances.append(i - tripMap[triplet]) 
        tripMap[triplet] = i

    if not distances:
        return 1 # if no triplet differences, assume 1

    gcd_freq = Counter() ## used to find most common length (gcd)

    # sliding window with window of 20 so O(n) time, any constant works, but should be big enough to allow for correct GCD found
    window = 20
    for i in range(len(distances)):
        for j in range(i + 1, min(i + window, len(distances))): ##check the next window numbers and find the gcd with i and j
            curr_gcd = math.gcd(distances[i], distances[j])
            if curr_gcd > 1: ## we know at this point the key is > 1 since we check for that before this function, so ignore coprimes
                gcd_freq[curr_gcd] += 1 
    return gcd_freq.most_common(1)[0][0] if gcd_freq else 1    

def getCeaserStrings(text, keyLength):
    ## get k strings where we assume that those strings are simply shift ciphers
    ceasers = [""]*keyLength
    for i in range(len(text)):
        ceasers[i%(keyLength)] += text[i]
    
    return ceasers

def getVigKey(text: str, verbose: bool=False):
    """
    finds the full key used to encrypt the plaintext message by finding the length and breaking it into substrings to find the key

    Args:
        text (str): the cipertext to find the key for

    Returns:
        string: the encryption key
    """
    text=text.lower()
    if abs(0.065 - CeaserCryptanalysis.getMIC(text, False)) < 0.005: ##probably already english, meaning shift cipher, use ceaser decryption
        key = CeaserCryptanalysis.getKey(text)
        if verbose:
            print("Looks like shift cipher, using single letter key:", key, "--> MIC: ")
        return key
    
    keyLen = getVigKeyLength(text)
    if verbose:
        print(f"Key length of {keyLen} found")
    ceaserStrings = getCeaserStrings(text, keyLen)
    key = ""
        
    i = 0
    if verbose:
        print("Keys for each substring and associated MICs:")
    for cstring in ceaserStrings:
        subKey = CeaserCryptanalysis.getKey(cstring)
        key += subKey
        if verbose:
            shifted_string = CeaserCryptanalysis._shift_text(cstring, (26 - ord(subKey)- ord("A"))%26)
            print(f"   - Cipher: {cstring.upper()} -- found key: {subKey} -- shifted substring: {shifted_string} -- MIC for key: {CeaserCryptanalysis.getMIC(shifted_string, True)}")
        i+=1

    return key
    
def decrypt_vigenere(ciphertext: str, verbose:bool=False):
    if verbose:
        print("Ciphertext:", ciphertext.upper())
    key = getVigKey(ciphertext, verbose)
    if verbose:
        print("Full Key:", key)
    plaintext = vig.decrypt_vigenere(ciphertext, key)
    if verbose:
        print("Found Plaintext:", plaintext)
        final_mic = CeaserCryptanalysis.getMIC(plaintext)
        print("Final MIC:",final_mic)
        if abs(final_mic - CeaserCryptanalysis.ENGLISH_MIC) > 0.006:
            print("MIC suggests decryption is probably incorrect")
        else:
            print("MIC suggests decryption is probably correct")
    else:
        print(plaintext)
    return plaintext
    
def main():
    
    cli_args = sys.argv[1:]
    ciphers = None
    verbose = False
    USAGE = """
    VigenereCryptanalysis.py:
        [ -v | -verbose ]: Optional. Enables verbose mode to display detailed decryption steps.
        [ -u | -h | -usage | -help ] output the usage
        <ciphertext>  : Required. The ciphertext to decrypt. Can be a string or a file path with one line containing the cipher.
        If a file is passed, each line will be treated as a seperate cipher
        If no args are provided, cipher must be given in the terminal and verbose will default to False.
    """
    
    i = 0
    if len(cli_args) >= 1: ## if command line args were used
        if cli_args[i] == "-u" or cli_args[i] == "-h" or cli_args[i] == "-help" or cli_args[i] == "-usage":
            print(USAGE)
            return
        if cli_args[i] == "-v" or cli_args[i] == "-verbose":
            verbose=True
            i+=1
        if len(cli_args) >= i+1:
            cipher = cli_args[i]
            if os.path.isfile(cipher):
                with open(cli_args[i], 'r') as file:
                    ciphers = file.read().strip().splitlines()
            else:
                ciphers = [cli_args[i]]
        
    if not ciphers:
        ciphers = [input("Please input cipher to decrypt: ")]
    
    for c in ciphers: ## decrypt every cipher
        decrypt_vigenere(c, verbose)
        print("\n")
    
if __name__ == "__main__":
    main()