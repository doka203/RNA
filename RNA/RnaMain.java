import java.util.Scanner;

public class RnaMain {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int tipoRede;
        int entrada;
        do {
            System.out.println("Selecione o tipo de Rede Neural:");
            System.out.println("1 - Perceptron Simples");
            System.out.println("2 - MLP (Multi-Layer Perceptron)");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");
            tipoRede = sc.nextInt();

            if (tipoRede != 1 && tipoRede != 2) {
                if (tipoRede != 0) {
                    System.out.println("\nOpção inválida. Tente novamente.\n");
                } else {
                    System.out.println("\nSaindo...\n");
                }
                continue;
            }

            do {
                int epocas = 10000;
                RNA redeNeural = null;

                if (tipoRede == 1) {
                    System.out.println("\n--- Perceptron Simples ---");
                } else {
                    System.out.println("\n--- MLP (Multi-Layer Perceptron) ---");
                }
                System.out.println("Selecione a base de dados:");
                System.out.println("1 - Porta Lógica 'E'");
                System.out.println("2 - Porta Lógica 'OU'");
                System.out.println("3 - Porta Lógica 'XOR'");
                System.out.println("4 - Movimentação do Robô");
                System.out.println("5 - Skin Segmentation");
                System.out.println("0 - Sair");
                System.out.print("Opção: ");
                entrada = sc.nextInt();

                switch (entrada) {
                    case 1:
                        double[][][] baseE = {
                                { { 0, 0 }, { 0 } },
                                { { 0, 1 }, { 0 } },
                                { { 1, 0 }, { 0 } },
                                { { 1, 1 }, { 1 } } };
                        if (tipoRede == 1) {
                            redeNeural = new Perceptron(2, 1);
                        } else {
                            redeNeural = new Mlp(2, 2, 1);
                        }
                        treinar(redeNeural, baseE, epocas);
                        break;
                    case 2:
                        double[][][] baseOR = {
                                { { 0, 0 }, { 0 } },
                                { { 0, 1 }, { 1 } },
                                { { 1, 0 }, { 1 } },
                                { { 1, 1 }, { 1 } } };
                        if (tipoRede == 1) {
                            redeNeural = new Perceptron(2, 1);
                        } else {
                            redeNeural = new Mlp(2, 2, 1);
                        }
                        treinar(redeNeural, baseOR, epocas);
                        break;
                    case 3:
                        double[][][] baseXOR = {
                                { { 0, 0 }, { 0 } },
                                { { 0, 1 }, { 1 } },
                                { { 1, 0 }, { 1 } },
                                { { 1, 1 }, { 0 } } };
                        if (tipoRede == 1) {
                            redeNeural = new Perceptron(2, 1);
                        } else {
                            redeNeural = new Mlp(2, 2, 1);
                        }
                        treinar(redeNeural, baseXOR, epocas);
                        break;
                    case 4:
                        double[][][] baseRobo = {
                                { { 0, 0, 0 }, { 1, 1 } },
                                { { 0, 0, 1 }, { 0, 1 } },
                                { { 0, 1, 0 }, { 1, 0 } },
                                { { 0, 1, 1 }, { 0, 1 } },
                                { { 1, 0, 0 }, { 1, 0 } },
                                { { 1, 0, 1 }, { 1, 0 } },
                                { { 1, 1, 0 }, { 1, 0 } },
                                { { 1, 1, 1 }, { 1, 0 } }, };
                        if (tipoRede == 1) {
                            redeNeural = new Perceptron(3, 2);
                        } else {
                            redeNeural = new Mlp(3, 2, 2);
                        }
                        treinar(redeNeural, baseRobo, epocas);
                        break;
                    case 5:
                        double[][][] baseSkin = LeitorBase.lerBase("RNA/Skin_NonSkin.txt");
                        LeitorBase.BaseSeparada baseSeparadaSkin = LeitorBase.separarBase(baseSkin);
                        if (baseSeparadaSkin != null) {
                            if (tipoRede == 1) {
                                redeNeural = new Perceptron(3, 1);
                                treinar (redeNeural, baseSkin, epocas);
                            } else {
                                System.out.print("\nQuantidade de neurônios na camada intermediária (qtdH): ");
                                int qtdH = sc.nextInt();
                                redeNeural = new Mlp(3, qtdH, 1);
                                treinar(redeNeural, baseSeparadaSkin.baseTreino, baseSeparadaSkin.baseTeste, epocas);
                            }
                        }
                        break;
                    case 0:
                        if (tipoRede == 1) {
                            System.out.println("\nSaindo do Perceptron.\n");
                        } else {
                            System.out.println("\nSaindo do MLP.\n");
                        }
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }

            } while (entrada != 0);
        } while (tipoRede != 0);
        sc.close();
    }

    // Treina a rede neural com a base de treino e teste
    public static void treinar(RNA rna, double[][][] base, double[][][] baseTeste, int epocas) {
        System.out.println("\n--- Iniciando Treinamento com " + rna.getClass().getName() + " ---");

        for (int e = 0; e < epocas; e++) {
            double erroAprox = 0.0, erroClassificacao = 0.0;
            double erroAproxTeste = 0.0, erroClassificacaoTeste = 0.0;

            // Treina com a base de treino
            for (int a = 0; a < base.length; a++) {
                double[] x = base[a][0];
                double[] y = base[a][1];

                double[] out = rna.treinar(x, y); // Realiza o treinamento de uma amostra, ajustando os pesos

                // Calcula o erro de aproximação da amostra
                double erroAproxAmostra = 0.0;
                for (int j = 0; j < y.length; j++) {
                    erroAproxAmostra += Math.abs(y[j] - out[j]);
                }
                erroAprox += erroAproxAmostra;

                // Aplica o limiar de disparo
                double[] out_t = new double[out.length];
                for (int j = 0; j < out.length; j++) {
                    if (out[j] >= 0.5) {
                        out_t[j] = 1.0;
                    } else {
                        out_t[j] = 0.0;
                    }
                }

                // Verifica se houve erro de classificação na amostra
                double erroClassificacaoAmostra = 0.0;
                for (int j = 0; j < y.length; j++) {
                    erroClassificacaoAmostra += Math.abs(out_t[j] - y[j]);
                }

                if (erroClassificacaoAmostra > 0.0) {
                    erroClassificacao += 1.0;
                }
            }

            // Testa com a base de teste
            for (int a = 0; a < baseTeste.length; a++) {
                double[] x = baseTeste[a][0];
                double[] y = baseTeste[a][1];

                double[] out = rna.treinar(x, y); // Realiza o treinamento de uma amostra, ajustando os pesos

                // Calcula o erro de aproximação da amostra
                double erroAproxAmostra = 0.0;
                for (int j = 0; j < y.length; j++) {
                    erroAproxAmostra += Math.abs(y[j] - out[j]);
                }
                erroAproxTeste += erroAproxAmostra;

                // Aplica o limiar de disparo
                double[] out_t = new double[out.length];
                for (int j = 0; j < out.length; j++) {
                    if (out[j] >= 0.5) {
                        out_t[j] = 1.0;
                    } else {
                        out_t[j] = 0.0;
                    }
                }

                // Verifica se houve erro de classificação na amostra
                double erroClassificacaoAmostra = 0.0;
                for (int j = 0; j < y.length; j++) {
                    erroClassificacaoAmostra += Math.abs(out_t[j] - y[j]);
                }

                if (erroClassificacaoAmostra > 0.0) {
                    erroClassificacaoTeste += 1.0;
                }
            }

            System.out.printf("Época: %d\t| Treino: %f\t- %.0f\t| Teste: %f\t- %.0f\n", e, erroAprox,
                    erroClassificacao, erroAproxTeste, erroClassificacaoTeste);
        }
    }
    public static void treinar(RNA rna, double[][][] base, int epocas) {
        System.out.println("\n--- Iniciando Treinamento com " + rna.getClass().getName() + " ---");

        for (int e = 0; e < epocas; e++) {
            double erroAprox = 0.0, erroClassificacao = 0.0;

            // Treina com a base de treino
            for (int a = 0; a < base.length; a++) {
                double[] x = base[a][0];
                double[] y = base[a][1];

                double[] out = rna.treinar(x, y); // Realiza o treinamento de uma amostra, ajustando os pesos

                // Calcula o erro de aproximação da amostra
                double erroAproxAmostra = 0.0;
                for (int j = 0; j < y.length; j++) {
                    erroAproxAmostra += Math.abs(y[j] - out[j]);
                }
                erroAprox += erroAproxAmostra;

                // Aplica o limiar de disparo
                double[] out_t = new double[out.length];
                for (int j = 0; j < out.length; j++) {
                    if (out[j] >= 0.5) {
                        out_t[j] = 1.0;
                    } else {
                        out_t[j] = 0.0;
                    }
                }

                // Verifica se houve erro de classificação na amostra
                double erroClassificacaoAmostra = 0.0;
                for (int j = 0; j < y.length; j++) {
                    erroClassificacaoAmostra += Math.abs(out_t[j] - y[j]);
                }

                if (erroClassificacaoAmostra > 0.0) {
                    erroClassificacao += 1.0;
                }
            }

            System.out.printf("Época: %d\t| Treino: %f\t- %.0f\n", e, erroAprox,
                    erroClassificacao);
        }
    }
}