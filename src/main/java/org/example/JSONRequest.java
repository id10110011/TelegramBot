package org.example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONRequest {
    public String getJSON(final String address) {
        StringBuilder response = new StringBuilder("");
        try {
            URL url = new URL("https://geonet.shodan.io/api/geoping/" + address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(1500);
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (FileNotFoundException e) {
            return e.getMessage();
        } catch (Exception e) {
            return e.getMessage();
        }

        return response.toString();
    }
}
