package azula.sql;

import azula.QueryType;
import azula.condition.Condition;
import azula.mongo.MongoQuery;
import azula.mongo.MongoSelectQuery;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс имлементирующий sql select запросы
 * Author : Farrukh Karimov
 * Modification Date : 30.03.2020
 */
public class SqlSelectQuery implements SqlQuery {
    @NotNull
    private final QueryType type;
    @NotNull
    private final List<String> columns;
    @NotNull
    private final String from;
    private final Condition whereCondition;
    private final Integer limit;
    private final Integer skip;

    public SqlSelectQuery(@NotNull final QueryType type, @NotNull final String from, final Condition whereCondition, final Integer limit, final Integer skip, @NotNull final List<String> columns){
        this.type = type;
        this.columns = columns;
        this.from = from;
        this.whereCondition = whereCondition;
        this.limit = limit;
        this.skip = skip;
    }

    public QueryType getType() {
        return type;
    }

    @Override
    public MongoQuery toMongoQuery() {
        List<String> columns = new ArrayList<>(this.columns);
        if((columns.size() == 1) && (columns.get(0).equals("*"))) {
            columns.clear();
        }
        return new MongoSelectQuery(
                from,
                whereCondition,
                limit,
                skip,
                columns
        );
    }

    @Override
    public String toSqlVue() {
        final StringBuilder vue = new StringBuilder();
        vue.append(type).append(" ");
        for(String column : columns){
            vue.append(column).append(", ");
        }
        vue.deleteCharAt(vue.length() - 1);
        vue.deleteCharAt(vue.length() - 1);
        vue.append(" FROM ").append(from);
        if(whereCondition != null){
            vue.append(" WHERE ").append(whereCondition.toSqlVue());
        }
        if(limit != null){
            vue.append(" LIMIT ").append(limit);
        }
        if(skip != null){
            vue.append(" SKIP ").append(skip);
        }
        return vue.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SqlSelectQuery)) return false;

        SqlSelectQuery that = (SqlSelectQuery) o;

        if (getType() != that.getType()) return false;
        if (columns != null ? !columns.equals(that.columns) : that.columns != null) return false;
        if (from != null ? !from.equals(that.from) : that.from != null) return false;
        if (whereCondition != null ? !whereCondition.equals(that.whereCondition) : that.whereCondition != null)
            return false;
        if (limit != null ? !limit.equals(that.limit) : that.limit != null) return false;
        return skip != null ? skip.equals(that.skip) : that.skip == null;
    }

    @Override
    public int hashCode() {
        int result = getType() != null ? getType().hashCode() : 0;
        result = 31 * result + (columns != null ? columns.hashCode() : 0);
        result = 31 * result + (from != null ? from.hashCode() : 0);
        result = 31 * result + (whereCondition != null ? whereCondition.hashCode() : 0);
        result = 31 * result + (limit != null ? limit.hashCode() : 0);
        result = 31 * result + (skip != null ? skip.hashCode() : 0);
        return result;
    }
}
