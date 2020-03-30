import azula.QueryType;
import azula.condition.Condition;
import azula.condition.ConditionType;
import azula.condition.Statement;
import azula.mongo.MongoQuery;
import azula.mongo.MongoSelectQuery;
import azula.sql.SqlQuery;
import azula.sql.SqlSelectQuery;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 *
 * Author : Farrukh Karimov
 * Modification Date : 30.03.2020
 */
@RunWith(DataProviderRunner.class)
public class SqlSelectQueryTest {

    @DataProvider
    public static Object[][] queryData(){
        return new Object[][]{
                {"book", null, null, null, Arrays.asList("*"), "SELECT * FROM book"},
                {"book", null, null, null, Arrays.asList("name", "age"), "SELECT name, age FROM book"},
                {"book", new Condition("age", Statement.GRATER_THAN, "5"), null, null, Arrays.asList("*"),
                        "SELECT * FROM book WHERE (age > 5)"},
                {"book",
                        new Condition(
                                ConditionType.OR,
                                Arrays.asList(
                                        new Condition("age", Statement.GRATER_THAN, "10"),
                                        new Condition("price", Statement.LESS_THAN, "650")
                                )
                        ),
                        5, // LIMIT
                        10, // SKIP
                        Arrays.asList("name", "age", "price"),
                        "SELECT name, age, price FROM book WHERE ((age > 10) OR (price < 650)) LIMIT 5 SKIP 10"
                }
        };
    }

    @Test
    @UseDataProvider("queryData")
    public void canConvertToMongoQuery(String tableName, Condition whereCondition, Integer limit, Integer skip, List<String> columns, String queryStr){
        final SqlQuery sqlQuery = new SqlSelectQuery(
                QueryType.SELECT,
                tableName,
                whereCondition,
                limit,
                skip,
                new ArrayList<>(columns)
        );
        if(columns.get(0).equals("*")){
            columns = new ArrayList<>();
        }
        final MongoQuery mongoQuery = new MongoSelectQuery(
                tableName,
                whereCondition,
                limit,
                skip,
                new ArrayList<>(columns)
        );
        assertEquals(mongoQuery, sqlQuery.toMongoQuery());
    }

    @Test
    @UseDataProvider("queryData")
    public void canConvertToString(String tableName, Condition whereCondition, Integer limit, Integer skip, List<String> columns, String queryStr){
        final SqlQuery sqlQuery = new SqlSelectQuery(
                QueryType.SELECT,
                tableName,
                whereCondition,
                limit,
                skip,
                new ArrayList<>(columns)
        );
        assertEquals(queryStr, sqlQuery.toSqlVue());
    }
}
