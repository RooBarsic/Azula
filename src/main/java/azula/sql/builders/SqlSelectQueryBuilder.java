package azula.sql.builders;

import azula.QueryType;
import azula.condition.Condition;
import azula.sql.SqlQuery;
import azula.sql.SqlSelectQuery;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class SqlSelectQueryBuilder implements QueryBuilder {
    private QueryType type;
    private List<String> columns;
    private String from;
    private Condition whereCondition;
    private Integer limit;
    private Integer skip;

    public SqlSelectQueryBuilder() {
        type = QueryType.SELECT;
        columns = new LinkedList<>();
    }

    public SqlSelectQueryBuilder column(final String column) {
        columns.add(column);
        return this;
    }

    public SqlSelectQueryBuilder column(final List<String> columns) {
        this.columns.addAll(columns);
        return this;
    }

    public SqlSelectQueryBuilder from(final String from) {
        this.from = from;
        return this;
    }

    public SqlSelectQueryBuilder where(final Condition whereCondition) {
        this.whereCondition = whereCondition;
        return this;
    }

    public SqlSelectQueryBuilder limit(final Integer limit) {
        this.limit = limit;
        return this;
    }

    public SqlSelectQueryBuilder skip(final Integer skip) {
        this.skip = skip;
        return this;
    }

    @Override
    public SqlQuery perform() {
        return new SqlSelectQuery(type, from, whereCondition, limit, skip, columns);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SqlSelectQueryBuilder)) return false;

        SqlSelectQueryBuilder that = (SqlSelectQueryBuilder) o;

        if (type != that.type) return false;
        if (!Objects.equals(new HashSet<>(columns), new HashSet<>(that.columns))) return false;
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