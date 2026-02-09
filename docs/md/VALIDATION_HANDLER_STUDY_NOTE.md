# ValidationHandler 학습 정리

## 1. 내가 이해한 핵심

1. `@Valid`는 검증 실행 트리거이고, 실제 규칙은 DTO의 `@NotBlank`, `@Size` 같은 제약 어노테이션이다.
2. 컨트롤러마다 `if (errors.hasErrors())`를 쓰면 중복이 생긴다.
3. 이 중복을 AOP(`ValidationHandler`)로 모으면 검증 실패 처리를 한 곳에서 관리할 수 있다.

## 2. 처리 흐름

1. 요청이 `@PostMapping` 메서드로 들어온다.
2. AOP `@Before`가 먼저 실행된다.
3. `JoinPoint` 파라미터에서 `Errors`를 찾는다.
4. `errors.hasErrors()`가 `true`면 `Exception400`을 던진다.
5. 예외는 전역 예외 핸들러에서 사용자 응답으로 처리된다.

## 3. 적용 조건

1. 컨트롤러 파라미터에 `@Valid`가 있어야 한다.
2. 메서드 파라미터에 `Errors`가 있어야 한다.
3. DTO 필드에 제약 어노테이션이 있어야 한다.
4. `spring-boot-starter-validation` 의존성이 있어야 한다.

## 4. 직접 처리 vs AOP 처리

1. 직접 처리 장점: 한 메서드 기준으로 직관적이다.
2. 직접 처리 단점: 코드 중복, 누락 위험, 유지보수 비용 증가.
3. AOP 처리 장점: 공통 로직 중앙화, 컨트롤러는 비즈니스 로직에 집중.
4. AOP 처리 단점: 실행 흐름이 눈에 바로 안 보여 처음엔 디버깅이 어렵다.

## 5. 이번 학습에서 얻은 포인트

1. 검증 실패 로직은 반복하지 말고 공통 관심사로 분리하는 게 좋다.
2. 인증은 Filter/Interceptor, 입력 검증 공통화는 AOP처럼 역할을 나누면 구조가 깔끔해진다.
3. 오타(`Vaildation`)나 import 누락(`jakarta.validation.Valid`) 같은 기본 실수가 실제 동작 실패의 주요 원인이다.
