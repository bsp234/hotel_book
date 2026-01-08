package test.servlets;

import test.api.Rooms;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/api/rooms/update")
public class RoomUpdateServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1. Read the raw JSON string from the request body
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        String json = sb.toString();

        // 2. Manually extract values (Basic parsing logic)
        try {
            int id = Integer.parseInt(extractValue(json, "id").replaceAll("[^0-9]", ""));
            String room_number = extractValue(json, "room_number");
            String room_type = extractValue(json, "room_type");
            double base_price = Double.parseDouble(extractValue(json, "base_price").replaceAll("[^0-9.]", ""));
            String status = extractValue(json, "status");

            // 3. Call DAO
            Rooms dao = new Rooms();
            boolean success = dao.updateRoom(id, room_number, room_type, base_price, status);

            if (success) {
                response.setStatus(200);
            } else {
                response.sendError(500, "Database update failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(400, "Invalid JSON data");
        }
    }

    // Helper method to grab the value after a specific key in JSON
    private String extractValue(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int start = json.indexOf(searchKey) + searchKey.length();
        int end = json.indexOf(",", start);
        if (end == -1)
            end = json.indexOf("}", start);

        String value = json.substring(start, end).trim();
        return value.replace("\"", ""); // Remove quotes around strings
    }
}