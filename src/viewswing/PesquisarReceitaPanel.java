package viewswing;

import dao.ReceitaDAO;
import model.Receita;
import util.ExportadorReceitas;
import viewswing.ReceitaExport;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PesquisarReceitaPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtNome;
    private JTextField txtCpf;
    private final ReceitaDAO receitaDAO;

    // Botões do menu lateral
    private JButton btnCadastrarReceita;
    private JButton btnPesquisarReceita;
    private JButton btnEstoque;
    private JButton btnSair;

    // Botões de ação
    private JButton btnPesquisar;
    private JButton btnExportar;
    private JButton btnValidar;
    private JButton btnDeletar;  // novo botão para deletar receita

    // Lista para exportação e para manipulação interna
    private List<ReceitaExport> receitaExportList;
    private List<Receita> receitas;

    public PesquisarReceitaPanel(ReceitaDAO receitaDAO) {
        super();
        this.receitaDAO = receitaDAO;
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
            // Já estamos nesta tela
        });
        btnPesquisarReceita.setEnabled(false);
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

        // CONTEÚDO CENTRAL
        JPanel conteudoCentral = new JPanel();
        conteudoCentral.setLayout(new BoxLayout(conteudoCentral, BoxLayout.Y_AXIS));
        conteudoCentral.setBorder(new EmptyBorder(30, 30, 30, 30));
        conteudoCentral.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Pesquisar Receita");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Painel de filtros
        JPanel filtrosPanel = new JPanel(new GridBagLayout());
        filtrosPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        filtrosPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblNome = new JLabel("Nome do Paciente:");
        lblNome.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        filtrosPanel.add(lblNome, gbc);

        txtNome = new JTextField(15);
        txtNome.setToolTipText("Digite o nome");
        gbc.gridx = 1;
        filtrosPanel.add(txtNome, gbc);

        JLabel lblCpf = new JLabel("CPF do Paciente:");
        lblCpf.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        filtrosPanel.add(lblCpf, gbc);

        txtCpf = new JTextField(15);
        txtCpf.setToolTipText("Digite o CPF");
        gbc.gridx = 1;
        filtrosPanel.add(txtCpf, gbc);

        // Botões de ação
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        botoesPanel.setBackground(Color.WHITE);
        btnPesquisar = new JButton("Pesquisar");
        btnPesquisar.addActionListener(e -> pesquisarReceitas(txtNome.getText().trim(), txtCpf.getText().trim()));
        btnExportar = new JButton("Exportar Receitas");
        btnExportar.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Salvar Receitas como XLSX");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx"));
            int result = fileChooser.showSaveDialog(PesquisarReceitaPanel.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String caminho = file.getAbsolutePath();
                if (!caminho.toLowerCase().endsWith(".xlsx")) {
                    caminho += ".xlsx";
                }
                ExportadorReceitas.exportarReceitasXLSX(getReceitaExportList(), caminho);
                JOptionPane.showMessageDialog(
                        PesquisarReceitaPanel.this,
                        "Exportação concluída para o arquivo: " + caminho,
                        "Exportar",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        btnValidar = new JButton("Validar Receita");
        btnValidar.addActionListener(e -> validarReceitas());
        btnDeletar = new JButton("Deletar Receita");
        btnDeletar.addActionListener(e -> deletarReceita());
        botoesPanel.add(btnPesquisar);
        botoesPanel.add(btnExportar);
        botoesPanel.add(btnValidar);
        botoesPanel.add(btnDeletar);

        // Tabela de receitas
        String[] colunas = { "Paciente", "CPF", "Medicamentos", "Data", "Status" };
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Monta o painel central
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(lblTitulo);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(filtrosPanel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(botoesPanel);
        centerPanel.add(Box.createVerticalStrut(20));
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
        } catch (Exception ex) {
            System.err.println("Logo não encontrada!");
        }

        // LAYOUT PRINCIPAL
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.add(menuLateral, BorderLayout.WEST);
        mainPanel.add(conteudoCentral, BorderLayout.CENTER);
        mainPanel.add(bottomBar, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
        setPreferredSize(new Dimension(900, 600));

        carregarDados();
    }

    private void pesquisarReceitas(String nome, String cpf) {
        carregarDados();
        List<ReceitaExport> filtrado = receitaExportList.stream()
                .filter(r ->
                        (nome.isEmpty() || r.getPaciente().toLowerCase().contains(nome.toLowerCase())) &&
                                (cpf.isEmpty() || r.getCpf().startsWith(cpf))
                )
                .collect(Collectors.toList());

        tableModel.setRowCount(0);
        for (ReceitaExport r : filtrado) {
            tableModel.addRow(new Object[]{
                    r.getPaciente(),
                    r.getCpf(),
                    r.getMedicamentos(),
                    r.getDataPrescricao(),
                    r.getStatus()
            });
        }
    }

    private List<ReceitaExport> getReceitaExportList() {
        int rowCount = tableModel.getRowCount();
        List<ReceitaExport> lista = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            lista.add(new ReceitaExport(
                    (String) tableModel.getValueAt(i, 0),
                    (String) tableModel.getValueAt(i, 1),
                    (String) tableModel.getValueAt(i, 2),
                    (String) tableModel.getValueAt(i, 3),
                    (String) tableModel.getValueAt(i, 4)
            ));
        }
        return lista;
    }

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

    // Método que carrega os dados da lista de receitas e atualiza a tabela.
    private void carregarDados() {
        tableModel.setRowCount(0);
        receitas = receitaDAO.listarReceitas();
        receitaExportList = new ArrayList<>();
        for (Receita r : receitas) {
            ReceitaExport re = new ReceitaExport(
                    r.getPaciente() != null ? r.getPaciente() : "",
                    r.getCpf() != null ? r.getCpf() : "",
                    r.getMedicamentosAsString(),
                    r.getDataPrescricao() != null ? r.getDataPrescricao() : "",
                    r.getStatus() != null ? r.getStatus() : ""
            );
            receitaExportList.add(re);
        }
        for (ReceitaExport re : receitaExportList) {
            tableModel.addRow(new Object[]{
                    re.getPaciente(),
                    re.getCpf(),
                    re.getMedicamentos(),
                    re.getDataPrescricao(),
                    re.getStatus()
            });
        }
    }

    // Método de atualização para ser chamado pelo MainFrame
    public void atualizarDados() {
        carregarDados();
    }

    private void validarReceitas() {
        // Lógica para validar receitas (já existente)
    }

    // Método para deletar uma receita, exigindo senha do usuário
    private void deletarReceita() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma receita para deletar.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Solicita a senha do usuário
        JPasswordField pf = new JPasswordField();
        int okCxl = JOptionPane.showConfirmDialog(this, pf, "Digite a senha para confirmar a exclusão:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (okCxl == JOptionPane.OK_OPTION) {
            String senha = new String(pf.getPassword());
            // Para este exemplo, assumimos que a senha correta é "1234"
            if (!"1234".equals(senha)) {
                JOptionPane.showMessageDialog(this, "Senha incorreta.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Confirmação final de exclusão
            int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente deletar a receita selecionada?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                Receita receitaSelecionada = receitas.get(selectedRow);
                // Chama o método de exclusão no DAO (certifique-se de que o método removerReceita existe e retorna boolean)
                boolean removido = receitaDAO.removerReceita(receitaSelecionada);
                if (removido) {
                    JOptionPane.showMessageDialog(this, "Receita deletada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    atualizarDados();
                } else {
                    JOptionPane.showMessageDialog(this, "Falha ao deletar a receita.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // Método main para teste isolado do painel
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Teste - Pesquisar Receita");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(new PesquisarReceitaPanel(new dao.ReceitaDAO()));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
