package com.example.ex5.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Member extends BasicEntity {
  @Id
  private String email;
  private String password;
  private String name;
}
