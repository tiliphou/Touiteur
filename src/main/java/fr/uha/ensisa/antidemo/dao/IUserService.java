package fr.uha.ensisa.antidemo.dao;

import fr.uha.ensisa.antidemo.dto.UserRegistrationDto;
import fr.uha.ensisa.antidemo.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {

  User findByEmail(String email);

  void save(UserRegistrationDto userDto);

}
