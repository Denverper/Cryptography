def intsToStrs(ints: list[int]) -> str:
    ## This function takes a list of integers and converts them to a string
    ## by converting each integer to its corresponding letter in the alphabet
    ## The integers are assumed to be in the range 0-25, which corresponds to the letters a-z
    result = ""
    for i in ints:
        result += (chr(i%26 + ord("a"))) ## mod 26 to wrap around, add ord("a") to get the ascii value of the lowercase letter
    return result

def strsToInts(s: str) -> list[int]:
    ## This function takes a string and converts it to a list of integers
    ## by converting each letter to its corresponding index in the alphabet
    ## The letters are assumed to be in the range a-z, which corresponds to the integers 0-25
    result = []
    for letter in s:
        if letter.isalpha(): 
            result.append(ord(letter.lower()) - ord("a")) ## subtract ord("a") to get the 0-indexed index of the letter in the alphabet
    return result


def main():
    ints2 = strsToInts("Exploring classical and modern cipher systems")
    for int in ints2:
        print(int, end=" ") ## prints the list of integers representation of the string
    print() ## print a new line

    print(intsToStrs(strsToInts("Exploring classical and modern cipher systems"))) ## prints the string representation of the list of integers
    
if __name__ == "__main__":
    main()