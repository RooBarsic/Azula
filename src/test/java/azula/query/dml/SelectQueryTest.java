package azula.query.dml;

import azula.condition.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Класс для тестирования Select запросов
 *
 * Author : Farrukh Karimov
 * Modification Date : 04.04.2020
 */
public class SelectQueryTest {

    @Test
    public void canConvertToSqlVue() {
        final String sqlSelectQueryVue = "SELECT name, age, price FROM book WHERE ((age > 10) OR (price < 650)) SKIP 10 LIMIT 5";
        final SelectQueryBuilder selectQueryBuilder = new SelectQueryBuilder();
        final SelectQuery selectQuery = (SelectQuery) selectQueryBuilder
                .newQuery()
                .column("name", "age", "price")
                .from("book")
                .where(new ComplexCondition(
                        BooleanOperator.OR,
                        new SimpleCondition("age", RelationalOperator.GRATER_THAN, "10"),
                        new SimpleCondition("price", RelationalOperator.LESS_THAN, "650")
                ))
                .limit(5)
                .skip(10)
                .perform();
        assertEquals(sqlSelectQueryVue, selectQuery.toSqlVue());


        final String sqlSelectQueryVue2 = "SELECT * FROM book";
        final SelectQuery selectQuery2 = (SelectQuery) selectQueryBuilder
                .newQuery()
                .column("*")
                .from("book")
                .perform();
        assertEquals(sqlSelectQueryVue2, selectQuery2.toSqlVue());
    }

    @Test
    public void canConvertToMongoVue() {
        final String sqlSelectQueryVue = "SELECT name, age, price FROM book WHERE ((age > 10) OR (price < 650)) SKIP 10 LIMIT 5";
        final String mongoSelectQueryVue = "db.book.find({$or: [{age: {$gt: 10}}, {price: {$lt: 650}}]}, {name: 1, age: 1, price: 1, _id: 0}).skip(10).limit(5)";
        final SelectQueryBuilder selectQueryBuilder = new SelectQueryBuilder();
        final SelectQuery selectQuery = (SelectQuery) selectQueryBuilder
                .newQuery()
                .column("name", "age", "price")
                .from("book")
                .where(new ComplexCondition(
                        BooleanOperator.OR,
                        new SimpleCondition("age", RelationalOperator.GRATER_THAN, "10"),
                        new SimpleCondition("price", RelationalOperator.LESS_THAN, "650")
                ))
                .limit(5)
                .skip(10)
                .perform();
        assertEquals(sqlSelectQueryVue, selectQuery.toSqlVue());
        assertEquals(mongoSelectQueryVue, selectQuery.toMongoVue());

        final String sqlSelectQueryVue2 = "SELECT * FROM book";
        final String mongoSelectQueryVue2 = "db.book.find({})";
        final SelectQuery selectQuery2 = (SelectQuery) selectQueryBuilder
                .newQuery()
                .column("*")
                .from("book")
                .perform();
        assertEquals(sqlSelectQueryVue2, selectQuery2.toSqlVue());
        assertEquals(mongoSelectQueryVue2, selectQuery2.toMongoVue());

        final String sqlSelectQueryVue3 = "SELECT _id FROM book";
        final String mongoSelectQueryVue3 = "db.book.find({}, {_id: 1})";
        final SelectQuery selectQuery3 = (SelectQuery) selectQueryBuilder
                .newQuery()
                .column("_id")
                .from("book")
                .perform();
        assertEquals(sqlSelectQueryVue3, selectQuery3.toSqlVue());
        assertEquals(mongoSelectQueryVue3, selectQuery3.toMongoVue());

        final String sqlSelectQueryVue4 = "SELECT name, _id FROM book";
        final String mongoSelectQueryVue4 = "db.book.find({}, {name: 1})";
        final SelectQuery selectQuery4 = (SelectQuery) selectQueryBuilder
                .newQuery()
                .column("name", "_id")
                .from("book")
                .perform();
        assertEquals(sqlSelectQueryVue4, selectQuery4.toSqlVue());
        assertEquals(mongoSelectQueryVue4, selectQuery4.toMongoVue());

    }
}
