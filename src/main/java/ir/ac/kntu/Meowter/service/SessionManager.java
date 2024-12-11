package ir.ac.kntu.Meowter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.kntu.Meowter.model.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SessionManager {

    private static final String SESSION_FILE = "userSession.json";
    private static String sessionChecksum = null;

    public static void saveSession(User user) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File sessionFile = new File(SESSION_FILE);
            objectMapper.writeValue(sessionFile, user);

            sessionChecksum = computeChecksum(sessionFile);
            System.out.println("Session saved successfully.");
        } catch (IOException | NoSuchAlgorithmException e) {
            System.out.println("Error saving session: " + e.getMessage());
        }
    }

    public static User loadSession() {
        try {
            File sessionFile = new File(SESSION_FILE);
            if (sessionFile.exists() && sessionFile.length() > 0) {
                String currentChecksum = computeChecksum(sessionFile);
                if (sessionChecksum != null && !sessionChecksum.equals(currentChecksum)) {
                    System.out.println("⚠️ Session file has been modified! Session will be terminated.");
                    clearSession();
                    return null;
                }

                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(sessionFile, User.class);
            } else {
                System.out.println("⚠️ Session file is empty or corrupted.");
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            System.out.println("Error loading session: " + e.getMessage());
        }
        return null;
    }

    public static void clearSession() {
        File sessionFile = new File(SESSION_FILE);
        if (sessionFile.exists()) {
            sessionFile.delete();
            sessionChecksum = null;
            System.out.println("Session cleared.");
        }
    }

    private static String computeChecksum(File file) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] fileBytes = Files.readAllBytes(file.toPath());
        byte[] checksumBytes = md.digest(fileBytes);

        StringBuilder sb = new StringBuilder();
        for (byte b : checksumBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
