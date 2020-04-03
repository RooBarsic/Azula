package azula.condition;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * Класс сложных условных выражений
 * Author : Farrukh Karimov
 * Modification Date : 02.04.2020
 */
public class ComplexCondition extends Condition {
    private final BooleanOperator booleanOperator;
    private final List<Condition> conditionList;

    public ComplexCondition(@NotNull final BooleanOperator booleanOperator, @NotNull final Condition... conditions){
        this.booleanOperator = booleanOperator;
        conditionList = new ArrayList<>();
        Collections.addAll(conditionList, conditions);
    }

    ComplexCondition(@NotNull final BooleanOperator booleanOperator, @NotNull final List<Condition> conditions){
        this.booleanOperator = booleanOperator;
        conditionList = new ArrayList<>(conditions);
    }

    public void addCondition(@NotNull final Condition condition){
        conditionList.add(condition);
    }

    public BooleanOperator getConditionOperator(){
        return booleanOperator;
    }

    public List<Condition> getConditionList(){
        return new ArrayList<>(conditionList);
    }

    protected void reverseConditionsOrder(){
        final LinkedList<Condition> reversedConditionsList = new LinkedList<>();
        for(final Condition condition : conditionList){
            reversedConditionsList.addFirst(condition);
            if (condition instanceof ComplexCondition){
                final ComplexCondition complexCondition = (ComplexCondition) condition;
                complexCondition.reverseConditionsOrder();
            }
        }
        conditionList.clear();
        conditionList.addAll(reversedConditionsList);
    }

    @Override
    public String toMongoVue() {
        final StringBuilder mongoVueBuilder = new StringBuilder("{");
        mongoVueBuilder
                .append("$")
                .append(booleanOperator.toMongoVue())
                .append(": [");
        for(final Condition condition : conditionList){
            mongoVueBuilder.append(condition.toMongoVue());
            mongoVueBuilder.append(", ");
        }
        return mongoVueBuilder.deleteCharAt(mongoVueBuilder.length() - 1)
                .deleteCharAt(mongoVueBuilder.length() - 1)
                .append("]")
                .append("}")
                .toString();
    }

    @Override
    public String toSqlVue() {
        final StringBuilder sqlBueBuilder = new StringBuilder("(");
        sqlBueBuilder.append(
                conditionList
                        .get(0)
                        .toSqlVue());
        for(int i = 1; i < conditionList.size(); i++){
            final Condition condition = conditionList.get(i);
            sqlBueBuilder
                    .append(" ")
                    .append(booleanOperator.toSqlVue())
                    .append(" ")
                    .append(condition.toSqlVue());
        }
        return sqlBueBuilder
                .append(")")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComplexCondition)) return false;

        ComplexCondition that = (ComplexCondition) o;

        if (booleanOperator != that.booleanOperator) return false;

        return getConditionList() != null ? getConditionList().equals(that.getConditionList()) : that.getConditionList() == null;
    }

    @Override
    public int hashCode() {
        int result = booleanOperator != null ? booleanOperator.hashCode() : 0;
        result = 31 * result + (getConditionList() != null ? getConditionList().hashCode() : 0);
        return result;
    }
}
