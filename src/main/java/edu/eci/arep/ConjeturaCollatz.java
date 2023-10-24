package edu.eci.arep;

import static spark.Spark.*;

import java.util.ArrayList;
import java.util.List;

public class ConjeturaCollatz {
    
    public static void main(String... args){
          port(getPort());
          get("hello", (req,res) -> "Hello Docker!");

          get("collatzsequence", (req,res) -> {
            String response; 
            Boolean validNum = true;
            int collatzFromNum = Integer.parseInt(req.queryParams("value"));
            if (collatzFromNum < 1){
                validNum = false;
            }
            List<Integer> collatzList = calculateCollatz(validNum, collatzFromNum);
            response = formJson(validNum, collatzList);
            
            return response; 
          });
    }

    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
    }

    private static List<Integer> calculateCollatz(Boolean validNum, int collatzFromNum){
        List<Integer> collatzList = new ArrayList<Integer>();
        collatzList.add(collatzFromNum);
        if(validNum){
            while (collatzFromNum != 1){
                if (collatzFromNum % 2 == 0){
                    collatzFromNum = collatzFromNum/2;
                    collatzList.add(collatzFromNum);
                } else {
                    collatzFromNum = 3*collatzFromNum+1;
                    collatzList.add(collatzFromNum);
                }
            }
        }
        
        return collatzList;
    }
    
    private static String formJson(Boolean validNum,List<Integer> list){

        String prettyList;

        if (validNum){
            prettyList = formList(list);
        } else {
            prettyList = "Número Inválido. Ingrese un valor mayor que 0!";
        }


        String message = "{" +
                "\"operation\": \"collatzsequence\"," + 
                "\"input\": \"" + list.get(0).toString() + "\"," +
                "\"output\": \""+ prettyList + "\"" +
                "}";
        return message;
    }

    private static String formList(List<Integer> list){
        String prettyList = "";

        for (int i = 0; i < list.size() -1; i++){
            prettyList += list.get(i) + " -> ";
        }

        prettyList += list.get(list.size() - 1).toString();

        return prettyList;
    
    }
}

// java -cp "target/classes/;target/dependency/*" edu.eci.arep.ConjeturaCollatz
// mvn exec:java

