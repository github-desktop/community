package com.laxi.controller;

import com.google.code.kaptcha.Producer;
import com.laxi.service.UserService;
import com.laxi.util.CommunityConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;


@Controller
public class LoginController implements CommunityConstant {

    @Autowired
    private Producer kaptchaProducter;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @GetMapping("/login")
    public String loginPage() {
        return "/site/login";
    }

    @GetMapping("/kaptcha")
    public void getKaptcha(HttpServletResponse response, HttpSession session) {
        //生成验证码
        String text = kaptchaProducter.createText();
        session.setAttribute("kaptcha", text);
        BufferedImage image = kaptchaProducter.createImage(text);
        response.setContentType("image/png");
        try {
            ServletOutputStream os = response.getOutputStream();
            ImageIO.write(image, "png", os);
        } catch (IOException exception) {
            exception.printStackTrace();
            logger.error("响应验证码失败" + exception.getMessage());
        }
    }

    @PostMapping("/login")
    public String login(String username, String password, String code, boolean remember, Model model, HttpSession session, HttpServletResponse response) {
        String kaptcha = (String) session.getAttribute("kaptcha");
        if (kaptcha == null || kaptcha.isEmpty() || code == null || !kaptcha.equalsIgnoreCase(code)) {
            model.addAttribute("codeMsg", "验证码不正确");
            return "site/login";
        }
        int seconds = DEFAULT_EXPIRED_SECONDS;
        if (remember) seconds = REMEMBER_EXPIRED_SECOND;
        Map<String, Object> map = userService.login(username, password, seconds);
        if (map.containsKey("ticket")) {
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            cookie.setPath("/community");
            cookie.setMaxAge(seconds);
            response.addCookie(cookie);
            return "redirect:/index";
        }
        model.addAttribute("usernameMsg", map.get("usernameMsg"));
        model.addAttribute("passwordMsg", map.get("passwordMsg"));
        return "/site/login";
    }


    @GetMapping("/logout")
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/login";
    }

}
