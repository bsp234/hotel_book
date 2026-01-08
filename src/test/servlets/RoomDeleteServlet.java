package test.servlets;

import test.api.Rooms;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/api/rooms/delete")
public class RoomDeleteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParam = request.getParameter("id");

        if (idParam != null) {
            try {
                int id = Integer.parseInt(idParam);
                Rooms dao = new Rooms();
                boolean success = dao.deleteRoom(id);

                if (success) {
                    response.setStatus(200);
                } else {
                    response.sendError(500, "Could not delete room from database.");
                }
            } catch (NumberFormatException e) {
                response.sendError(400, "Invalid ID format.");
            }
        } else {
            response.sendError(400, "Missing ID parameter.");
        }
    }
}