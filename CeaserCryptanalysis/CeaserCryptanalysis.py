english_freq = [
    0.082,  # a
    0.015,  # b
    0.028,  # c
    0.043,  # d
    0.127,  # e
    0.022,  # f
    0.020,  # g
    0.061,  # h
    0.070,  # i
    0.002,  # j
    0.008,  # k
    0.040,  # l
    0.024,  # m
    0.067,  # n
    0.075,  # o
    0.019,  # p
    0.001,  # q
    0.060,  # r
    0.063,  # s
    0.091,  # t
    0.028,  # u
    0.010,  # v
    0.023,  # w
    0.001,  # x
    0.020,  # y
    0.001,  # z
]

ENGLISH_MIC = 0.065

def getIC(cipertext: str) -> float:
    """
    This function takes a ciphertext and returns the index of coincidence (IC).
    """
    # Initialize the frequency count for each letter
    freq_count = [0] * 26

    # Count the frequency of each letter in the ciphertext
    for char in cipertext:
        if char.isalpha():
            freq_count[ord(char.lower()) - ord('a')] += 1

    # get the IC
    ic = 0.0
    total_chars = sum(freq_count) ##total number of valid characters in the cipertext
    for count in freq_count:
        if count > 0:
            ic += (count / total_chars) ** 2

    return ic

def getMIC(cipertext: str) -> float:
    """
    This function takes a ciphertext and returns the mutual index of coincidence (MIC) between the text and English.
    """
    # Initialize the frequency count for each letter
    freq_count = [0] * 26

    # Count the frequency of each letter in the ciphertext
    for char in cipertext:
        if char.isalpha():
            freq_count[ord(char.lower()) - ord('a')] += 1

    # get the MIC
    mic = 0.0
    total_chars = sum(freq_count) ##total number of valid characters in the cipertext
    for i, count in enumerate(freq_count):
        if count > 0:
            mic += (count / total_chars) * english_freq[i] ## multiply the frequency of the letter in the cipertext with the frequency of the letter in English

    return mic

def _shift_text(text: str, shift: int) -> str:
    """
    This function takes a text and a shift value and returns the shifted text.
    """
    
    shifted_text = ""
    for char in text:
        if char.isalpha():
            shifted_char = chr((ord(char.lower()) - ord('a') + shift) % 26 + ord('a'))
            shifted_text += shifted_char
    
    return shifted_text

def getKey(cipertext: str) -> int:
    """
    This function takes a ciphertext and returns the key used in the Caesar cipher.
    """
    BEST_KEY = (0, float("inf")) 
    all_shifts = [_shift_text(cipertext, i) for i in range(26)] ## get all the shifts of the cipertext
    for i, text in enumerate(all_shifts):
        mic = getMIC(text)
        distance = abs(ENGLISH_MIC - mic) ## get the distance from the english MIC, how accurate the shift is to being English
        if distance < BEST_KEY[1]:
            BEST_KEY = (chr(i+ord("A")), distance)

    return BEST_KEY[0] ## return the best key found

def decryptCaesar(cipertext: str) -> str:
    """
    This function takes a ciphertext and a key and returns the decrypted text.
    """
    key = getKey(cipertext)
    decryptedText = _shift_text(cipertext, ord(key) - ord("A")) ## decrypt the cipertext using the key
    return decryptedText

def encryptCaesar(plaintext: str, key: int) -> str:
    """
    This function takes a plaintext and a key and returns the encrypted text.
    """
    encryptedText = _shift_text(plaintext, key).upper() ## encrypt the plaintext using the key
    return encryptedText

