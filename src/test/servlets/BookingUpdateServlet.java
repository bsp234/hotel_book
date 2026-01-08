package test.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import test.api.Booking;

@WebServlet("/api/bookings/update")
public class BookingUpdateServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1. Get parameters from the URL
        Booking dao = new Booking();

        String roomNum = request.getParameter("room_number");

        int roomId = dao.getRoomIdByNumber(roomNum);

        int id = Integer.parseInt(request.getParameter("id"));

        String checkIn = request.getParameter("check_in");
        String checkOut = request.getParameter("check_out");
        double price = Double.parseDouble(request.getParameter("total_price").replace("$", "").trim());
        String status = request.getParameter("status");

        // 2. Call your Booking DAO

        // Note: Your DAO updateBooking method might need room_id (int)
        // If you only have room_number, ensure your SQL update uses that.
        boolean success = dao.updateBooking(id, roomId, checkIn, checkOut, price, status);

        // 3. Respond back to JavaScript
        if (success) {
            response.setStatus(200);
            response.getWriter().print("Success");
        } else {
            response.sendError(500, "Database update failed");
        }
    }
}