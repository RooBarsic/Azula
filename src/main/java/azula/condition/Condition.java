package azula.condition;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Класс для хранения условного выражения аргумента WHERE
 * Author : Farrukh Karimov
 * Modification Date : 30.03.2020
 */
public class Condition implements Comparable{
    private ConditionType type;
    private List<Condition> conditionList = new ArrayList<>();
    private String left;
    private Statement statement;
    private String right;

    public Condition(final ConditionType type, final List<Condition> conditionList){
        this.type = type;
        this.conditionList.addAll(conditionList);
    }

    public Condition(final String left, final Statement statement, final String right){
        type = ConditionType.SINGLE;
        this.left = left;
        this.statement = statement;
        this.right = right;
    }

    public Condition(){
        this.type = ConditionType.EMPTY;
    }

    private Condition(final ConditionType type, final List<Condition> conditions, final String left, final String right, final Statement statement){
        this.type = type;
        this.conditionList = conditions;
        this.left = left;
        this.right = right;
        this.statement = statement;
    }

    public ConditionType getType(){
        return type;
    }

    public boolean addCondition(final ConditionType conditionType, final Condition condition){
        if(type == ConditionType.SINGLE){
            conditionList.add(this.copyOf());
            type = conditionType;
        }
        if(conditionType == type){
            conditionList.add(condition);
            return true;
        }
        return false;
    }

    public boolean addConditions(final ConditionType conditionType, final List<Condition> conditions){
        if(type == ConditionType.SINGLE){
            conditionList.add(this.copyOf());
            type = conditionType;
        }
        if(type == conditionType){
            conditionList.addAll(conditions);
            return true;
        }
        return false;
    }

    public List<Condition> getConditionList(){
        return new ArrayList<>(conditionList);
    }

    /**
     * @return - возвращает копию данного выражения
     */
    public Condition copyOf(){
        List<Condition> copiedConditions = new ArrayList<>();
        for(Condition condition : conditionList){
            copiedConditions.add(condition.copyOf());
        }
        return new Condition(
                type,
                copiedConditions,
                left,
                right,
                statement
        );
    }

    /**
     * Метод для парсинга условного выражения из строковой sql формы
     * @param conditionStr - получает строковое представление условного sql выражения
     */
    public static Condition parseCondition(final String conditionStr){
        if((conditionStr == null) || (conditionStr.trim().equals(""))){
            return new Condition();
        }

        LinkedList<Character> queue = new LinkedList<>();
        LinkedList<Condition> parsedConditions = new LinkedList<>();
        LinkedList<ConditionType> parsedConditionTypes = new LinkedList<>();
        StringBuilder stringBuilder = new StringBuilder();
        char[] conditionCharArray = conditionStr.trim().toCharArray();
        for (char c : conditionCharArray) {
            if (c == '(') {
                final String parsedStrConditionType = stringBuilder
                        .toString()
                        .trim()
                        .toUpperCase();
                if ((parsedStrConditionType.length() > 0) && (ConditionType.contains(parsedStrConditionType))) {
                    final ConditionType parsedConditionType = ConditionType
                            .valueOf(parsedStrConditionType);
                    parsedConditionTypes.add(parsedConditionType);
                }
                stringBuilder.delete(0, stringBuilder.length());
                queue.add(c);
            } else if (c == ')') {
                final String singleConditionStr = stringBuilder.toString().trim();
                if (singleConditionStr.length() > 0) {
                    parsedConditions.addLast(parseSingleCondition(singleConditionStr));
                    queue.addLast('.');
                }
                stringBuilder.delete(0, stringBuilder.length());
                while (queue.getLast() != '(') {
                    queue.removeLast();
                    if (queue.getLast() != '(') {
                        final Condition condition1 = parsedConditions.removeLast();
                        final Condition condition2 = parsedConditions.removeLast();
                        final ConditionType conditionType = parsedConditionTypes.removeLast();
                        final Condition mergedCondition = ConditionBuilder.unionConditions(
                                condition1,
                                condition2,
                                conditionType
                        );
                        parsedConditions.addLast(mergedCondition);
                    }
                }
                queue.removeLast();
                queue.addLast('.');
            } else {
                stringBuilder.append(c);
            }
        }
        return parsedConditions.getFirst();
    }

