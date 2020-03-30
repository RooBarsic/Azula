package builders;

import azula.QueryType;
import azula.condition.Condition;
import azula.condition.ConditionType;
import azula.condition.Statement;
import azula.sql.SqlQuery;
import azula.sql.SqlSelectQuery;
import azula.sql.builders.SqlSelectQueryBuilder;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 *
 * Author : Farrukh Karimov
 * Modification Date : 30.03.2020
 */
public class SqlSelectQueryBuilderTest {

    @Test
    public void canBuildQuery(){
        final String tableName = "books";
        final List<String> columns = Arrays.asList("name", "age");
        final int limit = 15;
        final int skip = 10;
        final Condition whereCondition = new Condition(
                ConditionType.AND,
                Arrays.asList(
                        new Condition("age", Statement.GRATER_THAN, "33"),
                        new Condition("price", Statement.LESS_THAN, "600")
                )
        );
        final SqlSelectQueryBuilder queryBuilder = new SqlSelectQueryBuilder();
        final SqlQuery buildedQuery = queryBuilder
                .column(columns)
                .from(tableName)
                .where(whereCondition)
                .limit(limit)
                .skip(skip)
                .perform();
        final SqlQuery sqlQuery = new SqlSelectQuery(
                QueryType.SELECT,
                tableName,
                whereCondition,
                limit,
                skip,
                columns
        );
        assertEquals(sqlQuery, buildedQuery);
    }
}
