import sys
import math
import binascii

# Auto-generated code below aims at helping you parse
# the standard input according to the problem statement.

message_1 = input('391813c092a2d5ac9acb705dfe41be3df08de67d1145cbcc3f')
message_2 = input()
message_3 = input()

def xor_string(b1, b2): 
    result = bytearray()
    for b1, b2 in zip(b1, b2):
        result.append(b1 ^ b2)
    return result

msgxor1 = binascii.unhexlify(message_1)
msgxor2 = binascii.unhexlify(message_2) 
msgxor3 = binascii.unhexlify(message_3) 

alice_key = xor_string(msgxor2, msgxor3)
intercepted_string = xor_string(msgxor1, alice_key)
print(intercepted_string.decode())