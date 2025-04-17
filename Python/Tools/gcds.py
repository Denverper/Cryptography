import copy

def gcd(a: int, b:int):
    return a if b==0 else gcd(b, a%b)

def extended_euclidean(a:int, n:int):
    a,n = max(a,n), min(a,n)
    prevs = [None, None]
    while a%n != 0:
        ## remainder reexpression
        currRemaind = [[a if prevs[1] is None else copy.deepcopy(prevs[1]), 1], [n if prevs[0] is None else copy.deepcopy(prevs[0]), -(a//n)]]

        ## substitution
        substituted = []
    
        for thing in currRemaind:
            if type(thing[0]) != int:
                for subthing in thing[0]:
                    subthing[1] *= thing[1]
                    substituted.append(subthing)
            else:
                substituted.append(thing)
        
        ## combine
        seenMap = {}
        for thing in substituted:
            if thing[0] in seenMap:
                seenMap[thing[0]] += thing[1]
            else:
                seenMap[thing[0]] = thing[1]
        
        combinedRemainder = []
        for key, val in seenMap.items():
            combinedRemainder.append([key, val]) 
    
        tmp = prevs[0]

        prevs = [combinedRemainder, tmp]
        
        a, n =  n, a%n
    
    ## now prev
    print(prevs)
    if prevs[0][0][0] < prevs[0][1][0]:
        return abs(prevs[0][0][1])
    else:
        return abs(prevs[0][1][1])

print(extended_euclidean(26, 25))

