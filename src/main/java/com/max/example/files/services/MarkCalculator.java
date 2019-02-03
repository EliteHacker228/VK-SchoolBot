package com.max.example.files.services;

import java.util.ArrayList;
import java.util.Scanner;

public class MarkCalculator {
    private String marks;

   public MarkCalculator(String marks){
       this.marks=marks;
    }

    public ArrayList<String> workMethod(){
        String[] digits = marks.split("");

        ArrayList<String> markList = new ArrayList<>();

        double sum=0;
        for(String digit: digits){
            sum+=Double.parseDouble(digit);
        }
        double middleScore=sum/digits.length;
        System.out.println("Ваш средний бал: " + middleScore);

        if(middleScore>=4.0 && middleScore<4.5){

            markList.add(howMuchFives(middleScore, sum, digits.length, 4.5));
            markList.add(howMuchFours(middleScore, sum, digits.length, 3.5));

        }

        if(middleScore>=3.0 && middleScore<4.0){
            markList.add(howMuchFives(middleScore, sum, digits.length, 4.0));
            markList.add(howMuchFours(middleScore, sum, digits.length, 3.5));
        }

        if(middleScore>=2.0 && middleScore<3.0){
            int count=digits.length;
            int val=0;
            while(middleScore<3.0){
                sum+=5;
                count++;
                val++;
                middleScore=sum/count;
            }
            System.out.println("Вам нужно получить ещё "+val
                    +" оценок \"5\", чтобы ваш средний бал стал " + middleScore);

            markList.add(howMuchFives(middleScore, sum, digits.length, 3.0));
            markList.add(howMuchFours(middleScore, sum, digits.length, 3.0));
        }

        return markList;
    }

    public static String howMuchFives(double middleScore, double sum, int count, double expectableResult){
        int val=0;
        while(middleScore<expectableResult){
            sum+=5;
            count++;
            val++;
            middleScore=sum/count;
        }
       return "Вам нужно получить ещё "+val
                +" оценок \"5\", чтобы ваш средний бал стал " + middleScore;
    }

    public static String howMuchFours(double middleScore, double sum, int count, double expectableResult){
        int val=0;
        while(middleScore<expectableResult){
            sum+=4;
            count++;
            val++;
            middleScore=sum/count;
        }

        return "Вам нужно получить ещё "+val
                +" оценок \"4\", чтобы ваш средний бал стал " + middleScore;
    }
}
