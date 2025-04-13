import math

inverses = {
    1: 1,
    3: 9,
    5: 21,
    7: 15,
    9: 3,
    11: 19,
    15: 7,
    17: 23,
    19: 11,
    21: 5,
    23: 17,
    25: 25,
}

def encrypt_affine(plaintext: str, key):
    a,b = key
    if math.gcd(a,26) != 1:
        print("Invalid key")
        return ""
    ciphertext= ""
    plaintext = plaintext.lower()
    for c in plaintext:
        if c.isalpha():
            ciphertext += chr(((ord(c) - ord("a"))*a + b)%26 + ord('A'))
    
    return ciphertext

def decrypt_affine(ciphertext: str, key):
    a,b = key
    inv_a = inverses[a]
    plaintext= ""
    
    ciphertext = ciphertext.lower()
    for c in ciphertext:
        if c.isalpha():
            plaintext += chr(((ord(c) - ord("a") - b)*inv_a)%26 + ord('a'))
    
    return plaintext