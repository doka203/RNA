import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LeitorBase {

    public static double[][][] lerBase(String caminhoArquivo) {
        List<double[][]> baseList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                // Divide a linha pelos separadores
                String[] valores = linha.trim().split("\\s+");

                if (valores.length == 4) {
                    // Pega os 3 primeiros valores (B, G, R) e normaliza
                    double[] entradas = new double[3];
                    entradas[0] = Double.parseDouble(valores[0]) / 255.0;
                    entradas[1] = Double.parseDouble(valores[1]) / 255.0;
                    entradas[2] = Double.parseDouble(valores[2]) / 255.0;

                    double[] saidas = new double[1];
                    // Converte a classe 2 para 0 e se for 1, mantém 1
                    if (Double.parseDouble(valores[3]) == 2){
                        saidas[0] = 0.0;
                    }else{
                        saidas[0] = 1.0;
                    }
                    
                    baseList.add(new double[][]{entradas, saidas});
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo da base de dados: " + e.getMessage());
            return null;
        }

        return baseList.toArray(new double[0][][]);
    }
}