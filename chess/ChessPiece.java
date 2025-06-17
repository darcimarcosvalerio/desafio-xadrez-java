// chess/ChessPiece.java
package chess;

import board.Board;
import board.Position;
import chess.enums.Color;

public abstract class ChessPiece {

    protected Position position; // Não é private para ser acessada pelas subclasses no mesmo pacote board
    private Color color;
    private int moveCount; // Contador de movimentos para regras como roque e en passant

    public ChessPiece(Board board, Color color) {
        this.position = null; // Começa sem posição no tabuleiro
        // A peça sabe em qual tabuleiro está, mas não passa o board no construtor dela.
        // O board é passado para ela no método placePiece do Board.
        this.color = color;
        this.moveCount = 0;
    }

    // Getter para o tabuleiro (board) - precisa ser acessado pelas peças para verificar movimentos
    // No entanto, ChessPiece não tem uma referência direta ao Board,
    // então precisa ser passada nos métodos de movimento ou acessar via ChessMatch.
    // Para simplificar, vou adicionar um atributo Board aqui e inicializá-lo com um setter.
    // Ou, podemos refatorar: piece(position) no Board já retorna a ChessPiece.

    // Correção: A peça PRECISA saber qual é o tabuleiro em que ela está para fazer os movimentos.
    // O construtor deve receber o Board.

    protected Board board; // A peça precisa de uma referência ao tabuleiro onde ela está

    public ChessPiece(Board board, Color color) {
        this.board = board;
        this.color = color;
        this.position = null; // Peça começa sem posição no tabuleiro
        this.moveCount = 0;
    }

    public Color getColor() {
        return color;
    }

    public Position getPosition() {
        return position;
    }

    protected Board getBoard() {
        return board;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public void increaseMoveCount() {
        moveCount++;
    }

    public void decreaseMoveCount() {
        moveCount--;
    }

    // Método abstrato para movimentos possíveis - cada peça implementa o seu
    public abstract boolean[][] possibleMoves();

    // Método concreto que verifica se pode mover para uma posição
    public boolean possibleMove(Position position) {
        return possibleMoves()[position.getRow()][position.getColumn()];
    }

    // Método que verifica se existe algum movimento possível para a peça
    public boolean isThereAnyPossibleMove() {
        boolean[][] mat = possibleMoves();
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat.length; j++) {
                if (mat[i][j]) {
                    return true;
                }
            }
        }
        return false;
    }

    // Método auxiliar para verificar se é uma peça do oponente
    protected boolean isThereOpponentPiece(Position position) {
        ChessPiece p = (ChessPiece) board.piece(position);
        return p != null && p.getColor() != color;
    }
}
