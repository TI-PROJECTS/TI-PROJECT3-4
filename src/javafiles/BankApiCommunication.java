
package javafiles;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

// binnen deze class gebeurt de communicatie met de api


public class BankApiCommunication {
    private String fromCtry;
    private String fromBank;

    private Gson gsonPretty = new GsonBuilder()
    .setPrettyPrinting()
    .create();

    private Gson gson = new Gson();
    
    public BankApiCommunication(String fromCtry, String fromBank){
        this.fromBank = fromBank;
        this.fromCtry = fromCtry;
    }

    // controleerd of het rekeningnummer van onze bank is en returnt true of false
    private boolean checkIfLocalAccount(String acctNo){
        int offset = Math.min(9, acctNo.length());
        String text = acctNo.substring(0, offset);
        return text.equals("LUX01BANK");
    }

    public boolean checkIfError(String inpuString){
        int offset = Math.min(3, inpuString.length());
        String text = inpuString.substring(0, offset);
        return text.equals("LU: ");
    }

    // true goedgekeurd, // false niet goedgekeurd
    Double getBalanceFromJsonString(String apiResponse) {
        try {
            JsonObject jsonObject = gson.fromJson(apiResponse, JsonObject.class);
            return jsonObject.getAsJsonObject("body").get("balance").getAsDouble();

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

    }

    Double getBalanceFromJson(String apiResponse) {
        try {

            JsonObject jsonObject = gson.fromJson(apiResponse, JsonObject.class);
            if (jsonObject.getAsJsonObject("body").get("succes").getAsBoolean() != true){
                return null;
            } else {
                return jsonObject.getAsJsonObject("body").get("balance").getAsDouble();
            }
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

    }

    private String createJsonPacket(String toCtry, String toBank, String acctNo, String pin, int amount){
        JsonObject payload = new JsonObject();
        JsonObject head = new JsonObject();
        JsonObject body = new JsonObject();

        head.addProperty("fromCtry", fromCtry);
        head.addProperty("fromBank", fromBank);
        head.addProperty("toCtry", toCtry);
        head.addProperty("toBank", toBank);

        body.addProperty("acctNo", acctNo);
        body.addProperty("pin", pin);
        if (amount != 0){
            body.addProperty("amount", amount);

        }
        payload.add("head", head);
        payload.add("body", body);

        return gsonPretty.toJson(payload);
    }

    public String postApiRequest(String toCtry, String toBank, String acctNo, String pin, int amount){
        String jsonRequestString = createJsonPacket(toCtry, toBank, acctNo, pin, amount);
        String jsonAnswer = "";
        URL url;
        HttpURLConnection conn;
        try {
            if (checkIfLocalAccount(acctNo) && amount == 0){
                url = new URL("http://localhost:8443/balance");
            } else if (checkIfLocalAccount(acctNo) && amount != 0){
                url = new URL("http://localhost:8443/withdraw");
            } else if (!checkIfLocalAccount(acctNo) && amount == 0){
                url = new URL("http://145.24.222.241:8443/balance");
            } else if (!checkIfLocalAccount(acctNo) && amount != 0) {
                url = new URL("http://145.24.222.241:8443/withdraw");
            } else {
                System.out.println("Could not get the right url");
                return "";
            }
            
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            
            
            // send json payload
            try (DataOutputStream dos = new DataOutputStream(conn.getOutputStream())) {
                dos.writeBytes(jsonRequestString);
            }
            
            if (conn.getResponseCode() >= 400){
                try (BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                    StringBuilder errorMessage = new StringBuilder();
                    String line;
                    while ((line = bf.readLine()) != null) {
                        errorMessage.append(line);
                    }
                    System.out.println("Error Response from API: " + errorMessage.toString());
                    return errorMessage.toString();
                }
            }
            else {
                // Read Response from API
                try (BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    while ((jsonAnswer = bf.readLine()) != null) {
                        System.out.println("jsonAnswer: " + jsonAnswer);
                        return jsonAnswer;
                    }
                    return "";
                    
                }
            }
        } catch (Exception e) {
            System.out.println("error in function postApiRequest");
            System.out.println(e.getMessage());
            return jsonAnswer;
        }
    }
}