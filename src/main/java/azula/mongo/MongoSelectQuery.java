package azula.mongo;

import azula.QueryType;
import azula.condition.Condition;

import java.util.List;
import java.util.Objects;

/**
 * Класс имплементирующий MongoDB select запросы
 * Author : Farrukh Karimov
 * Modification Date : 30.03.2020
 */
public class MongoSelectQuery implements MongoQuery{
    private final QueryType type;
    private final List<String> columns;
    private final String from;
    private final Condition whereCondition;
    private final Integer limit;
    private final Integer skip;

    public MongoSelectQuery(final String from, final Condition whereCondition, final Integer limit, final Integer skip, final List<String> columns){
        type = QueryType.SELECT;
        this.columns = columns;
        this.from = from;
        this.whereCondition = whereCondition;
        this.limit = limit;
        this.skip = skip;
    }

    @Override
    public String toVue(){
        StringBuilder query = new StringBuilder("db." + from + ".find(");

        if(whereCondition != null) {
            query.append(whereCondition.toMongoVue());
        } else {
            query.append("{}");
        }

        if(columns.size() > 0) {
            query.append(", {");
            int printedColumnsNum = 0;
            for (String column : columns) {
                if(!column.equals("_id")){
                    printedColumnsNum++;
                    if(printedColumnsNum > 1){
                        query.append(", ");
                    }
                    query.append(column).append(": 1");
                }
            }
            if(!this.columns.contains("_id")){
                query.append(", _id: 0");
            }
            query.append("}");
        }

        query.append(")");
        if(skip != null){
            query.append(".skip(").append(skip).append(")");
        }
        if(limit != null){
            query.append(".limit(").append(limit).append(")");
        }
        return query.toString();
    }

    @Override
    public String toString() {
        return "MongoSelectQuery{" +
                "type=" + type +
                ", columns=" + columns +
                ", from='" + from + '\'' +
                ", whereCondition='" + whereCondition + '\'' +
                ", limit=" + limit +
                ", skip=" + skip +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MongoSelectQuery)) return false;

        MongoSelectQuery that = (MongoSelectQuery) o;

        if (type != that.type) return false;
        if (!Objects.equals(columns, that.columns)) return false;
        if (!Objects.equals(from, that.from)) return false;
        if (!Objects.equals(whereCondition, that.whereCondition))
            return false;
        if (!Objects.equals(limit, that.limit)) return false;
        return Objects.equals(skip, that.skip);
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (columns != null ? columns.hashCode() : 0);
        result = 31 * result + (from != null ? from.hashCode() : 0);
        result = 31 * result + (whereCondition != null ? whereCondition.hashCode() : 0);
        result = 31 * result + (limit != null ? limit.hashCode() : 0);
        result = 31 * result + (skip != null ? skip.hashCode() : 0);
        return result;
    }
}
