// application/Program.java
package application;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Program {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ChessMatch chessMatch = new ChessMatch();
        List<ChessPiece> captured = new ArrayList<>(); // Lista de peças capturadas

        while (!chessMatch.getCheckMate()) {
            try {
                UI.clearScreen(); // Limpa a tela do console
                UI.printMatch(chessMatch, captured); // Imprime o tabuleiro e informações da partida

                System.out.println();
                System.out.print("Origem: ");
                ChessPosition source = UI.readChessPosition(sc); // Lê a posição de origem

                boolean[][] possibleMoves = chessMatch.possibleMoves(source);
                UI.clearScreen();
                UI.printBoard(chessMatch.getPieces(), possibleMoves); // Imprime tabuleiro com movimentos possíveis

                System.out.println();
                System.out.print("Destino: ");
                ChessPosition target = UI.readChessPosition(sc); // Lê a posição de destino

                ChessPiece capturedPiece = chessMatch.performChessMove(source, target); // Tenta realizar o movimento

                if (capturedPiece != null) {
                    captured.add(capturedPiece); // Adiciona peça capturada à lista
                }

                if (chessMatch.getPromoted() != null) { // Lógica de promoção de peão
                    System.out.print("Entre a peça para promoção (B/N/R/Q): ");
                    String type = sc.nextLine().toUpperCase();
                    while (!type.equals("B") && !type.equals("N") && !type.equals("R") & !type.equals("Q")) {
                        System.out.print("Valor inválido! Entre a peça para promoção (B/N/R/Q): ");
                        type = sc.nextLine().toUpperCase();
                    }
                    chessMatch.replacePromotedPiece(type);
                }

            } catch (ChessException e) {
                System.out.println(e.getMessage());
                sc.nextLine(); // Espera o usuário apertar Enter para continuar
            } catch (InputMismatchException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
            }
        }
        UI.clearScreen();
        UI.printMatch(chessMatch, captured); // Imprime o tabuleiro final com o resultado
        sc.close();
    }
}
