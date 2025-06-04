def encrypt_vigenere(text, key):
    ## encrypt the text using the Vigenere cipher
    ## encrypt text[i] with key[i % len(key)], looping the key if needed
    ## use key[i % len(key)] the same as in ceaser
    result = ""
    text = text.lower()
    key = key.lower()
    for i, c in enumerate(text):
        if c.isalpha():
            shift = ord(key[i % len(key)]) - ord('a')
            result += chr((ord(c) - ord('a') + shift) % 26 + ord('a')).upper()
    return result
            
            
def decrypt_vigenere(text, key):
    ## decrypt the text using the Vigenere cipher
    ## decrypt text[i] with key[i % len(key)], looping the key if needed
    ## use key[i % len(key)] the same as in ceaser
    ## use the same logic as encrypt but subtract the shift
    result = ""
    text = text.upper()
    key = key.upper()
    for i, c in enumerate(text):
        if c.isalpha():
            shift = ord(key[i % len(key)]) - ord('A')
            result += chr((ord(c) - ord('A') - shift) % 26 + ord('A')).lower()
    return result
    