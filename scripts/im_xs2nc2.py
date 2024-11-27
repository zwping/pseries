#!/usr/bin/env python3

import pandas as pd
import pymongo, re

exit()
# fpath = '~/Downloads/dd/学生1.4亿_1.csv' # 70999873
fpath = '~/Downloads/dd/学生1.4亿_2_1.csv' # 70859367

myclient = pymongo.MongoClient("mongodb://admin:222222@192.168.0.149:27017/")
mydb = myclient["xs_20230725"]
mycol = mydb["xs"]

def idno(it):
    match = re.search(r'.?(\d{6})(\d{4})(\d{2})(\d{2})(\d{3})([0-9Xx])', it['name'])
    its = list(it.items())
    its.insert(2, ('year', match.group(2) if match else ''))
    return dict(its)

i = 1
chunk_size=10000
for chunk in pd.read_csv(fpath, dtype=str, chunksize=chunk_size):
    data = chunk.fillna('') # nan 转为 ''
    # print(data)
    data = data.to_dict(orient="records") # 转为dict
    data = list(map(idno, filter(lambda it: it['tel'] and re.match(r'^1[3-9]\d{9}$', str(it['tel'])), data)))
    if not data:
        continue
    # print(data)
    mycol.insert_many(data)
    # print(list(map(lambda it: it['display_name'] + it['tel'] + ' ' + it['year'] + ' ' + it['name'] , data)))

    print(str(i*chunk_size)  + ' ' + '70859367')
    i = i+1
    # if i > 3:
    #     break
