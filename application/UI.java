// application/UI.java
package application;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.enums.Color;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class UI {

    // https://stackoverflow.com/questions/5762491/how-to-clear-console-in-java-eclipse
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // Cores do texto para o console (ANSI escape codes)
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    // Cores de fundo
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    // Lê uma posição de xadrez do usuário (ex: a1, e4)
    public static ChessPosition readChessPosition(Scanner sc) {
        try {
            String s = sc.nextLine();
            char column = s.charAt(0);
            int row = Integer.parseInt(s.substring(1));
            return new ChessPosition(column, row);
        } catch (RuntimeException e) {
            throw new InputMismatchException("Erro lendo ChessPosition. Valores válidos são de a1 a h8.");
        }
    }

    // Imprime a partida (tabuleiro, turno, xeque)
    public static void printMatch(ChessMatch chessMatch, List<ChessPiece> captured) {
        printBoard(chessMatch.getPieces());
        System.out.println();
        printCapturedPieces(captured);
        System.out.println();
        System.out.println("Turno: " + chessMatch.getTurn());
        if (!chessMatch.getCheckMate()) {
            System.out.println("Jogador atual: " + chessMatch.getCurrentPlayer());
            if (chessMatch.getCheck()) {
                System.out.println("XEQUE!");
            }
        }
        else {
            System.out.println("XEQUE-MATE!");
            System.out.println("Vencedor: " + chessMatch.getCurrentPlayer());
        }
    }

    // Imprime o tabuleiro
    public static void printBoard(ChessPiece[][] pieces) {
        for (int i = 0; i < pieces.length; i++) {
            System.out.print((8 - i) + " "); // Números das linhas (8 a 1)
            for (int j = 0; j < pieces.length; j++) {
                printPiece(pieces[i][j], false); // Imprime a peça na posição
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h"); // Letras das colunas
    }

    // Imprime o tabuleiro com movimentos possíveis destacados
    public static void printBoard(ChessPiece[][] pieces, boolean[][] possibleMoves) {
        for (int i = 0; i < pieces.length; i++) {
            System.out.print((8 - i) + " ");
            for (int j = 0; j < pieces.length; j++) {
                printPiece(pieces[i][j], possibleMoves[i][j]); // Destaca se for um movimento possível
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }

    // Imprime uma peça individualmente, com ou sem destaque
    private static void printPiece(ChessPiece piece, boolean background) {
        if (background) {
            System.out.print(ANSI_BLUE_BACKGROUND); // Cor de fundo para movimentos possíveis
        }
        if (piece == null) {
            System.out.print("-" + ANSI_RESET); // Vazio
        } else {
            if (piece.getColor() == Color.WHITE) {
                System.out.print(ANSI_WHITE + piece + ANSI_RESET); // Peça branca
            } else {
                System.out.print(ANSI_YELLOW + piece + ANSI_RESET); // Peça preta (ou outra cor para diferenciar)
            }
        }
        System.out.print(" "); // Espaço entre as peças
    }

    // Imprime as peças capturadas
    private static void printCapturedPieces(List<ChessPiece> captured) {
        List<ChessPiece> white = captured.stream().filter(x -> x.getColor() == Color.WHITE).collect(Collectors.toList());
        List<ChessPiece> black = captured.stream().filter(x -> x.getColor() == Color.BLACK).collect(Collectors.toList());
        System.out.println("Peças capturadas:");
        System.out.print("Brancas: ");
        System.out.print(ANSI_WHITE);
        System.out.println(Arrays.toString(white.toArray()));
        System.out.print(ANSI_RESET);
        System.out.print("Pretas: ");
        System.out.print(ANSI_YELLOW);
        System.out.println(Arrays.toString(black.toArray()));
        System.out.print(ANSI_RESET);
    }
}
