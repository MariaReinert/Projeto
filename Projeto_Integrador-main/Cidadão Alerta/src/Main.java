import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/sistema_marcacoes?useSSL=false&serverTimezone=UTC";
        String usuario = "root";
        String senha = "root";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conexao = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Conectado com sucesso!");

            // Insert Usuario
            String sqlUsuario = "INSERT INTO Usuario (nome, email, cpf, senha, perfil) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement psUsuario = conexao.prepareStatement(sqlUsuario, PreparedStatement.RETURN_GENERATED_KEYS);
            psUsuario.setString(1, "João Silva");
            psUsuario.setString(2, "joao.silva@email.com");
            psUsuario.setString(3, "12345678901");
            psUsuario.setString(4, "senhaSegura123");
            psUsuario.setString(5, "Usuario");
            psUsuario.executeUpdate();

            // Pegar o ID do usuário inserido
            var rsUsuario = psUsuario.getGeneratedKeys();
            long usuarioId = 0;
            if (rsUsuario.next()) {
                usuarioId = rsUsuario.getLong(1);
            }
            psUsuario.close();

            // Insert Marcacao
            String sqlMarcacao = "INSERT INTO Marcacao (nome, descricao, foto_path, latitude, longitude, status, data_criacao, usuario_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement psMarcacao = conexao.prepareStatement(sqlMarcacao, PreparedStatement.RETURN_GENERATED_KEYS);
            psMarcacao.setString(1, "Buraco na Rua");
            psMarcacao.setString(2, "Um grande buraco na avenida principal");
            psMarcacao.setString(3, "/imagens/buraco1.jpg");
            psMarcacao.setDouble(4, -26.8233);
            psMarcacao.setDouble(5, -49.2706);
            psMarcacao.setString(6, "Aberto");
            psMarcacao.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            psMarcacao.setLong(8, usuarioId);
            psMarcacao.executeUpdate();

            // Pegar o ID da marcação inserida
            var rsMarcacao = psMarcacao.getGeneratedKeys();
            long marcacaoId = 0;
            if (rsMarcacao.next()) {
                marcacaoId = rsMarcacao.getLong(1);
            }
            psMarcacao.close();

            // Insert VotosMarcacao
            String sqlVoto = "INSERT INTO VotosMarcacao (usuarioID, MarcacaoID) VALUES (?, ?)";
            PreparedStatement psVoto = conexao.prepareStatement(sqlVoto);
            psVoto.setLong(1, usuarioId);
            psVoto.setLong(2, marcacaoId);
            psVoto.executeUpdate();
            psVoto.close();

            System.out.println("Dados inseridos com sucesso!");

            conexao.close();

        } catch (ClassNotFoundException e) {
            System.out.println("Driver JDBC não encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Erro SQL: " + e.getMessage());
        }
    }
}