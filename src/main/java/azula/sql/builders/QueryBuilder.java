package azula.sql.builders;

import azula.sql.SqlQuery;

public interface QueryBuilder {
    SqlQuery perform();
}
