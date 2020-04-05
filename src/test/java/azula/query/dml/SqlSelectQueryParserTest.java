package azula.query.dml;

import azula.condition.BooleanOperator;
import azula.condition.ComplexCondition;
import azula.condition.RelationalOperator;
import azula.condition.SimpleCondition;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Класс для тестирования парсера SQL запросов типа SELECT
 *
 * Author : Farrukh Karimov
 * Modification Date : 04.04.2020
 */
public class SqlSelectQueryParserTest {

    @Test
    public void canParseQuery(){
        final String sqlQueryString = "SELECT name, age, price FROM book WHERE ((age > 10) OR (price < 650)) SKIP 10 LIMIT 5";
        final SqlSelectQueryParser sqlQueryParser = new SqlSelectQueryParser();
        final SelectQuery parsedSqlQuery = sqlQueryParser.parseQuery(sqlQueryString);
        assertEquals(sqlQueryString, parsedSqlQuery.toSqlVue());

        final SelectQuery sqlQuery = new SelectQueryBuilder()
                .newQuery()
                .column("name", "age", "price")
                .from("book")
                .where(new ComplexCondition(
                        BooleanOperator.OR,
                        new SimpleCondition("age", RelationalOperator.GRATER_THAN, "10"),
                        new SimpleCondition("price", RelationalOperator.LESS_THAN, "650")
                ))
                .skip(10)
                .limit(5)
                .perform();
        assertEquals(sqlQuery, parsedSqlQuery);
    }
}
