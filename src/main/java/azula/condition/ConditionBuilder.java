package azula.condition;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Вспомогательный класс для создания условий.
 * Author : Farrukh Karimov
 * Modification Date : 02.04.2020
 */
public class ConditionBuilder {
    private BooleanOperator booleanOperator = null;
    private List<Condition> conditionsList;

    ConditionBuilder(){
        conditionsList = new ArrayList<>();
    }

    ConditionBuilder(final Condition condition){
        conditionsList = new ArrayList<>();
        if(condition  instanceof SimpleCondition){
            booleanOperator = null;
            conditionsList.add(condition);
        } else {
            final ComplexCondition complexCondition = (ComplexCondition) condition;
            booleanOperator = complexCondition.getConditionOperator();
            conditionsList.addAll(
                    complexCondition.getConditionList()
            );
        }
    }

    ConditionBuilder(final BooleanOperator booleanOperator, final Condition... conditions){
        this.booleanOperator = booleanOperator;
        this.conditionsList = new ArrayList<>();
        Collections.addAll(this.conditionsList, conditions);
    }

    public void and(@NotNull final Condition... conditions){
        changeBooleanOperatorIfNot(BooleanOperator.AND);
        addConditions(conditions);
    }

    public void or(@NotNull final Condition... conditions){
        changeBooleanOperatorIfNot(BooleanOperator.OR);
        addConditions(conditions);
    }

    public void add(@NotNull final BooleanOperator booleanOperator, @NotNull Condition... conditions){
        changeBooleanOperatorIfNot(booleanOperator);
        addConditions(conditions);
    }

    private void addConditions(@NotNull final Condition... conditions){
        for(final Condition condition : conditions){
            if(condition instanceof ComplexCondition){
                final ComplexCondition complexCondition = (ComplexCondition) condition;
                if(booleanOperator == complexCondition.getConditionOperator()) {
                    conditionsList.addAll(complexCondition.getConditionList());
                } else {
                    conditionsList.add(complexCondition);
                }
            } else {
                conditionsList.add(condition);
            }
        }
    }

    public Condition perform(){
        if(booleanOperator == null){
            return conditionsList.get(0);
        }
        return new ComplexCondition(booleanOperator, conditionsList);
    }

    private void changeBooleanOperatorIfNot(final BooleanOperator booleanOperator){
        if(this.booleanOperator != booleanOperator) {
            if(this.booleanOperator != null){
                final Condition condition = new ComplexCondition(booleanOperator, new ArrayList<>(conditionsList));
                conditionsList.clear();
                conditionsList.add(condition);
            }
            this.booleanOperator = booleanOperator;
        }
    }
}
