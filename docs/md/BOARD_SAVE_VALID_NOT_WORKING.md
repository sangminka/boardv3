# save 메서드에 @Valid를 붙였는데도 검증이 안 되는 이유

## 현재 코드에서 확인된 사실

1. `save` 메서드에 `@Valid`가 없습니다.
   - 파일: `src/main/java/com/example/boardv1/board/BoardController.java`
   - 현재 선언: `public String save(BoardRequest.SaveOrUpdateDTO reqDTO)`

2. `SaveOrUpdateDTO`에 검증 애노테이션이 없습니다.
   - 파일: `src/main/java/com/example/boardv1/board/BoardRequest.java`
   - `title`, `content`가 단순 `String`이라 검증할 규칙 자체가 없습니다.

## 왜 @Valid만으로는 안 되는가

`@Valid`는 "검증 실행 스위치"이고, 실제 검증 규칙은 DTO 필드의 `@NotBlank`, `@NotNull`, `@Size` 같은 제약조건입니다.
즉, `@Valid`만 있고 DTO에 제약조건이 없으면 통과됩니다.

## 자주 놓치는 포인트

1. 오타
   - `@vaild`(오타)로 쓰면 동작하지 않습니다. 정확히 `@Valid`여야 합니다.

2. import 경로
   - Spring Boot 3/4 기준: `jakarta.validation.Valid`를 사용해야 합니다.

3. 폼 필드 이름 불일치
   - 폼 `name`이 `title`, `content`와 다르면 DTO 바인딩이 비정상일 수 있습니다.

## 최소 동작 예시

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
public String save(@Valid BoardRequest.SaveOrUpdateDTO reqDTO) {
    ...
}
```

## 정리

"`@Valid`를 붙였는데 안 된다"의 대부분은 DTO 제약조건 부재(예: `@NotBlank` 없음) 또는 `@Valid` 오타/잘못된 import 문제입니다.
현재 코드 기준 핵심 원인은 `save`에 `@Valid` 미적용 + DTO 제약조건 미정의입니다.
