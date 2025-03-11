package viewswing;

import controller.ReceitaController;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Versão Swing da tela "CadastrarReceita" que existia em JavaFX.
 * Substitui Stage, Scene, VBox, etc. por JFrame, JPanel, etc.
 */
public class CadastrarReceitaSwing extends JFrame {

    private final ReceitaController receitaController;

    // Componentes do menu lateral
    private JPanel menuLateral;
    private JButton btnCadastrarReceita;
    private JButton btnPesquisarReceita;
    private JButton btnEstoque;
    private JButton btnSair;

    // Componentes do formulário
    private JLabel lblTitulo;
    private JTextField txtPaciente;
    private JTextField txtCPF;
    private JTextField txtCRM;
    private JTextField txtMedicamentos;
    private JTextField txtDataPrescricao;
    private JButton btnCadastrar;
    private JButton btnCancelar;

    // Barra inferior (logo)
    private JPanel bottomBar;

    public CadastrarReceitaSwing(ReceitaController receitaController) {
        super("EasyFarma - Cadastrar Receita");
        this.receitaController = receitaController;

        initComponents();
        pack();
        setLocationRelativeTo(null); // Centraliza a janela
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        // Layout principal (BorderLayout)
        setLayout(new BorderLayout());

        // ===================== MENU LATERAL (verde) =====================
        menuLateral = new JPanel();
        menuLateral.setLayout(new BoxLayout(menuLateral, BoxLayout.Y_AXIS));
        menuLateral.setBackground(new Color(46, 125, 50)); // #2E7D32
        menuLateral.setPreferredSize(new Dimension(180, getHeight()));
        menuLateral.setBorder(new EmptyBorder(20, 20, 20, 20));

        btnCadastrarReceita = criarBotaoMenu("Cadastrar Receita");
        btnCadastrarReceita.setEnabled(false); // desabilitado pois já estamos nessa tela
        btnPesquisarReceita = criarBotaoMenu("Pesquisar Receita");
        btnEstoque = criarBotaoMenu("Estoque");
        btnSair = criarBotaoMenu("Sair");

        menuLateral.add(btnCadastrarReceita);
        menuLateral.add(Box.createVerticalStrut(15));
        menuLateral.add(btnPesquisarReceita);
        menuLateral.add(Box.createVerticalStrut(15));
        menuLateral.add(btnEstoque);
        menuLateral.add(Box.createVerticalStrut(15));
        menuLateral.add(btnSair);

        // Ações dos botões do menu
        btnPesquisarReceita.addActionListener(e -> {
            // Substitua pela tela Swing de pesquisa
            // new PesquisarReceitaSwing().setVisible(true);
            dispose();
        });

        btnEstoque.addActionListener(e -> {
            // Substitua pela tela Swing de estoque
            // new VisualizarEstoqueSwing().setVisible(true);
            dispose();
        });

        btnSair.addActionListener(e -> dispose());

        // ===================== CONTEÚDO CENTRAL (FORMULÁRIO) =====================
        JPanel conteudoCentral = new JPanel();
        conteudoCentral.setLayout(new BoxLayout(conteudoCentral, BoxLayout.Y_AXIS));
        conteudoCentral.setBorder(new EmptyBorder(30, 30, 30, 30));

        lblTitulo = new JLabel("Cadastro de Receita");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Campos do formulário
        txtPaciente = new JTextField(20);
        txtCPF = new JTextField(20);
        txtCRM = new JTextField(20);
        txtMedicamentos = new JTextField(20);
        txtDataPrescricao = new JTextField(20); // substitui o DatePicker por um JTextField simples

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        // Campo "Nome do Paciente"
        adicionarCampo(formPanel, gbc, row++, "Nome do Paciente:", txtPaciente);
        // Campo "CPF do Paciente"
        adicionarCampo(formPanel, gbc, row++, "CPF do Paciente:", txtCPF);
        // Campo "CRM do Médico"
        adicionarCampo(formPanel, gbc, row++, "CRM do Médico:", txtCRM);
        // Campo "Medicamentos (Nome Quantidade, ...)"
        adicionarCampo(formPanel, gbc, row++, "Medicamentos:", txtMedicamentos);
        // Campo "Data da Prescrição"
        adicionarCampo(formPanel, gbc, row++, "Data da Prescrição:", txtDataPrescricao);

        // Botões do formulário
        btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setBackground(new Color(76, 175, 80)); // #4CAF50
        btnCadastrar.setForeground(Color.WHITE);
        btnCadastrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCadastrar.setPreferredSize(new Dimension(100, 30));

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(244, 67, 54)); // #f44336
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancelar.setPreferredSize(new Dimension(100, 30));

