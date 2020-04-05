package azula.condition;

import azula.VueAble;

/**
 * Enum класс булевых операторов
 * Author : Farrukh Karimov
 * Modification Date : 02.04.2020
 */
public enum BooleanOperator implements VueAble {
    AND{
        @Override
        public String toMongoVue() {
            return "and";
        }

        @Override
        public String toSqlVue() {
            return "AND";
        }
    },
    OR{
        @Override
        public String toMongoVue() {
            return "or";
        }

        @Override
        public String toSqlVue() {
            return "OR";
        }
    };

    public static boolean contains(final String val){
        if(val.length() == 0){
            return false;
        } else {
            final String fixedVal = val.trim().toUpperCase();
            return (val.equals("OR")) || (val.equals("AND"));
        }
    }

}
