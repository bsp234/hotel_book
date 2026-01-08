package test.servlets;

import test.api.Booking;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/api/bookings/search")
public class BookingSearchServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String roomNum = request.getParameter("room_number");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Booking dao = new Booking();
        List<Map<String, Object>> results = dao.searchByRoomNumber(roomNum);

        // Build JSON manually
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < results.size(); i++) {
            Map<String, Object> b = results.get(i);
            json.append(String.format(
                    "{\"id\":%d, \"user_id\":%d, \"room_number\":\"%s\", \"guests\":%d, \"check_in\":\"%s\", \"check_out\":\"%s\", \"status\":\"%s\", \"total_price\":\"%s\", \"special_request\":\"%s\"}",
                    b.get("id"), b.get("user_id"), b.get("room_number"), b.get("guests"),
                    b.get("check_in"), b.get("check_out"), b.get("status"), b.get("total_price"),
                    b.get("special_request")));
            if (i < results.size() - 1)
                json.append(",");
        }
        json.append("]");
        response.getWriter().print(json.toString());
    }
}