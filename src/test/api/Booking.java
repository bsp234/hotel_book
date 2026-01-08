package test.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import test.util.DBConnection;

public class Booking {

    public List<Map<String, Object>> getAllBookings() {
        List<Map<String, Object>> bookings = new ArrayList<>();

        // We JOIN rooms for the number AND users for the name
        // b = bookings, r = rooms, u = users
        String sql = "SELECT b.*, r.room_number, u.name AS guest_full_name " +
                "FROM bookings b " +
                "INNER JOIN rooms r ON b.room_id = r.id " +
                "INNER JOIN users u ON b.user_id = u.id";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> book = new HashMap<>();
                book.put("id", rs.getInt("id"));
                book.put("user_id", rs.getInt("user_id"));

                // This gets "biniyam zenebe" from the users table name column
                book.put("guest_name", rs.getString("guest_full_name"));

                book.put("room_number", rs.getString("room_number"));
                book.put("check_in", rs.getString("check_in"));
                book.put("check_out", rs.getString("check_out"));
                book.put("status", rs.getString("status"));
                book.put("total_price", rs.getString("total_price"));
                book.put("special_request", rs.getString("special_request"));

                bookings.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public boolean updateBooking(int id, int room_id, String check_in, String check_out,
            double total_price, String status) {
        // 1. Removed guest_name as it is not in your bookings table
        // 2. Used check_in and check_out to match your getAllBookings logic
        String sql = "UPDATE bookings SET room_id = ?, check_in = ?, check_out = ?, total_price = ?, status = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, room_id);
            ps.setString(2, check_in);
            ps.setString(3, check_out);
            ps.setDouble(4, total_price);
            ps.setString(5, status);
            ps.setInt(6, id);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteBooking(int id) {
        String sql = "DELETE FROM bookings WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Map<String, Object>> searchByRoomNumber(String roomNumber) {
        List<Map<String, Object>> bookings = new ArrayList<>();
        // Join allows us to filter by r.room_number
        String sql = "SELECT b.*, r.room_number " +
                "FROM bookings b " +
                "INNER JOIN rooms r ON b.room_id = r.id " +
                "WHERE r.room_number LIKE ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + roomNumber + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> book = new HashMap<>();
                book.put("id", rs.getInt("id"));
                book.put("user_id", rs.getInt("user_id"));
                book.put("room_number", rs.getString("room_number")); // Friendly room number
                book.put("guests", rs.getInt("guests"));
                book.put("check_in", rs.getString("check_in"));
                book.put("check_out", rs.getString("check_out"));
                book.put("status", rs.getString("status"));
                book.put("total_price", rs.getString("total_price"));
                book.put("special_request", rs.getString("special_request"));
                bookings.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public int getRoomIdByNumber(String roomNumber) {
        String sql = "SELECT id FROM rooms WHERE room_number = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, roomNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // Not found
    }

    public String getGuestNameByUserId(int userId) {
        String sql = "SELECT name FROM users WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Unknown Guest"; // Fallback if not found
    }
}
