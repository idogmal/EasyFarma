package viewswing;

import dao.UsuarioDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Map;

public class CadastrarUsuarioPanel extends JPanel {

    private UsuarioDAO usuarioDAO;
    private Map<String, String> usuarios;

    // Componentes da interface
    private JTextField txtNovoUsuario;
    private JPasswordField txtNovaSenha;
    private JLabel lblMensagem;

    // Construtor que recebe as dependências
    public CadastrarUsuarioPanel(UsuarioDAO usuarioDAO, Map<String, String> usuarios) {
        this.usuarioDAO = usuarioDAO;
        this.usuarios = usuarios;
        initComponents();
    }

    private void initComponents() {
        // Define o layout com BoxLayout vertical
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        // Rótulo e campo para o novo usuário
        JLabel lblNovoUsuario = new JLabel("Novo Usuário:");
        lblNovoUsuario.setFont(new Font("Arial", Font.BOLD, 16));
        txtNovoUsuario = new JTextField(20);
        txtNovoUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtNovoUsuario.getPreferredSize().height));

        // Rótulo e campo para a nova senha
        JLabel lblNovaSenha = new JLabel("Nova Senha:");
        lblNovaSenha.setFont(new Font("Arial", Font.BOLD, 16));
        txtNovaSenha = new JPasswordField(20);
        txtNovaSenha.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtNovaSenha.getPreferredSize().height));

        // Botão para cadastrar
        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCadastrar.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Rótulo de mensagem para feedback
        lblMensagem = new JLabel("", SwingConstants.CENTER);
        lblMensagem.setFont(new Font("Arial", Font.PLAIN, 14));

        // Adiciona os componentes ao painel com espaçamento
        add(lblNovoUsuario);
        add(txtNovoUsuario);
        add(Box.createVerticalStrut(10));
        add(lblNovaSenha);
        add(txtNovaSenha);
        add(Box.createVerticalStrut(10));
        add(btnCadastrar);
        add(Box.createVerticalStrut(10));
        add(lblMensagem);

        // Ação do botão cadastrar
        btnCadastrar.addActionListener(e -> {
            String novoUsuario = txtNovoUsuario.getText().trim();
            String novaSenha = new String(txtNovaSenha.getPassword()).trim();

            if (novoUsuario.isEmpty() || novaSenha.isEmpty()) {
                lblMensagem.setText("Preencha todos os campos.");
            } else {
                usuarios.put(novoUsuario, novaSenha);
                usuarioDAO.salvarUsuarios(usuarios);
                lblMensagem.setText("Usuário cadastrado com sucesso!");
                // Se preferir, limpe os campos:
                txtNovoUsuario.setText("");
                txtNovaSenha.setText("");
            }
        });
    }

    // Método main para testar a tela de forma isolada
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            Map<String, String> usuarios = usuarioDAO.carregarUsuarios();
            JFrame frame = new JFrame("Teste - Cadastro de Usuário");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(new CadastrarUsuarioPanel(usuarioDAO, usuarios));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
