package viewswing;

import controller.ReceitaController;
import dao.EstoqueDAO;
import dao.ReceitaDAO;
import dao.UsuarioDAO;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Map;

public class LoginSwing extends JFrame {
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

    public LoginSwing() {
        super("EasyFarma - Login");

        // ===================== Lógica de Usuários e Controllers =====================
        usuarioDAO = new UsuarioDAO();
        // Carrega os usuários do arquivo JSON
        usuarios = usuarioDAO.carregarUsuarios();
        // Se estiver vazio, adiciona os usuários fixos e salva
        if (usuarios.isEmpty()) {
            usuarios.put("admin", "1234");
            usuarios.put("farmaceutico", "5678");
            usuarioDAO.salvarUsuarios(usuarios);
        }
        // Cria instâncias do DAO e do Controller para o restante do sistema
        ReceitaDAO receitaDAO = new ReceitaDAO();
        EstoqueDAO estoqueDAO = new EstoqueDAO();
        receitaController = new ReceitaController(receitaDAO, estoqueDAO);

        // ===================== Inicializar e Configurar Componentes =====================
        initComponents();

        pack(); // Ajusta o tamanho ao conteúdo
        setLocationRelativeTo(null); // Centraliza na tela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initComponents() {
        // Define o layout principal
        setLayout(new BorderLayout());

        // ===================== TOPO: LOGO CENTRALIZADA =====================
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        try {
            // Tenta carregar a logo (coloque o logo.png na pasta de recursos apropriada)
            Image logo = ImageIO.read(getClass().getResourceAsStream("/logo.png"));
            Image scaled = logo.getScaledInstance(100, -1, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaled));
            topPanel.add(logoLabel);
        } catch (IOException | NullPointerException ex) {
            System.err.println("Logo não encontrada!");
        }

        // ===================== FORMULÁRIO CENTRAL =====================
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Label e campo para "Usuário"
        JLabel lblUsuario = new JLabel("Usuário:");
        lblUsuario.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(lblUsuario, gbc);

        txtUsuario = new JTextField(15);
        txtUsuario.setToolTipText("Digite o usuário");
        gbc.gridx = 1;
        formPanel.add(txtUsuario, gbc);

        // Label e campo para "Senha"
        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(lblSenha, gbc);

        txtSenha = new JPasswordField(15);
        txtSenha.setToolTipText("Digite a senha");
        gbc.gridx = 1;
        formPanel.add(txtSenha, gbc);

        // Botões de ação
        JButton btnLogin = new JButton("Entrar");
        btnLogin.setBackground(new Color(76, 175, 80));  // Cor similar a #4CAF50
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(btnLogin, gbc);

        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setBackground(new Color(46, 125, 50)); // Cor similar a #2E7D32
        btnCadastrar.setForeground(Color.WHITE);
        btnCadastrar.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 1;
        formPanel.add(btnCadastrar, gbc);

        // Label de mensagem para feedback
        lblMensagem = new JLabel("", SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(lblMensagem, gbc);

        // ===================== AÇÕES DOS BOTÕES =====================
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = txtUsuario.getText().trim();
                String senha = new String(txtSenha.getPassword()).trim();

                if (usuarios.containsKey(usuario) && usuarios.get(usuario).equals(senha)) {
                    // Armazena os dados do usuário logado
                    LoginSwing.usuarioLogado = usuario;
                    LoginSwing.senhaLogada = senha;
                    lblMensagem.setText("Login bem-sucedido!");

                    // Abrir a tela de Cadastrar Receita (substitua por sua implementação Swing)
                    // new CadastrarReceitaSwing(receitaController).setVisible(true);

                    // Fecha a tela de login
                    dispose();
                } else {
                    lblMensagem.setText("Usuário ou senha incorretos!");
                }
            }
        });

        btnCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abrir a tela de cadastro de usuário (substitua por sua implementação Swing)
                // new CadastrarUsuarioSwing(usuarioDAO, usuarios).setVisible(true);
            }
        });

        // ===================== LAYOUT PRINCIPAL =====================
        add(topPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        getContentPane().setBackground(Color.WHITE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginSwing().setVisible(true);
        });
    }
}
