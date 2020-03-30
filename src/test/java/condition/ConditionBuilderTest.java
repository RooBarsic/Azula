package condition;

import azula.condition.Condition;
import azula.condition.ConditionBuilder;
import azula.condition.Statement;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 * Author : Farrukh Karimov
 * Modification Date : 30.03.2020
 */
public class ConditionBuilderTest {
    @Test
    public void canBuildCondition(){
        final Condition buildedCondition = ConditionBuilder.or(
                new Condition("name", Statement.EQUAL, "'Bob'"),
                new Condition("age", Statement.GRATER_THAN, "65"),
                ConditionBuilder.and(
                        new Condition("price", Statement.LESS_THAN, "650"),
                        new Condition("age", Statement.LESS_THAN, "55")
                )
        );
        final String conditionStr = "((name = 'Bob') OR (age > 65) OR ((price < 650) AND (age < 55)))";
        assertEquals(conditionStr, buildedCondition.toSqlVue());
    }
}
