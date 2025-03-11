package viewswing;

import dao.UsuarioDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Map;

public class CadastrarUsuarioSwing extends JFrame {

    private UsuarioDAO usuarioDAO;
    private Map<String, String> usuarios;

    // Componentes da interface
    private JTextField txtNovoUsuario;
    private JPasswordField txtNovaSenha;
    private JLabel lblMensagem;

    // Construtor que recebe as dependências
    public CadastrarUsuarioSwing(UsuarioDAO usuarioDAO, Map<String, String> usuarios) {
        super("Cadastro de Usuário");
        this.usuarioDAO = usuarioDAO;
        this.usuarios = usuarios;
        initComponents();
    }

    // Construtor padrão (caso necessário)
    public CadastrarUsuarioSwing() {
        this(null, null);
    }

    private void initComponents() {
        // Cria um painel principal com BoxLayout vertical
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

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

        // Adiciona os componentes ao painel com espaçamentos
        panel.add(lblNovoUsuario);
        panel.add(txtNovoUsuario);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblNovaSenha);
        panel.add(txtNovaSenha);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnCadastrar);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblMensagem);

        // Configura a ação do botão cadastrar
        btnCadastrar.addActionListener(e -> {
            String novoUsuario = txtNovoUsuario.getText().trim();
            String novaSenha = new String(txtNovaSenha.getPassword()).trim();

            if (novoUsuario.isEmpty() || novaSenha.isEmpty()) {
                lblMensagem.setText("Preencha todos os campos.");
            } else {
                // Adiciona o novo usuário ao mapa e persiste as alterações
                usuarios.put(novoUsuario, novaSenha);
                usuarioDAO.salvarUsuarios(usuarios);
                lblMensagem.setText("Usuário cadastrado com sucesso!");
                // Opcional: feche a janela após o cadastro
                // dispose();
            }
        });

        // Configura o JFrame
        setContentPane(panel);
        setPreferredSize(new Dimension(300, 250));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Para teste, crie ou carregue o DAO e o mapa de usuários
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            Map<String, String> usuarios = usuarioDAO.carregarUsuarios();
            new CadastrarUsuarioSwing(usuarioDAO, usuarios).setVisible(true);
        });
    }
}
