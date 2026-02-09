# Code Review Report

작성일: 2026-02-06
대상: `c:\workspace\Spring\boardv1`

## 요약
현재 코드베이스는 기본 기능이 동작하는 수준의 구조를 갖추고 있으나, 로그인 없이 접근 가능한 흐름에서 NPE가 발생할 수 있으며, 삭제 처리의 데이터 무결성 위험이 존재합니다. 인증/권한 처리 및 입력 검증이 UI 중심(알림/뒤로가기)에 집중되어 있어 API/비정상 요청에 대한 견고함이 떨어집니다.

## 주요 발견 사항 (심각도 순)

### 1. High — 로그인 없이 상세 접근 시 NPE 가능
- 위치: `src/main/java/com/example/boardv1/board/BoardResponse.java`
- 내용: `DetailDTO` 생성자에서 `sessionUserId`가 `null`일 수 있음에도 `equals()`를 호출합니다.
- 영향: 로그인 없이 상세 페이지/`/api/boards/{id}` 호출 시 NullPointerException 발생 가능.
- 권장 수정: 
  - `this.isOwner = sessionUserId != null && board.getUser().getId().equals(sessionUserId);`

### 2. High — 인증/권한 실패 응답 방식이 UI 중심
- 위치: `src/main/java/com/example/boardv1/_core/errors/GlobalExceptionHandler.java`, `BoardController`, `ReplyController`
- 내용: 예외 처리 결과가 JS `alert` + `history.back()` 또는 로그인 페이지 이동으로 고정되어 API/비정상 요청에 취약.
- 영향: 자동화 테스트, API 호출, 외부 클라이언트에서 예측 불가능한 응답.
- 권장 수정:
  - 에러 응답을 상태 코드 기반으로 정리하거나, API 전용 엔드포인트는 JSON 에러 응답 제공.

### 3. Medium — 게시글 삭제 시 댓글 데이터 무결성 위험
- 위치: `src/main/java/com/example/boardv1/board/BoardService.java`
- 내용: 댓글의 `board`를 null 처리 후 게시글 삭제. 스키마가 `NOT NULL`이면 실패, nullable이면 고아 데이터 발생.
- 권장 수정:
  - `orphanRemoval=true` 또는 `CascadeType.REMOVE` 적용
  - 또는 댓글을 명시적으로 삭제

### 4. Medium — 로그인 실패 메시지 구분으로 계정 유추 가능
- 위치: `src/main/java/com/example/boardv1/user/UserService.java`
- 내용: `username` 미존재/비밀번호 불일치 메시지가 구분됨.
- 권장 수정:
  - 동일한 메시지로 통일 (예: “아이디 또는 비밀번호가 올바르지 않습니다.”)

### 5. Low — CSRF 방어 부재
- 위치: 전반
- 내용: 세션 기반 인증인데 CSRF 토큰 처리 없음.
- 권장 수정:
  - Spring Security 적용 또는 자체 CSRF 토큰 처리

### 6. Low — 입력값 검증 부재
- 위치: `BoardRequest`, `UserRequset`, `ReplyRequest`
- 내용: DTO에 유효성 검증 어노테이션 없음.
- 권장 수정:
  - `@NotBlank`, `@Size`, `@Email` 등 추가 및 400 처리

## 테스트/리스크 갭
- 로그인 없이 상세 접근 시 NPE 방지 테스트 없음
- 게시글 삭제 시 댓글 처리(고아 여부) 통합 테스트 없음
- 인증/권한 관련 시나리오 테스트 없음

## 개선 우선순위 제안
1. `DetailDTO`의 NPE 방지
2. 게시글 삭제 시 댓글 처리 방식 확정 및 적용
3. 예외 응답 포맷 정리(웹 vs API 분리)
4. 입력값 검증 추가
5. CSRF 방어 적용

## 참고
- 본 보고서는 현재 코드 상태 기준이며, 실행 환경/데이터베이스 제약 조건에 따라 일부 영향 범위가 달라질 수 있습니다.

