package shape.service;

import shape.Polygon;
import shape.model.User;
import shape.repo.UserRepo;
import perched.Parakeet;
import qio.annotate.Inject;
import qio.annotate.Service;
import qio.model.web.ResponseData;

import javax.servlet.http.HttpServletRequest;

@Service
public class AuthService {

    @Inject
    private UserRepo userRepo;

    public boolean signin(String phone, String password){
        User user = userRepo.getPhone(phone);
        if(user == null) {
            return false;
        }
        return Parakeet.login(phone, password);
    }

    public boolean signout(){
        return Parakeet.logout();
    }

    public boolean isAuthenticated(){
        return Parakeet.isAuthenticated();
    }

    public boolean isAdministrator(){
        return Parakeet.hasRole(Polygon.SUPER_ROLE);
    }

    public boolean hasPermission(String permission){
        return Parakeet.hasPermission(permission);
    }

    public boolean hasRole(String role){
        return Parakeet.hasRole(role);
    }

    public User getUser(){
        String phone = Parakeet.getUser();
        User user = userRepo.getPhone(phone);
        return user;
    }

    public String authenticate(ResponseData data, HttpServletRequest req) {

        try{

            String phone = Polygon.getPhone(req.getParameter("phone"));
            String password = req.getParameter("password");
            if(!signin(phone, password)){
                data.put("message", "Wrong phone and password");
                return "[redirect]/signin";
            }

            User authdUser = userRepo.getPhone(phone);

            req.getSession().setAttribute("name", authdUser.getName());
            req.getSession().setAttribute("userId", authdUser.getId());

        } catch ( Exception e ) {
            e.printStackTrace();
            data.put("message", "Please yell at one of us, something is a little off.");
            return "[redirect]/";
        }

        return "[redirect]/";
    }

    public String deAuthenticate(ResponseData data, HttpServletRequest req) {
        signout();
        data.put("message", "Successfully signed out");
        req.getSession().setAttribute("username", "");
        req.getSession().setAttribute("userId", "");
        return "[redirect]/";
    }
}
