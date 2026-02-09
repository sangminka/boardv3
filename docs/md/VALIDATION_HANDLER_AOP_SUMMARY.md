# ValidationHandler 기반 에러 처리 정리

## 1. 이전 방식: 컨트롤러 메서드마다 직접 처리

각 `@PostMapping` 메서드에서 아래 코드를 반복했다.

```java
if (errors.hasErrors()) {
    throw new Exception400(errors.getAllErrors().get(0).getDefaultMessage());
}
```

예시 파일:
- `src/main/java/com/example/boardv1/board/BoardController.java`
- `src/main/java/com/example/boardv1/reply/ReplyController.java`

문제점:
1. 중복 코드 증가
2. 누락 위험 (어떤 메서드는 체크를 빼먹을 수 있음)
3. 검증 정책 변경 시 여러 파일 수정 필요

## 2. AOP 방식: VaildationHandler에서 공통 처리

핵심 파일:
- `src/main/java/com/example/boardv1/_core/aop/VaildationHandler.java`

동작 흐름:
1. `@Aspect` + `@Component`로 스프링 빈 등록
2. `@Before("@annotation(org.springframework.web.bind.annotation.PostMapping)")`
3. `@PostMapping` 메서드 실행 전에 파라미터 전체(`JoinPoint#getArgs`) 검사
4. 파라미터 중 `Errors` 타입을 찾고 `hasErrors()`가 true면
5. `Exception400`을 던져 요청을 중단

즉, 컨트롤러는 비즈니스 로직에 집중하고, 검증 실패 처리는 AOP가 공통으로 담당한다.

## 3. 컨트롤러/DTO에서 필요한 최소 조건

1. 컨트롤러 파라미터에 `@Valid` 적용
2. `Errors` 파라미터를 함께 선언
3. DTO 필드에 `@NotBlank` 같은 제약 어노테이션 선언

예시:

```java
public String save(@Valid BoardRequest.SaveOrUpdateDTO reqDTO, Errors errors) { ... }
```

```java
public static class SaveOrUpdateDTO {
    @NotBlank(message = "제목은 필수입니다.")
    private String title;
}
```

## 4. 현재 프로젝트 기준 정리

현재는 `VaildationHandler`가 있어도 컨트롤러 내부에 `if (errors.hasErrors())`가 남아있다.
이 코드는 기능적으로 문제는 없지만, AOP 학습 목적이라면 중복 체크를 제거하고 AOP 한 곳에서만 처리하는 방향이 더 명확하다.

## 5. 참고

클래스명 `VaildationHandler`는 오타 형태다. 동작에는 문제 없지만, 일반적으로 `ValidationHandler`로 쓰는 편이 읽기 좋다.
