package com.example.app.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "users")
@Builder
@AllArgsConstructor
@Data
public class UserEntity {
    // Entityクラスには、必ずデフォルトコンストラクタ（引数なし）を用意する
    public UserEntity() {}

    @Id
    private UUID id;
    private String name;
    private UUID room_id;

    // getter/setter
}
