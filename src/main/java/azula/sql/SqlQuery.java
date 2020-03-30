package azula.sql;

import azula.mongo.MongoQuery;

/**
 * Интерфейс различных sql запросов
 * Author : Farrukh Karimov
 * Modification Date : 30.03.2020
 */
public interface SqlQuery {
    MongoQuery toMongoQuery();
    public String toSqlVue();
}
