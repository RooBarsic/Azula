package azula.condition;

/**
 * Класс - представляющий типы оперпторов сравнения
 * Author : Farrukh Karimov
 * Modification Date : 30.03.2020
 */
public enum Statement {
    EQUAL{
        @Override
        public String toMongoVue() {
            return "$eq";
        }

        @Override
        public String toSqlVue(){
            return "=";
        }
    },
    NON_EQUAL{
        @Override
        public String toMongoVue(){
            return "$ne";
        }

        @Override
        public String toSqlVue(){
            return "<>";
        }
    },
    GRATER_THAN{
        @Override
        public String toMongoVue(){
            return "$gt";
        }

        @Override
        public String toSqlVue(){
            return ">";
        }
    },
    LESS_THAN{
        @Override
        public String toMongoVue(){
            return "$lt";
        }

        @Override
        public String toSqlVue(){
            return "<";
        }
    };
    public abstract String toMongoVue();
    public abstract String toSqlVue();
}
