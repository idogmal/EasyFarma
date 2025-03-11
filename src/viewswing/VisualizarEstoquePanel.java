package viewswing;

import dao.EstoqueDAO;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VisualizarEstoquePanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private EstoqueDAO estoqueDAO;
    private List<MedicamentoView> medicamentoList;
    private static final int LIMITE_ESTOQUE_BAIXO = 10;

    public VisualizarEstoquePanel() {
        estoqueDAO = new EstoqueDAO();
        medicamentoList = new ArrayList<>();
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

        JButton btnCadastrarReceita = criarBotaoMenu("Cadastrar Receita", () -> firePropertyChange("showCadastrar", false, true));
        JButton btnPesquisarReceita = criarBotaoMenu("Pesquisar Receita", () -> firePropertyChange("showPesquisar", false, true));
        JButton btnEstoque = criarBotaoMenu("Estoque", () -> {}); // Estamos nesta tela
        btnEstoque.setEnabled(false);
        JButton btnSair = criarBotaoMenu("Sair", () -> firePropertyChange("exit", false, true));

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

        // Painel de pesquisa
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        searchPanel.setBackground(Color.WHITE);
        JTextField txtPesquisar = new JTextField(15);
        txtPesquisar.setToolTipText("Pesquisar medicamento");
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> pesquisarMedicamento(txtPesquisar.getText().trim()));
        searchPanel.add(new JLabel("Pesquisar:"));
        searchPanel.add(txtPesquisar);
        searchPanel.add(btnBuscar);

        // Configuração da tabela
        String[] colunas = {"Nome do Medicamento", "Quantidade", "Alerta"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 300));

        // Painel para adicionar novo medicamento
        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        addPanel.setBackground(Color.WHITE);
        JTextField txtNomeMed = new JTextField(15);
        txtNomeMed.setToolTipText("Nome do Medicamento");
        JTextField txtQuantidade = new JTextField(5);
        txtQuantidade.setToolTipText("Quantidade");
        JButton btnAdicionar = new JButton("Adicionar Medicamento");
        btnAdicionar.addActionListener(e -> {
            String nome = txtNomeMed.getText().trim();
            String qtdStr = txtQuantidade.getText().trim();
            if (nome.isEmpty() || qtdStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o nome e a quantidade.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                int quantidade = Integer.parseInt(qtdStr);
                if (quantidade <= 0) {
                    JOptionPane.showMessageDialog(this, "A quantidade deve ser maior que zero.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                estoqueDAO.adicionarMedicamento(nome, quantidade);
                carregarDados();
                txtNomeMed.setText("");
                txtQuantidade.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Quantidade inválida. Informe um número.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        addPanel.add(txtNomeMed);
        addPanel.add(txtQuantidade);
        addPanel.add(btnAdicionar);

        // Monta o painel central
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(lblTitulo);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(searchPanel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(scrollPane);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(addPanel);
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        conteudoCentral.add(centerPanel);

        // LOGO NO RODAPÉ
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

        // LAYOUT PRINCIPAL
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.add(menuLateral, BorderLayout.WEST);
        mainPanel.add(conteudoCentral, BorderLayout.CENTER);
        mainPanel.add(bottomBar, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        add(mainPanel, BorderLayout.CENTER);
    }

    // Método para pesquisar medicamentos pelo nome
    private void pesquisarMedicamento(String filtro) {
        if (filtro.isEmpty()) {
            carregarDados();
        } else {
            List<MedicamentoView> filtrado = medicamentoList.stream()
                    .filter(mv -> mv.getNome().toLowerCase().contains(filtro.toLowerCase()))
                    .collect(Collectors.toList());
            tableModel.setRowCount(0);
            for (MedicamentoView mv : filtrado) {
                tableModel.addRow(new Object[]{mv.getNome(), mv.getQuantidade(), mv.getAlerta()});
            }
        }
    }

    // Método público para recarregar os dados do estoque (para atualização em tempo real)
    public void carregarDados() {
        tableModel.setRowCount(0);
        medicamentoList = new ArrayList<>();
        // Atualiza o estoque lendo novamente do arquivo
        estoqueDAO.recarregarEstoque();  // Certifique-se de que este método esteja implementado no EstoqueDAO
        Map<String, Integer> medicamentos = estoqueDAO.getEstoque().getMedicamentos();
        for (Map.Entry<String, Integer> entry : medicamentos.entrySet()) {
            String nome = entry.getKey();
            int quantidade = entry.getValue();
            String alerta = quantidade < LIMITE_ESTOQUE_BAIXO ? "Estoque Baixo" : "";
            medicamentoList.add(new MedicamentoView(nome, quantidade, alerta));
        }
        for (MedicamentoView mv : medicamentoList) {
            tableModel.addRow(new Object[]{mv.getNome(), mv.getQuantidade(), mv.getAlerta()});
        }
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

    // Classe auxiliar para representar cada medicamento na tabela
    public static class MedicamentoView {
        private final String nome;
        private final int quantidade;
        private final String alerta;

        public MedicamentoView(String nome, int quantidade, String alerta) {
            this.nome = nome;
            this.quantidade = quantidade;
            this.alerta = alerta;
        }

        public String getNome() {
            return nome;
        }

        public int getQuantidade() {
            return quantidade;
        }

        public String getAlerta() {
            return alerta;
        }
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
