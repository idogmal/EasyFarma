package model;

import java.util.HashMap;
import java.util.Map;

public class Estoque {
    private HashMap<String, Integer> medicamentos;

    // Construtor
    public Estoque() {
        medicamentos = new HashMap<>();
    }

    // Adicionar medicamento ao estoque (ou aumentar quantidade existente)
    public void adicionarMedicamento(String nome, int quantidade) {
        if (nome == null || nome.isEmpty() || quantidade <= 0) {
            throw new IllegalArgumentException("Nome do medicamento e quantidade devem ser válidos.");
        }
        String nomePadronizado = nome.toLowerCase();
        medicamentos.put(nomePadronizado, medicamentos.getOrDefault(nomePadronizado, 0) + quantidade);
    }

    // Diminuir estoque (Redução após compra ou retirada)
    public boolean diminuirEstoque(String nome, int quantidade) {
        if (nome == null || nome.isEmpty() || quantidade <= 0) {
            throw new IllegalArgumentException("Nome do medicamento e quantidade devem ser válidos.");
        }

        String nomePadronizado = nome.toLowerCase(); // Padroniza o nome
        int atual = medicamentos.getOrDefault(nomePadronizado, 0);

        if (atual >= quantidade) {
            medicamentos.put(nomePadronizado, atual - quantidade);
            return true; // Estoque atualizado com sucesso
        }
        return false; // Estoque insuficiente
    }

    // Consultar quantidade disponível de um medicamento
    public int consultarEstoque(String nome) {
        if (nome == null || nome.isEmpty()) {
            throw new IllegalArgumentException("Nome do medicamento deve ser válido.");
        }

        String nomePadronizado = nome.toLowerCase();
        return medicamentos.getOrDefault(nomePadronizado, 0);
    }

    // Exibir o estoque completo (para depuração)
    public void exibirEstoque() {
        System.out.println("Estoque Atual:");
        if (medicamentos.isEmpty()) {
            System.out.println("O estoque está vazio.");
        } else {
            medicamentos.forEach((nome, quantidade) ->
                    System.out.println("Medicamento: " + nome + ", Quantidade: " + quantidade)
            );
        }
    }

    // Novo método getter para retornar o HashMap de medicamentos
    public Map<String, Integer> getMedicamentos() {
        return medicamentos;
    }

    @Override
    public String toString() {
        return "Estoque: " + medicamentos.toString();
    }
} ;