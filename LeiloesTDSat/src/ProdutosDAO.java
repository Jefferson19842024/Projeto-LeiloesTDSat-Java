
import com.mysql.jdbc.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class ProdutosDAO {

    private Connection conn;
    private ArrayList<ProdutosDTO> listagem;

    public ProdutosDAO() {
        conn = new conectaDAO().connectDB();
        listagem = new ArrayList<>();
    }

    public void cadastrarProduto(ProdutosDTO produto) {
        if (conn == null) {
            JOptionPane.showMessageDialog(null, "Não foi possível estabelecer conexão com o banco de dados.");
            return;
        }

        String sql = "INSERT INTO produtos (nome, valor, status) VALUES (?, ?, ?)";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, produto.getNome());
            stmt.setInt(2, produto.getValor());
            stmt.setString(3, produto.getStatus());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                JOptionPane.showMessageDialog(null, "Erro ao cadastrar produto: Não foi possível inserir o produto.");
                return;
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    produto.setId(generatedKeys.getInt(1));
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao cadastrar produto: Não foi possível obter o ID gerado.");
                    return;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar produto: " + e.getMessage());
        }
    }

    public void venderProduto(int id) {
        String sql = "UPDATE produtos SET status = 'Vendido' WHERE id = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Produto vendido com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Produto não encontrado ou não pôde ser vendido.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao vender produto: " + e.getMessage());
        }
    }

    public ArrayList<ProdutosDTO> listarProdutos() {
         String sql = "SELECT * FROM produtos";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            listagem.clear();

            while (rs.next()) {
                ProdutosDTO produto = new ProdutosDTO();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setValor(rs.getInt("valor"));
                produto.setStatus(rs.getString("status"));

                listagem.add(produto);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar produtos: " + e.getMessage());
        }

        return listagem;
    }
    
     public ArrayList<ProdutosDTO> listarProdutosVendidos() {
        String sql = "SELECT * FROM produtos WHERE status = 'Vendido'";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            listagem.clear();

            while (rs.next()) {
                ProdutosDTO produto = new ProdutosDTO();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setValor(rs.getInt("valor"));
                produto.setStatus(rs.getString("status"));

                listagem.add(produto);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar produtos vendidos: " + e.getMessage());
        }

        return listagem;
    }

    public void fecharConexao() {
        new conectaDAO().FecharConexao();
    }
}
