def encrypt_vigenere(text, key):
    result = ""
    text = text.lower()
    for i, c in enumerate(text):
        if c.isalpha():
            shift = ord(key[i % len(key)]) - ord('a')
            result += chr((ord(c) - ord('a') + shift) % 26 + ord('a')).upper()
    return result
            
            
def decrypt_vigenere(text, key):
    result = ""
    text = text.upper()
    key = key.upper()
    for i, c in enumerate(text):
        if c.isalpha():
            shift = ord(key[i % len(key)]) - ord('A')
            result += chr((ord(c) - ord('A') - shift) % 26 + ord('A')).lower()
    return result
    
key = "tryst"
print(encrypt_vigenere("meetmeatmidnight", key))
print(decrypt_vigenere(encrypt_vigenere("meetmeatmidnight", key), key))
