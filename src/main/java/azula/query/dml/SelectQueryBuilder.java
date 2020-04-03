package azula.query.dml;

import azula.condition.Condition;
import azula.query.Query;
import azula.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Класс простых условных выражений
 *
 * Author : Farrukh Karimov
 * Modification Date : 04.04.2020
 */
public class SelectQueryBuilder implements QueryBuilder {
    private List<String> columns;
    private String from;
    private Condition whereCondition;
    private Integer limit;
    private Integer skip;

    public SelectQueryBuilder(){
        columns = new ArrayList<>();
    }

    /**
     * Сбрасывает поля в дефолтные значения для собирания нового запроса
     */
    public SelectQueryBuilder newQuery(){
        columns.clear();
        from = null;
        whereCondition = null;
        limit = null;
        skip = null;
        return this;
    }

    /**
     * метод для добавления колонок в результат
     */
    public SelectQueryBuilder column(final String... columns){
        Collections.addAll(this.columns, columns);
        return this;
    }

    /**
     * метод для добавления аргумента from в результат
     */
    public SelectQueryBuilder from(final String from){
        this.from = from;
        return this;
    }

    /**
     * метод для добавления аргумента where в результат
     */
    public SelectQueryBuilder where(final Condition whereCondition){
        this.whereCondition = whereCondition;
        return this;
    }

    /**
     * метод для добавления аргумента limit в результат
     */
    public SelectQueryBuilder limit(final Integer limit){
        this.limit = limit;
        return this;
    }

    /**
     * метод для добавления аргумента skip в результат
     */
    public SelectQueryBuilder skip(final Integer skip){
        this.skip = skip;
        return this;
    }

    /**
     * Собирает новое query из переданных аргументов
     * @return - возвращает null если не передано название таблицы или колонки
     */
    @Override
    public Query perform() {
        if((from == null) || (columns.size() == 0)) {
            return null;
        }
        final String[] columnsArray = columns.toArray(new String[0]);
        return new SelectQuery(from, whereCondition, limit, skip, columnsArray);
    }
}
