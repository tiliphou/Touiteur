package fr.uha.ensisa.antidemo.service;

import fr.uha.ensisa.antidemo.dao.IUserService;
import fr.uha.ensisa.antidemo.dto.UserRegistrationDto;
import fr.uha.ensisa.antidemo.entity.Role;
import fr.uha.ensisa.antidemo.entity.User;
import fr.uha.ensisa.antidemo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public User findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  public void save(UserRegistrationDto userDto) {
    Collection<Role> roles = new ArrayList<>();
    roles.add(Role.builder().name("ROLE_USER").build());
    userRepository.save(User.builder()
      .firstName(userDto.getFirstName())
      .lastName(userDto.getLastName())
      .email(userDto.getEmail())
      .password(passwordEncoder.encode(userDto.getPassword()))
        .roles(roles)
      .build());
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email);
    if (user == null) {
      throw new UsernameNotFoundException("Invalid username or password.");
    }
    return new org.springframework.security.core.userdetails.User(user.getEmail(),
      user.getPassword(),
      mapRolesToAuthorities(user.getRoles()));
  }

  private Collection<GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
    return roles.stream()
      .map(role -> new SimpleGrantedAuthority(role.getName()))
      .collect(Collectors.toList());
  }
}
