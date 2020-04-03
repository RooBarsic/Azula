package azula.query.dml;

import azula.condition.Condition;
import azula.query.Query;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
}
