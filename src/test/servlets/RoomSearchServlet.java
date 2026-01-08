package test.servlets;

import test.api.Rooms;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet("/api/rooms/search")
public class RoomSearchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Set response headers for JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        // 2. Get the room_number parameter from the URL
        String roomNum = request.getParameter("room_number");

        if (roomNum == null || roomNum.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"Room number parameter is missing\"}");
            return;
        }

        // 3. Call the DAO to find the room
        Rooms dao = new Rooms();
        Map<String, Object> room = dao.getRoomByNumber(roomNum);

        // 4. Handle the result
        if (room != null) {
            // Manually construct the JSON object string
            StringBuilder json = new StringBuilder();
            json.append("{")
                    .append("\"id\":").append(room.get("id")).append(",")
                    .append("\"room_number\":\"").append(room.get("room_number")).append("\",")
                    .append("\"room_type\":\"").append(room.get("room_type")).append("\",")
                    .append("\"base_price\":").append(room.get("base_price")).append(",")
                    .append("\"status\":\"").append(room.get("status")).append("\"")
                    .append("}");

            out.print(json.toString());
        } else {
            // Send 404 if the room number doesn't exist
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.print("{\"error\": \"Room not found\"}");
        }
    }
}