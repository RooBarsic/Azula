package azula.sql;

import azula.QueryType;
import azula.condition.Condition;
import azula.sql.builders.SqlSelectQueryBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Класс для парсинга различных sql запросов
 * Author : Farrukh Karimov
 * Modification Date : 30.03.2020
 */
public class SqlQueryParser {
    private boolean queryStatus;
    public SqlQuery parseQuery(final String query){
        QueryType queryType = identifyQueryType(query);
        switch (queryType){
            case SELECT :
                return selectParser(query.split(" ", 2)[1]);
            default:
                return null;
        }
    }

    private SqlQuery selectParser(final String stringQuery){
        List<List<String>> keyWords = Arrays.asList(
                Collections.singletonList("SELECT"),
                Collections.singletonList("FROM"),
                Collections.singletonList("WHERE"),
                Arrays.asList("SKIP", "LIMIT")
        );

        SqlSelectQueryBuilder queryBuilder = new SqlSelectQueryBuilder();
        String[] buffer = stringQuery.split(" ", 2);
        StringBuilder stringBuilder = new StringBuilder();
        while ((buffer[0].length() + buffer[1].length() > 0) && (!buffer[0].equals("FROM"))){
            stringBuilder.delete(0, stringBuilder.length());
            stringBuilder.append(buffer[0]);
            if(stringBuilder.charAt(stringBuilder.length() - 1) == ','){
                stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
            }

            queryBuilder.column(stringBuilder.toString());
            buffer = buffer[1].split(" ", 2);
        }

        if((buffer[0].length() + buffer[1].length() == 0) || (!buffer[0].equals("FROM"))){
            return null;
        }
        buffer = buffer[1].split(" ", 2);
        queryBuilder.from(buffer[0]);
        //TODO: implement where parser

        if(buffer.length == 2) {
            buffer = buffer[1].split(" ", 2);
            if ((buffer[0].length() + buffer[1].length() > 0) && (buffer[0].equals("LIMIT"))) {
                buffer = buffer[1].split(" ", 2);
                queryBuilder.limit(Integer.parseInt(buffer[0]));

                if (buffer.length == 2) {
                    buffer = buffer[1].split(" ", 2);
                    if ((buffer[0].length() + buffer[1].length() > 0) && (buffer[0].equals("SKIP"))) {
                        buffer = buffer[1].split(" ", 2);
                        queryBuilder.skip(Integer.parseInt(buffer[0]));
                    }
                }
            } else if ((buffer[0].length() + buffer[1].length() > 0) && (buffer[0].equals("SKIP"))) {
                buffer = buffer[1].split(" ", 2);
                queryBuilder.skip(Integer.parseInt(buffer[0]));

                if (buffer.length == 2) {
                    buffer = buffer[1].split(" ", 2);
                    if ((buffer[0].length() + buffer[1].length() > 0) && (buffer[0].equals("LIMIT"))) {
                        buffer = buffer[1].split(" ", 2);
                        queryBuilder.limit(Integer.parseInt(buffer[0]));
                    }
                }
            }
        }

        return queryBuilder.perform();
    }

    private QueryType identifyQueryType(final String query){
        String stringQueryType = query.split(" ", 2)[0];
        return QueryType.valueOf(stringQueryType);
    }

    public SqlQuery parseQuery2(final String stringQuery){
        List<String> keysAndValues = parseQueryKeyAndValues(stringQuery);
        SqlSelectQueryBuilder selectQueryBuilder = new SqlSelectQueryBuilder();
        for(int i = 0; i < keysAndValues.size(); i += 2){
            final String keyWord = keysAndValues.get(i).trim();
            final String value = clearSpaces(keysAndValues.get(i + 1)).trim();
            switch (keyWord){
                case "SELECT" :
                    selectQueryBuilder.column(
                            parseColumns(value)
                    );
                    break;
                case "FROM" :
                    selectQueryBuilder.from(value);
                    break;
                case "WHERE" :
                    selectQueryBuilder.where(
                            Condition.parseCondition(value)
                    );
                    break;
                case "LIMIT" :
                    selectQueryBuilder.limit(
                            Integer.parseInt(value)
                    );
                    break;
                case "SKIP" :
                    selectQueryBuilder.skip(
                            Integer.parseInt(value)
                    );
                    break;
            }
        }
        return selectQueryBuilder.perform();
    }

    private String clearSpaces(final String string){
        final StringBuilder stringBuilder = new StringBuilder(string);
        while ((stringBuilder.length() > 0) && (stringBuilder.charAt(0) == ' ')){
            stringBuilder.deleteCharAt(0);
        }
        while ((stringBuilder.length() > 0) && (stringBuilder.charAt(stringBuilder.length() - 1) == ' ')){
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

    private List<String> parseQueryKeyAndValues(final String stringQuery){
        List<String> keysAndValuesList = new ArrayList<>();
        String[] buffer;
        keysAndValuesList.add("SELECT ");
        keysAndValuesList.add(stringQuery.split("SELECT ", 2)[1]);

        buffer = keysAndValuesList.get(keysAndValuesList.size() - 1).split(" FROM ", 2);
        if(buffer.length == 2){
            keysAndValuesList.set(keysAndValuesList.size() - 1, buffer[0]);
            keysAndValuesList.add(" FROM ");
            keysAndValuesList.add(buffer[1]);
        }

        buffer = keysAndValuesList.get(keysAndValuesList.size() - 1).split(" WHERE ", 2);
        if(buffer.length == 2){
            keysAndValuesList.set(keysAndValuesList.size() - 1, buffer[0]);
            keysAndValuesList.add(" WHERE ");
            keysAndValuesList.add(buffer[1]);
        }

        buffer = keysAndValuesList.get(keysAndValuesList.size() - 1).split(" LIMIT ", 2);
        if(buffer.length == 2){
            String[] buffer2 = buffer[0].split(" SKIP ", 2);
            if(buffer2.length == 2){
                keysAndValuesList.set(keysAndValuesList.size() - 1, buffer2[0]);
                keysAndValuesList.add(" SKIP ");
                keysAndValuesList.add(buffer2[1]);
            } else {
                keysAndValuesList.set(keysAndValuesList.size() - 1, buffer[0]);
            }
            keysAndValuesList.add(" LIMIT ");
            keysAndValuesList.add(buffer[1]);
        }

        buffer = keysAndValuesList.get(keysAndValuesList.size() - 1).split(" SKIP ", 2);
        if(buffer.length == 2){
            keysAndValuesList.set(keysAndValuesList.size() - 1, buffer[0]);
            keysAndValuesList.add(" SKIP ");
            keysAndValuesList.add(buffer[1]);
        }

        keysAndValuesList.set(
                keysAndValuesList
                        .size() - 1,
                keysAndValuesList
                        .get(keysAndValuesList.size() - 1)
                        .split(":", 2)[0]
        );
        return keysAndValuesList;
    }

    private List<String> parseColumns(final String columnsStr){
        List<String> columnsList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        for(char e : columnsStr.toCharArray()){
            if((('a' <= e) && (e <= 'z')) || (('A' <= e) && (e <= 'Z')) ||
                    (('0' <= e) && (e <= '9')) || (e == '_') || (e == '*')){
                stringBuilder.append(e);
            } else if(e == ','){
                if(stringBuilder.length() > 0){
                    columnsList.add(stringBuilder.toString());
                    stringBuilder.delete(0, stringBuilder.length());
                }
            } else if(e != ' '){
                queryStatus = false;
                return columnsList;
            }
        }
        if(stringBuilder.length() > 0){
            columnsList.add(stringBuilder.toString());
        }
        return columnsList;
    }
}
