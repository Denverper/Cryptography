import math


def jacobi(a, n):
    if n <= 0 or n % 2 == 0: ## added this as it seems neccessary, when n is even, n%8 will be even, meaning rule 4 will never work.
        raise ValueError("n must be a positive and odd")
    
    if a < 0:
        a = a % n
        
    if a == 0:
        return 0
    
    if a == 1 or n == 1:
        return 1
    
    if math.gcd(a, n) != 1: ## if they are not coprime, the Jacobi symbol is 0, and the program would run forever, so return 0
        return 0

    result = 1
    while a > 0:
        ## while a is even, we can reduce it with rule 4
        while a % 2 == 0:
            a //= 2
            if n % 8 in [3, 5]:
                result = -result
            
        ## now a is odd, we can use rule 5 (rule of quadratic reciprocity)
        a, n = n, a
        if a % 4 == 3 and n % 4 == 3:
            result = -result
        
        a %= n # reduce a mod n (Rule 1)

    return result if n == 1 else 0

def safe_prime(num):
    if num < 3 or num % 2 == 0:
        return False
    p = (num - 1) // 2
    return p > 1 and all(p % i != 0 for i in range(2, int(p**0.5) + 1))

def sofie_prime(num):
    if num < 3 or num % 2 == 0:
        return False
    p = (num * 2) + 1
    print(f"Checking if {p} is a prime number...")
    return p > 1 and all(p % i != 0 for i in range(2, int(p**0.5) + 1))


def is_generator(g, p):
    if not safe_prime(p):
        raise ValueError("Input must be a safe prime number.")
    
    s = (p - 1) // 2  
    if (g**1 % p) != 1 and (g**(2) % p) != 1 and (g**(s) % p) != 1:
        return True
    
    return False
