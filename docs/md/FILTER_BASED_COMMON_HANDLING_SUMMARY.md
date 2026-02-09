# Filter 기반 공통 처리 핵심 정리 (AOP 학습 연결)

## 1. 개념 요약

1. `Filter`는 DispatcherServlet(스프링 MVC) 전에 동작한다.
2. 그래서 컨트롤러 진입 전에 인증/로깅/차단 같은 공통 정책을 먼저 적용할 수 있다.
3. AOP는 스프링 빈(주로 컨트롤러/서비스) 메서드 호출 시점에 동작하고, Filter는 HTTP 요청 단위에서 동작한다.

## 2. 현재 프로젝트 적용 구조

### Filter 등록
- 파일: `src/main/java/com/example/boardv1/_core/filter/FilterConfig.java`
- `FilterRegistrationBean`으로 필터를 명시 등록
- 실행 순서:
1. `FirstFilter` (`order=1`, `/*`)
2. `LoginFilter` (`order=2`, `/boards/*`, `/replies/*`)

### FirstFilter 역할
- 파일: `src/main/java/com/example/boardv1/_core/filter/FirstFilter.java`
- 모든 요청에 대해 먼저 실행되는 공통 전처리 지점
- 현재는 로그 출력 후 `chain.doFilter(...)`로 다음 필터/서블릿으로 전달

### LoginFilter 역할
- 파일: `src/main/java/com/example/boardv1/_core/filter/LoginFilter.java`
- 세션의 `sessionUser` 존재 여부로 인증 체크
- 미인증이면 `/login-form`으로 리다이렉트
- 예외 규칙:
1. `GET /boards/{id}` 상세조회는 비로그인 허용
2. 그 외 `/boards/*`, `/replies/*`는 로그인 필요

## 3. 처리 흐름

1. 클라이언트 요청
2. `FirstFilter`
3. `LoginFilter` (대상 URL인 경우)
4. DispatcherServlet
5. Controller

즉, 인증 실패 요청은 컨트롤러까지 가지 않고 Filter 단계에서 종료된다.

## 4. AOP와 함께 볼 때 포인트

1. Filter: 요청 경로/세션/헤더 기반 정책에 적합 (인증, CORS, 로깅 등)
2. AOP: 메서드 파라미터/비즈니스 공통 로직에 적합 (검증, 트랜잭션 경계 보조 등)
3. 실무에서는 보통
- Filter(또는 Spring Security)로 인증/인가 전처리
- AOP로 컨트롤러/서비스 공통 관심사 분리

## 5. 현재 코드에서 보이는 개선 포인트

1. `FilterConfig`에 `bean.addUrlPatterns("/replies/*")`가 중복 선언되어 1회로 정리 가능
2. `System.out.println` 대신 로거 사용 권장
3. `LoginFilter` 예외 URL 패턴은 상수/설정으로 분리하면 유지보수에 유리
