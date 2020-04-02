package azula.condition;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(DataProviderRunner.class)
public class SimpleConditionTest {
    private final String leftLiteral = "age";
    private final String rightLiteral = "56";

    @DataProvider
    public static List<List<Object>> provider(){
        List<List<Object>> dataList = new ArrayList<>();
        for(RelationalOperator operator : RelationalOperator.values()){
            dataList.add(Collections.singletonList(operator));
        }
        return dataList;
    }

    @Test
    @UseDataProvider("provider")
    public void canParseCondition(final RelationalOperator operator){
        System.out.println(operator);
        final String stringCondition = leftLiteral + " " + operator.toSqlVue() + " " + rightLiteral;
        final Condition condition = new SimpleCondition(leftLiteral, operator, rightLiteral);
        final Condition parsedCondition = SimpleCondition.parseCondition(stringCondition);
        assertEquals(condition, parsedCondition);
    }

    @Test
    @UseDataProvider("provider")
    public void canConvertToMongoVue(final RelationalOperator operator){
        final String mongoConditionVue = leftLiteral + ": {$" + operator.toMongoVue() + ": " + rightLiteral + "}";
        final Condition condition = new SimpleCondition(leftLiteral, operator, rightLiteral);
        assertEquals(mongoConditionVue, condition.toMongoVue());
    }

    @Test
    public void cantParseWrongCondition(){
        final String wrongSqlConditionVue = "age >> 56";
        final Condition condition = SimpleCondition.parseCondition(wrongSqlConditionVue);
        assertNull(condition);
    }
}