        JPanel buttonBox = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonBox.add(btnCadastrar);
        buttonBox.add(btnCancelar);

        // Ações dos botões do formulário
        btnCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String paciente = txtPaciente.getText().trim();
                String cpf = txtCPF.getText().trim();
                String crm = txtCRM.getText().trim();
                String medicamentosEntrada = txtMedicamentos.getText().trim();
                String dataPrescricao = txtDataPrescricao.getText().trim();

                if (paciente.isEmpty() || cpf.isEmpty() || crm.isEmpty() ||
                        medicamentosEntrada.isEmpty() || dataPrescricao.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            CadastrarReceitaSwing.this,
                            "Todos os campos são obrigatórios!",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                // Chama o controller para cadastrar
                receitaController.cadastrarReceita(paciente, cpf, crm, medicamentosEntrada, dataPrescricao);
                JOptionPane.showMessageDialog(
                        CadastrarReceitaSwing.this,
                        "Receita cadastrada com sucesso!",
                        "Informação",
                        JOptionPane.INFORMATION_MESSAGE
                );

                // Limpa os campos para permitir novo cadastro
                txtPaciente.setText("");
                txtCPF.setText("");
                txtCRM.setText("");
                txtMedicamentos.setText("");
                txtDataPrescricao.setText("");
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // Monta o conteudoCentral
        conteudoCentral.add(lblTitulo);
        conteudoCentral.add(Box.createVerticalStrut(20));
        conteudoCentral.add(formPanel);
        conteudoCentral.add(Box.createVerticalStrut(15));
        conteudoCentral.add(buttonBox);

        // ===================== LOGO NO RODAPÉ (inferior direito) =====================
        bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomBar.setBorder(new EmptyBorder(10, 10, 10, 10));
        try {
            Image logo = ImageIO.read(getClass().getResourceAsStream("/logo.png"));
            Image scaled = logo.getScaledInstance(50, -1, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaled));
            bottomBar.add(logoLabel);
        } catch (IOException | NullPointerException ex) {
            System.err.println("Logo não encontrada!");
        }

        // Adiciona tudo no layout principal
        add(menuLateral, BorderLayout.WEST);
        add(conteudoCentral, BorderLayout.CENTER);
        add(bottomBar, BorderLayout.SOUTH);
    }

    /**
     * Cria um botão para o menu lateral com estilo básico.
     */
    private JButton criarBotaoMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Arial", Font.PLAIN, 16));
        btn.setFocusPainted(false);
        return btn;
    }

    /**
     * Adiciona um campo (Label + componente de entrada) em formPanel usando GridBagLayout.
     */
    private void adicionarCampo(JPanel formPanel, GridBagConstraints gbc, int row,
                                String labelText, JComponent input) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        formPanel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(input, gbc);
    }

    public static void main(String[] args) {
        // Para testar isoladamente
        SwingUtilities.invokeLater(() -> {
            // Você precisaria passar um ReceitaController real ou mock
            ReceitaController dummyController = null;
            new CadastrarReceitaSwing(dummyController).setVisible(true);
        });
    }
}
