package azula.condition;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Класс простых условных выражений
 * Author : Farrukh Karimov
 * Modification Date : 02.04.2020
 */
public class SimpleCondition extends Condition {
    private String leftLiteral;
    private RelationalOperator operator;
    private String rightLiteral;

    SimpleCondition(@NotNull final String leftLiteral, @NotNull final RelationalOperator operator, @NotNull final String rightLiteral){
        this.leftLiteral = leftLiteral;
        this.operator = operator;
        this.rightLiteral = rightLiteral;
    }

    @Nullable
    public static Condition parseCondition(@NotNull final String conditionStr) {
        RelationalOperator operator = findComparisonOperationType(conditionStr);
        if(operator != null){
            String[] buffer = conditionStr.split(operator.toSqlVue(), 2);
            buffer[0] = buffer[0].trim();
            buffer[1] = buffer[1].trim();
            if((buffer.length == 2) && (checkLiteral(buffer[0])) && (checkLiteral(buffer[1]))){
                return new SimpleCondition(buffer[0], operator, buffer[1]);
            }
        }
        return null;
    }

    /**
     * Возврашает тип операции сравнения в строке
     */
    @Nullable
    private static RelationalOperator findComparisonOperationType(@NotNull final String conditionSqlVue){
        if(conditionSqlVue.contains(RelationalOperator.NOT_EQUAL.toSqlVue())){
            return RelationalOperator.NOT_EQUAL;
        }
        for(RelationalOperator operator : RelationalOperator.values()){
            if(conditionSqlVue.contains(operator.toSqlVue())){
                return operator;
            }
        }
        return null;
    }

    public String toMongoVue() {
        return leftLiteral + ": {" +
                "$" + operator.toMongoVue() +
                ": " + rightLiteral + "}" ;
    }

    public String toSqlVue() {
        return leftLiteral + " "
                + operator.toSqlVue() + " "
                + rightLiteral;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleCondition)) return false;

        SimpleCondition that = (SimpleCondition) o;

        if (!Objects.equals(leftLiteral, that.leftLiteral)) return false;
        if (operator != that.operator) return false;
        return Objects.equals(rightLiteral, that.rightLiteral);
    }

    @Override
    public int hashCode() {
        int result = leftLiteral != null ? leftLiteral.hashCode() : 0;
        result = 31 * result + (operator != null ? operator.hashCode() : 0);
        result = 31 * result + (rightLiteral != null ? rightLiteral.hashCode() : 0);
        return result;
    }
}
