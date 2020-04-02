package azula.condition;

import azula.VueAble;

/**
 * Класс условных выражений
 * Author : Farrukh Karimov
 * Modification Date : 02.04.2020
 */
public abstract class Condition implements VueAble {

    static boolean checkLiteral(final String str){
        for(char e : str.toCharArray()){
            if(!checkLetter(e)){
                return false;
            }
        }
        return true;
    }

    static boolean checkLetter(final char e){
        return (('a' <= e) && (e <= 'z')) || (('A' <= e) && (e <= 'Z'))
                || (('0' <= e) && (e <= '9')) || (e == '_');
    }
}
