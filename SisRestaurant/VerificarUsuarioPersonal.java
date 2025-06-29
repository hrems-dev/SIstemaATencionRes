import java.sql.*;
import java.util.Properties;
import java.io.FileInputStream;

public class VerificarUsuarioPersonal {
    public static void main(String[] args) {
        try {
            // Cargar propiedades de la aplicación
            Properties props = new Properties();
            props.load(new FileInputStream("src/main/resources/application.properties"));
            
            String url = props.getProperty("spring.datasource.url");
            String username = props.getProperty("spring.datasource.username", "");
            String password = props.getProperty("spring.datasource.password", "");
            
            System.out.println("Conectando a: " + url);
            
            // Conectar a la base de datos
            Connection conn = DriverManager.getConnection(url, username, password);
            
            // Verificar usuarios
            System.out.println("\n=== USUARIOS ===");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM usuario");
            while (rs.next()) {
                System.out.println("ID: " + rs.getLong("id_user") + 
                                 ", Nombre: " + rs.getString("nombre") + 
                                 ", Tipo: " + rs.getString("tipo_usuario"));
            }
            
            // Verificar personal
            System.out.println("\n=== PERSONAL ===");
            rs = stmt.executeQuery("SELECT * FROM personal");
            while (rs.next()) {
                System.out.println("ID Personal: " + rs.getLong("id_personal") + 
                                 ", ID Usuario: " + rs.getLong("id_user") + 
                                 ", DNI: " + rs.getString("dni"));
            }
            
            // Verificar relación usuario-personal
            System.out.println("\n=== RELACIÓN USUARIO-PERSONAL ===");
            rs = stmt.executeQuery("SELECT u.id_user, u.nombre, p.id_personal, p.dni " +
                                 "FROM usuario u LEFT JOIN personal p ON u.id_user = p.id_user");
            while (rs.next()) {
                Long idUser = rs.getLong("id_user");
                String nombre = rs.getString("nombre");
                Long idPersonal = rs.getLong("id_personal");
                String dni = rs.getString("dni");
                
                System.out.println("Usuario ID: " + idUser + 
                                 ", Nombre: " + nombre + 
                                 ", Personal ID: " + (idPersonal != 0 ? idPersonal : "NULL") + 
                                 ", DNI: " + (dni != null ? dni : "NULL"));
            }
            
            conn.close();
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 