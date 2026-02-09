# BoardRequest.SaveOrUpdateDTO 에서 @NotBlank 가 동작하지 않는 이유

## 현재 코드 기준 원인

1. `SaveOrUpdateDTO` 필드에 `@NotBlank`가 선언되어 있지 않습니다.
   - 파일: `src/main/java/com/example/boardv1/board/BoardRequest.java`
   - 현재 `title`, `content`는 단순 `String` 필드입니다.

2. 컨트롤러 파라미터에 `@Valid`(또는 `@Validated`)가 없습니다.
   - 파일: `src/main/java/com/example/boardv1/board/BoardController.java`
   - `save(...)`, `update(...)` 메서드의 `BoardRequest.SaveOrUpdateDTO` 파라미터 앞에 검증 트리거 애노테이션이 없어 Bean Validation이 실행되지 않습니다.

3. (참고) 의존성은 이미 포함되어 있습니다.
   - `build.gradle`에 `org.springframework.boot:spring-boot-starter-validation` 존재
   - 즉, 문제는 라이브러리 부재가 아니라 DTO 선언/컨트롤러 사용 방식입니다.

## 적용 예시

```java
// BoardRequest.java
import jakarta.validation.constraints.NotBlank;

@Data
public static class SaveOrUpdateDTO {
    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @NotBlank(message = "내용은 필수입니다.")
    private String content;
}
```

```java
// BoardController.java
import jakarta.validation.Valid;

@PostMapping("/boards/save")
public String save(@Valid BoardRequest.SaveOrUpdateDTO reqDTO) { ... }

@PostMapping("/boards/{id}/update")
public String update(@PathVariable("id") int id, @Valid BoardRequest.SaveOrUpdateDTO reqDto) { ... }
```

## 정리

`@NotBlank`는 DTO 필드에 선언되어 있어야 하고, 해당 DTO를 받는 컨트롤러 파라미터에 `@Valid`가 있어야 실제로 검증이 동작합니다.
