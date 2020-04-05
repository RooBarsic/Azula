package azula;

import azula.query.Query;
import azula.query.dml.SqlSelectQueryParser;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try(Scanner scanner = new Scanner(System.in)){
            SqlSelectQueryParser sqlQueryParser = new SqlSelectQueryParser();
            while(scanner.hasNext()){
                String stringQuery = scanner.nextLine();
                stringQuery = stringQuery.trim();
                Query query = sqlQueryParser.parseQuery(stringQuery);
                if(query == null){
                    System.out.println("Не смог перевести это SQl выражение. Исправьте пожалуйста формат.");
                } else {
                    System.out.println(query.toMongoVue() + "\n");
                }
            }
        }

    }
}

  //  SELECT BOOK.DIST FROM AAA WHERE BOOK.AGE > 35
//  SELECT BOOK.DIST FROM AAA WHERE BOOK.AGE > 35 AND price < 60
//  SELECT BOOK.DIST FROM AAA WHERE (BOOK.AGE > "35")
