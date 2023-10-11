package fr.uha.ensisa.antidemo.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class UserRegistrationDto {

  @NotEmpty
  private String firstName;
  @NotEmpty
  private String lastName;
  @NotEmpty @Email
  private String email;
  @NotEmpty
  private String password;
  @NotEmpty
  private String passwordConfirmation;

}
