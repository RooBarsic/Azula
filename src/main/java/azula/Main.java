package azula;

import azula.mongo.MongoQuery;
import azula.sql.SqlQuery;
import azula.sql.SqlQueryParser;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello Azula");
        try(Scanner scanner = new Scanner(System.in)){
            SqlQueryParser sqlQueryParser = new SqlQueryParser();
            while(scanner.hasNext()){
                String stringQuery = scanner.nextLine();
                stringQuery = stringQuery.trim();
                SqlQuery sqlQuery = sqlQueryParser.parseQuery2(stringQuery);
                MongoQuery mongoQuery = sqlQuery.toMongoQuery();
                System.out.println(mongoQuery.toVue() + "\n");
            }
        }

    }
}
