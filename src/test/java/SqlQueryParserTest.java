import azula.condition.Condition;
import azula.condition.ConditionType;
import azula.condition.Statement;
import azula.sql.SqlQuery;
import azula.sql.SqlQueryParser;
import azula.sql.builders.SqlSelectQueryBuilder;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 *
 * Author : Farrukh Karimov
 * Modification Date : 30.03.2020
 */
public class SqlQueryParserTest {

    @Test
    public void canParseQuery(){
        final String sqlQueryString = "SELECT name, age, price FROM book WHERE ((age > 10) OR (price < 650)) LIMIT 5 SKIP 10";
        final String parsingResultString = "SELECT name, age, price FROM book WHERE ((price < 650) OR (age > 10)) LIMIT 5 SKIP 10";
        final SqlQueryParser sqlQueryParser = new SqlQueryParser();
        final SqlQuery parsedSqlQuery = sqlQueryParser.parseQuery2(sqlQueryString);
        assertEquals(parsingResultString, parsedSqlQuery.toSqlVue());

        final SqlQuery sqlQuery = new SqlSelectQueryBuilder()
                .column(Arrays.asList("name", "age", "price"))
                .from("book")
                .where(new Condition(
                        ConditionType.OR,
                        Arrays.asList(
                                new Condition("age", Statement.GRATER_THAN, "10"),
                                new Condition("price", Statement.LESS_THAN, "650")
                        )))
                .limit(5)
                .skip(10)
                .perform();
        assertEquals(sqlQuery, parsedSqlQuery);
    }
}
