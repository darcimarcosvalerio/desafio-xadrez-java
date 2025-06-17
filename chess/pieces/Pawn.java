// chess/pieces/Pawn.java
package chess.pieces;

import board.Board;
import board.Position;
import chess.ChessMatch; // Necessário para a referência ao ChessMatch para 'en passant'
import chess.ChessPiece;
import chess.enums.Color;

public class Pawn extends ChessPiece {

    private ChessMatch chessMatch; // Necessário para a regra 'en passant'

    public Pawn(Board board, Color color, ChessMatch chessMatch) {
        super(board, color);
        this.chessMatch = chessMatch; // Recebe a referência da partida
    }

    @Override
    public String toString() {
        return "P"; // Representação para o peão
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];

        Position p = new Position(0, 0);

        if (getColor() == Color.WHITE) {
            // 1 casa para frente
            p.setValues(position.getRow() - 1, position.getColumn());
            if (getBoard().positionExists(p) && !getBoard().hasPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }
            // 2 casas para frente (primeiro movimento)
            p.setValues(position.getRow() - 2, position.getColumn());
            Position p2 = new Position(position.getRow() - 1, position.getColumn()); // Casa intermediária
            if (getBoard().positionExists(p) && !getBoard().hasPiece(p) && getBoard().positionExists(p2) && !getBoard().hasPiece(p2) && getMoveCount() == 0) {
                mat[p.getRow()][p.getColumn()] = true;
            }
            // Captura diagonal esquerda
            p.setValues(position.getRow() - 1, position.getColumn() - 1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }
            // Captura diagonal direita
            p.setValues(position.getRow() - 1, position.getColumn() + 1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }

            // #Specialmove en passant white
            if (position.getRow() == 3) { // Na 5ª linha (índice 3 para branco)
                Position left = new Position(position.getRow(), position.getColumn() - 1);
                if (getBoard().positionExists(left) && isThereOpponentPiece(left) && getBoard().piece(left) == chessMatch.getEnPassantVulnerable()) {
                    mat[left.getRow() - 1][left.getColumn()] = true;
                }
                Position right = new Position(position.getRow(), position.getColumn() + 1);
                if (getBoard().positionExists(right) && isThereOpponentPiece(right) && getBoard().piece(right) == chessMatch.getEnPassantVulnerable()) {
                    mat[right.getRow() - 1][right.getColumn()] = true;
                }
            }
        } else { // Color.BLACK
            // 1 casa para frente
            p.setValues(position.getRow() + 1, position.getColumn());
            if (getBoard().positionExists(p) && !getBoard().hasPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }
            // 2 casas para frente (primeiro movimento)
            p.setValues(position.getRow() + 2, position.getColumn());
            Position p2 = new Position(position.getRow() + 1, position.getColumn()); // Casa intermediária
            if (getBoard().positionExists(p) && !getBoard().hasPiece(p) && getBoard().positionExists(p2) && !getBoard().hasPiece(p2) && getMoveCount() == 0) {
                mat[p.getRow()][p.getColumn()] = true;
            }
            // Captura diagonal esquerda
            p.setValues(position.getRow() + 1, position.getColumn() - 1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }
            // Captura diagonal direita
            p.setValues(position.getRow() + 1, position.getColumn() + 1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }

            // #Specialmove en passant black
            if (position.getRow() == 4) { // Na 4ª linha (índice 4 para preto)
                Position left = new Position(position.getRow(), position.getColumn() - 1);
                if (getBoard().positionExists(left) && isThereOpponentPiece(left) && getBoard().piece(left) == chessMatch.getEnPassantVulnerable()) {
                    mat[left.getRow() + 1][left.getColumn()] = true;
                }
                Position right = new Position(position.getRow(), position.getColumn() + 1);
                if (getBoard().positionExists(right) && isThereOpponentPiece(right) && getBoard().piece(right) == chessMatch.getEnPassantVulnerable()) {
                    mat[right.getRow() + 1][right.getColumn()] = true;
                }
            }
        }
        return mat;
    }
}
