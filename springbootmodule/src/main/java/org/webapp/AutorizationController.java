package org.webapp;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.models.BaseDAOImpl;
import org.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


@RestController//@Controller
@RequestMapping(value="")

@SessionAttributes(value = "userAuth")
public class AutorizationController extends HttpServlet {

	@ModelAttribute("userAuth")
	public User createUser() {
	        return new User();
	}
	
	/*@RequestMapping(value="showAuth", method = RequestMethod.GET)
	public String showAuth(Model m) {
		User user = new User();
		m.addAttribute("userJSP", user);
		return "index";		
	}
	
    @RequestMapping(value = "login", method =  RequestMethod.GET)   
    public ModelAndView login(ModelMap m, @ModelAttribute(value="userJSP") User user, @ModelAttribute(value="userAuth") User userAuth) {
    	ModelAndView modelAndView = new ModelAndView();
    	modelAndView.setViewName("index");   
    	
    	BaseDAOImpl base = new BaseDAOImpl();
    	if(base.userExists(user.getLogin()) && user.getPassword().equals(base.findPasswordByLogin(user.getLogin()))) {
    		m.addAttribute("message", "Вы успешно авторизовались");
    		userAuth = user;
    		m.addAttribute("userAuth", userAuth);
    	}
    	
    	else if(!base.userExists(user.getLogin())) {
    		m.addAttribute("message", "Пользователь с данным логином не существует");
    		
    	}
    	
    	else if(base.userExists(user.getLogin()) && !user.getPassword().equals(base.findPasswordByLogin(user.getLogin()))) {
    		m.addAttribute("message", "Вы ввели неверный пароль");
    	}
        return modelAndView;
    }*/
	
	 @RequestMapping(value = "login", method =  RequestMethod.POST)
	    public ModelAndView login(ModelMap m, HttpServletRequest request, @ModelAttribute(value="userAuth") User userAuth) {
	    	ModelAndView modelAndView = new ModelAndView();
	    	modelAndView.setViewName("index"); 
	    	User userForm = new User();
	    	m.addAttribute("userForm", userForm);
	    	//String login = request.getParameter("login");
	    	//String password = request.getParameter("password");
	    	String login = userForm.getLogin();
	    	String password = userForm.getPassword();
	    	BaseDAOImpl base = new BaseDAOImpl();
	    	if (base.userExists(login)) {
	    		User user = base.findUserByLogin(login);
				System.out.println(user);
				String salt = user.getSalt();
				String securityPassword = get_SHA_512_SecurePassword(password, salt);
				if (user.getPassword().equals(securityPassword)) {
					m.addAttribute("message", "Вы успешно авторизовались");
					userAuth = user;
					m.addAttribute("userAuth", userAuth);

				}

	    	}
	    	/*
	    	if(base.userExists(user.getLogin()) && user.getPassword().equals(base.findPasswordByLogin(user.getLogin()))) {
	    	//if(base.userExists(login) && password.equals(base.findPasswordByLogin(login))) {

	    	}
	    	
	    	else if(!base.userExists(user.getLogin())) {
	    		m.addAttribute("message", "Пользователь с данным логином не существует");
	    		
	    	}
	    	
	    	else if(base.userExists(user.getLogin()) && !user.getPassword().equals(base.findPasswordByLogin(user.getLogin()))) {
	    		m.addAttribute("message", "Вы ввели неверный пароль");
	    	}
	    	*/
	        return modelAndView;
	    }
    
    @RequestMapping(value="showReg")
	public String showReg(Model m) {
		User user = new User();
		m.addAttribute("userReg", user);
		return "reg";		
	}

    @RequestMapping(value="registration", method =  RequestMethod.POST)
    public ModelAndView registration(ModelMap m, @ModelAttribute(value="userReg") User user, @ModelAttribute(value="userAuth") User userAuth) {
    	ModelAndView modelAndView = new ModelAndView();
		try {
			modelAndView.setViewName("reg");
			BaseDAOImpl base = new BaseDAOImpl();
			String salt = new String(getSalt());
			user.setSalt(salt);
			user.setPassword(get_SHA_512_SecurePassword(user.getPassword(), salt));
			if(base.userExists(user.getLogin())) {
				m.addAttribute("message", "Пользователь с данным логином уже существует");
			}
			else if(user.getLogin() != "" && user.getPassword() != ""){
				base.save(user);
				m.addAttribute("message", "Вы успешно зарегистрировались");
				userAuth = user;
				m.addAttribute("userAuth", userAuth);
			}
			else {
				m.addAttribute("message", "Вы не ввели логин или пароль");
			}
			} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
        return modelAndView;
    }

    @RequestMapping("forget")
    public String forgetPassword(ModelMap m) {
        return "index";
    }
    
    @RequestMapping("mainProc")
    public String mainProc(ModelMap m, @ModelAttribute(value="userAuth") User userAuth) {
    	if(userAuth.getLogin() == null || userAuth.getLogin() == "") {
    		m.addAttribute("message", "Вы вошли как не зарегистрированный пользователь");
    	}
    	else {
    		m.addAttribute("message", "Здравствуйте " + userAuth.getLogin() + "!");
    	}
    	
    	return "main";
    }


	private static String get_SHA_512_SecurePassword(String passwordToHash, String salt){
		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(salt.getBytes(StandardCharsets.UTF_8));
			byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();
			for(int i=0; i< bytes.length ;i++){
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;
	}



	private static byte[] getSalt() throws NoSuchAlgorithmException
	{
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		return salt;
	}
    
}
