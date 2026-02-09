# boardv1 프로젝트 보고서

작성일: 2026-02-06

## 개요
Spring Boot 기반의 간단한 게시판/댓글 시스템입니다. 세션 기반 인증을 사용하며, 게시글/댓글의 소유자 권한 체크를 포함합니다. 뷰 템플릿은 Mustache를 사용하고, 게시글 본문 내 YouTube 링크를 안전하게 iframe으로 렌더링합니다.

## 기술 스택
- Java 21 (toolchain)
- Spring Boot 4.0.2
- Spring Web MVC
- Spring Data JPA (EntityManager 기반 Repository 구현)
- H2 Database (콘솔 활성화)
- Mustache 템플릿
- Jsoup (HTML 정제 및 YouTube 링크 렌더링)
- Lombok

## 패키지 구조
- `com.example.boardv1.board`: 게시글 도메인 (Entity, Controller, Service, Repository, DTO)
- `com.example.boardv1.reply`: 댓글 도메인 (Entity, Controller, Service, Repository, DTO)
- `com.example.boardv1.user`: 사용자 도메인 (Entity, Controller, Service, Repository, DTO)
- `com.example.boardv1._core.errors`: 전역 예외 처리 및 커스텀 예외
- `com.example.boardv1._core.util`: 유틸리티 (YouTube 렌더링)

## 주요 기능
- 회원가입/로그인/로그아웃
- 게시글 목록, 상세, 작성, 수정, 삭제
- 댓글 작성, 삭제
- 세션 기반 인증과 소유자 권한 확인
- 게시글 본문에서 YouTube 링크 자동 iframe 변환

## 주요 엔드포인트
- `GET /`: 게시글 목록
- `GET /boards/{id}`: 게시글 상세
- `GET /api/boards/{id}`: 게시글 상세 JSON
- `GET /boards/save-form`: 게시글 작성 폼
- `GET /boards/{id}/update-form`: 게시글 수정 폼
- `POST /boards/save`: 게시글 저장
- `POST /boards/{id}/update`: 게시글 수정
- `POST /boards/{id}/delete`: 게시글 삭제
- `POST /replies/save`: 댓글 저장
- `POST /replies/{id}/delete`: 댓글 삭제
- `GET /join-form`: 회원가입 폼
- `POST /join`: 회원가입
- `GET /login-form`: 로그인 폼
- `POST /login`: 로그인
- `GET /logout`: 로그아웃

## 데이터 모델
- `User` (`user_tb`)
- `id`, `username`(unique), `password`, `email`, `created_at`
- `Board` (`board_tb`)
- `id`, `title`, `content`, `user_id`(ManyToOne), `created_at`
- `Reply` (`reply_tb`)
- `id`, `comment`, `board_id`(ManyToOne), `user_id`(ManyToOne), `created_at`

## 예외 처리 및 보안
- 전역 예외 핸들러(`GlobalExceptionHandler`)가 커스텀 예외를 받아 JS alert + 이전 페이지 이동 또는 로그인 페이지 리다이렉트 처리
- 세션 기반 인증: `sessionUser`가 없으면 401 처리
- 권한 체크: 게시글/댓글 소유자만 수정·삭제 가능

## 초기 데이터
- `src/main/resources/db/data.sql`에 사용자 2명, 게시글 6개, 댓글 5개 시드 데이터 포함

## 실행 방법
- Gradle: `./gradlew bootRun` (Windows는 `gradlew.bat bootRun`)
- 기본 포트: `8080`
- H2 콘솔: `spring.h2.console.enabled=true` (기본 경로 `/h2-console`)

## 템플릿
- `src/main/resources/templates/index.mustache`
- `src/main/resources/templates/board/*`
- `src/main/resources/templates/user/*`

## 참고 사항
- `spring.jpa.open-in-view=false` 설정
- `spring.jpa.properties.hibernate.default_batch_fetch_size=10` 설정
- 게시글 상세 응답에서 `isOwner`, 댓글의 `isReplyOwner` 계산

