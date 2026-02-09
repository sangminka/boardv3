# 프로젝트 기술스택 정리

## 1. Language / Build

1. Java 21
2. Gradle
3. Spring Boot 4.0.2
4. io.spring.dependency-management 1.1.7

## 2. Backend Framework

1. Spring Web MVC
2. Spring Data JPA
3. Spring AOP

## 3. View / Frontend (SSR)

1. Mustache (Spring Boot Starter Mustache)
2. Static Resource (CSS, JS)

## 4. Database

1. H2 Database (runtime)

## 5. Validation / Error Handling

1. spring-boot-starter-validation
2. jakarta.validation (`@Valid`, `@NotBlank`, `@Email`, `@Size`)
3. `Errors` 바인딩 결과 처리
4. AOP 기반 공통 검증 처리 (`VaildationHandler`)
5. `@RestControllerAdvice` 전역 예외 처리 (`GlobalExceptionHandler`)
6. 커스텀 예외 클래스 (`Exception400`, `Exception401`, `Exception403`, `Exception404`, `Exception500`)

## 6. Auth / Session

1. HttpSession 기반 로그인 상태 관리
2. Cookie 사용 (예: username 저장)

## 7. Utilities / Productivity

1. Lombok (`@Data`, `@RequiredArgsConstructor`)
2. jsoup (HTML sanitize/파싱)
3. Spring Boot DevTools

## 8. Test Dependencies

1. spring-boot-starter-webmvc-test
2. spring-boot-starter-data-jpa-test
3. spring-boot-starter-mustache-test
4. junit-platform-launcher (testRuntimeOnly)
