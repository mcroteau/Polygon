package shape.service;

import shape.Polygon;
import shape.model.Prospect;
import shape.model.ProspectActivity;
import shape.model.User;
import shape.repo.ProspectRepo;
import shape.repo.UserRepo;
import qio.Qio;
import qio.annotate.Inject;
import qio.annotate.Service;
import qio.model.web.ResponseData;
import perched.Parakeet;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class UserService {

    @Inject
    UserRepo userRepo;

    @Inject
    ProspectRepo prospectRepo;

    @Inject
    AuthService authService;

    @Inject
    SmsService smsService;


    private String getPermission(String id){
        return Polygon.USER_MAINTENANCE + id;
    }

    public void setPretty(ProspectActivity prospectActivity){
        try {
            Prospect prospect = prospectRepo.get(prospectActivity.getProspectId());
            prospectActivity.setProspectName(prospect.getName());
            SimpleDateFormat format = new SimpleDateFormat(Polygon.DATE_TIME);
            Date date = format.parse(Long.toString(prospectActivity.getTaskDate()));

            SimpleDateFormat formatter = new SimpleDateFormat(Polygon.DATE_PRETTY);
            String pretty = formatter.format(date);
            prospectActivity.setPrettyTime(pretty);
        }catch (Exception ex){}
    }

    public String create(ResponseData data) {
        if (!authService.isAuthenticated()) {
            return "[redirect]/";
        }
        if (!authService.isAdministrator()) {
            data.put("message", "You must be a super user in order to access users.");
            return "[redirect]/";
        }
        List<ProspectActivity> prospectActivities = prospectRepo.getActivities();
        prospectActivities.stream().forEach((prospectActivity -> setPretty(prospectActivity)));
        data.put("prospectActivities", prospectActivities);

        data.put("users", "active");
        data.put("title", "Create User");
        data.put("page", "/pages/user/create.jsp");
        return "/designs/auth.jsp";
    }


    public String getUsers(ResponseData data){
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        if(!authService.isAdministrator()){
            data.put("message", "You must be a super user in order to access users.");
            return "[redirect]/";
        }
        List<ProspectActivity> prospectActivities = prospectRepo.getActivities();
        prospectActivities.stream().forEach((prospectActivity -> setPretty(prospectActivity)));
        data.put("prospectActivities", prospectActivities);

        List<User> users = userRepo.getList();
        data.put("usersHref", "active");

        data.put("users", users);
        data.put("title", "Users");
        data.put("page", "/pages/user/index.jsp");
        return "/designs/auth.jsp";
    }

    public String save(ResponseData data, HttpServletRequest req){
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        if(!authService.isAdministrator()){
            data.put("message", "You must be a super user in order to access users.");
            return "[redirect]/";
        }

        User user = (User) Qio.hydrate(req, User.class);
        String phone = Polygon.getPhone(user.getPhone());
        User storedUser = userRepo.getPhone(phone);
        if(storedUser != null){
            data.set("message", "Someone already exists with the same phone number. Please try a different number.");
            return "[redirect]/users/create";
        }

        user.setPhone(phone);
        String password = Parakeet.dirty(user.getPassword());
        user.setPassword(password);
        userRepo.save(user);

        User savedUser = userRepo.getSaved();

        data.set("message", "Successfully added user!");
        return "[redirect]/users/edit/" + savedUser.getId();
    }



    public String getEditUser(Long id, ResponseData data){
        String permission = getPermission(Long.toString(id));
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            return "[redirect]/";
        }

        List<ProspectActivity> prospectActivities = prospectRepo.getActivities();
        prospectActivities.stream().forEach((prospectActivity -> setPretty(prospectActivity)));

        User user = userRepo.get(id);

        data.put("usersHref", "active");
        data.put("user", user);
        data.put("prospectActivities", prospectActivities);

        data.put("page", "/pages/user/edit.jsp");
        return "/designs/auth.jsp";
    }


    public String editPassword(Long id, ResponseData data) {

        String permission = getPermission(Long.toString(id));
        if(!authService.isAdministrator() ||
                !authService.hasPermission(permission)){
            return "[redirect]/";
        }

        User user = userRepo.get(id);
        data.put("user", user);

        data.put("page", "/pages/user/password.jsp");
        return "/designs/auth.jsp";
    }


    public String updatePassword(User user, ResponseData data) {

        String permission = getPermission(Long.toString(user.getId()));
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            return "[redirect]/";
        }

        if(user.getPassword().length() < 7){
            data.put("message", "Passwords must be at least 7 characters long.");
            return "[redirect]/signup";
        }

        if(!user.getPassword().equals("")){
            user.setPassword(Parakeet.dirty(user.getPassword()));
            userRepo.updatePassword(user);
        }

        data.put("message", "password successfully updated");
        Long id = authService.getUser().getId();
        return "[redirect]/user/edit_password/" + id;

    }

    public String deleteUser(Long id, ResponseData data) {
        if(!authService.isAdministrator()){
            data.put("message", "You don't have permission");
            return "[redirect]/";
        }

        data.put("message", "Successfully deleted user");
        return "[redirect]/admin/users";
    }


    public String sendReset(ResponseData data, HttpServletRequest req) {

        try {
            String phone = Polygon.getPhone(req.getParameter("phone"));
            User user = userRepo.getPhone(phone);
            if (user == null) {
                data.put("message", "We were unable to find user with given cell phone number.");
                return ("[redirect]/user/reset");
            }

            String guid = Polygon.getString(6);
            user.setPassword(Parakeet.dirty(guid));
            userRepo.update(user);

            String message = "Atto >_ Your temporary password is " + guid;
            smsService.send(user.getPhone(), message);

        }catch(Exception e){
            e.printStackTrace();
            data.set("message", "Something went awry! You might need to contact support!");
            return "[redirect]/signin";
        }

        data.set("message", "Successfully sent reset password!");
        return "[redirect]/signin";
    }

    public String resetPassword(Long id, ResponseData data, HttpServletRequest req) {

        User user = userRepo.get(id);
        String uuid = req.getParameter("uuid");
        String username = req.getParameter("username");
        String rawPassword = req.getParameter("password");

        if(rawPassword.length() < 7){
            data.put("message", "Passwords must be at least 7 characters long.");
            return "[redirect]/user/confirm?username=" + username + "&uuid=" + uuid;
        }

        if(!rawPassword.equals("")){
            String password = Parakeet.dirty(rawPassword);
            user.setPassword(password);
            userRepo.updatePassword(user);
        }

        data.put("message", "Password successfully updated");
        data.put("page", "/pages/user/success.jsp");
        return "/designs/guest.jsp";
    }

    public String updateUser(Long id, ResponseData data, HttpServletRequest req) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }

        String permission = getPermission(Long.toString(id));
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            return "[redirect]/";
        }

        User user = userRepo.get(id);
        String originalPhone = user.getPhone();
        String phone = Polygon.getPhone(req.getParameter("phone"));

        user.setPhone(phone);
        userRepo.update(user);

        if(!phone.equals(originalPhone)){
            authService.signout();
            data.put("message", "Successfully updated user. Your cell changed to " + phone + ". Please sign in with your new cell.");
            return "[redirect]/signin";
        }else{
            data.put("message", "Successfully updated user.");
            return "[redirect]/users/edit/" + id;
        }
    }
}
