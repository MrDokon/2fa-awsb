package awsb.security.example.app2fa.controller;

import awsb.security.example.app2fa.model.dto.User;
import awsb.security.example.app2fa.service.UserService;
import javax.validation.Valid;
import org.springframework.ui.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WebController {

    private static Logger logger = LoggerFactory.getLogger(WebController.class);

    @Autowired
    private UserService service;

    @GetMapping("/")
    public ModelAndView getHomePage(java.security.Principal principal) {
        return principal == null
                ? new ModelAndView("home")
                : new ModelAndView("home", "user", principal);
    }

    @GetMapping("/user/registration")
    public String getRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("user/registration")
    public ModelAndView registerNewUser(
            @ModelAttribute("user") @Valid User user, BindingResult bindingResult, RedirectAttributes redirect) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("registration", "user", user);
        }
        try {
            service.createNewUser(user);
            redirect.addFlashAttribute("user", user);
            return new ModelAndView("redirect:/user/registered");

        } catch (UserService.UserExistsException ex) {
            service.loadUserByUsername(user.getUsername());
            logger.warn("Próbowano zarejestrować już istniejącego użytkownika: {}", user.getUsername());
            bindingResult.rejectValue("username", "already.exists");
            return new ModelAndView("registration");
        }
    }

    @GetMapping("/user/registered")
    public String getRegisteredPage(Model model) {
        User user = (User) (model.asMap().get("user"));
        if (user != null){
            model.addAttribute("qrCode", service.getQrUrl(user, model));
            return "registered";
        }
     return "redirect:/user/registration";
    }

    @RequestMapping("/login")
    public String getLoginPage(){
        return "login";
    }

    @RequestMapping("/user/home")
    public ModelAndView getUserHomePage(java.security.Principal principal){
        return new ModelAndView("userhome.html", "user", principal);
    }

}
