package com.example.boardv1.board;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 데이터 베이스 세상의 테이블을 자바 세상에 모델링한 결과 = 엔티티
 */

@NoArgsConstructor // 디폴트 생성자
@Data // Getter,Setter,ToString
@Entity // 해당 어노테이션을 보고, 컴포넌트 스캔 후, 데이터베이스 테이블을 생성한다.
@Table(name = "board_tb") // 테이블 명
public class Board {
    @Id // PK 키 설정정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PK키 Auto Increment 설정
    private Integer id;
    private String title;
    private String content;
    private Timestamp createdAt;

}
