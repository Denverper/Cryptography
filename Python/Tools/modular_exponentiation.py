import gcds

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

def findSmallestWittness(n):
    for i in range(2, n):
        if gcds.gcd(n, i) == 1 and modular_exponentiation(i, n-1, n) != 1:
            return i
    
def findSmallestLiar(n):
    for i in range(2, n):
        if gcds.gcd(n, i) == 1 and modular_exponentiation(i, n-1, n) == 1:
            return i

def findNumLiars(n):
    count = 0
    for i in range(2, n):
        if gcds.gcd(n, i) == 1 and modular_exponentiation(i, n-1, n) == 1:
            count += 1
    return count
def getPhi(n):
    phi = 0
    for i in range(1, n):
        if gcds.gcd(n, i) == 1:
            phi += 1
    return phi

print(modular_exponentiation(138,40, 150))