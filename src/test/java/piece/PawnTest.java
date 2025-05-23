package test.java.piece;

import board.Board;
import board.PieceFactory;
import data.MoveResult;
import data.PieceColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import piece.Pawn;
import piece.Piece;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PawnTest {
    private Board testBoard;

    @BeforeEach
    void setUp() {
        // 빈 보드 생성 (기본적으로 모든 칸이 비어 있음)
        testBoard = new Board(false);
    }

    /**
     * - 체스게임은 아래쪽이 화이트 말입니다.
     * - White Major Pieces는 row[7]에, White Pawn은 row[6]에 배치됩니다.
     * - 따라서 White Pawn은 전진 방향이 **row 감소** (예: 6 → 5 → 4)여야 합니다.
     */

    // 1. 단식 전진 테스트: White Pawn이 (6,4)에서 (5,4)로 한 칸 전진할 수 있어야 함
    @Test
    void testPawnSingleStepMove() {
        Piece whitePawn = PieceFactory.createPieceFromSymbol("P");
        // **수정됨: White Pawn을 row=6, col=4에 배치 (보드 아래쪽)**
        testBoard.setPieceTest(6, 4, whitePawn);

        // 전진: (6,4) → (5,4)
        MoveResult moved = testBoard.movePiece(6, 4, 5, 4);
        assertTrue(moved == MoveResult.SUCCESS, "화이트 Pawn은 한 칸 전진 가능해야 한다. (6,4)→(5,4)");
    }

    // 2. 두 칸 전진 테스트: Pawn의 초기 위치에서 (6,4)에서 (4,4)로 이동 (중간 칸 (5,4)가 비어 있어야 함)
    @Test
    void testPawnDoubleStepMove() {
        Piece whitePawn = PieceFactory.createPieceFromSymbol("P");
        // **수정됨: 초기 위치 row=6, col=4**
        testBoard.setPieceTest(6, 4, whitePawn);

        // 전진: (6,4) → (4,4) (2칸 전진)
        MoveResult moved = testBoard.movePiece(6, 4, 4, 4);
        assertTrue(moved == MoveResult.SUCCESS, "화이트 Pawn은 초기 위치에서 두 칸 전진 가능해야 한다. (6,4)→(4,4)");
    }

    // 3. 전진 경로에 기물이 있으면 이동 불가 테스트
    @Test
    void testPawnBlockedForwardMove() {
        Piece whitePawn = PieceFactory.createPieceFromSymbol("P");
        Piece blocker = PieceFactory.createPieceFromSymbol("P");
        // **수정됨: White Pawn을 row=6, col=4에 배치**
        testBoard.setPieceTest(6, 4, whitePawn);
        // 전진 경로인 (5,4)에 아군 blocker 배치
        testBoard.setPieceTest(5, 4, blocker);

        // 한 칸 전진 시도: (6,4) → (5,4) → 실패해야 함
        MoveResult singleMoved = testBoard.movePiece(6, 4, 5, 4);
        assertFalse(singleMoved == MoveResult.SUCCESS, "전진 경로에 기물이 있으면 Pawn은 한 칸 전진할 수 없어야 한다.");

        // 두 칸 전진 시도: (6,4) → (4,4) → 역시 실패해야 함
        MoveResult doubleMoved = testBoard.movePiece(6, 4, 4, 4);
        assertFalse(doubleMoved == MoveResult.SUCCESS, "전진 경로에 기물이 있으면 Pawn은 두 칸 전진할 수 없어야 한다.");
    }

    // 4. 대각 캡처 테스트: White Pawn이 (6,4)에서 대각선 (5,3) 또는 (5,5)로 이동하여 적 기물을 캡처할 수 있어야 한다.
    @Test
    void testPawnCaptureDiagonal() {
        Piece whitePawn = PieceFactory.createPieceFromSymbol("P");
        // **수정됨: 화이트 Pawn 배치는 (6,4)에서 시작**
        testBoard.setPieceTest(6, 4, whitePawn);

        // 대각 왼쪽 캡처: (6,4) → (5,3)
        Piece blackPawnLeft = PieceFactory.createPieceFromSymbol("p");
        testBoard.setPieceTest(5, 3, blackPawnLeft);
        MoveResult capturedLeft = testBoard.movePiece(6, 4, 5, 3);
        assertTrue(capturedLeft == MoveResult.SUCCESS, "화이트 Pawn은 대각선 왼쪽에 있는 적 기물을 캡처할 수 있어야 한다. (6,4)→(5,3)");

        // Reset: 동일 Pawn 다시 (6,4)에 배치
        testBoard.setPieceTest(6, 4, whitePawn);

        // 대각 오른쪽 캡처: (6,4) → (5,5)
        Piece blackPawnRight = PieceFactory.createPieceFromSymbol("p");
        testBoard.setPieceTest(5, 5, blackPawnRight);
        MoveResult capturedRight = testBoard.movePiece(6, 4, 5, 5);
        assertTrue(capturedRight == MoveResult.SUCCESS, "화이트 Pawn은 대각선 오른쪽에 있는 적 기물을 캡처할 수 있어야 한다. (6,4)→(5,5)");
    }

    // 5. 정면으로의 캡처 불가 테스트: 정면 칸에 적 기물이 있어도 전진 캡처는 불가능해야 한다.
    @Test
    void testPawnCannotCaptureStraightForward() {
        Piece whitePawn = PieceFactory.createPieceFromSymbol("P");
        // **수정됨: White Pawn 배치는 (6,4)에서 시작**
        testBoard.setPieceTest(6, 4, whitePawn);

        // 정면 전진 칸 (5,4)에 블랙 Pawn 배치
        Piece blackPawnFront = PieceFactory.createPieceFromSymbol("p");
        testBoard.setPieceTest(5, 4, blackPawnFront);

        MoveResult moved = testBoard.movePiece(6, 4, 5, 4);
        assertFalse(moved == MoveResult.SUCCESS, "화이트 Pawn은 정면 전진 칸에 적 기물이 있어도 캡처할 수 없어야 한다. (6,4)→(5,4)");
    }

    // 추가 테스트: 초기 위치가 아닌 곳에서는 2칸 전진 불가
    @Test
    void testPawnCannotDoubleStepAfterFirstMove() {
        Piece whitePawn = PieceFactory.createPieceFromSymbol("P");
        testBoard.setPieceTest(5, 4, whitePawn); // 이미 한 칸 전진한 상태

        MoveResult moved = testBoard.movePiece(5, 4, 3, 4); // 2칸 전진 시도
        assertFalse(moved == MoveResult.SUCCESS, "초기 위치를 벗어난 Pawn은 두 칸 전진할 수 없어야 한다. (5,4)→(3,4)");
    }

    // 추가 테스트: 뒤로 이동 불가
    @Test
    void testPawnCannotMoveBackward() {
        Piece whitePawn = PieceFactory.createPieceFromSymbol("P");
        testBoard.setPieceTest(5, 4, whitePawn);

        MoveResult moved = testBoard.movePiece(5, 4, 6, 4); // 뒤로 이동 시도
        assertFalse(moved == MoveResult.SUCCESS, "Pawn은 뒤로 이동할 수 없어야 한다. (5,4)→(6,4)");
    }

    // 추가 테스트: 제자리 이동(같은 칸 이동) 불가
    @Test
    void testPawnCannotStayInPlace() {
        Piece whitePawn = PieceFactory.createPieceFromSymbol("P");
        testBoard.setPieceTest(6, 4, whitePawn);

        MoveResult moved = testBoard.movePiece(6, 4, 6, 4); // 제자리 이동 시도
        assertFalse(moved == MoveResult.SUCCESS, "Pawn은 제자리 이동할 수 없어야 한다. (6,4)→(6,4)");
    }

    // 추가 테스트: 칸 전진할 때 중간에 기물이 있으면 이동 불가
    @Test
    void testPawnCannotDoubleStepOverPiece() {
        Piece whitePawn = PieceFactory.createPieceFromSymbol("P");
        Piece blocker = PieceFactory.createPieceFromSymbol("P");
        testBoard.setPieceTest(6, 4, whitePawn);
        testBoard.setPieceTest(5, 4, blocker); // 중간에 기물 있음

        MoveResult moved = testBoard.movePiece(6, 4, 4, 4); // 2칸 전진 시도
        assertFalse(moved == MoveResult.SUCCESS, "Pawn은 중간에 기물이 있으면 두 칸 전진할 수 없어야 한다. (6,4)→(4,4)");
    }
}