    /**
     * Метод для парсинга простого условного выражения из строковой sql формы
     * @param conditionStr - получает строковое представление условного sql выражения
     */
    private static Condition parseSingleCondition(final String conditionStr){
        String[] buffer;
        Statement statement = null;
        if(conditionStr.split("<>", 2).length == 2){
            buffer = conditionStr.split("<>", 2);
            statement = Statement.NON_EQUAL;
        } else if(conditionStr.split("<", 2).length == 2){
            buffer = conditionStr.split("<", 2);
            statement = Statement.LESS_THAN;
        } else if(conditionStr.split(">", 2).length == 2){
            buffer = conditionStr.split(">", 2);
            statement = Statement.GRATER_THAN;
        } else if(conditionStr.split("=", 2).length == 2){
            buffer = conditionStr.split("=", 2);
            statement = Statement.EQUAL;
        } else {
            return new Condition();
        }
        return new Condition(
                buffer[0].trim(),
                statement,
                buffer[1].trim()
        );
    }

    /**
     * @return - возвращает строковое представление исходного условного выражения - в принятом MongoDB формате
     */
    public String toMongoVue(){
        final StringBuilder vue = new StringBuilder("{");
        if(type == ConditionType.SINGLE){
            vue.append(left)
                    .append(": {")
                    .append(statement.toMongoVue())
                    .append(" : ")
                    .append(right)
                    .append("}");
        } else {
            vue.append(type.toMongoVue()).append(" : [");
            for(Condition condition : conditionList){
                vue.append(condition.toMongoVue());
                vue.append(", ");
            }
            vue.deleteCharAt(vue.length() - 1);
            vue.deleteCharAt(vue.length() - 1);
            vue.append("]");
        }
        vue.append("}");
        return vue.toString();
    }

    /**
     * @return - возвращает строковое представление исходного условного выражения - в принятом SQL формате
     */
    public String toSqlVue(){
        final StringBuilder vue = new StringBuilder("(");
        if(type == ConditionType.SINGLE){
            vue.append(left)
                    .append(" ")
                    .append(statement.toSqlVue())
                    .append(" ")
                    .append(right);
        } else {
            vue.append(conditionList.get(0).toSqlVue());
            for(int i = 1; i < conditionList.size(); i++){
                vue.append(" ")
                        .append(type)
                        .append(" ")
                        .append(conditionList.get(i).toSqlVue());
            }
        }
        vue.append(")");
        return vue.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Condition)) return false;

        Condition that = (Condition) o;

        if (getType() != that.getType()) return false;
        if (!Objects.equals(left, that.left)) return false;
        if (statement != that.statement) return false;
        if (!Objects.equals(right, that.right)) return false;

        return this.compareTo(that) == 0;
    }

    @Override
    public int hashCode() {
        int result = getType() != null ? getType().hashCode() : 0;
        List<Condition> conditions = getConditionList();
        conditions.sort(Condition::compareTo);
        result = 31 * result + (getConditionList() != null ? conditions.hashCode() : 0);
        result = 31 * result + (statement != null ? statement.hashCode() : 0);
        result = 31 * result + (left != null ? left.hashCode() : 0);
        result = 31 * result + (right != null ? right.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Object o) {
        if (this == o) return 1;
        if (!(o instanceof Condition)) return 1;

        Condition that = (Condition) o;

        if(type.compareTo(that.type) != 0){
            return type.compareTo(that.type);
        }

        if(conditionList.size() < that.conditionList.size()){
            return -1;
        } else if(conditionList.size() > that.conditionList.size()){
            return 1;
        } else {
            final List<Condition> thisConditionsList = new ArrayList<>(conditionList);
            final List<Condition> thatConditionsList = new ArrayList<>(that.conditionList);
            thisConditionsList.sort(Condition::compareTo);
            thatConditionsList.sort(Condition::compareTo);
            for(int i = 0; i < thisConditionsList.size(); i++){
                int comparisionResult = thisConditionsList
                        .get(i)
                        .compareTo(thatConditionsList.get(i));
                if(comparisionResult != 0){
                    return comparisionResult;
                }
            }
        }
        return 0;
    }
}