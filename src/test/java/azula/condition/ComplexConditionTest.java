package azula.condition;

import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Класс для тестирования сложных условных выражений
 * Author : Farrukh Karimov
 * Modification Date : 02.04.2020
 */
public class ComplexConditionTest {
    @Test
    public void canParseSingleCondition(){
        final String sqlConditionVue = "((a > 5) AND ((b < 10) OR (c = 45) OR (d <> 16)) AND (e < 19))";
        final Condition parsedCondition = ComplexCondition.parseCondition(sqlConditionVue);
        final Condition condition = new ComplexCondition(
                BooleanOperator.AND,
                new SimpleCondition("a", RelationalOperator.GRATER_THAN, "5"),
                new ComplexCondition(
                        BooleanOperator.OR,
                        new SimpleCondition("b", RelationalOperator.LESS_THAN, "10"),
                        new SimpleCondition("c", RelationalOperator.EQUAL, "45"),
                        new SimpleCondition("d", RelationalOperator.NOT_EQUAL, "16")
                ),
                new SimpleCondition("e", RelationalOperator.LESS_THAN, "19")
        );

        assertNotNull(parsedCondition);
        assertEquals(condition, parsedCondition);
        assertEquals(sqlConditionVue, parsedCondition.toSqlVue());
    }

    @Test
    public void canConvertToSqlCondition(){
        final Condition condition = new ComplexCondition(
                BooleanOperator.AND,
                new SimpleCondition("a", RelationalOperator.GRATER_THAN, "5"),
                new ComplexCondition(
                        BooleanOperator.OR,
                        new SimpleCondition("b", RelationalOperator.LESS_THAN, "10"),
                        new SimpleCondition("c", RelationalOperator.EQUAL, "45"),
                        new SimpleCondition("d", RelationalOperator.NOT_EQUAL, "16")
                ),
                new SimpleCondition("e", RelationalOperator.LESS_THAN, "19")
        );
        final Condition parsedCondition = Condition.parseCondition(condition.toSqlVue());
        assertEquals(condition, parsedCondition);
    }

    @Test
    public void canConvertToMongoCondition(){
        final String mongoConditionVue = "{$and : [{a: {$gt: 5}}, {$or : [{b: {$lt: 10}}, {c: {$eq: 45}}, {d: {$ne: 16}}]}, {e: {$lt: 19}}]}";

        final Condition condition = new ComplexCondition(
                BooleanOperator.AND,
                new SimpleCondition("a", RelationalOperator.GRATER_THAN, "5"),
                new ComplexCondition(
                        BooleanOperator.OR,
                        new SimpleCondition("b", RelationalOperator.LESS_THAN, "10"),
                        new SimpleCondition("c", RelationalOperator.EQUAL, "45"),
                        new SimpleCondition("d", RelationalOperator.NOT_EQUAL, "16")
                ),
                new SimpleCondition("e", RelationalOperator.LESS_THAN, "19")
        );

        assertEquals(mongoConditionVue, condition.toMongoVue());
    }

}
