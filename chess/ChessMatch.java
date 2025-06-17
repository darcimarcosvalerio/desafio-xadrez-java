// chess/ChessMatch.java
package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import board.Board;
import board.Position;
import chess.enums.Color;
import chess.pieces.*; // Importa todas as peças

public class ChessMatch {

    private Board board;
    private int turn;
    private Color currentPlayer;
    private boolean check; // Estado de xeque
    private boolean checkMate; // Estado de xeque-mate
    private ChessPiece enPassantVulnerable; // Peça vulnerável a en passant
    private ChessPiece promoted; // Peça promovida (para promoção de peão)

    private List<ChessPiece> piecesOnBoard = new ArrayList<>(); // Peças no tabuleiro
    private List<ChessPiece> capturedPieces = new ArrayList<>(); // Peças capturadas

    public ChessMatch() {
        board = new Board(8, 8); // Tabuleiro de 8x8
        turn = 1;
        currentPlayer = Color.WHITE; // Branco começa
        initialSetup(); // Posiciona as peças
    }

    public int getTurn() {
        return turn;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean getCheck() {
        return check;
    }

    public boolean getCheckMate() {
        return checkMate;
    }

    public ChessPiece getEnPassantVulnerable() {
        return enPassantVulnerable;
    }

    public ChessPiece getPromoted() {
        return promoted;
    }

    // Retorna a matriz de peças de xadrez para a UI
    public ChessPiece[][] getPieces() {
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                mat[i][j] = (ChessPiece) board.piece(i, j); // Cast para ChessPiece
            }
        }
        return mat;
    }

    // Retorna os movimentos possíveis de uma peça na origem
    public boolean[][] possibleMoves(ChessPosition sourcePosition) {
        Position position = sourcePosition.toPosition();
        validateSourcePosition(position);
        return board.piece(position).possibleMoves();
    }

    // Executa um movimento de xadrez
    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        validateSourcePosition(source); // Valida a posição de origem
        validateTargetPosition(source, target); // Valida a posição de destino
        ChessPiece capturedPiece = (ChessPiece) makeMove(source, target); // Realiza o movimento no tabuleiro

        // Verifica xeque
        if (testCheck(currentPlayer)) {
            undoMove(source, target, capturedPiece); // Desfaz o movimento se colocar em xeque
            throw new ChessException("Você não pode se colocar em xeque!");
        }

        // Lógica de promoção de peão
        promoted = null;
        if (board.piece(target) instanceof Pawn) {
            if ((board.piece(target).getColor() == Color.WHITE && target.getRow() == 0) ||
                (board.piece(target).getColor() == Color.BLACK && target.getRow() == 7)) {
                promoted = (ChessPiece) board.piece(target);
                promoted = replacePromotedPiece("Q"); // Padrão: promove para Rainha. Pode ser melhorado para pedir ao usuário.
            }
        }

        check = (testCheck(opponent(currentPlayer))) ? true : false; // Verifica se o movimento colocou o oponente em xeque

        if (testCheckMate(opponent(currentPlayer))) {
            checkMate = true;
        } else {
            nextTurn(); // Avança o turno
        }

        // #Specialmove en passant
        if (board.piece(target) instanceof Pawn && (target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2)) {
            enPassantVulnerable = (ChessPiece)board.piece(target);
        }
        else {
            enPassantVulnerable = null;
        }

        return capturedPiece;
    }

    // Substitui a peça promovida
    public ChessPiece replacePromotedPiece(String type) {
        if (promoted == null) {
            throw new IllegalStateException("Não há peça para ser promovida!");
        }
        if (!type.equals("B") && !type.equals("N") && !type.equals("R") && !type.equals("Q")) {
            return promoted; // Não promove se tipo inválido (pode ser melhorado para lançar exceção)
        }

        Position pos = promoted.getPosition();
        ChessPiece p = (ChessPiece)board.removePiece(pos);
        piecesOnBoard.remove(p);

        ChessPiece newPiece = newPiece(type, promoted.getColor());
        board.placePiece(newPiece, pos);
        piecesOnBoard.add(newPiece);

        return newPiece;
    }

    private ChessPiece newPiece(String type, Color color) {
        if (type.equals("B")) return new Bishop(board, color);
        if (type.equals("N")) return new Knight(board, color);
        if (type.equals("Q")) return new Queen(board, color);
        return new Rook(board, color);
    }


    // Realiza o movimento físico no tabuleiro
    private board.Piece makeMove(Position source, Position target) { // Retorna Piece para capturada
        ChessPiece p = (ChessPiece) board.removePiece(source);
        p.increaseMoveCount();
        board.placePiece(p, target);

        ChessPiece capturedPiece = (ChessPiece) board.removePiece(target); // Captura peça no destino
        if (capturedPiece != null) {
            piecesOnBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);
        }

        // #Specialmove roque pequeno (King side castling)
        if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
            Position sourceRook = new Position(source.getRow(), source.getColumn() + 3);
            Position targetRook = new Position(source.getRow(), source.getColumn() + 1);
            ChessPiece rook = (ChessPiece)board.removePiece(sourceRook);
            board.placePiece(rook, targetRook);
            rook.increaseMoveCount();
        }

        // #Specialmove roque grande (Queen side castling)
        if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
            Position sourceRook = new Position(source.getRow(), source.getColumn() - 4);
            Position targetRook = new Position(source.getRow(), source.getColumn() - 1);
            ChessPiece rook = (ChessPiece)board.removePiece(sourceRook);
            board.placePiece(rook, targetRook);
            rook.increaseMoveCount();
        }

        // #Specialmove en passant
        if (p instanceof Pawn) {
            if (source.getColumn() != target.getColumn() && capturedPiece == null) { // Foi uma captura e não tinha peça no destino
                Position pawnPosition;
                if (p.getColor() == Color.WHITE) {
                    pawnPosition = new Position(target.getRow() + 1, target.getColumn());
                } else {
                    pawnPosition = new Position(target.getRow() - 1, target.getColumn());
                }
                capturedPiece = (ChessPiece)board.removePiece(pawnPosition);
                capturedPieces.add(capturedPiece);
                piecesOnBoard.remove(capturedPiece);
            }
        }

        return capturedPiece;
    }

    // Desfaz um movimento (para verificar xeque)
    private void undoMove(Position source, Position target, ChessPiece capturedPiece) {
        ChessPiece p = (ChessPiece) board.removePiece(target);
        p.decreaseMoveCount();
        board.placePiece(p, source);

        if (capturedPiece != null) {
            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            piecesOnBoard.add(capturedPiece);
        }

        // #Specialmove undo roque pequeno
        if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
            Position sourceRook = new Position(source.getRow(), source.getColumn() + 3);
            Position targetRook = new Position(source.getRow(), source.getColumn() + 1);
            ChessPiece rook = (ChessPiece)board.removePiece(targetRook);
            board.placePiece(rook, sourceRook);
            rook.decreaseMoveCount();
        }

        // #Specialmove undo roque grande
        if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
            Position sourceRook = new Position(source.getRow(), source.getColumn() - 4);
            Position targetRook = new Position(source.getRow(), source.getColumn() - 1);
            ChessPiece rook = (ChessPiece)board.removePiece(targetRook);
            board.placePiece(rook, sourceRook);
            rook.decreaseMoveCount();
        }

        // #Specialmove undo en passant
        if (p instanceof Pawn) {
            if (source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable) { // Se foi uma captura en passant
                ChessPiece pawn = (ChessPiece)board.removePiece(target); // Remove o peão que foi para a casa vazia
                Position pawnPosition;
                if (p.getColor() == Color.WHITE) {
                    pawnPosition = new Position(3, target.getColumn()); // Volta para a linha 5 (índice 3)
                } else {
                    pawnPosition = new Position(4, target.getColumn()); // Volta para a linha 4 (índice 4)
                }
                board.placePiece(capturedPiece, pawnPosition); // Coloca a peça capturada de volta
                piecesOnBoard.add(capturedPiece); // Adiciona de volta à lista de peças no tabuleiro
            }
        }
    }


    // Valida se a posição de origem possui uma peça do jogador atual e se ela pode mover
    private void validateSourcePosition(Position position) {
        if (!board.hasPiece(position)) {
            throw new ChessException("Não há peça na posição de origem");
        }
        if (currentPlayer != ((ChessPiece) board.piece(position)).getColor()) {
            throw new ChessException("A peça escolhida não é sua");
        }
        if (!((ChessPiece) board.piece(position)).isThereAnyPossibleMove()) {
            throw new ChessException("Não há movimentos possíveis para a peça escolhida");
        }
    }

    // Valida se a posição de destino é um movimento válido para a peça de origem
    private void validateTargetPosition(Position source, Position target) {
        if (!board.piece(source).possibleMove(target)) {
            throw new ChessException("A peça escolhida não pode mover para a posição de destino");
        }
    }

    // Retorna o oponente da cor atual
    private Color opponent(Color color) {
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    // Encontra o Rei de uma determinada cor
    private ChessPiece king(Color color) {
        List<ChessPiece> list = piecesOnBoard.stream().filter(x -> x.getColor() == color && x instanceof King).collect(Collectors.toList());
        if (list.isEmpty()) {
            throw new IllegalStateException("Não há rei " + color + " no tabuleiro!");
        }
        return list.get(0);
    }

    // Verifica se a cor especificada está em xeque
    private boolean testCheck(Color color) {
        Position kingPosition = king(color).getPosition(); // Posição do rei da cor
        // Pega todas as peças do oponente
        List<ChessPiece> opponentPieces = piecesOnBoard.stream().filter(x -> x.getColor() == opponent(color)).collect(Collectors.toList());
        for (ChessPiece p : opponentPieces) {
            boolean[][] mat = p.possibleMoves();
            if (mat[kingPosition.getRow()][kingPosition.getColumn()]) { // Se alguma peça do oponente pode mover para a posição do rei
                return true;
            }
        }
        return false;
    }

    // Verifica se a cor especificada está em xeque-mate
    private boolean testCheckMate(Color color) {
        if (!testCheck(color)) { // Se não está em xeque, não pode ser xeque-mate
            return false;
        }
        List<ChessPiece> list = piecesOnBoard.stream().filter(x -> x.getColor() == color).collect(Collectors.toList());
        for (ChessPiece p : list) { // Para cada peça da cor que está em xeque
            boolean[][] mat = p.possibleMoves(); // Pega os movimentos possíveis da peça
            for (int i = 0; i < board.getRows(); i++) {
                for (int j = 0; j < board.getColumns(); j++) {
                    if (mat[i][j]) { // Se a peça pode mover para a posição [i][j]
                        Position source = p.getPosition();
                        Position target = new Position(i, j);
                        ChessPiece capturedPiece = (ChessPiece) makeMove(source, target); // Simula o movimento
                        boolean testCheck = testCheck(color); // Verifica se ainda está em xeque
                        undoMove(source, target, capturedPiece); // Desfaz o movimento simulado
                        if (!testCheck) { // Se o movimento tirou do xeque
                            return false; // Não é xeque-mate
                        }
                    }
                }
            }
        }
        return true; // Se nenhuma peça pode mover para tirar do xeque, é xeque-mate
    }

    // Avança para o próximo turno
    private void nextTurn() {
        turn++;
        currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    // Coloca uma nova peça na posição de xadrez (ex: 'a1', 'e4')
    private void placeNewPiece(char column, int row, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        piecesOnBoard.add(piece); // Adiciona à lista de peças no tabuleiro
    }

    // Configuração inicial das peças no tabuleiro
    private void initialSetup() {
        // Peças Brancas
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('b', 1, new Knight(board, Color.WHITE));
        placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('d', 1, new Queen(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE));
        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('g', 1, new Knight(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE, this)); // Peão precisa da referência da partida para en passant
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE, this));

        // Peças Pretas
        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('b', 8, new Knight(board, Color.BLACK));
        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('d', 8, new Queen(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK));
        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('g', 8, new Knight(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK, this));
    }
}
