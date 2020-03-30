package azula.condition;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Author : Farrukh Karimov
 * Modification Date : 30.03.2020
 */
public class ConditionBuilder {

    /**
     * Метод для создания простого Condition.
     * Принимает :
     * @param left - операнд слева
     * @param operation - оператор сравнения
     * @param right - операнд справа
     * @return - возврашает новый Condition с таким условием
     */
    public static Condition condition(final String left, final Statement operation, final String right){
        return new Condition(
                left,
                operation,
                right
        );
    }

    /**
     * Метод для обеденения двух Condition в одну, в соответствии с переданным conditionType
     * @return - возвращет обедение переданных Condition-нов
     */
    public static Condition unionConditions(final Condition condition1, final Condition condition2, final ConditionType conditionType){
        if((condition1.getType() == condition2.getType()) && (condition1.getType() == conditionType)){
            condition1.addConditions(conditionType, condition2.getConditionList());
            return condition1;
        } else if((condition1.getType() == conditionType) && (condition2.getType() == ConditionType.SINGLE)){
            condition1.addCondition(conditionType, condition2);
            return condition1;
        } else if((condition2.getType() == conditionType) && (condition1.getType() == ConditionType.SINGLE)){
            condition2.addCondition(conditionType, condition1);
            return condition2;
        }
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(condition1);
        conditionList.add(condition2);
        return new Condition(conditionType, conditionList);
    }

    /**
     * Метод для создания нового Condition типа and
     */
    public static Condition and(Condition condition1, Condition condition2){
        final List<Condition> conditionList = new ArrayList<>();
        conditionList.add(condition1);
        conditionList.add(condition2);
        return new Condition(ConditionType.AND, conditionList);
    }

    public static Condition and(Condition condition1, Condition condition2, Condition condition3){
        final List<Condition> conditionList = new ArrayList<>();
        conditionList.add(condition1);
        conditionList.add(condition2);
        conditionList.add(condition3);
        return new Condition(ConditionType.AND, conditionList);
    }

    public static Condition or(Condition condition1, Condition condition2){
        final List<Condition> conditionList = new ArrayList<>();
        conditionList.add(condition1);
        conditionList.add(condition2);
        return new Condition(ConditionType.OR, conditionList);
    }

    public static Condition or(Condition condition1, Condition condition2, Condition condition3){
        final List<Condition> conditionList = new ArrayList<>();
        conditionList.add(condition1);
        conditionList.add(condition2);
        conditionList.add(condition3);
        return new Condition(ConditionType.OR, conditionList);
    }
}
