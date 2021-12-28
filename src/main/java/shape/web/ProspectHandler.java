package shape.web;

import shape.service.ProspectService;
import qio.annotate.HttpHandler;
import qio.annotate.Inject;
import qio.annotate.Text;
import qio.annotate.Variable;
import qio.annotate.verbs.Get;
import qio.annotate.verbs.Post;
import qio.model.web.ResponseData;

import javax.servlet.http.HttpServletRequest;

@HttpHandler
public class ProspectHandler {

    @Inject
    ProspectService prospectService;

    @Text
    @Get("/data")
    public String data(ResponseData data){
        return prospectService.data(data);
    }

    @Get("/snapshot")
    public String snapshot(ResponseData data){
        return prospectService.snapshot(data);
    }

    @Get("/prospects")
    public String searchScreen(HttpServletRequest req,
                                ResponseData data){
        return prospectService.searchScreen(data, req);
    }

    @Get("/prospects/search")
    public String getProspects(HttpServletRequest req,
                              ResponseData data){
        return prospectService.getProspects(data, req);
    }

    @Get("/prospects/{{id}}")
    public String index(ResponseData data,
                        @Variable Long id){
        return prospectService.index(id, data);
    }

    @Get("/prospects/create")
    public String index(ResponseData data){
        return prospectService.create(data);
    }

    @Post("/prospects/save")
    public String save(HttpServletRequest req,
                       ResponseData data){
        return prospectService.save(data, req);
    }

    @Get("/prospects/edit/{{id}}")
    public String getEdit(ResponseData data,
                          @Variable Long id){
        return prospectService.getEdit(id, data);
    }

    @Post("/prospects/update")
    public String update(HttpServletRequest req,
                         ResponseData data){
        return prospectService.update(data, req);
    }

    @Post("/prospects/delete/{{id}}")
    public String delete(HttpServletRequest req,
                         ResponseData data,
                         @Variable Long id){
        return prospectService.delete(id, data);
    }

    @Get("/prospects/history/{{id}}")
    public String history(ResponseData data,
                          @Variable Long id){
        return prospectService.history(id, data);
    }

    @Get("/prospects/activity/add/{{id}}")
    public String addActivity(ResponseData data,
                                @Variable Long id){
        return prospectService.addActivity(id, data);
    }

    @Post("/prospects/activity/save/{{id}}")
    public String saveActivity(HttpServletRequest req,
                              ResponseData data,
                              @Variable Long id){
        return prospectService.saveActivity(id, data, req);
    }

    @Get("/prospects/activity/edit/{{id}}")
    public String editActivity(ResponseData data,
                              @Variable Long id){
        return prospectService.editActivity(id, data);
    }

    @Post("/prospects/activity/update/{{id}}")
    public String updateActivity(HttpServletRequest req,
                                 ResponseData data,
                                 @Variable Long id){
        return prospectService.updateActivity(id, data, req);
    }

    @Post("/prospects/activity/delete/{{id}}")
    public String deleteActivity(HttpServletRequest req,
                                 ResponseData data,
                                 @Variable Long id){
        return prospectService.deleteActivity(id, data, req);
    }

    @Post("/prospects/activity/complete/{{id}}")
    public String completeActivity(HttpServletRequest req,
                             ResponseData data,
                             @Variable Long id){
        return prospectService.completeActivity(id, data, req);
    }

    @Post("/prospects/effort/save/{{id}}")
    public String saveEffort(HttpServletRequest req,
                               ResponseData data,
                               @Variable Long id){
        return prospectService.saveEffort(id, data, req);
    }

    @Post("/prospects/effort/stop/{{id}}")
    public String stopEffort(HttpServletRequest req,
                             ResponseData data,
                             @Variable Long id){
        return prospectService.stopEffort(id, data, req);
    }

    @Get("/prospects/notes/edit/{{id}}")
    public String editNotes(ResponseData data,
                            @Variable Long id){
        return prospectService.editNotes(id, data);
    }

    @Post("/prospects/notes/update/{{id}}")
    public String updateNotes(HttpServletRequest req,
                             ResponseData data,
                             @Variable Long id){
        return prospectService.updateNotes(id, data, req);
    }

    @Post("/prospects/sale/{{id}}")
    public String markSale(HttpServletRequest req,
                              ResponseData data,
                              @Variable Long id){
        return prospectService.markSale(id, data, req);
    }
}
