package azula;

import azula.query.Query;
import azula.query.dml.SelectQuery;
import azula.query.dml.SelectQueryBuilder;
import azula.query.dml.SqlSelectQueryParser;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 *
 * Author : Farrukh Karimov
 * Modification Date : 04.04.2020
 */
@RunWith(DataProviderRunner.class)
public class PreparedTranslationTest {

    @Test
    public void test1(){
        final String mongoQueryVue = "db.sales.find({}).limit(10)";
        final Query selectQuery = new SelectQueryBuilder()
                .column("*")
                .from("sales")
                .limit(10)
                .perform();
        assertEquals(mongoQueryVue, selectQuery.toMongoVue());
    }

    @Test
    public void test2(){
        final String sqlQueryVue = "SELECT name, surname FROM collection";
        final Query parsedSelectQuery = new SqlSelectQueryParser().parseQuery(sqlQueryVue);
        final Query sqlQuery = new SelectQueryBuilder()
                .column("name")
                .column("surname")
                .from("collection")
                .perform();
        assertEquals(sqlQuery, parsedSelectQuery);
        assertEquals(parsedSelectQuery.toMongoVue(),
                "db.collection.find({}, {name: 1, surname: 1, _id: 0})"
        );
    }

    @Test
    public void test3(){
        final String sqlQueryVue = "SELECT * FROM collection SKIP 5 LIMIT 10";
        final String mongoQueryVue = "db.collection.find({}).skip(5).limit(10)";
        final Query parsedQuery = new SqlSelectQueryParser().parseQuery(sqlQueryVue);
        final Query selectQuery = new SelectQueryBuilder()
                .column("*")
                .from("collection")
                .skip(5)
                .limit(10)
                .perform();
        assertEquals(selectQuery, parsedQuery);
        assertEquals(mongoQueryVue, parsedQuery.toMongoVue());
        assertEquals(sqlQueryVue, parsedQuery.toSqlVue());
    }

    @Test
    public void test4(){
        final String sqlQueryVue = "SELECT name, _id FROM book SKIP 5 LIMIT 10";
        final String mongoQueryVue = "db.book.find({}, {name: 1}).skip(5).limit(10)";
        final Query parsedQuery = new SqlSelectQueryParser().parseQuery(sqlQueryVue);
        final Query selectQuery = new SelectQueryBuilder()
                .column("name")
                .column("_id")
                .from("book")
                .skip(5)
                .limit(10)
                .perform();
        assertEquals(selectQuery, parsedQuery);
        assertEquals(mongoQueryVue, parsedQuery.toMongoVue());
    }

    @DataProvider
    public static Object[][] sqlAndMongoMatchedExamples(){
        return new Object[][]{
                {"SELECT * FROM book WHERE (a <> 25)", "db.book.find({a: {$ne: 25}})"},
                {"SELECT * FROM book WHERE (a < 25)","db.book.find({a: {$lt: 25}})"},
                {"SELECT * FROM book WHERE (a > 25)", "db.book.find({a: {$gt: 25}})"},
                {"SELECT * FROM book WHERE (a = 25)", "db.book.find({a: {$eq: 25}})"},
                {"SELECT a1, b2 FROM book WHERE ((a1 < b1) and ((a2 > b2) or (a3 < b3) or (a4 = b4)) and (a5 <> b5))",
                        "db.book.find({$and: [{a1: {$lt: b1}}, {$or: [{a2: {$gt: b2}}, {a3: {$lt: b3}}, {a4: {$eq: b4}}]}, {a5: {$ne: b5}}]}, {a1: 1, b2: 1, _id: 0})"
                }
        };
    }

    @Test
    @UseDataProvider("sqlAndMongoMatchedExamples")
    public void testWithConditionParsing(final String sqlQueryVue, final String mongoQueryVue){
        final SqlSelectQueryParser sqlSelectQueryParser = new SqlSelectQueryParser();
        final SelectQuery parsedSelectQuery = sqlSelectQueryParser.parseQuery(sqlQueryVue);
        assertEquals(mongoQueryVue, parsedSelectQuery.toMongoVue());
    }
}
