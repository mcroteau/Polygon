package shape.web;

import shape.service.UserService;
import qio.annotate.HttpHandler;
import qio.annotate.Inject;
import qio.annotate.Variable;
import qio.annotate.verbs.Get;
import qio.annotate.verbs.Post;
import qio.model.web.ResponseData;

import javax.servlet.http.HttpServletRequest;


@HttpHandler
public class UserHandler {

	@Inject
	UserService userService;

	@Get("/users")
	public String getUsers(ResponseData data){
		return userService.getUsers(data);
	}

	@Get("/users/create")
	public String create(ResponseData data){
		return userService.create(data);
	}

	@Post("/users/save")
	public String save(HttpServletRequest req,
					   ResponseData data) {
		return userService.save(data, req);
	}

	@Get("/users/edit/{{id}}")
	public String getEditUser(ResponseData data,
							  @Variable Long id){
		return userService.getEditUser(id, data);
	}

	@Post("/users/delete/{{id}}")
	public String deleteUser(ResponseData data,
							 @Variable Long id) {
		return userService.deleteUser(id, data);
	}

	@Post("/users/update/{{id}}")
	public String updateUser(HttpServletRequest req,
							 ResponseData data,
							 @Variable Long id){
		return userService.updateUser(id, data, req);
	}

	@Get("/users/reset")
	public String reset(){
		return "/pages/user/reset.jsp";
	}

	@Post("/users/send")
	public String sendReset(HttpServletRequest request,
							ResponseData data){
		return userService.sendReset(data, request);
	}

	@Post("/users/reset/{{id}}")
	public String resetPassword(HttpServletRequest req,
								ResponseData data,
								@Variable Long id){
		return userService.resetPassword(id, data, req);
	}

}