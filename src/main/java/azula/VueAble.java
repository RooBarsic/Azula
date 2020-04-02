package azula;

/**
 * Интерфейс представления в mongo и sql форматы
 * Author : Farrukh Karimov
 * Modification Date : 02.04.2020
 */
public interface VueAble {
    String toMongoVue();
    String toSqlVue();
}
