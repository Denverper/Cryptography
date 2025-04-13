
from math import gcd


def print_mult_table(n):
    print(f"Multiplication Table for {n} (mod n):")
    print("   | ", end="")
    for i in range(n):
        if i == n-1:
            print(f"{i:2} ")
        else:
            print(f"{i:2}", end=" | ")
    
    print("-" * (n * 5 + 2))
    
    for i in range(n):
        print(f"{i:2} | ", end="")
        for j in range(n):
            if j == n-1:
                print(f"{((i*j)%n):2} ")
            else:
                print(f"{((i*j)%n):2}", end=" | ")

def numberofinv(n):
    count = 0
    for i in range(n):
        if gcd(i,n) == 1:
            count += 1
    return count

print(print_mult_table(14))