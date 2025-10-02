import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
                    if (Double.parseDouble(valores[3]) == 2) {
                        saidas[0] = 0.0;
                    } else {
                        saidas[0] = 1.0;
                    }

                    baseList.add(new double[][] { entradas, saidas });
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo da base de dados: " + e.getMessage());
            return null;
        }

        return baseList.toArray(new double[0][][]);
    }

    public static double[][][] lerBaseOrdenada(String caminhoArquivo, boolean ordenar) {
        double[][][] base = lerBase(caminhoArquivo);

        if (base != null && ordenar) {
            // Ordena a base de dados pela coluna de saída (crescente)
            Arrays.sort(base, (amostra1, amostra2) -> Double.compare(amostra1[1][0], amostra2[1][0]));
        }

        return base;
    }

    public static class BaseSeparada {
        public double[][][] baseTreino;
        public double[][][] baseTeste;

        public BaseSeparada(double[][][] baseTreino, double[][][] baseTeste) {
            this.baseTreino = baseTreino;
            this.baseTeste = baseTeste;
        }
    }

    public static BaseSeparada separarBase(String caminhoArquivo) {
        double[][][] baseCompleta = lerBase(caminhoArquivo);
        return separarBase(baseCompleta);
    }

    public static BaseSeparada separarBase(double[][][] baseCompleta) {
        if (baseCompleta == null) {
            return null;
        }

        // Separar amostras por classe
        List<double[][]> classe0 = new ArrayList<>(); // Classe 0 (não-pele)
        List<double[][]> classe1 = new ArrayList<>(); // Classe 1 (pele)

        for (int i = 0; i < baseCompleta.length; i++) {
            double[][] amostra = baseCompleta[i];
            if (amostra[1][0] == 0.0) {
                classe0.add(amostra);
            } else {
                classe1.add(amostra);
            }
        }

        // Embaralhar as listas de cada classe para seleção aleatória
        Collections.shuffle(classe0);
        Collections.shuffle(classe1);

        // Calcular 75% de cada classe para treino
        int tamanhoTreinoClasse0 = (int) Math.round(classe0.size() * 0.75);
        int tamanhoTreinoClasse1 = (int) Math.round(classe1.size() * 0.75);

        // Criar listas para treino e teste
        List<double[][]> treinoList = new ArrayList<>();
        List<double[][]> testeList = new ArrayList<>();

        // Adicionar 75% de cada classe (aleatoriamente selecionados) ao treino
        for (int i = 0; i < tamanhoTreinoClasse0; i++) {
            treinoList.add(classe0.get(i));
        }
        for (int i = 0; i < tamanhoTreinoClasse1; i++) {
            treinoList.add(classe1.get(i));
        }

        // Adicionar os 25% restantes ao teste
        for (int i = tamanhoTreinoClasse0; i < classe0.size(); i++) {
            testeList.add(classe0.get(i));
        }
        for (int i = tamanhoTreinoClasse1; i < classe1.size(); i++) {
            testeList.add(classe1.get(i));
        }

        // Converter para arrays
        double[][][] baseTreino = treinoList.toArray(new double[0][][]);
        double[][][] baseTeste = testeList.toArray(new double[0][][]);

        /*
         * Base separada aleatoriamente: 245057 amostras total
         * - Classe 0 (não-pele): 194198 total (145649 treino, 48549 teste)
         * - Classe 1 (pele): 50859 total (38144 treino, 12715 teste)
         * Base de treino: 183793 amostras (75,0%) - selecionadas aleatoriamente
         * Base de teste: 61264 amostras (25,0%) - selecionadas aleatoriamente
         */

        return new BaseSeparada(baseTreino, baseTeste);
    }
}