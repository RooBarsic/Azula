package azula.condition;

import azula.VueAble;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

/**
 * Класс условных выражений
 * Author : Farrukh Karimov
 * Modification Date : 02.04.2020
 */
public abstract class Condition implements VueAble {

    static boolean checkLiteral(final String str){
        for(char e : str.toCharArray()){
            if(!checkLetter(e)){
                return false;
            }
        }
        return true;
    }

    static boolean checkLetter(final char e){
        return (('a' <= e) && (e <= 'z')) || (('A' <= e) && (e <= 'Z'))
                || (('0' <= e) && (e <= '9')) || (e == '_') || (e == '.') || (e == '"');
    }

    @Nullable
    public static Condition parseCondition(@NotNull final String conditionStr) {
        if(!conditionStr.equals("")){
            final LinkedList<Character> bracketsList = new LinkedList<>();
            final LinkedList<Condition> parsedConditions = new LinkedList<>();
            final LinkedList<BooleanOperator> parsedConditionOperators = new LinkedList<>();
            final StringBuilder stringBuilder = new StringBuilder();

            char[] conditionCharArray = conditionStr.trim().toCharArray();

            for (char c : conditionCharArray) {
                if (c == '(') {
                    final String parsedStrConditionType = stringBuilder
                            .toString()
                            .trim()
                            .toUpperCase();
                    if (BooleanOperator.contains(parsedStrConditionType)) {
                        parsedConditionOperators.add(
                                BooleanOperator.valueOf(
                                        parsedStrConditionType
                                )
                        );
                    }
                    stringBuilder.delete(0, stringBuilder.length());
                    bracketsList.addLast(c);
                } else if (c == ')') {
                    final String sqlSimpleConditionVue = stringBuilder.toString().trim();
                    if (sqlSimpleConditionVue.length() > 0) {
                        parsedConditions.addLast(
                                SimpleCondition.parseCondition(
                                        sqlSimpleConditionVue
                                )
                        );
                        bracketsList.addLast('.');
                    }
                    stringBuilder.delete(0, stringBuilder.length());
                    while (bracketsList.getLast() != '(') {
                        bracketsList.removeLast();
                        if (bracketsList.getLast() != '(') {
                            final Condition lastParsedCondition = parsedConditions.removeLast();
                            final Condition penultimateParsedCondition = parsedConditions.removeLast();
                            final BooleanOperator lastParsedConditionOperator = parsedConditionOperators.removeLast();
                            final ConditionBuilder conditionBuilder = new ConditionBuilder();

                            conditionBuilder.add(
                                    lastParsedConditionOperator,
                                    lastParsedCondition,
                                    penultimateParsedCondition
                            );
                            parsedConditions.addLast(
                                    conditionBuilder.perform()
                            );
                        }
                    }
                    bracketsList.removeLast();
                    bracketsList.addLast('.');
                } else {
                    stringBuilder.append(c);
                }
            }
            System.out.println(stringBuilder);
            System.out.println(parsedConditions.toString());
            final Condition finalCondition = parsedConditions.getFirst();
            if(finalCondition instanceof ComplexCondition){
                final ComplexCondition finalComplexCondition = (ComplexCondition) finalCondition;
                finalComplexCondition.reverseConditionsOrder();
            }
            return finalCondition;
        }
        return null;
    }
}
