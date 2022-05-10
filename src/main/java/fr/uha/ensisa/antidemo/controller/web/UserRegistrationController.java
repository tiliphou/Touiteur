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

    if (result.hasErrors()) {
      return "registration";
    }

    IUserService.save(userDto);
    return "redirect:/registration?success";
  }
}
