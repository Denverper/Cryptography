import gcds

def getOrd(z, n):
    num = 0
    pow = 1
    while (z**pow)%n != 1:
        num += 1
        pow += 1
    return num + 1

def getZn(n):
    elems = []
    for i in range(1,n):
        if gcds.gcd(n, i) == 1:
            elems.append(i)           
    return elems
        
def printOrdZn(n):
    ords = []
    elems =  getZn(n)
    for val in elems:
        ords.append(getOrd(val, n))
    # print(elems)
    # print(ords)
    print("n:", n)
    print("Phi:", len(elems))
    print(elems)
    print("Num Generators:", ords.count(len(elems)), "\n")
    
printOrdZn(13)
