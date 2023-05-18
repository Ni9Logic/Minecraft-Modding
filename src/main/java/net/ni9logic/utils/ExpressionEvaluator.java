package net.ni9logic.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ExpressionEvaluator {

    public static double evaluateExpression(String expr) {
        try {
            String encodedExpression = URLEncoder.encode(expr, StandardCharsets.UTF_8);
            String url = "https://chart.googleapis.com/chart?cht=tx&chl=" + encodedExpression;
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Extract the evaluated result from the alt text of the image
                String altText = response.toString();
                int startIndex = altText.indexOf("alt=\"") + 5;
                int endIndex = altText.indexOf("\"", startIndex);
                String resultString = altText.substring(startIndex, endIndex);
                return Double.parseDouble(resultString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Double.NaN; // Return NaN if the evaluation fails
    }
}
