import org.la4j.Matrix;

import org.la4j.matrix.DenseMatrix;

import org.la4j.matrix.dense.Basic2DMatrix;

import org.la4j.decomposition.EigenDecompositor;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Projeto {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner ler = new Scanner(System.in);
        double[][] mat1 = {{0, 3.5, 1.5, 0.39}, {0.4, 0, 0, 0}, {0, 0.6, 0, 0}, {0, 0, 0.5, 0}};
        double matteste[][] = {{1, 5, -1}, {0, -2, 1}, {-4, 0, 3}};
        double[][] mat2 = {{600, 200, 130, 40}};
        int n;

        //mat1.length //linhas

        //mat1[0].length //colunas

        double soma = 0;
        double mvalorp;
        double[] percentagem = new double[mat1.length];
        double[] vetor_proprio = new double[mat1.length];
        int k = 0;

        mat2 = calcular_matriz_transposta(mat2);
        double[][] fim = new double[mat1.length][mat2[0].length];

        if (verificar_multiplicacao(mat1,mat2,fim)) {
            System.out.println("Quantas gerações pretende calcular?");
            n = ler.nextInt();
            double[] taxacrescimento = new double[n];
            double[] vecsoma = new double[n+1];
            double[][] grafico= new double[mat1.length][n+1];
            soma = 0;
            for (int i=0;i<grafico.length;i++){
                grafico[i][0]=mat2[i][0];
                soma+=mat2[i][0];
            }
            vecsoma[k] = soma;
            k++;
            for (int i=0;i<n;i++){
                soma = 0;
                for (int j=0;j<mat1.length;j++) {
                    grafico[j][i+1] = fim[j][0];
                    mat2[j][0]= fim[j][0];
                    soma+=mat2[j][0];
                }
                vecsoma[k] = soma;
                k++;
                verificar_multiplicacao(mat1,mat2,fim);
            }
            print(grafico);
            printar(vecsoma);
            taxa_variacao(vecsoma, taxacrescimento);
            num_individuos(vecsoma);
            mvalorp = calcular_vetor_valor_próprio(mat1, percentagem, vetor_proprio);
            System.out.printf("Maior valor próprio -> %.4f\n", mvalorp);
            comp_assintotico(mvalorp);
            //printar(medias(grafico, vecsoma, n));
            printar(percentagem);
            gerar_ficheiro_texto(n, mat1, vecsoma, taxacrescimento, grafico, mvalorp, vetor_proprio, percentagem);
        }
        else{
            System.out.println("Não é possivel realizar a operação devido ao tamanho");
        }
    }

    public static double[][] calcular_matriz_transposta(double[][] matriz){
        double [][] matriztransposta =new double[matriz[0].length][matriz.length];
        for (int l=0;l<matriz.length;l++){
            for (int c=0;c<matriz[0].length;c++){
                matriztransposta[c][l]=matriz[l][c];
            }
        }
        return matriztransposta;
    }

    public static boolean verificar_multiplicacao(double[][] matriz1, double[][] matriz2, double[][] fim) {
        if (matriz1[0].length == matriz2.length) {
            calcular_multiplicacao_matrizes(matriz1, matriz2,fim);
            return true;
        } else {
            return false;
        }

    }

    public static void calcular_multiplicacao_matrizes(double[][] matriz1, double[][] matriz2,double[][] fim){
        for (int l = 0; l < matriz1.length; l++) {
            double soma = 0;
            for (int k = 0; k < matriz2.length; k++) {
                double valor = matriz1[l][k] * matriz2[k][0];
                soma = soma + valor;
            }
            fim[l][0] = soma;
        }
    }

    public static void num_individuos(double[] soma){
        Scanner ler = new Scanner(System.in);
        int n;
        System.out.println("Indique qual a geração que pretende consultar.");
        n = ler.nextInt();
        while (n < 0 || n >= soma.length) {
            System.out.println("Geração inválida.");
            System.out.println("Indique qual a geração que pretende consultar.");
            n = ler.nextInt();
        }
        System.out.printf("%.2f\n", soma[n]);
    }

    public static double calcular_vetor_valor_próprio(double[][] matriz, double[] percentagem, double[] vetor_proprio){
        double mai;
        Matrix a = new Basic2DMatrix(matriz);
        EigenDecompositor eigenD = new EigenDecompositor(a);
        Matrix [] mattD= eigenD.decompose();
        double[][] matA = mattD[0].toDenseMatrix().toArray();
        double[][] matB = mattD[1].toDenseMatrix().toArray();

        mai = maior_valor(matB, matA, percentagem, vetor_proprio);
        return mai;
    }

    public static double maior_valor(double[][] vetB, double[][] vetA, double[] percentagem, double[] vetor_proprio){
        int maiorc = 0;
        double maior = vetB[0][0];
        for (int i = 0; i < vetB.length; i++){
            if (Math.abs(vetB[i][i]) > maior){
                maior = vetB[i][i];
                maiorc = i;
            }
        }

        for (int j = 0; j < vetA.length; j++){
            System.out.printf("[%.2f]", vetA[j][maiorc]);
        }
        System.out.println();
        vetor_determinar_percentagem(maiorc, vetA, percentagem, vetor_proprio);
        return maior;
    }

    public static void vetor_determinar_percentagem(int coluna, double[][] vetA, double[] percentagem, double[] vetor_proprio){
        double soma = 0;

        for (int i = 0; i < vetA.length; i++){
            soma += vetA[i][coluna];
            vetor_proprio[i] = vetA[i][coluna];
        }

        for (int j = 0; j < vetA.length; j++){
            percentagem[j] = vetA[j][coluna] / soma * 100;
        }
    }


    //Nao esta terminado(fazer outro metodo para taxa em altura especifica)

    public static void taxa_variacao(double[] matriz, double[] taxacrescimento){
        for (int i = 0; i < matriz.length - 1; i++){
            taxacrescimento[i] = matriz[i+1]/matriz[i];
            System.out.printf("[%.2f]", taxacrescimento[i]);
        }
        System.out.println();
    }



    public static void comp_assintotico(double numero){
        double numf= Math.abs(numero);
        if (numf > 1){
            System.out.println("O número de indivíduos da população tenderá para +infinto.");
        }
        else if (numf == 1){
            System.out.println("O número de indivíduos da população permanecerá constante.");
        }
        else{
            System.out.println("O número de indivíduos da população tenderá para 0.");
        }
    }

    public static void gerar_ficheiro_texto(int n, double[][] matrizleslie, double[] numind, double[] taxacrescimento, double[][] matrizfinal, double maior_valor_normal, double[] vetor_proprio, double[] percentagem) throws FileNotFoundException {
        int i, j;
        PrintWriter out = new PrintWriter("Resultados.txt");
        out.print("k=");
        out.println(n);
        out.println("Matriz de Leslie");
        for (i = 0; i < matrizleslie.length; i++){
            for (j = 0; j < matrizleslie.length; j++){
                if (j == matrizleslie.length - 1){
                    out.printf("%.2f", matrizleslie[i][j]);
                }
                else{
                    out.printf("%.2f, ", matrizleslie[i][j]);
                }
            }
            out.println();
        }
        out.println();
        out.println("Número total de indivíduos");
        out.println("(t, Nt)");
        for (i = 0; i <= n; i++){
            out.printf("(%d, %.2f)\n", i, numind[i]);
        }
        out.println();
        out.println("Crescimento da população");
        out.println("(t, delta_t)");
        for (i = 0; i < n; i++){
            out.printf("(%d, %.2f)\n", i, taxacrescimento[i]);
        }
        out.println();
        out.println("Numero por classe (não normalizado)");
        out.print("(t, ");
        for (i = 0; i < matrizleslie.length; i++){
            if (i == matrizleslie.length - 1){
                out.printf("x%d)", i+1);
            }
            else{
                out.printf("x%d, ", i+1);
            }
        }
        out.println();
        for (i = 0; i <= n; i++){
            out.printf("(%d, ", i);
            for (j = 0; j < matrizleslie.length; j++){
                if (j == matrizleslie.length - 1){
                    out.printf("%.2f)", matrizfinal[j][i]);
                }
                else{
                    out.printf("%.2f, ", matrizfinal[j][i]);
                }
            }
            out.println();
        }
        out.println();
        out.println("Numero por classe (normalizado)");
        out.print("(t, ");
        for (i = 0; i < matrizleslie.length; i++){
            if (i == matrizleslie.length - 1){
                out.printf("x%d)", i+1);
            }
            else{
                out.printf("x%d, ", i+1);
            }
        }
        out.println();
        for (i = 0; i <= n; i++){
            out.printf("(%d, ", i);
            for (j = 0; j < matrizleslie.length; j++){
                if (j == matrizleslie.length - 1){
                    out.printf("%.2f)", matrizfinal[j][i]*100/numind[i]);
                }
                else{
                    out.printf("%.2f, ", matrizfinal[j][i]*100/numind[i]);
                }
            }
            out.println();
        }
        out.println();
        out.println("Maior valor próprio e vetor associado");
        out.printf("lambda=%.4f\n", maior_valor_normal);
        out.print("vetor proprio associado=(");
        for (i = 0; i < matrizleslie.length; i++){
            if (i == matrizleslie.length - 1){
                out.printf("%.2f)", vetor_proprio[i]);
            }
            else{
                out.printf("%.2f, ", vetor_proprio[i]);
            }
        }
        out.println();
        out.print("vetor proprio normalizado=(");
        for (i = 0; i < matrizleslie.length; i++){
            if (i == matrizleslie.length - 1){
                out.printf("%.2f)", percentagem[i]);
            }
            else{
                out.printf("%.2f, ", percentagem[i]);
            }
        }
        out.println();
        out.close();
    }












    public static void print(double[][] matriz){
        for(int i=0; i<matriz.length;i++)
        {
            for (int j=0; j<matriz[0].length; j++) {
                System.out.printf("[%.2f]", matriz[i][j]);
            }
            System.out.println();
        }
    }

    public static void printar(double[] matriz){
        for(int i=0; i<matriz.length;i++)
        {
            System.out.printf("[%.2f]", matriz[i]);
        }
        System.out.println();
    }
}
