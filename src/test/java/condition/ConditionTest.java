package condition;

import azula.condition.Condition;
import azula.condition.ConditionType;
import azula.condition.Statement;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 *
 * Author : Farrukh Karimov
 * Modification Date : 30.03.2020
 */

public class ConditionTest {
    @Test
    public void canParseSingleCondition(){
        final String inputConditionStr = "((a > 5) AND ((b < 10) OR (c = 45) OR (d <> 16)) AND (e < 19))";
        final String finalConditionStr = "((e < 19) AND ((d <> 16) OR (c = 45) OR (b < 10)) AND (a > 5))";
        final Condition parsedCondition = Condition.parseCondition(inputConditionStr);
        final Condition condition = new Condition(
                ConditionType.AND,
                Arrays.asList(
                        new Condition("a", Statement.GRATER_THAN, "5"),
                        new Condition(
                                ConditionType.OR,
                                Arrays.asList(
                                        new Condition("b", Statement.LESS_THAN, "10"),
                                        new Condition("c", Statement.EQUAL, "45"),
                                        new Condition("d", Statement.NON_EQUAL, "16")
                                )
                        ),
                        new Condition("e", Statement.LESS_THAN, "19")
                )
        );

        assertEquals(finalConditionStr, parsedCondition.toSqlVue());
        assertEquals(condition, parsedCondition);
    }

    @Test
    public void canConvertToSqlCondition(){
        final Condition condition = new Condition(
                ConditionType.AND,
                Arrays.asList(
                        new Condition("a", Statement.GRATER_THAN, "5"),
                        new Condition(
                                ConditionType.OR,
                                Arrays.asList(
                                        new Condition("b", Statement.LESS_THAN, "10"),
                                        new Condition("c", Statement.EQUAL, "45"),
                                        new Condition("d", Statement.NON_EQUAL, "16")
                                )
                        ),
                        new Condition("e", Statement.LESS_THAN, "19")
                )
        );
        final Condition parsedCondition = Condition.parseCondition(condition.toSqlVue());
        assertEquals(condition, parsedCondition);
    }

    @Test
    public void canConvertToMongoCondition(){
        final Condition condition = new Condition(
                ConditionType.AND,
                Arrays.asList(
                        new Condition("a", Statement.GRATER_THAN, "5"),
                        new Condition(
                                ConditionType.OR,
                                Arrays.asList(
                                        new Condition("b", Statement.LESS_THAN, "10"),
                                        new Condition("c", Statement.EQUAL, "45"),
                                        new Condition("d", Statement.NON_EQUAL, "16")
                                )
                        ),
                        new Condition("e", Statement.LESS_THAN, "19")
                )
        );
        System.out.println(condition.toMongoVue());
        final String mongoConditionVue = "{$and : [{a: {$gt : 5}}, {$or : [{b: {$lt : 10}}, {c: {$eq : 45}}, {d: {$ne : 16}}]}, {e: {$lt : 19}}]}";
        assertEquals(mongoConditionVue, condition.toMongoVue());
    }
}
