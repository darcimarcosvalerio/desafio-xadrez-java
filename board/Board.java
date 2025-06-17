// board/Board.java
package board;

import chess.ChessPiece; // Importe ChessPiece, pois o tabuleiro armazena peças de xadrez

public class Board {

    private int rows;
    private int columns;
    private ChessPiece[][] pieces; // Alterado para ChessPiece

    public Board(int rows, int columns) {
        if (rows < 1 || columns < 1) {
            throw new BoardException("Erro criando tabuleiro: deve haver pelo menos 1 linha e 1 coluna");
        }
        this.rows = rows;
        this.columns = columns;
        pieces = new ChessPiece[rows][columns]; // Alterado para ChessPiece
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public ChessPiece piece(int row, int column) { // Alterado para ChessPiece
        if (!positionExists(row, column)) {
            throw new BoardException("Posição não existe no tabuleiro");
        }
        return pieces[row][column];
    }

    public ChessPiece piece(Position position) { // Alterado para ChessPiece
        if (!positionExists(position)) {
            throw new BoardException("Posição não existe no tabuleiro");
        }
        return pieces[position.getRow()][position.getColumn()];
    }

    public void placePiece(ChessPiece piece, Position position) { // Alterado para ChessPiece
        if (hasPiece(position)) {
            throw new BoardException("Já existe uma peça na posição " + position);
        }
        pieces[position.getRow()][position.getColumn()] = piece;
        piece.position = position; // Define a posição da peça no tabuleiro
    }

    public ChessPiece removePiece(Position position) { // Alterado para ChessPiece
        if (!positionExists(position)) {
            throw new BoardException("Posição não existe no tabuleiro");
        }
        if (piece(position) == null) {
            return null; // Não há peça para remover
        }
        ChessPiece aux = piece(position);
        aux.position = null; // Remove a posição da peça
        pieces[position.getRow()][position.getColumn()] = null;
        return aux;
    }

    private boolean positionExists(int row, int column) {
        return row >= 0 && row < rows && column >= 0 && column < columns;
    }

    public boolean positionExists(Position position) {
        return positionExists(position.getRow(), position.getColumn());
    }

    public boolean hasPiece(Position position) {
        if (!positionExists(position)) {
            throw new BoardException("Posição não existe no tabuleiro");
        }
        return piece(position) != null;
    }

    // Retorna os movimentos possíveis de uma peça na dada posição
    public boolean[][] possibleMoves(Position position) {
        if (!hasPiece(position)) {
            throw new BoardException("Não há peça na posição de origem!");
        }
        return piece(position).possibleMoves();
    }
}
