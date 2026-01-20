import java.io.*;
import java.nio.file.*;
import java.sql.*;
import io.github.cdimascio.dotenv.Dotenv;

public class CreateDB {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure()
                .directory("./src")
                .load();

        String url = dotenv.get("DB_URL");
        String user = dotenv.get("DB_USER");
        String pass = dotenv.get("DB_PASS");

        if (url == null || user == null) {
            System.err.println("Error: Missing DB_URL or DB_USER in src/.env");
            return;
        }

        try (Connection conn = DriverManager.getConnection(url, user, pass);
                Statement stmt = conn.createStatement()) {

            System.out.println("Connected to MySQL successfully!");

            String sqlPath = "src/sql/createdb.sql";
            String sqlScript = Files.readString(Path.of(sqlPath));

            stmt.executeUpdate(sqlScript);

            System.out.println("------------------------------------------");
            System.out.println("Success! Database setup completed.");
            System.out.println("Script executed: " + sqlPath);
            System.out.println("------------------------------------------");

        } catch (IOException e) {
            System.err.println("File Error: Could not read " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected Error: " + e.getMessage());
        }
    }
}