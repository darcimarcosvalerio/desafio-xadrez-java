# desafio-xadrez-java
Estacio_Jogo de Xadrez
# Desafio Xadrez em Java (Console)

Este repositório contém a implementação de um jogo de Xadrez básico em Java, executado via console. O projeto foi desenvolvido para atender aos requisitos de um desafio da disciplina de [Nome da Disciplina, se houver].

## Sumário

* [Visão Geral](#visão-geral)
* [Funcionalidades Implementadas](#funcionalidades-implementadas)
* [Funcionalidades a Serem Implementadas/Melhoradas](#funcionalidades-a-serem-implementadasmelhoradas)
* [Estrutura do Projeto](#estrutura-do-projeto)
* [Como Compilar e Executar](#como-compilar-e-executar)
* [Requisitos](#requisitos)
* [Tecnologias Utilizadas](#tecnologias-utilizadas)
* [Autor](#autor)
* [Licença](#licença)

## Visão Geral

Este projeto simula uma partida de xadrez para dois jogadores (um humano controlando as peças brancas e oponentes alternando) em um ambiente de terminal. O foco principal foi na implementação da lógica central do xadrez, incluindo o movimento básico de todas as peças, capturas, detecção de xeque e xeque-mate.

## Funcionalidades Implementadas

* Tabuleiro de xadrez 8x8.
* Representação das peças no console (ex: `K` para Rei, `R` para Torre, etc.).
* Movimento básico e captura para todas as peças:
    * **Rei** (`K`): Uma casa em qualquer direção.
    * **Torre** (`R`): Qualquer número de casas em linha reta (horizontal ou vertical).
    * **Bispo** (`B`): Qualquer número de casas em diagonal.
    * **Cavalo** (`N`): Em forma de "L".
    * **Rainha** (`Q`): Combinação de movimentos da Torre e do Bispo.
    * **Peão** (`P`): Uma casa para frente, duas casas no primeiro movimento, captura diagonal.
* **Xeque:** Detecção quando o Rei está sob ataque.
* **Xeque-Mate:** Detecção de xeque-mate (quando o Rei está em xeque e não há movimentos legais para escapar).
* **Promoção de Peão:** Lógica básica de promoção (automaticamente para Rainha, pode ser melhorado para escolha do usuário).
* **En Passant:** Implementação básica da captura especial.
* **Roque (Castling):** Lógica de movimento da torre e rei em `makeMove`/`undoMove`, mas as *validações de elegibilidade para o roque não estão completas*.
* Registro de peças capturadas.
* Limpeza de tela para melhor experiência no console.

## Funcionalidades a Serem Implementadas/Melhoradas

* **Validação Completa do Roque:** Incluir verificações se o Rei e a Torre nunca se moveram, se as casas entre eles estão vazias e se o Rei não passa ou para em casas atacadas.
* **Escolha de Promoção do Peão:** Permitir que o jogador escolha a peça para a qual o peão será promovido (Torre, Bispo, Cavalo, Rainha).
* **Regras de Empate:** Implementar regras como:
    * Rei Afogado (Stalemate).
    * Regra das 50 jogadas.
    * Repetição de três lances.
    * Material insuficiente.
* **Validação de Movimentos Legais:** O sistema já verifica se o movimento coloca o próprio Rei em xeque. Melhorar a validação para que a peça não possa fazer um movimento que resulte em seu próprio rei estar em xeque.
* **Interface Gráfica (GUI):** Migrar para uma interface gráfica (JavaFX, Swing) para uma experiência de usuário mais rica.
* **Validação de Movimentos do Peão:** Aprimorar a lógica do peão para incluir mais detalhes de validação de avanço e captura.
* **Undo/Redo:** Funcionalidade de desfazer/refazer movimentos.
* **IA para o Oponente:** Implementar um módulo de inteligência artificial para que o jogo possa ser jogado contra o computador.

## Estrutura do Projeto

O projeto está organizado em pacotes para melhor modularidade e separação de responsabilidades:

seu-projeto-xadrez/
├── src/
│   ├── application/      # Contém a classe principal (Program) e a interface de usuário (UI)
│   │   ├── Program.java
│   │   └── UI.java
│   ├── board/            # Classes que representam o tabuleiro e suas posições
│   │   ├── Board.java
│   │   └── Position.java
│   │   └── BoardException.java # Exceção para erros do tabuleiro
│   └── chess/            # Lógica central do jogo de xadrez
│       ├── ChessException.java   # Exceção personalizada para o xadrez
│       ├── ChessMatch.java     # Gerencia a partida de xadrez, turnos, regras
│       ├── ChessPiece.java     # Classe abstrata base para todas as peças de xadrez
│       ├── ChessPosition.java  # Converte coordenadas de xadrez (a1) para posições internas
│       ├── pieces/             # Implementações concretas de cada tipo de peça
│       │   ├── Bishop.java
│       │   ├── King.java
│       │   ├── Knight.java
│       │   ├── Pawn.java
│       │   ├── Queen.java
│       │   └── Rook.java
│       └── enums/              # Enumerações para cores das peças
│           └── Color.java
## Como Compilar e Executar

Certifique-se de ter o **Java Development Kit (JDK)** instalado em sua máquina.

1.  **Baixe ou clone este repositório:**
    ```bash
    git clone [https://github.com/seu-usuario/desafio-xadrez-java.git](https://github.com/seu-usuario/desafio-xadrez-java.git)
    cd desafio-xadrez-java
    ```

2.  **Navegue até a pasta `src`:**
    ```bash
    cd src
    ```

3.  **Compile todos os arquivos Java:**
    ```bash
    javac application/*.java board/*.java chess/*.java chess/pieces/*.java chess/enums/*.java
    ```
    *Se você estiver usando uma IDE (como IntelliJ IDEA ou Eclipse), a compilação é geralmente automática.*

4.  **Execute o jogo:**
    Permaneça na pasta `src` e execute a classe `Program` do pacote `application`:
    ```bash
    java application.Program
    ```

## Requisitos

* Java Development Kit (JDK) 11 ou superior.

## Tecnologias Utilizadas

* Java

## Autor

[Darcimarcos Valerio Leite]
[www.linkedin.com/in/darcimarcos]

## Licença

Este projeto está licenciado sob a Licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.


