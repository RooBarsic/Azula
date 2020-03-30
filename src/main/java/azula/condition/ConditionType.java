package azula.condition;

/**
 * Класс - представляющий типы условных выражений
 * Author : Farrukh Karimov
 * Modification Date : 30.03.2020
 */
public enum ConditionType {
    OR {
        @Override
        public String toMongoVue() {
            return "$or";
        }
    },
    AND {
        @Override
        public String toMongoVue() {
            return "$and";
        }
    },
    SINGLE {     // Простое ( единичное ) сравнение
        @Override
        public String toMongoVue() {
            return "";
        }
    },
    EMPTY {      // Пустое выражение
        @Override
        public String toMongoVue() {
            return "";
        }
    };

    public abstract String toMongoVue();
    public static boolean contains(String val){
        val = val.trim().toUpperCase();
        return (val.equals("OR")) || (val.equals("AND"));
    }
}
