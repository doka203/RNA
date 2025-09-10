import java.util.Scanner;

public class RnaMain {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int entrada;

        do {
            int epocas = 10000;

            System.out.println("\nSelecione o modo de entrada:");
            System.out.println("1 - Porta Lógica 'E'");
            System.out.println("2 - Porta Lógica 'OU'");
            System.out.println("3 - Porta Lógica 'XOR'");
            System.out.println("4 - Movimentação do Robô");
            System.out.println("5 - Skin Segmentation.txt");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");
            entrada = sc.nextInt();

            switch (entrada) {
                case 1:
                    double[][][] baseE = {
                            { { 0, 0 }, { 0 } },
                            { { 0, 1 }, { 0 } },
                            { { 1, 0 }, { 0 } },
                            { { 1, 1 }, { 1 } }
                    };
                    treinar(2, 1, baseE, epocas);
                    break;
                case 2:
                    double[][][] baseOU = {
                            { { 0, 0 }, { 0 } },
                            { { 0, 1 }, { 1 } },
                            { { 1, 0 }, { 1 } },
                            { { 1, 1 }, { 1 } }
                    };
                    treinar(2, 1, baseOU, epocas);
                    break;
                case 3:
                    double[][][] baseXOR = {
                            { { 0, 0 }, { 0 } },
                            { { 0, 1 }, { 1 } },
                            { { 1, 0 }, { 1 } },
                            { { 1, 1 }, { 0 } }
                    };
                    treinar(2, 1, baseXOR, epocas);
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
                            { { 1, 1, 1 }, { 1, 0 } },
                    };
                    treinar(3, 2, baseRobo, epocas);
                    break;
                case 5:
                    double[][][] baseSkin = LeitorBase.lerBase("Perceptron-Simples/Skin_NonSkin.txt");
                    if (baseSkin != null) {
                        treinar(3, 1, baseSkin, epocas);
                    }
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }

        } while (entrada != 0);
        sc.close();
    }

    // Treina uma rede Perceptron com uma base de dados
    public static void treinar(int qtdIn, int qtdOut, double[][][] base, int epocas) {
        Perceptron rna = new Perceptron(qtdIn, qtdOut, 0.3);

        for (int e = 0; e < epocas; e++) {
            double erroAproxEpoca = 0;
            double erroClassificacaoEpoca = 0;

            for (int a = 0; a < base.length; a++) {
                double[] x = base[a][0];
                double[] y = base[a][1];

                double[] out = rna.treinar(x, y); // Realiza o treinamento de uma amostra, ajustando os pesos

                // Calcula o erro de aproximação da amostra
                double erroAproxAmostra = 0;
                for (int j = 0; j < y.length; j++) {
                    erroAproxAmostra += Math.abs(y[j] - out[j]);
                }
                erroAproxEpoca += erroAproxAmostra;

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
                double erroClassificacaoAmostra = 0;
                for (int j = 0; j < y.length; j++) {
                    erroClassificacaoAmostra += Math.abs(y[j] - out_t[j]);
                }

                if (erroClassificacaoAmostra > 0) {
                    erroClassificacaoEpoca += 1;
                }
            }

            System.out.printf("Época: %d - %f - %.0f\n", e, erroAproxEpoca,
                    erroClassificacaoEpoca);
        }
    }
}