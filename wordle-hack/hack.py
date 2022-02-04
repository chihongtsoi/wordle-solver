from datetime import date, datetime

file = open("wordle-hidden-pool.txt", "r")
words = file.readlines()
start_ts = int(datetime(2021, 6, 19).timestamp())
today = date.today()
today_ts = int(datetime(today.year, today.month, today.day).timestamp())
diff = (today_ts - start_ts)
day_diff = round(diff / 86400.0)
print("Date: ", today)
print("Answer: ", words[day_diff % len(words)])