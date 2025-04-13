def gcd(a: int, b:int):
    return a if b==0 else gcd(b, a%b)

# def extended_euclidean(a:int, n:int):
#     numMap = {}
#     stopVal = gcd(a,n)
#     while a%n != stopVal:
        