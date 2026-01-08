package test.servlets;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import test.api.Booking;

@WebServlet("/api/bookings")
public class BookingListServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Booking dao = new Booking();
        // Use the method from your Booking.java class
        List<Map<String, Object>> results = dao.getAllBookings();

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < results.size(); i++) {
            Map<String, Object> b = results.get(i);

            // We use String.format to ensure quotes are handled correctly
            json.append("{")
                    .append("\"id\":").append(b.get("id")).append(",")
                    .append("\"user_id\":").append(b.get("user_id")).append(",")
                    .append("\"room_number\":\"").append(b.get("room_number")).append("\",")
                    .append("\"check_in\":\"").append(b.get("check_in")).append("\",")
                    .append("\"check_out\":\"").append(b.get("check_out")).append("\",")
                    .append("\"total_price\":\"").append(b.get("total_price")).append("\",")
                    .append("\"status\":\"").append(b.get("status")).append("\"")
                    .append("}");

            if (i < results.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");

        response.getWriter().print(json.toString());
    }
}