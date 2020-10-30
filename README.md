# Azula - SQL to MongoDB converter

**At the input, the converter receives an SQL query and issues the corresponding MongoDB query.

*Sample input: SELECT * FROM sales LIMIT 10 *
*Sample output: db.sales.find({}).limit(10) *
