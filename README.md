# Azula - SQL to MongoDB command line commands converter

**At the input, the converter receives an SQL query and issues the corresponding MongoDB query.**

*Sample input: SELECT * FROM sales LIMIT 10*

*Sample output: db.sales.find({}).limit(10)*


# Supported query types

**SELECT**

Supported options :
```sh
- WHERE. supported complex conditions like ((a > 5) AND ((b < 10) OR (c = 45) OR (d <> 16)) AND (e < 19))
- SKIP
- LIMIT
  ```


    

