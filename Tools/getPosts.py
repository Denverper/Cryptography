import os
import dotenv
import requests
from canvasapi import Canvas

base_url = "https://canvas.du.edu/"
dotenv.load_dotenv()
API_KEY = os.environ['CANVAS_API']

canvas = Canvas(base_url, API_KEY)

crypt = canvas.get_course(186585)
discussion = crypt.get_discussion_topic(1447000)
entries = discussion.get_topic_entries()
ciphers = []
for entry in entries: 
    cipher = (str(entry).split(">"))
    for i in range(len(cipher)):
        if cipher[i][0].isalpha():
            ciphers.append(cipher[i].split("<")[0])
            break

filepath = os.path.join(os.path.dirname(__file__), "../Assets/txts/vig_ciphers.txt")
with open(filepath, "w") as f:
    for cipher in ciphers:
        f.write(cipher + "\n")
    
