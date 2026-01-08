package test.servlets;

import test.api.Rooms;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;

@WebServlet("/api/rooms/get")
public class RoomGetServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int id = Integer.parseInt(idParam);
        Rooms dao = new Rooms();
        Map<String, Object> room = dao.getRoomById(id);

        if (room != null) {
            // Manually building JSON for the single object
            StringBuilder json = new StringBuilder("{");
            json.append("\"id\":").append(room.get("id")).append(",")
                .append("\"room_number\":\"").append(room.get("room_number")).append("\",")
                .append("\"room_type\":\"").append(room.get("room_type")).append("\",")
                .append("\"base_price\":").append(room.get("base_price")).append(",")
                .append("\"status\":\"").append(room.get("status")).append("\"")
                .append("}");
            response.getWriter().print(json.toString());
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}