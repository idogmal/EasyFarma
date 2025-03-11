package util;

import model.Receita;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import viewswing.ReceitaExport;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class ExportadorReceitas {

    /**
     * Exporta a lista de receitas (model.Receita) para um arquivo CSV.
     * Usa ponto e vírgula como delimitador e inclui o BOM para UTF-8.
     *
     * @param receitas       Lista de receitas a serem exportadas.
     * @param caminhoArquivo Caminho (e nome) do arquivo CSV a ser gerado.
     */
    public static void exportarReceitasCSV(List<Receita> receitas, String caminhoArquivo) {
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(caminhoArquivo), "UTF-8")) {
            writer.write("\uFEFF");
            writer.write("ID;Paciente;CPF;DataPrescricao;Status;Medicamentos\n");
            for (Receita r : receitas) {
                writer.write(String.valueOf(r.getId()));
                writer.write(";");
                writer.write(r.getPaciente());
                writer.write(";");
                writer.write(r.getCpf() != null ? r.getCpf() : "");
                writer.write(";");
                writer.write(r.getDataPrescricao());
                writer.write(";");
                writer.write(r.getStatus());
                writer.write(";");
                writer.write(r.getMedicamentosAsString());
                writer.write("\n");
            }
            writer.flush();
            System.out.println("Exportação CSV concluída: " + caminhoArquivo);
        } catch (IOException e) {
            System.err.println("Erro ao exportar receitas: " + e.getMessage());
        }
    }

    /**
     * Exporta a lista de dados usando a classe auxiliar ReceitaExport para um arquivo CSV.
     * Usa ponto e vírgula como delimitador e inclui o BOM para UTF-8.
     *
     * @param receitasExport Lista de dados (ReceitaExport) a serem exportados.
     * @param caminhoArquivo Caminho (e nome) do arquivo CSV a ser gerado.
     */
    public static void exportarReceitasCSVExport(List<ReceitaExport> receitasExport, String caminhoArquivo) {
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(caminhoArquivo), "UTF-8")) {
            writer.write("\uFEFF");
            // Cabeçalho (sem ID, pois ReceitaExport não possui esse campo)
            writer.write("Paciente;CPF;DataPrescricao;Status;Medicamentos\n");
            for (ReceitaExport r : receitasExport) {
                writer.write(r.getPaciente());
                writer.write(";");
                writer.write(r.getCpf());
                writer.write(";");
                writer.write(r.getDataPrescricao());
                writer.write(";");
                writer.write(r.getStatus());
                writer.write(";");
                writer.write(r.getMedicamentos());
                writer.write("\n");
            }
            writer.flush();
            System.out.println("Exportação CSV (ReceitaExport) concluída: " + caminhoArquivo);
        } catch (IOException e) {
            System.err.println("Erro ao exportar receitas (ReceitaExport): " + e.getMessage());
        }
    }

    /**
     * Exporta a lista de dados (ReceitaExport) para um arquivo XLSX (Excel) usando Apache POI.
     *
     * @param receitasExport Lista de dados (ReceitaExport) a serem exportados.
     * @param caminhoArquivo Caminho (e nome) do arquivo XLSX a ser gerado.
     */
    public static void exportarReceitasXLSX(List<ReceitaExport> receitasExport, String caminhoArquivo) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Receitas");

        // Cria um estilo para o cabeçalho
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        // Cria a linha de cabeçalho
        String[] cabecalho = {"Paciente", "CPF", "DataPrescricao", "Status", "Medicamentos"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < cabecalho.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(cabecalho[i]);
            cell.setCellStyle(headerStyle);
        }

        // Preenche os dados
        int rowNum = 1;
        for (ReceitaExport r : receitasExport) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(r.getPaciente());
            row.createCell(1).setCellValue(r.getCpf());
            row.createCell(2).setCellValue(r.getDataPrescricao());
            row.createCell(3).setCellValue(r.getStatus());
            row.createCell(4).setCellValue(r.getMedicamentos());
        }

        // Autoajusta a largura das colunas
        for (int i = 0; i < cabecalho.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Grava o arquivo XLSX
        try (FileOutputStream fos = new FileOutputStream(caminhoArquivo)) {
            workbook.write(fos);
            System.out.println("Exportação XLSX concluída: " + caminhoArquivo);
        } catch (IOException e) {
            System.err.println("Erro ao exportar XLSX: " + e.getMessage());
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                // Ignorar exceção
            }
        }
    }
}
