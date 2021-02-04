from hashlib import sha256

h = sha256()
h.update(b'dontpwnme4')
hash = h.hexdigest()
print(hash)