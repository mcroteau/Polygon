package shape.web;

import shape.service.AuthService;
import qio.annotate.HttpHandler;
import qio.annotate.Inject;
import qio.annotate.verbs.Get;
import qio.annotate.verbs.Post;
import qio.model.web.ResponseData;

import javax.servlet.http.HttpServletRequest;

@HttpHandler
public class AuthHandler {

	@Inject
	AuthService authService;

	@Post("/authenticate")
	public String authenticate(HttpServletRequest req,
							   ResponseData data){
		return authService.authenticate(data, req);
	}

	@Get("/signin")
	public String signin(ResponseData data){
		data.put("page", "/pages/signin.jsp");
		return "/designs/guest.jsp";
	}

	@Get("/signup")
	public String signup(ResponseData data){
		data.put("page", "/pages/signup.jsp");
		return "/designs/guest.jsp";
	}

	@Get("/signout")
	public String signout(HttpServletRequest request,
						  ResponseData data){
		return authService.deAuthenticate(data, request);
	}

	@Get("/unauthorized")
	public String unauthorized(ResponseData data){
		data.put("page", "/pages/401.jsp");
		return "/designs/guest.jsp";
	}

}