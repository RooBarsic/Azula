package azula.query.dml;

import azula.condition.Condition;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Парсер SQL запросов типа SELECT
 *
 * Author : Farrukh Karimov
 * Modification Date : 04.04.2020
 */
public class SqlSelectQueryParser {
    private boolean queryStatus;

    public boolean getQueryStatus(){
        return queryStatus;
    }

    /**
     * Парсит SQL запрос типа SELECT из переданного аргумента.
     * @return - возвращает null если переданный аргумент является неверным выражением.
     */
    public SelectQuery parseQuery(@NotNull final String stringQuery){
        if(stringQuery.trim().equals("")){
            return null;
        }
        final List<String> keysAndValues = parseQueryKeyAndValues(stringQuery);
        final SelectQueryBuilder selectQueryBuilder = new SelectQueryBuilder();

        for(int i = 0; i < keysAndValues.size(); i += 2){
            final String keyWord = keysAndValues.get(i).trim();
            final String value = keysAndValues.get(i + 1).trim();
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

    /**
     * Парсит переданную строку на операторы select-а и их аргументы
     * @param query - строка с SELECT запросом в SQL формате
     * @return - возвращает List, последовательнос содержащий операторы и их аргументы
     * в формате : оператор1, аргумент1, оператор2, аргумент2, ...
     */
    private List<String> parseQueryKeyAndValues(final String query){
        final String stringQuery = query.trim();
        final List<String> keysAndValuesList = new ArrayList<>();
        String[] buffer;
        keysAndValuesList.add("SELECT ");
        keysAndValuesList.add(stringQuery.split("SELECT ", 2)[1]);

        buffer = keysAndValuesList
                .get(keysAndValuesList.size() - 1)
                .split(" FROM ", 2);
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

    /**
     * @param columnsStr - получает на вход строку содержащую название колонок которые будут в результате
     * @return - Возвращает список распарсеных названий колонок
     */
    private String[] parseColumns(final String columnsStr){
        if(columnsStr.trim().equals("*")){
            return new String[]{"*"};
        }
        List<String> columnsList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        for(char e : columnsStr.toCharArray()){
            if((('a' <= e) && (e <= 'z')) || (('A' <= e) && (e <= 'Z')) ||
                    (('0' <= e) && (e <= '9')) || (e == '_') || (e == '.')){
                stringBuilder.append(e);
            } else if(e == ','){
                if(stringBuilder.length() > 0){
                    columnsList.add(stringBuilder.toString());
                    stringBuilder.delete(0, stringBuilder.length());
                }
            } else if(e != ' '){
                queryStatus = false;
                return columnsList.toArray(new String[0]);
            }
        }
        if(stringBuilder.length() > 0){
            columnsList.add(stringBuilder.toString());
        }
        return columnsList.toArray(new String[0]);
    }
}
