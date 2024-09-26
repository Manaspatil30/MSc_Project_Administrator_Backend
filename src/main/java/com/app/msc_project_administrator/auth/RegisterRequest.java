package com.app.msc_project_administrator.auth;


import com.app.msc_project_administrator.user.Role;
import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
  @CsvBindByName
  private String firstname;
  @CsvBindByName
  private String lastname;
  @CsvBindByName
  private String email;
  @CsvBindByName
  private String password;
  @CsvBindByName
  private Role role;
}
