package azula.query.dml;

import azula.condition.Condition;
import azula.query.Query;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Класс SQL запросов типа SELECT
 *
 * Author : Farrukh Karimov
 * Modification Date : 04.04.2020
 */
public class SelectQuery implements Query {
    private final DmlTypes DML_TYPE = DmlTypes.SELECT;
    @NotNull
    private final List<String> columns;
    @NotNull
    private final String from;
    private final Condition whereCondition;
    private final Integer limit;
    private final Integer skip;

    SelectQuery(@NotNull final String from, final Condition whereCondition, final Integer limit, final Integer skip, @NotNull final String... columns) {
        this.columns = new ArrayList<>();
        Collections.addAll(this.columns, columns);
        this.from = from;
        this.whereCondition = whereCondition;
        this.limit = limit;
        this.skip = skip;
    }

    @Override
    public String toMongoVue() {
        final StringBuilder stringBuilder = new StringBuilder("db." + from + ".find(");

        if(whereCondition != null) {
            stringBuilder.append(whereCondition.toMongoVue());
        } else {
            stringBuilder.append("{}");
        }

        if((columns.size() > 0) && (!columns.contains("*"))) {
            stringBuilder.append(", {");
            for (final String column : columns) {
                if(!column.equals("_id")){
                    stringBuilder.append(column)
                            .append(": 1")
                            .append(", ");
                }
            }
            if(columns.contains("_id")){
                if(columns.size() == 1){
                    stringBuilder.append("_id: 1, ");
                }
            } else {
                stringBuilder.append("_id: 0, ");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            stringBuilder.append("}");
        }

        stringBuilder.append(")");
        if(skip != null){
            stringBuilder.append(".skip(").append(skip).append(")");
        }
        if(limit != null){
            stringBuilder.append(".limit(").append(limit).append(")");
        }
        return stringBuilder.toString();
    }

    @Override
    public String toSqlVue() {
        final StringBuilder stringBuilder = new StringBuilder("SELECT ");

        for(final String column : columns){
            stringBuilder.append(column).append(", ");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);

        stringBuilder.append(" FROM ").append(from);

        if(whereCondition != null){
            stringBuilder
                    .append(" WHERE ")
                    .append(whereCondition.toSqlVue());
        }
        if(skip != null){
            stringBuilder
                    .append(" SKIP ")
                    .append(skip);
        }
        if(limit != null){
            stringBuilder
                    .append(" LIMIT ")
                    .append(limit);
        }

        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SelectQuery)) return false;

        SelectQuery that = (SelectQuery) o;

        if (DML_TYPE != that.DML_TYPE) return false;
        if (!columns.equals(that.columns)) return false;
        if (!from.equals(that.from)) return false;
        if (!Objects.equals(whereCondition, that.whereCondition))
            return false;
        if (!Objects.equals(limit, that.limit)) return false;
        return Objects.equals(skip, that.skip);
    }

    @Override
    public int hashCode() {
        int result = DML_TYPE.hashCode();
        result = 31 * result + columns.hashCode();
        result = 31 * result + from.hashCode();
        result = 31 * result + (whereCondition != null ? whereCondition.hashCode() : 0);
        result = 31 * result + (limit != null ? limit.hashCode() : 0);
        result = 31 * result + (skip != null ? skip.hashCode() : 0);
        return result;
    }
}
