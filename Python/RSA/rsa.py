def modular_exponentiation(b, e, m):
    if m == 1:
        return 0  # Any number mod 1 is 0, base case
    
    result = 1
    b = b % m # reduce base mod m
    while e > 0:
        if (e % 2) == 1:
            result = (result * b) % m
        e = e >> 1  # bitwise right shift to look at the next bit
        b = (b * b) % m  # square the base mod m
    return result

def encrypt_RSA(m, e, n):
    return modular_exponentiation.mod_exp(m, e, n)

def decrypt_RSA(c, d, n):
    return modular_exponentiation.mod_exp(c, d, n)

def main():
    file = input("Enter the file name containing message to encrypt/decrp: ")
    key = input("Enter the key (public or private depending on passed file): ")
    with open(file, 'r') as f:
        data = f.read()
    print(data)

if __name__ == "__main__":
    main()