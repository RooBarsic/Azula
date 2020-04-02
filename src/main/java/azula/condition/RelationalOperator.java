package azula.condition;

import azula.VueAble;

/**
 * Enum класс операторов сравнения
 * Author : Farrukh Karimov
 * Modification Date : 02.04.2020
 */
public enum RelationalOperator implements VueAble {
    NOT_EQUAL{
        public String toMongoVue() {
            return "ne";
        }

        public String toSqlVue() {
            return "<>";
        }
    },
    EQUAL{
        public String toMongoVue() {
            return "eq";
        }

        public String toSqlVue() {
            return "=";
        }
    },
    LESS_THAN{
        public String toMongoVue() {
            return "lt";
        }

        public String toSqlVue() {
            return "<";
        }
    },
    GRATER_THAN{
        public String toMongoVue() {
            return "gt";
        }

        public String toSqlVue() {
            return ">";
        }
    }
}
