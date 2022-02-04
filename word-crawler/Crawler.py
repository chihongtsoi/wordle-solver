import random
from time import sleep
import requests

OUTPUT_PATH = "data/"

# page, length, letter, type, nonce
url = "https://www.dictionary.com/e/crb-ajax/cached.php?page={}&wordLength=5&letter={}&action=get_wf_widget_page&pageType=4&nonce={}"

# A-Z
for i in range(ord("A"), ord("Z")+1):
    page = 1
    success = True
    letter = chr(i)
    f = open(OUTPUT_PATH + letter + ".txt", "a")
    while success:
        try:
            nonce = '%010x' % random.randrange(16 ** 10)
            r = requests.get(url.format(str(page), letter, nonce))
            res = r.json()
            success = res["success"]
            if success:
                word_list = res["data"]["words"]
                f.write("\n".join(word_list))
                f.write("\n")
                page += 1
            sleep(1)
        except:
            print(letter, str(page))
