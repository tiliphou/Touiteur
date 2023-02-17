package fr.uha.ensisa.antidemo.controller.web;

import fr.uha.ensisa.antidemo.dao.IUserService;
import fr.uha.ensisa.antidemo.dto.UserRegistrationDto;
import fr.uha.ensisa.antidemo.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriUtils;

import java.net.URLEncoder;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller @RequestMapping("/registration")
public class UserRegistrationController {

  private final IUserService IUserService;


  @ModelAttribute("user")
  public UserRegistrationDto userRegistrationDto() {
    return new UserRegistrationDto();
  }

  @GetMapping
  public String showRegistrationForm(Model model) {
    return "registration";
  }

  @PostMapping
  public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDto userDto,
                                    BindingResult result) {

    User existing = IUserService.findByEmail(userDto.getEmail());
    if (existing != null) {
      result.rejectValue("email", null, "There is already an account registered with that email");
    }

    if (userDto.getPassword() == null ||
        userDto.getPassword().length() < 2) {
        result.rejectValue("password", "Password too short");
    }
    if (!userDto.getPassword().equals(userDto.getPasswordConfirmation())) {
        result.rejectValue("password", "Passwords differ");
    }

    if (result.hasErrors()) {
      return "registration";
    }

    IUserService.save(userDto);
    return "redirect:/login?user=" + UriUtils.encodeQueryParam(userDto.getEmail(), "UTF-8") ;
  }
}
