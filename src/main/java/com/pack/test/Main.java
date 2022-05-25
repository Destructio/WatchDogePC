package com.pack.test;

import com.google.gson.Gson;
import com.squareup.okhttp.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;


public class Main {
    private static Scanner inputConsole = new Scanner(System.in);

    private static String webServiceIP = "localhost:8080";

    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    private static OkHttpClient httpClient = new OkHttpClient();
    private static String computerName = new Computer().getComputerName();
    private static Computer computer = new Computer();

    private static String inputEmail;
    private static String inputPassword;

    public static void main(String[] args){

        System.out.print("Введите email : ");
        String email = inputConsole.next();
        System.out.print("\nВведите пароль : ");
        String password = inputConsole.next();
        System.out.print("Инициализация");
        if(isValidAcc(email,password)){
            System.out.println(".");
            inputEmail = email;
            inputPassword = password;

            start();
        }
        else System.out.println("Email или пароль неверен.");

        System.out.println("Завершение работы.");
    }

    private static void start(){

        String resultCode = sendFirstJSON(httpClient,inputEmail);

        switch (resultCode) {
            case "1":
                System.out.println("Отправка первичных сведений завершениа успешно.");
                loop();
                break;
            case "DUPLICATE":
                loop();
                break;
            default:
                System.out.println("Что то пошло не так. " + resultCode);
                break;
        }

    }

    private static boolean isValidAcc(String email, String password) {
        Boolean valid = false;

        String url = "http://" + webServiceIP + "/login?email=" + email  + "&pass=" + password;

        Request request = new Request.Builder()
                .url(url)
                .build();

        System.out.print(".");
        try {

            Response response = httpClient.newCall(request).execute();
            String code = response.body().string();
            if(code.equals("true")) valid = true;
            System.out.print(".");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return valid;
    }

    private static void loop() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        boolean needStop = false;

        while(!needStop){
            System.out.println("Отправка статистики начата.");

            String resultCodeL = sendJSON();

            if(resultCodeL.equals("1"))
            {
                System.out.println("Статистика за " + sdf.format(new Date()) + " была успешно доставлена.");
                try { Thread.sleep(60 * 1000);
                } catch (InterruptedException e) {
                    System.err.println(e.getLocalizedMessage());
                    needStop = true;
                }
            }
            else
            {
                System.out.println("Ошибка отправления!");
                needStop = true;
            }

        }

    }

    private static String sendJSON() {
        Gson gson = new Gson();

        String resultCodeL = "NULL";

        String url3 = "http://" + webServiceIP + "/postClientJsonL?computer=" + computerName  + "&email=" + inputEmail;

        RequestBody body2 = RequestBody.create(MEDIA_TYPE_JSON, gson.toJson(new ComputerStatistic()));

        Request requestJsonL = new Request.Builder()
                .url(url3)
                .post(body2)
                .build();
        try {
            Response response = httpClient.newCall(requestJsonL).execute();
            resultCodeL = response.body().string();

        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        }

        return resultCodeL;

    }

    private static String sendFirstJSON(OkHttpClient httpClient, String email) {

        String resultCode = "NULL";

        Gson gson = new Gson();

        String url = "http://" + webServiceIP + "/postClientJson?computer=" + computerName+ "&email=" + email;

        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, gson.toJson(computer));

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            Response response = httpClient.newCall(request).execute();
            resultCode = response.body().string();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return resultCode;
    }


}