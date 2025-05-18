import gcds
import math

def crt():
    print(
"""
Provide variables in the form of:
    x ≡ a (mod m1)
    x ≡ b (mod m2)
""")
    a = int(input("a: ")) ## assume ints are passed in
    b = int(input("b: "))
    m1 = int(input("m1: "))
    m2 = int(input("m2: "))
    while math.gcd(m1, m2) != 1: ## check if m1 and m2 are coprime, if not, reprompt
        print("m1 and m2 must be coprime")
        m1 = int(input("m1: "))
        m2 = int(input("m2: "))
        
    final_express = gcds.get_extended_coeffs(m1, m2) ## function that returns the extended gcd of m1 and m2 in the form of [[m1, m1_coef], [m2, m2_coef]]
    if m1 < m2: ## since coefficients are returned in sorted order, we need to check which is smaller
        m1_coef = final_express[0][1] ## get the coefficient from the extended gcd 
        m2_coef = final_express[1][1]
    else: 
        m1_coef = final_express[1][1]
        m2_coef = final_express[0][1]
    return (a*m2*m2_coef + b*m1*m1_coef) % (m1*m2)
    
print(crt())

def crt_brute_force():
    print(
"""
Provide variables in the form of:
    x ≡ a (mod m1)
    x ≡ b (mod m2)
""")
    a = int(input("a: ")) ## assume ints are passed in
    b = int(input("b: "))
    m1 = int(input("m1: "))
    m2 = int(input("m2: "))
    while math.gcd(m1, m2) != 1:
        print("m1 and m2 must be coprime")
        m1 = input("m1: ")
        m2 = input("m2: ")
    m1, m2 = min(m1,m2), max(m1,m2)
    for i in range(0, m1*m2):
        if (i % m1 == a) and (i % m2 == b):
            return i
    return -1 ## no solution

print(crt())