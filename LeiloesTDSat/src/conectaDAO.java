
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class conectaDAO {

    private Connection conn;

    public Connection connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/LeiloesTDSat?useSSL=false", "root", "2021");
            System.out.println("Conexão estabelecida com sucesso.");
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao conectar: " + ex.getMessage());
        }
        return conn;
    }

    public void FecharConexao() {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Conexão fechada com sucesso.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao fechar conexão: " + ex.getMessage());
            }
        }
    }
}
