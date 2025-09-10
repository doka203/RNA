import java.util.Random;

public class Perceptron {
    private double[][] w; // Pesos da rede
    private final double ni; // Taxa de aprendizado
    private final int qtdIn;
    private final int qtdOut;
    private static final Random random = new Random();

    public Perceptron(int qtdIn, int qtdOut, double ni) {
        this.qtdIn = qtdIn;
        this.qtdOut = qtdOut;
        this.ni = ni;
        this.w = new double[qtdIn + 1][qtdOut];

         // Inicializa os pesos da rede com valores aleatórios
        for (int i = 0; i < w.length; i++) {
            for (int j = 0; j < w[i].length; j++) {
                w[i][j] = random.nextDouble() * 0.6 - 0.3; // Valores entre -0.3 e 0.3
            }
        }
    }

    // Realiza o treinamento de uma amostra, ajustando os pesos
    public double[] treinar(double[] xin, double[] y) {
        double[] x = new double[qtdIn + 1];
        x[0] = 1;

        for (int i = 1; i <= xin.length; i++) {
            x[i] = xin[i-1];
        }

        // Executa a amostra na rede
        double[] out = new double [this.qtdOut];
        for (int j = 0; j < qtdOut; j++) {
            double u = 0;
            for (int i = 0; i < qtdIn + 1; i++) {
                u += x[i] * w[i][j];
            }
            out[j] = 1 / (1 + Math.exp(-u));
        }

        // Calcula a variação dos pesos
        double[][] deltaW = new double[w.length][w[0].length];
        for (int j = 0; j < qtdOut; j++) {
            for (int i = 0; i < qtdIn + 1; i++) {
                deltaW[i][j] = ni * (y[j] - out[j]) * x[i];
                w[i][j] += deltaW[i][j];
            }
        }
        return out;
    }
}