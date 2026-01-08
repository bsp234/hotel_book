package test.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import test.util.DBConnection;

public class Rooms {

    public List<Map<String, Object>> getAllUsers() {
        List<Map<String, Object>> rooms = new ArrayList<>();
        String sql = "SELECT id, room_number, room_type, base_price, status FROM rooms";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> room = new HashMap<>();
                room.put("id", rs.getInt("id"));
                room.put("room_number", rs.getInt("room_number"));
                room.put("room_type", rs.getString("room_type"));
                room.put("base_price", rs.getDouble("base_price"));
                room.put("status", rs.getString("status"));

                rooms.add(room);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public Map<String, Object> getRoomById(int id) {
        String sql = "SELECT id, room_number, room_type, base_price, status FROM rooms WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> room = new HashMap<>();
                    room.put("id", rs.getInt("id"));
                    room.put("room_number", rs.getString("room_number"));
                    room.put("room_type", rs.getString("room_type"));
                    room.put("base_price", rs.getDouble("base_price"));
                    room.put("status", rs.getString("status"));
                    return room;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateRoom(int id, String room_number, String room_type, double base_price, String status) {
        String sql = "UPDATE rooms SET room_number = ?, room_type = ?, base_price = ?, status = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, room_number);
            ps.setString(2, room_type);
            ps.setDouble(3, base_price);
            ps.setString(4, status);
            ps.setInt(5, id);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteRoom(int id) {
        String sql = "DELETE FROM rooms WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Map<String, Object> getRoomByNumber(String roomNumber) {
        // Use a Prepared Statement to prevent SQL Injection
        String sql = "SELECT id, room_number, room_type, base_price, status FROM rooms WHERE room_number = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, roomNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> room = new HashMap<>();
                    room.put("id", rs.getInt("id"));
                    room.put("room_number", rs.getString("room_number"));
                    room.put("room_type", rs.getString("room_type"));
                    room.put("base_price", rs.getDouble("base_price"));
                    room.put("status", rs.getString("status"));
                    return room;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean createRoom(String room_number, String room_type, double base_price, String status) {
        String sql = "INSERT INTO rooms (room_number, room_type, base_price, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, room_number);
            ps.setString(2, room_type);
            ps.setDouble(3, base_price);
            ps.setString(4, status);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
