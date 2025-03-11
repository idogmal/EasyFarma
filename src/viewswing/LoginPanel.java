package viewswing;

import controller.ReceitaController;
import dao.EstoqueDAO;
import dao.ReceitaDAO;
import dao.UsuarioDAO;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Map;

public class LoginPanel extends JPanel {
    // Variáveis estáticas para armazenar os dados do usuário logado
    public static String usuarioLogado;
    public static String senhaLogada;

    // Mapa de usuários persistentes
    private Map<String, String> usuarios;
    private final ReceitaController receitaController;
    private final UsuarioDAO usuarioDAO;

    // Componentes Swing
    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    private JLabel lblMensagem;

    public LoginPanel() {
        // Inicializa DAOs e controllers
        usuarioDAO = new UsuarioDAO();
        usuarios = usuarioDAO.carregarUsuarios();
        if (usuarios.isEmpty()) {
            usuarios.put("admin", "1234");
            usuarios.put("farmaceutico", "5678");
            usuarioDAO.salvarUsuarios(usuarios);
        }
        ReceitaDAO receitaDAO = new ReceitaDAO();
        EstoqueDAO estoqueDAO = new EstoqueDAO();
        receitaController = new ReceitaController(receitaDAO, estoqueDAO);

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // TOPO: LOGO CENTRALIZADA
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBackground(Color.WHITE);
        try {
            Image logo = ImageIO.read(getClass().getResourceAsStream("/logo.png"));
            Image scaled = logo.getScaledInstance(100, -1, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaled));
            topPanel.add(logoLabel);
        } catch (IOException | NullPointerException ex) {
            System.err.println("Logo não encontrada!");
        }

        // FORMULÁRIO CENTRAL
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblUsuario = new JLabel("Usuário:");
        lblUsuario.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(lblUsuario, gbc);

        txtUsuario = new JTextField(15);
        txtUsuario.setToolTipText("Digite o usuário");
        gbc.gridx = 1;
        formPanel.add(txtUsuario, gbc);

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(lblSenha, gbc);

        txtSenha = new JPasswordField(15);
        txtSenha.setToolTipText("Digite a senha");
        gbc.gridx = 1;
        formPanel.add(txtSenha, gbc);

        JButton btnLogin = new JButton("Entrar");
        btnLogin.setBackground(new Color(76, 175, 80));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(btnLogin, gbc);

        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setBackground(new Color(46, 125, 50));
        btnCadastrar.setForeground(Color.WHITE);
        btnCadastrar.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 1;
        formPanel.add(btnCadastrar, gbc);

        lblMensagem = new JLabel("", SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(lblMensagem, gbc);

        // AÇÕES
        btnLogin.addActionListener(e -> {
            String usuario = txtUsuario.getText().trim();
            String senha = new String(txtSenha.getPassword()).trim();
            if (usuarios.containsKey(usuario) && usuarios.get(usuario).equals(senha)) {
                LoginPanel.usuarioLogado = usuario;
                LoginPanel.senhaLogada = senha;
                lblMensagem.setText("Login bem-sucedido!");
                // Dispara um evento para notificar a MainFrame que o login foi bem-sucedido.
                firePropertyChange("loginSuccessful", false, true);
            } else {
                lblMensagem.setText("Usuário ou senha incorretos!");
            }
        });

        btnCadastrar.addActionListener(e -> {
            // Dispara um evento para abrir a tela de cadastro de usuário.
            firePropertyChange("cadastrarUsuario", false, true);
        });

        add(topPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
    }
}
