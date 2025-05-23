import sys
sys.path.append('/Users/denverpersinger/COMP3705/Python/Caesar')
import caesar_cryptanalysis # type: ignore
from affine import inverses, decrypt_affine, encrypt_affine


def get_key_brute_force(text):
    best = ((0,0), 10000)
    for inv in inverses.keys():
        for i in range(26):
            curr = decrypt_affine(text, (inv, i))
            dist = abs(caesar_cryptanalysis.ENGLISH_MIC - caesar_cryptanalysis.getMIC(curr, True))
            if dist < best[1]:
                best = ((inv, i), dist)
    return best[0]

def decrypt_affine_ciphertext_only(ciphertext: str):
    key = get_key_brute_force(ciphertext)
    print(key)
    return decrypt_affine(ciphertext, key)
