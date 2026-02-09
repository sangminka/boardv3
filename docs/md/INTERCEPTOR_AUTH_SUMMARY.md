# Interceptor를 통한 인증 검사 정리

## 1. 인터셉터 개념

1. `HandlerInterceptor`는 컨트롤러 실행 전/후에 동작한다.
2. Filter와 달리 스프링 MVC 레벨에서 동작해서 컨트롤러 매핑 기준 제어가 쉽다.
3. 인증/인가, 공통 로깅, 요청 전처리에 적합하다.

## 2. 현재 프로젝트 적용 방식

### LoginInterceptor
- 파일: `src/main/java/com/example/boardv1/_core/interceptor/Logininterceptor.java`
- `preHandle(...)`에서 세션 `sessionUser` 확인
- 세션 유저가 없으면 `Exception401` 발생
- 있으면 `true` 반환 후 컨트롤러 진입 허용

### WebMvcConfig 등록
- 파일: `src/main/java/com/example/boardv1/_core/interceptor/WebMvcConfig.java`
- `addInterceptors(...)`에서 인터셉터 등록
- 적용 경로:
1. `/boards/**`
2. `/replies/**`

## 3. 실행 흐름

1. 요청 수신
2. 인터셉터 `preHandle`
3. 인증 실패 시 예외 발생(요청 종료)
4. 인증 성공 시 컨트롤러 실행
5. `postHandle`
6. 뷰 렌더 완료 후 `afterCompletion`

## 4. 장점

1. 컨트롤러마다 인증 체크 중복 코드 제거
2. URL 패턴 기반으로 인증 대상 중앙 관리
3. 전역 예외 처리(`GlobalExceptionHandler`)와 결합 시 응답 일관성 확보

## 5. Filter/AOP와 차이

1. Filter: 서블릿 레벨(스프링 MVC 이전)
2. Interceptor: 스프링 MVC 레벨(컨트롤러 전/후)
3. AOP: 메서드 호출 관점(스프링 빈 공통 관심사)

## 6. 패턴 매칭 체크 포인트

1. `excludePathPatterns("/boards/[0-9]+")`는 정규식처럼 보이지만 인터셉터 패턴에서 의도대로 안 맞을 수 있다.
2. 숫자 경로 제외는 패턴 문법에 맞춰 선언해야 한다.
3. 상세 페이지만 열고 싶으면 숫자 id 경로만 제외하고, 나머지 `/boards/**`는 인증 유지해야 한다.

## 7. 이번 인터셉터 작업 반영 내용

1. 상세 페이지 비인증 허용 요구사항 반영
- 목표: `/boards/{숫자}`는 로그인 없이 접근 허용

2. 실제 수정 내용
- 파일: `src/main/java/com/example/boardv1/_core/interceptor/WebMvcConfig.java`
- 변경 전:
```java
.excludePathPatterns("/boards/[0-9]+");
```
- 변경 후:
```java
.excludePathPatterns("/boards/{id:[0-9]+}");
```

3. 기대 동작
1. 허용: `/boards/1`, `/boards/25` (비로그인 가능)
2. 유지: `/boards/save-form`, `/boards/1/update-form`, `/replies/**` (로그인 필요)
