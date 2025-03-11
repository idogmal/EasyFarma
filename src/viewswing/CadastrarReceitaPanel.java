package viewswing;

import controller.ReceitaController;
import java.io.IOException; // Import necessário para IOException
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CadastrarReceitaPanel extends JPanel {

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

    public CadastrarReceitaPanel(ReceitaController receitaController) {
        this.receitaController = receitaController;
        initComponents();
    }

    private void initComponents() {
        // Layout principal (BorderLayout)
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ===================== MENU LATERAL (verde) =====================
        menuLateral = new JPanel();
        menuLateral.setLayout(new BoxLayout(menuLateral, BoxLayout.Y_AXIS));
        menuLateral.setBackground(new Color(46, 125, 50)); // #2E7D32
        menuLateral.setPreferredSize(new Dimension(180, getHeight()));
        menuLateral.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Botões do menu lateral
        btnCadastrarReceita = criarBotaoMenu("Cadastrar Receita", () -> {
            // Estamos nesta tela, ação vazia.
        });
        btnCadastrarReceita.setEnabled(false);
        btnPesquisarReceita = criarBotaoMenu("Pesquisar Receita", () -> {
            firePropertyChange("showPesquisar", false, true);
        });
        btnEstoque = criarBotaoMenu("Estoque", () -> {
            firePropertyChange("showEstoque", false, true);
        });
        btnSair = criarBotaoMenu("Sair", () -> {
            firePropertyChange("exit", false, true);
        });

        menuLateral.add(btnCadastrarReceita);
        menuLateral.add(Box.createVerticalStrut(15));
        menuLateral.add(btnPesquisarReceita);
        menuLateral.add(Box.createVerticalStrut(15));
        menuLateral.add(btnEstoque);
        menuLateral.add(Box.createVerticalStrut(15));
        menuLateral.add(btnSair);

        // ===================== CONTEÚDO CENTRAL (FORMULÁRIO) =====================
        JPanel conteudoCentral = new JPanel();
        conteudoCentral.setLayout(new BoxLayout(conteudoCentral, BoxLayout.Y_AXIS));
        conteudoCentral.setBorder(new EmptyBorder(30, 30, 30, 30));
        conteudoCentral.setBackground(Color.WHITE);

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
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        adicionarCampo(formPanel, gbc, row++, "Nome do Paciente:", txtPaciente);
        adicionarCampo(formPanel, gbc, row++, "CPF do Paciente:", txtCPF);
        adicionarCampo(formPanel, gbc, row++, "CRM do Médico:", txtCRM);
        adicionarCampo(formPanel, gbc, row++, "Medicamentos:", txtMedicamentos);
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
        buttonBox.setBackground(Color.WHITE);
        buttonBox.add(btnCadastrar);
        buttonBox.add(btnCancelar);

        // Ações dos botões do formulário
        btnCadastrar.addActionListener((ActionEvent e) -> {
            String paciente = txtPaciente.getText().trim();
            String cpf = txtCPF.getText().trim();
            String crm = txtCRM.getText().trim();
            String medicamentosEntrada = txtMedicamentos.getText().trim();
            String dataPrescricao = txtDataPrescricao.getText().trim();

            if (paciente.isEmpty() || cpf.isEmpty() || crm.isEmpty() ||
                    medicamentosEntrada.isEmpty() || dataPrescricao.isEmpty()) {
                JOptionPane.showMessageDialog(
                        CadastrarReceitaPanel.this,
                        "Todos os campos são obrigatórios!",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            receitaController.cadastrarReceita(paciente, cpf, crm, medicamentosEntrada, dataPrescricao);
            JOptionPane.showMessageDialog(
                    CadastrarReceitaPanel.this,
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

            // Dispara evento para indicar que os dados foram atualizados
            firePropertyChange("refreshData", false, true);
            // Opcionalmente, pode disparar um evento para manter a tela atual ou navegar
            firePropertyChange("showCadastro", false, true);
        });

        btnCancelar.addActionListener((ActionEvent e) -> {
            firePropertyChange("backToMenu", false, true);
        });

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(lblTitulo);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(formPanel);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(buttonBox);
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        conteudoCentral.add(centerPanel);

        // ===================== LOGO NO RODAPÉ (inferior direito) =====================
        bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomBar.setBackground(Color.WHITE);
        bottomBar.setBorder(new EmptyBorder(10, 10, 10, 10));
        try {
            Image logo = ImageIO.read(getClass().getResourceAsStream("/logo.png"));
            Image scaled = logo.getScaledInstance(50, -1, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaled));
            bottomBar.add(logoLabel);
        } catch (IOException | NullPointerException ex) {
            System.err.println("Logo não encontrada!");
        }

        // ===================== LAYOUT PRINCIPAL =====================
        setLayout(new BorderLayout());
        add(menuLateral, BorderLayout.WEST);
        add(conteudoCentral, BorderLayout.CENTER);
        add(bottomBar, BorderLayout.SOUTH);
    }

    /**
     * Método auxiliar para adicionar um campo com label em formPanel usando GridBagLayout.
     */
    private void adicionarCampo(JPanel formPanel, GridBagConstraints gbc, int row, String labelText, JComponent input) {
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

    /**
     * Método auxiliar para criar um botão do menu lateral com a ação especificada.
     */
    private JButton criarBotaoMenu(String texto, Runnable acao) {
        JButton btn = new JButton(texto);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Arial", Font.PLAIN, 16));
        btn.addActionListener((ActionEvent e) -> acao.run());
        return btn;
    }
}
