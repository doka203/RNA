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
            double erroEpoca = 0;

            for (int a = 0; a < base.length; a++) {
                double[] x = base[a][0];
                double[] y = base[a][1];

                double[] out = rna.treinar(x, y); // Realiza o treinamento de uma amostra, ajustando os pesos

                // Calcula o erro da amostra
                double erroAmostra = 0;
                for (int j = 0; j < y.length; j++) {
                    erroAmostra += Math.abs(y[j] - out[j]);
                }
                erroEpoca += erroAmostra;
            }

            System.out.printf("Erro da época " + e + ": " + erroEpoca + "\n");
        }
    }
}