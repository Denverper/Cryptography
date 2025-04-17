import os
import dotenv
import requests
from canvasapi import Canvas

base_url = "https://canvas.du.edu/"
dotenv.load_dotenv()
API_KEY = os.environ['CANVAS_API']
canvas = Canvas(base_url, API_KEY)
vigenere = 1447000
affine = 1446999

def getPosts(discussion_id: int, file_to_save:str = ""):
    if not file_to_save:
        file_to_save = f"../Assets/txts/{discussion_id}.txt"
        
    crypt = canvas.get_course(186585) ## get the cryptography course
    discussion = crypt.get_discussion_topic(discussion_id)
    entries = discussion.get_topic_entries()
    ciphers = []
    for entry in entries: 
        cipher = (str(entry).split(">")) ## discussion entries are given as HTML
        for i in range(len(cipher)):
            if cipher[i][0].isalpha():
                ciphers.append(cipher[i].split("<")[0])
                break

    filepath = os.path.join(os.path.dirname(__file__), file_to_save)
    with open(filepath, "w") as f:
        for cipher in ciphers:
            f.write(cipher + "\n")
        
getPosts(affine)