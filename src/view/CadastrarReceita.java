package view;

import controller.ReceitaController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.time.LocalDate;

public class CadastrarReceita extends Application {
    private final ReceitaController receitaController;

    public CadastrarReceita(ReceitaController receitaController) {
        this.receitaController = receitaController;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("EasyFarma - Cadastrar Receita");

        // ===================== MENU LATERAL (verde) =====================
        VBox menuLateral = new VBox(15);
        menuLateral.setPadding(new Insets(20));
        menuLateral.setStyle("-fx-background-color: #2E7D32;");
        menuLateral.setPrefWidth(180);

        Button btnCadastrarReceita = criarBotaoMenu("Cadastrar Receita", () -> {}); // Tela atual, desabilitado
        btnCadastrarReceita.setDisable(true);
        Button btnPesquisarReceita = criarBotaoMenu("Pesquisar Receita", () -> {
            new PesquisarReceita().start(new Stage());
            primaryStage.close();
        });
        Button btnEstoque = criarBotaoMenu("Estoque", () -> {
            new VisualizarEstoqueView().start(new Stage());
            primaryStage.close();
        });
        Button btnSair = criarBotaoMenu("Sair", () -> primaryStage.close());
        menuLateral.getChildren().addAll(btnCadastrarReceita, btnPesquisarReceita, btnEstoque, btnSair);

        // ===================== CONTEÚDO CENTRAL (FORMULÁRIO) =====================
        VBox conteudoCentral = new VBox(20);
        conteudoCentral.setPadding(new Insets(30));
        conteudoCentral.setAlignment(Pos.TOP_CENTER);

        Label lblTitulo = new Label("Cadastro de Receita");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Criação dos campos do formulário
        VBox campoPaciente = criarCampo("Nome do Paciente:", new TextField());
        VBox campoCPF = criarCampo("CPF do Paciente:", new TextField());
        VBox campoCRM = criarCampo("CRM do Médico:", new TextField());
        VBox campoMedicamento = criarCampo("Medicamentos (Nome Quantidade, ...):", new TextField());
        VBox campoData = criarCampo("Data da Prescrição:", new DatePicker());

        // Botões do formulário
        Button btnCadastrar = new Button("Cadastrar");
        btnCadastrar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        btnCadastrar.setPrefWidth(100);
        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");
        btnCancelar.setPrefWidth(100);
        HBox buttonBox = new HBox(15, btnCadastrar, btnCancelar);
        buttonBox.setAlignment(Pos.CENTER);

        VBox formulario = new VBox(15, campoPaciente, campoCPF, campoCRM, campoMedicamento, campoData, buttonBox);
        formulario.setMaxWidth(400);
        formulario.setAlignment(Pos.CENTER_LEFT);
        conteudoCentral.getChildren().addAll(lblTitulo, formulario);

        // ===================== LOGO NO RODAPÉ (inferior direito) =====================
        ImageView logoView = null;
        try {
            Image logo = new Image(getClass().getResourceAsStream("/logo.png"));
            logoView = new ImageView(logo);
            logoView.setFitHeight(50);
            logoView.setPreserveRatio(true);
        } catch(Exception ex) {
            System.err.println("Logo não encontrada!");
        }
        HBox bottomBar = new HBox();
        bottomBar.setPadding(new Insets(10));
        bottomBar.setAlignment(Pos.BOTTOM_RIGHT);
        if (logoView != null) {
            bottomBar.getChildren().add(logoView);
        }

        // ===================== AÇÃO DOS BOTÕES DO FORMULÁRIO =====================
        btnCadastrar.setOnAction(e -> {
            TextField txtPaciente = (TextField) campoPaciente.getChildren().get(1);
            TextField txtCPF = (TextField) campoCPF.getChildren().get(1);
            TextField txtCRM = (TextField) campoCRM.getChildren().get(1);
            TextField txtMedicamento = (TextField) campoMedicamento.getChildren().get(1);
            DatePicker dpData = (DatePicker) campoData.getChildren().get(1);

            String paciente = txtPaciente.getText().trim();
            String cpf = txtCPF.getText().trim();
            String crm = txtCRM.getText().trim();
            String medicamentosEntrada = txtMedicamento.getText().trim();
            String dataPrescricao = (dpData.getValue() != null) ? dpData.getValue().toString() : "";

            if (paciente.isEmpty() || cpf.isEmpty() || crm.isEmpty() ||
                    medicamentosEntrada.isEmpty() || dataPrescricao.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Todos os campos são obrigatórios!", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            receitaController.cadastrarReceita(paciente, cpf, crm, medicamentosEntrada, dataPrescricao);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Receita cadastrada com sucesso!", ButtonType.OK);
            alert.showAndWait();

            // Limpa os campos para permitir cadastro de nova receita sem fechar a janela
            txtPaciente.clear();
            txtCPF.clear();
            txtCRM.clear();
            txtMedicamento.clear();
            dpData.setValue(null);
        });

        btnCancelar.setOnAction(e -> primaryStage.close());

        // ===================== LAYOUT PRINCIPAL (BorderPane) =====================
        BorderPane root = new BorderPane();
        root.setLeft(menuLateral);
        root.setCenter(conteudoCentral);
        root.setBottom(bottomBar);

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Método auxiliar para criar um campo de formulário com label acima do campo de entrada
    private VBox criarCampo(String labelText, Control input) {
        Label label = new Label(labelText);
        label.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        return new VBox(5, label, input);
    }

    // Método auxiliar para criar um botão no menu lateral com a ação fornecida
    private Button criarBotaoMenu(String texto, Runnable acao) {
        Button btn = new Button(texto);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 16px;");
        btn.setOnAction(e -> acao.run());
        return btn;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
