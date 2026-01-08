package test.servlets;

import test.api.Rooms;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/api/rooms/create")
public class RoomCreateServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1. Read raw JSON body
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        String json = sb.toString();

        try {
            // 2. Extract values manually using our helper logic
            String room_number = extractValue(json, "room_number");
            String room_type = extractValue(json, "room_type");
            double base_price = Double.parseDouble(extractValue(json, "base_price").replaceAll("[^0-9.]", ""));
            String status = extractValue(json, "status");

            // 3. Save to Database
            Rooms dao = new Rooms();
            boolean success = dao.createRoom(room_number, room_type, base_price, status);

            if (success) {
                response.setStatus(201); // Created
                response.getWriter().write("{\"message\":\"Room created\"}");
            } else {
                response.sendError(500, "Database error");
            }
        } catch (Exception e) {
            response.sendError(400, "Invalid Data");
        }
    }

    private String extractValue(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int start = json.indexOf(searchKey) + searchKey.length();
        int end = json.indexOf(",", start);
        if (end == -1)
            end = json.indexOf("}", start);

        String value = json.substring(start, end).trim();
        return value.replace("\"", ""); // Clean quotes
    }
}