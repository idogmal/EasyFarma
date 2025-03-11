package viewswing;

import controller.ReceitaController;
import dao.EstoqueDAO;
import dao.ReceitaDAO;
import dao.UsuarioDAO;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

/**
 * JFrame principal que usa CardLayout para navegar entre as telas.
 */
public class MainFrame extends JFrame implements PropertyChangeListener {

    private CardLayout cardLayout;
    private JPanel cardPanel;

    // Identificadores para cada tela (cards)
    private static final String LOGIN_CARD = "login";
    private static final String CADASTRO_RECEITA_CARD = "cadastroReceita";
    private static final String CADASTRO_USUARIO_CARD = "cadastroUsuario";
    private static final String PESQUISA_CARD = "pesquisa";
    private static final String ESTOQUE_CARD = "estoque";

    // Painéis
    private LoginPanel loginPanel;
    private CadastrarReceitaPanel cadastrarReceitaPanel;
    private CadastrarUsuarioPanel cadastrarUsuarioPanel;
    private PesquisarReceitaPanel pesquisarReceitaPanel;
    private VisualizarEstoquePanel visualizarEstoquePanel;

    public MainFrame() {
        setTitle("EasyFarma - Sistema com CardLayout");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // Instancia o CardLayout e o painel que o usa
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // ===================== Instancia DAOs e Controllers =====================
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        // Carrega o mapa de usuários
        Map<String, String> usuarios = usuarioDAO.carregarUsuarios();
        if (usuarios.isEmpty()) {
            // Se estiver vazio, adiciona usuários fixos
            usuarios.put("admin", "1234");
            usuarios.put("farmaceutico", "5678");
            usuarioDAO.salvarUsuarios(usuarios);
        }

        ReceitaDAO receitaDAO = new ReceitaDAO();
        EstoqueDAO estoqueDAO = new EstoqueDAO();
        ReceitaController receitaController = new ReceitaController(receitaDAO, estoqueDAO);

        // ===================== Cria os painéis =====================
        // 1. Painel de Login
        loginPanel = new LoginPanel();
        loginPanel.addPropertyChangeListener(this);

        // 2. Painel de Cadastro de Receita
        cadastrarReceitaPanel = new CadastrarReceitaPanel(receitaController);
        cadastrarReceitaPanel.addPropertyChangeListener(this);

        // 3. Painel de Cadastro de Usuário
        cadastrarUsuarioPanel = new CadastrarUsuarioPanel(usuarioDAO, usuarios);
        cadastrarUsuarioPanel.addPropertyChangeListener(this);

        // 4. Painel de Pesquisa de Receita
        pesquisarReceitaPanel = new PesquisarReceitaPanel(receitaDAO);
        pesquisarReceitaPanel.addPropertyChangeListener(this);

        // 5. Painel de Visualizar Estoque
        visualizarEstoquePanel = new VisualizarEstoquePanel();
        visualizarEstoquePanel.addPropertyChangeListener(this);

        // ===================== Adiciona os painéis ao cardPanel =====================
        cardPanel.add(loginPanel, LOGIN_CARD);
        cardPanel.add(cadastrarReceitaPanel, CADASTRO_RECEITA_CARD);
        cardPanel.add(cadastrarUsuarioPanel, CADASTRO_USUARIO_CARD);
        cardPanel.add(pesquisarReceitaPanel, PESQUISA_CARD);
        cardPanel.add(visualizarEstoquePanel, ESTOQUE_CARD);

        // Define o cardPanel como conteúdo da janela
        setContentPane(cardPanel);
        // Inicia exibindo o painel de login
        cardLayout.show(cardPanel, LOGIN_CARD);
    }

    /**
     * Escuta eventos disparados pelos painéis (PropertyChangeEvent).
     * Cada painel chama firePropertyChange("algumEvento", false, true).
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propName = evt.getPropertyName();
        boolean newValue = (evt.getNewValue() instanceof Boolean) && (Boolean) evt.getNewValue();

        if ("loginSuccessful".equals(propName) && newValue) {
            cardLayout.show(cardPanel, CADASTRO_RECEITA_CARD);
        }
        if ("showCadastrar".equals(propName) && newValue) {
            cardLayout.show(cardPanel, CADASTRO_RECEITA_CARD);
        }
        if ("cadastrarUsuario".equals(propName) && newValue) {
            cardLayout.show(cardPanel, CADASTRO_USUARIO_CARD);
        }
        if ("showPesquisar".equals(propName) && newValue) {
            cardLayout.show(cardPanel, PESQUISA_CARD);
        }
        if ("showEstoque".equals(propName) && newValue) {
            cardLayout.show(cardPanel, ESTOQUE_CARD);
        }
        // Novo tratamento: quando os dados são atualizados, recarregar a lista do painel de pesquisa
        if ("refreshData".equals(propName) && newValue) {
            if (pesquisarReceitaPanel != null) {
                pesquisarReceitaPanel.carregarDados();
            }
        }
        if ("exit".equals(propName) && newValue) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
