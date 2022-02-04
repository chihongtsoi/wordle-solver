const fs = require('fs')

try {
    const data = fs.readFileSync('wordle-hidden-pool.txt', 'utf8')
    const words = data.split("\n")
    const start = new Date(2021, 5, 19, 0, 0, 0, 0);
    const start_ts = start.setHours(0, 0, 0, 0);
    const today = new Date();
    const today_ts = today.setHours(0, 0, 0, 0);
    const day_diff = Math.round((today_ts - start_ts) / 864e5)
    console.log(`Date: ${today.toDateString()}`)
    console.log(`Answer: ${words[day_diff%words.length]}`)
} catch (err) {
    console.error(err)
}