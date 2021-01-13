import org.la4j.Matrix;

import org.la4j.matrix.DenseMatrix;

import org.la4j.matrix.dense.Basic2DMatrix;

import org.la4j.decomposition.EigenDecompositor;

import java.util.Scanner;

public class Projeto {

    public static void main(String[] args) {
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
        int k = 0;

        mat2 = transposta(mat2);
        double[][] fim = new double[mat1.length][mat2[0].length];

        if (multiplicar1(mat1,mat2,fim)) {
            System.out.println("Quantas gerações pretende calcular?");
            n = ler.nextInt();
            double[] vecsoma = new double[n];
            double[][] grafico= new double[mat1.length][n];
            soma = 0;
            for (int i=0;i<grafico.length;i++){
                grafico[i][0]=mat2[i][0];
                soma+=mat2[i][0];
            }
            vecsoma[k] = soma;
            k++;
            for (int i=0;i<n-1;i++){
                soma = 0;
                for (int j=0;j<mat1.length;j++) {
                    grafico[j][i+1] = fim[j][0];
                    mat2[j][0]= fim[j][0];
                    soma+=mat2[j][0];
                }
                vecsoma[k] = soma;
                k++;
                multiplicar1(mat1,mat2,fim);
            }
            print(grafico);
            printar(vecsoma);
            taxa_variacao(vecsoma);
            num_individuos(vecsoma);
            mvalorp = calcular_vetor_próprio(mat1, percentagem);
            System.out.printf("Maior valor próprio -> %.4f\n", mvalorp);
            comp_assintotico(mvalorp);
            //printar(medias(grafico, vecsoma, n));
            printar(percentagem);
        }
        else{
            System.out.println("Não é possivel realizar a operação devido ao tamanho");
        }
    }

    public static double[][] transposta(double[][] matriz){
        double [][] matriztransposta =new double[matriz[0].length][matriz.length];
        for (int l=0;l<matriz.length;l++){
            for (int c=0;c<matriz[0].length;c++){
                matriztransposta[c][l]=matriz[l][c];
            }
        }
        return matriztransposta;
    }

    public static boolean multiplicar1(double[][] matriz1, double[][] matriz2, double[][] fim) {
        if (matriz1[0].length == matriz2.length) {
            multiplicar2(matriz1, matriz2,fim);
            return true;
        } else {
            return false;
        }

    }

    public static void multiplicar2(double[][] matriz1, double[][] matriz2,double[][] fim){
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

    public static double calcular_vetor_próprio(double[][] matriz, double[] percentagem){
        double mai;
        Matrix a = new Basic2DMatrix(matriz);
        EigenDecompositor eigenD = new EigenDecompositor(a);
        Matrix [] mattD= eigenD.decompose();
        for(int i=0; i<2;i++)
        {
            System.out.println(mattD[i]);
        }
        double[][] matA = mattD[0].toDenseMatrix().toArray();
        double[][] matB = mattD[1].toDenseMatrix().toArray();

        mai = maior_valor(matB, matA, percentagem);
        return mai;
    }

    public static double maior_valor(double[][] vetB, double[][] vetA, double[] percentagem){
        int maiorc = 0;
        double maior = vetB[0][0];
        for (int i = 0; i < vetB.length; i++){
            if (Math.abs(vetB[i][i]) > maior){
                maior = vetB[i][i];
                maiorc = i;
            }
        }
        vetor_proprio(maiorc, vetA, percentagem);
        return maior;
    }

    public static void vetor_proprio(int coluna, double[][] vetA, double[] percentagem){
        double soma = 0;

        for (int i = 0; i < vetA.length; i++){
            soma += vetA[i][coluna];
        }

        for (int j = 0; j < vetA.length; j++){
            percentagem[j] = vetA[j][coluna] / soma * 100;
        }
    }


    //Nao esta terminado(fazer outro metodo para taxa em altura especifica)

    public static void taxa_variacao(double[] matriz){
        double taxa;
        for (int i = 0; i < matriz.length - 1; i++){
            taxa = matriz[i+1]/matriz[i];
            System.out.printf("[%.2f]", taxa);
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
