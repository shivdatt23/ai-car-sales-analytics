package com.shivai.carsales.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AIQueryServiceImpl implements AIQueryService{

    public ChatClient chatClient;

    public JdbcTemplate jdbcTemplate;

    public AIQueryServiceImpl(ChatClient.Builder builder,JdbcTemplate jdbcTemplate) {
        this.chatClient = builder.build();
        this.jdbcTemplate=jdbcTemplate;
    }

    @Override
    public String process(String question) {


        String sql=generateSQL(question);
        //tocheck
        System.out.println(sql);

        if(sql.equalsIgnoreCase("INVALID")){
            return "only table-related question allowed";

        }
        if (!isSafe(sql)){
            return "X UNSAFE QUERY";
        }

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);

        if(result.isEmpty()){
            return "NO DATA FOUND";
        }
        System.out.println(result);

        return toNaturalLanguage(question,result);
    }

    private boolean isSafe(String sql){
        String lower =sql.toLowerCase();

        return lower.startsWith("select")
                &&!lower.contains("drop")
                &&!lower.contains("delete")
                &&!lower.contains("update")
                &&!lower.contains("insert");


    }




    private String generateSQL(String question){

        String prompt= """
                You are a SQL Generator
                Tables :car_sales
                Columns: id, brand, car_number, city, color, contact_number, customer_name, date_of_purchase, email, engine, fuel_type, mileage, model, apyment_mode, price, state, time_of_purchase, warranty_period, year
                
                Rules :
                -Only SELECT queries
                -Use only given columns
                -If not related, return : INVALID
                - Return only SQL
                
                """+question;
        return chatClient.prompt().user(prompt).call().content().trim();
    }


    private String toNaturalLanguage(String question,List<Map<String , Object>> result){

        String prompt= """
                Convert database result into a human readable answer.
                
                User Question:
                """+question+ """
                
                DB Result:
                """+result.toString()+ """
                
                Rules:
                -Answer clearly (don't write too much)
                -Do not show JSON
                -Do not explain SQL
                """;

        return chatClient.prompt().user(prompt).call().content().trim();
    }





}
