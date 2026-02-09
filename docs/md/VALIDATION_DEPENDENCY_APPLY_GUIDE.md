# spring-boot-starter-validation 적용 안 될 때 점검/적용 가이드

## 현재 프로젝트에서 실제 원인

`build.gradle`에 아래 의존성은 이미 있습니다.

```gradle
implementation 'org.springframework.boot:spring-boot-starter-validation'
```

하지만 코드에서 검증 어노테이션 import가 누락되면 컴파일/검증이 동작하지 않습니다.

1. `src/main/java/com/example/boardv1/board/BoardController.java`
- 필요 import: `jakarta.validation.Valid`

2. `src/main/java/com/example/boardv1/board/BoardRequest.java`
- 필요 import: `jakarta.validation.constraints.NotBlank`

## 적용 완료 상태(이번에 반영한 내용)

1. `BoardController`에 `import jakarta.validation.Valid;` 추가
2. `BoardRequest`에 `import jakarta.validation.constraints.NotBlank;` 추가
3. `save`, `update` 파라미터의 `@Valid` 유지
4. DTO 필드의 `@NotBlank` 유지

## IDE에서 의존성 반영 방법

1. Gradle 리프레시
- IntelliJ: Gradle 탭에서 `Reload All Gradle Projects`

2. 그래도 안 되면 캐시 정리
- 터미널에서 `./gradlew --refresh-dependencies` (Windows: `gradlew.bat --refresh-dependencies`)

3. 컴파일 확인
- `./gradlew clean compileJava` (Windows: `gradlew.bat clean compileJava`)

## 체크리스트

1. `@Valid` 오타가 아닌지 (`@Vaild` X)
2. `jakarta.validation.*` import를 쓰는지
3. DTO 필드에 실제 제약조건이 있는지 (`@NotBlank` 등)
4. HTML form `name`이 DTO 필드명(`title`, `content`)과 일치하는지
