import math
import os
import sys
from typing import Counter

## for imports of helpers from other cipher code
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '../CaesarCryptanalysis')))
import caeser_cryptanalysts # type: ignore
import vigenere

def getVigKeyLength(text):
    """
    Find the probable length of the key using the first method learned in class (Kasiski):
    1. find the distance between pairs of matching triplets
    2. find the gcds of those distances and choose the one that leads to the length of the key most likely

    Args:
        text (string): the ciphertext to find the key length of

    Returns:
        list[int] (length 3): the top three most probably key lengths to try, for due dilligence 
    """
    text = text.lower() ## init buffers
    tripMap = {}
    distances = []

    for i in range(len(text) - 2): #slide a window to find all triplets
        triplet = text[i:i+3] #triplet starting at i
        if triplet in tripMap:
            distances.append(i - tripMap[triplet]) 
        tripMap[triplet] = i

    if not distances:
        return [1] # if no triplet differences, assume 1

    gcd_freq = Counter() ## used to find most common length (gcd)
    # sliding window with window of 20 so O(n) time, any constant works, but should be big enough to allow for correct GCD found
    window = 20
    for i in range(len(distances)):
        for j in range(i + 1, min(i + window, len(distances))): ##check the next window numbers and find the gcd with i and j
            curr_gcd = math.gcd(distances[i], distances[j])
            if curr_gcd > 1: ## we know at this point the key is > 1 since we check for that before this function, so ignore coprimes
                gcd_freq[curr_gcd] += 1 

    if gcd_freq: ## return top 3 GCDs to increase odds of success, still O(n) since checking a constant number of keys
        top3Lens = [x[0] for x in gcd_freq.most_common(3)]
        return top3Lens
    else:
        return [1]    

def getCeaserStrings(text, keyLength):
    """
    Breaks the text into keyLength substrings where each substring contains only letters a key of length keyLength would be responsible for decrypting

    Args:
        text (str): the text to breakup
        keyLength (int): the length of the key to use  

    Returns:
        list[str]: the list of ceaser strings
    """
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
    if abs(0.065 - caeser_cryptanalysts.getMIC(text, False)) <= 0.005: ##probably already english, meaning shift cipher, use ceaser decryption
        key = caeser_cryptanalysts.getKey(text)
        if verbose:
            print("Looks like shift cipher, using single letter key:", key, "--> MIC: ")
        return key
    
    possibleKeyLens = getVigKeyLength(text)
    if verbose:
        print(f"Key lengths of {possibleKeyLens} found (Top 3)")
        
    key = ""
    for keyLen in possibleKeyLens: ## try the top three key lengths
        key = ""
        ceaserStrings = getCeaserStrings(text, keyLen)
            
        i = 0
        if verbose:
            print(f"Trying key of length: {keyLen}")
            print("Keys for each substring and associated MICs:")
            
        mic_avg = 0
        best_so_far = (10000, "")
        
        for cstring in ceaserStrings: ## iterate over the found substrings and find their respective single char shifts, joining them to make a key
            subKey = caeser_cryptanalysts.getKey(cstring)
            key += subKey
            shifted_string = caeser_cryptanalysts._shift_text(cstring, (26 - ord(subKey)- ord("A"))%26)
            curr_mic = caeser_cryptanalysts.getMIC(shifted_string, True)

            if verbose:
                print(f"   - Cipher: {cstring.upper()} -- found key: {subKey} -- shifted substring: {shifted_string} -- MIC for key: {curr_mic:4f}")
                
            i+=1
            mic_avg += curr_mic
        
        distance = abs((mic_avg/len(ceaserStrings)) - caeser_cryptanalysts.ENGLISH_MIC) ## how good is this key
        if distance < 0.005: ## looks correct, so return the key
            return key
        elif distance < best_so_far[0]: ## not quite there yet, so look at next key
            best_so_far = (distance, key)
        
    return best_so_far[1]
    
def decrypt_vigenere(ciphertext: str, verbose:bool=False):
    """
    Puts the oher functions together to decrypt a given ciphertext with text alone.

    Args:
        ciphertext (str): the string to decrypt
        verbose (bool, optional): whether or not to print the details during runtime. Defaults to False.

    Returns:
        str: the plaintext found
    """
    
    if verbose:
        print("Ciphertext:", ciphertext.upper())
        
    key = getVigKey(ciphertext, verbose) 
    
    if verbose:
        print("Full Key:", key)
        
    plaintext = vigenere.decrypt_vigenere(ciphertext, key)
    if verbose:
        print("Found Plaintext:", plaintext)
        final_mic = caeser_cryptanalysts.getMIC(plaintext)
        print("Final MIC:",final_mic)
        if abs(final_mic - caeser_cryptanalysts.ENGLISH_MIC) > 0.006:
            print("MIC suggests decryption is probably incorrect")
        else:
            print("MIC suggests decryption is probably correct")
    else:
        ## not verbose, but still write to standard output
        print(plaintext)
    return plaintext
    
def main():
    """
    The main function, made CLI functionality.
    
    CLI format is outlined in USAGE
    
    if a filename is passed into the function, if reads every line of the file as a differnt cipher 
    and outputs each to standard output as well writting each to Assets/txts/vig_ciphers.txt
    flag options: -v or -verbose | show details of computation
                  -u or -usage or -h or -help | print usage
    if no cipher is provided, you can input it in the terminal
    """
    
    cli_args = sys.argv[1:]
    ciphers = None
    verbose = False
    is_file = False
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
                is_file = True
                with open(cli_args[i], 'r') as file:
                    ciphers = file.read().strip().splitlines()
            else:
                ciphers = [cli_args[i]]
        
    if not ciphers: 
        ciphers = [input("Please input cipher to decrypt: ")]
    
    decrypted = []
    for c in ciphers: ## decrypt every cipher
        decrypted.append(decrypt_vigenere(c, verbose))
        
    if is_file: # if a file, then write output to a file
        with open(os.path.join(os.path.dirname(__file__), "../../Assets/txts/decrypted.txt"), "w+") as f:
            for plaintext in decrypted:
                f.write(plaintext + "\n")
                   
if __name__ == "__main__":
    main()