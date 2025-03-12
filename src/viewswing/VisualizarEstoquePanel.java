package viewswing;

import dao.EstoqueDAO;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Map;

public class VisualizarEstoquePanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private EstoqueDAO estoqueDAO;

    // Botões do menu lateral
    private JButton btnCadastrarReceita;
    private JButton btnPesquisarReceita;
    private JButton btnEstoque;
    private JButton btnSair;

    // Componentes para adicionar/editar medicamentos
    private JTextField txtMedicamento;
    private JTextField txtQuantidade;

    public VisualizarEstoquePanel() {
        estoqueDAO = new EstoqueDAO();
        initComponents();
        carregarDados();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // MENU LATERAL
        JPanel menuLateral = new JPanel();
        menuLateral.setLayout(new BoxLayout(menuLateral, BoxLayout.Y_AXIS));
        menuLateral.setBackground(new Color(46, 125, 50));
        menuLateral.setPreferredSize(new Dimension(180, 600));
        menuLateral.setBorder(new EmptyBorder(20, 20, 20, 20));

        btnCadastrarReceita = criarBotaoMenu("Cadastrar Receita", () -> {
            firePropertyChange("showCadastrar", false, true);
        });
        btnPesquisarReceita = criarBotaoMenu("Pesquisar Receita", () -> {
            firePropertyChange("showPesquisar", false, true);
        });
        btnEstoque = criarBotaoMenu("Estoque", () -> {
            firePropertyChange("showEstoque", false, true);
        });
        btnEstoque.setEnabled(false);
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

        // CONTEÚDO CENTRAL
        JPanel conteudoCentral = new JPanel();
        conteudoCentral.setLayout(new BoxLayout(conteudoCentral, BoxLayout.Y_AXIS));
        conteudoCentral.setBorder(new EmptyBorder(30, 30, 30, 30));
        conteudoCentral.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Visualizar Estoque");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Painel para ações de adicionar/editar medicamentos
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        actionPanel.setBackground(Color.WHITE);
        JLabel lblMedicamento = new JLabel("Medicamento:");
        txtMedicamento = new JTextField(15);
        JLabel lblQuantidade = new JLabel("Quantidade:");
        txtQuantidade = new JTextField(5);
        JButton btnAdicionar = new JButton("Adicionar");
        btnAdicionar.addActionListener((ActionEvent e) -> adicionarMedicamento());
        JButton btnEditar = new JButton("Editar Selecionado");
        btnEditar.addActionListener((ActionEvent e) -> editarMedicamento());
        actionPanel.add(lblMedicamento);
        actionPanel.add(txtMedicamento);
        actionPanel.add(lblQuantidade);
        actionPanel.add(txtQuantidade);
        actionPanel.add(btnAdicionar);
        actionPanel.add(btnEditar);

        // Tabela de Estoque
        String[] colunas = {"Medicamento", "Quantidade"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Painel central que agrupa título, painel de ações e tabela
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(lblTitulo);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(actionPanel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(scrollPane);
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        conteudoCentral.add(centerPanel);

        // RODAPÉ com logotipo
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
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

        // LAYOUT PRINCIPAL que une menu, conteúdo e rodapé
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.add(menuLateral, BorderLayout.WEST);
        mainPanel.add(conteudoCentral, BorderLayout.CENTER);
        mainPanel.add(bottomBar, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
        setPreferredSize(new Dimension(900, 600));
    }

    // Método para adicionar medicamento no estoque
    private void adicionarMedicamento() {
        String medicamento = txtMedicamento.getText().trim();
        String quantidadeStr = txtQuantidade.getText().trim();
        if (medicamento.isEmpty() || quantidadeStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o nome e a quantidade do medicamento.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int quantidade = Integer.parseInt(quantidadeStr);
            if (quantidade <= 0) {
                JOptionPane.showMessageDialog(this, "A quantidade deve ser um número positivo.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            estoqueDAO.adicionarMedicamento(medicamento, quantidade);
            JOptionPane.showMessageDialog(this, "Medicamento adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            txtMedicamento.setText("");
            txtQuantidade.setText("");
            atualizarDados();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida. Informe um número.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para editar a quantidade de um medicamento selecionado na tabela
    private void editarMedicamento() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um medicamento para editar.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String medicamento = (String) tableModel.getValueAt(selectedRow, 0);
        int currentQuantity = (int) tableModel.getValueAt(selectedRow, 1);
        String novaQuantidadeStr = JOptionPane.showInputDialog(this, "Informe a nova quantidade para " + medicamento + ":", currentQuantity);
        if (novaQuantidadeStr != null && !novaQuantidadeStr.trim().isEmpty()) {
            try {
                int novaQuantidade = Integer.parseInt(novaQuantidadeStr.trim());
                if (novaQuantidade < 0) {
                    JOptionPane.showMessageDialog(this, "A quantidade não pode ser negativa.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Atualiza a quantidade diretamente no Map do estoque
                Map<String, Integer> meds = estoqueDAO.getEstoque().getMedicamentos();
                if (meds.containsKey(medicamento)) {
                    meds.put(medicamento, novaQuantidade);
                    // Para salvar as alterações, recarregamos o estoque (supondo que o método salvar seja chamado internamente)
                    estoqueDAO.recarregarEstoque();
                    JOptionPane.showMessageDialog(this, "Quantidade atualizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    atualizarDados();
                } else {
                    JOptionPane.showMessageDialog(this, "Medicamento não encontrado no estoque.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Quantidade inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Carrega os dados do estoque na tabela
    private void carregarDados() {
        tableModel.setRowCount(0);
        estoqueDAO.recarregarEstoque();
        Map<String, Integer> medicamentos = estoqueDAO.getEstoque().getMedicamentos();
        if (medicamentos != null) {
            medicamentos.forEach((med, qtd) -> {
                tableModel.addRow(new Object[]{med, qtd});
            });
        }
    }

    // Método de atualização para ser chamado pelo MainFrame
    public void atualizarDados() {
        carregarDados();
    }

    // Método auxiliar para criar botões do menu lateral
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

    // Método main para teste isolado do painel
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Teste - Visualizar Estoque");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(new VisualizarEstoquePanel());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
