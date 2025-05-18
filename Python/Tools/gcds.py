import copy

def gcd(a: int, b:int):
    return a if b==0 else gcd(b, a%b)

def mod_inv(m:int, n:int):    
    n, m = max(n, m), min(n, m) ## make sure correct order
    mod_val = n
    
    if gcd(n,m) != 1:
        return -1 ## no inv
    
    ## buffer for last two remainder values
    prevs = [None, None]
    while n%m != 0:
        ## remainder reexpression and substitution
        ## expressions are in the form [value, coefficent] -> value * coeffiecent with a list of expressions being seperated by +
        currRemaind = [[n if prevs[1] is None else copy.deepcopy(prevs[1]), 1], [m if prevs[0] is None else copy.deepcopy(prevs[0]), -(n//m)]]

        substituted = [] ## buffer to hold remainder with everything seperated and multiplied out
        for value in currRemaind:
            if type(value[0]) != int: ## first value is a list, resolve nested expression by multiplying it out by its coefficient
                for expression in value[0]:
                    expression[1] *= value[1]
                    substituted.append(expression)
            else: ## if it's a number, meaning its in the form of a single expression, add it to the longer substituted expression
                substituted.append(value)
        
        ## combine step, put everything into a map to combine coefficents
        seenMap = {}
        for expression in substituted:
            if expression[0] in seenMap:
                seenMap[expression[0]] += expression[1]
            else:
                seenMap[expression[0]] = expression[1]
        
        ## combine everything from the map
        combinedRemainder = []
        for key, val in seenMap.items():
            combinedRemainder.append([key, val]) 
    
        tmp = prevs[0]
        prevs = [combinedRemainder, tmp] ## push current remainder to the stack
        
        m, n =  n%m, m ##next n and m
    
    if prevs[0] is None:
        return 1 ## no coefficents, so mult by 1, think about case of m = 1 and n = 2
    
    ## find the coefficient of the smaller value between n and m
    if prevs[0][0][0] < prevs[0][1][0]:
        return prevs[0][0][1] % mod_val
    else:
        return prevs[0][1][1] % mod_val
    
def get_extended_coeffs(m:int, n:int):
    n, m = max(n, m), min(n, m) ## make sure correct order
        
    if gcd(n,m) != 1:
        return -1 ## no inv
    
    ## buffer for last two remainder values
    prevs = [None, None]
    while n%m != 0:
        ## remainder reexpression and substitution
        ## expressions are in the form [value, coefficent] -> value * coeffiecent with a list of expressions being seperated by +
        currRemaind = [[n if prevs[1] is None else copy.deepcopy(prevs[1]), 1], [m if prevs[0] is None else copy.deepcopy(prevs[0]), -(n//m)]]

        substituted = [] ## buffer to hold remainder with everything seperated and multiplied out
        for value in currRemaind:
            if type(value[0]) != int: ## first value is a list, resolve nested expression by multiplying it out by its coefficient
                for expression in value[0]:
                    expression[1] *= value[1]
                    substituted.append(expression)
            else: ## if it's a number, meaning its in the form of a single expression, add it to the longer substituted expression
                substituted.append(value)
        
        ## combine step, put everything into a map to combine coefficents
        seenMap = {}
        for expression in substituted:
            if expression[0] in seenMap:
                seenMap[expression[0]] += expression[1]
            else:
                seenMap[expression[0]] = expression[1]
        
        ## combine everything from the map
        combinedRemainder = []
        for key, val in seenMap.items():
            combinedRemainder.append([key, val]) 
    
        tmp = prevs[0]
        prevs = [combinedRemainder, tmp] ## push current remainder to the stack
        
        m, n =  n%m, m ##next n and m
    
    if prevs[0] is None:
        return 1 ## no coefficents, so mult by 1, think about case of m = 1 and n = 2
    
    ## find the coefficient of the smaller value between n and m
    return prevs[0]
