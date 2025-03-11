package dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Estoque;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

public class EstoqueDAO {
    private Estoque estoque;
    private static final String ARQUIVO_ESTOQUE = "estoque.json";

    public EstoqueDAO() {
        this.estoque = carregarEstoque();
    }

    public Estoque getEstoque() {
        return estoque;
    }

    public void adicionarMedicamento(String nome, int quantidade) {
        estoque.adicionarMedicamento(nome, quantidade);
        salvarEstoque();
    }

    public void recarregarEstoque() {
        this.estoque = carregarEstoque();
    }


    public int consultarEstoque(String nome) {
        return estoque.consultarEstoque(nome);
    }

    public boolean diminuirEstoque(String nome, int quantidade) {
        boolean reduzido = estoque.diminuirEstoque(nome, quantidade);
        if (reduzido) {
            salvarEstoque();
        }
        return reduzido;
    }

    private void salvarEstoque() {
        try (FileWriter writer = new FileWriter(ARQUIVO_ESTOQUE)) {
            Gson gson = new Gson();
            gson.toJson(estoque, writer);
            System.out.println("Estoque salvo com sucesso no arquivo: " + ARQUIVO_ESTOQUE);
        } catch (IOException e) {
            System.err.println("Erro ao salvar estoque: " + e.getMessage());
        }
    }

    private Estoque carregarEstoque() {
        File file = new File(ARQUIVO_ESTOQUE);
        if (!file.exists()) {
            System.out.println("Arquivo " + ARQUIVO_ESTOQUE + " não encontrado. Iniciando com estoque vazio.");
            return new Estoque();
        }

        try (FileReader reader = new FileReader(file)) {
            Gson gson = new Gson();
            Type estoqueType = new TypeToken<Estoque>() {}.getType();
            Estoque estoqueCarregado = gson.fromJson(reader, estoqueType);
            System.out.println("Estoque carregado com sucesso do arquivo: " + ARQUIVO_ESTOQUE);
            return estoqueCarregado != null ? estoqueCarregado : new Estoque();
        } catch (IOException e) {
            System.err.println("Erro ao carregar estoque: " + e.getMessage());
            return new Estoque();
        }
    }
}
