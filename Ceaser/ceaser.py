def intsToStrs(ints: list[int]) -> str:
    result = ""
    for i in ints:
        result += (chr(i%26 + ord("a"))) ## mod 26 to wrap around, add ord("a") to get the ascii value of the lowercase letter
    return result

def strsToInts(s: str) -> list[int]:
    result = []
    for letter in s:
        if letter.isalpha(): 
            result.append(ord(letter.lower()) - ord("a")) ## subtract ord("a") to get the 0-indexed index of the letter in the alphabet
    return result

# ints = [22, 4, 11, 2, 14, 12, 4, 19, 14, 12, 0, 19, 7, 4, 12, 0, 19, 8, 2, 0, 11, 2, 17, 24, 15, 19, 14, 6, 17, 0, 15, 7, 24]
# print(intsToStrs(ints)) ## prints the string representation of the list of integers
# print(strsToInts(intsToStrs(ints))) ## prints the list of integers representation of the string

ints2 = strsToInts("Exploring classical and modern cipher systems")
for int in ints2:
    print(int, end=" ") ## prints the list of integers representation of the string
print() ## print a new line

print(intsToStrs(strsToInts("Exploring classical and modern cipher systems"))) ## prints the string representation of the list of integerss