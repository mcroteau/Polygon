package shape.web;

import shape.service.AuthService;
import qio.annotate.HttpHandler;
import qio.annotate.Inject;
import qio.annotate.verbs.Get;

@HttpHandler
public class BasicHandler {

    @Inject
    AuthService authService;

    @Get("/")
    public String index(){
        if(authService.isAuthenticated()){
            return "[redirect]/snapshot";
        }
        return "[redirect]/signin";
    }

    @Get("/about")
    public String about(){
        return "pages/signin.jsp";
    }

    @Get("/contact")
    public String a(){
        return "/pages/signin.jsp";
    }
    @Get("/mock")
    public String m(){
        return "/pages/signin.jsp";
    }
}
