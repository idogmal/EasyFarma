package view;

import controller.ReceitaController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class CadastrarReceita extends Application {
    private final ReceitaController receitaController;

    public CadastrarReceita(ReceitaController receitaController) {
        this.receitaController = receitaController;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Cadastrar Receita");

        // Layout principal
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        // Campos de entrada
        Label lblPaciente = new Label("Nome do Paciente:");
        TextField txtPaciente = new TextField();

        Label lblCPF = new Label("CPF do Paciente:");
        TextField txtCPF = new TextField();

        Label lblCRM = new Label("CRM do Médico:");
        TextField txtCRM = new TextField();

        Label lblMedicamento = new Label("Medicamentos (Nome Quantidade, Nome Quantidade):");
        TextField txtMedicamento = new TextField(); // Exemplo: "Ibuprofeno 2, Paracetamol 1"

        Label lblDataPrescricao = new Label("Data da Prescrição:");
        DatePicker dpDataPrescricao = new DatePicker();

        // Botões
        Button btnCadastrar = new Button("Cadastrar");
        Button btnCancelar = new Button("Cancelar");

        // Ações dos botões
        btnCadastrar.setOnAction(e -> {
            String paciente = txtPaciente.getText().trim();
            String cpf = txtCPF.getText().trim();
            String crm = txtCRM.getText().trim();
            String medicamentosEntrada = txtMedicamento.getText().trim(); // Captura os medicamentos como String
            String dataPrescricao = (dpDataPrescricao.getValue() != null) ? dpDataPrescricao.getValue().toString() : "";

            if (paciente.isEmpty() || cpf.isEmpty() || crm.isEmpty() || medicamentosEntrada.isEmpty() || dataPrescricao.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Todos os campos são obrigatórios!", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            // Chamar o método do controlador para cadastrar a receita
            receitaController.cadastrarReceita(paciente, cpf, crm, medicamentosEntrada, dataPrescricao);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Receita cadastrada com sucesso!", ButtonType.OK);
            alert.showAndWait();

            primaryStage.close();
        });

        btnCancelar.setOnAction(e -> primaryStage.close());

        // Adicionar elementos ao grid
        grid.add(lblPaciente, 0, 0);
        grid.add(txtPaciente, 1, 0);
        grid.add(lblCPF, 0, 1);
        grid.add(txtCPF, 1, 1);
        grid.add(lblCRM, 0, 2);
        grid.add(txtCRM, 1, 2);
        grid.add(lblMedicamento, 0, 3);
        grid.add(txtMedicamento, 1, 3);
        grid.add(lblDataPrescricao, 0, 4);
        grid.add(dpDataPrescricao, 1, 4);

        HBox buttonBox = new HBox(10, btnCadastrar, btnCancelar);
        buttonBox.setAlignment(Pos.CENTER);
        grid.add(buttonBox, 0, 5, 2, 1);

        // Cena
        Scene scene = new Scene(grid, 500, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
