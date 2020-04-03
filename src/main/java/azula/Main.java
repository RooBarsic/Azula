package azula;

import azula.query.Query;
import azula.query.dml.SqlSelectQueryParser;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello Azula");
        try(Scanner scanner = new Scanner(System.in)){
            SqlSelectQueryParser sqlQueryParser = new SqlSelectQueryParser();
            while(scanner.hasNext()){
                String stringQuery = scanner.nextLine();
                stringQuery = stringQuery.trim();
                Query query = sqlQueryParser.parseQuery(stringQuery);
                System.out.println(query.toMongoVue() + "\n");
            }
        }

    }
}
