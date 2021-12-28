package shape.service;

import shape.Polygon;
import shape.model.*;
import shape.repo.ActivityRepo;
import shape.repo.EffortRepo;
import shape.repo.StatusRepo;
import shape.repo.ProspectRepo;
import qio.annotate.Inject;
import qio.annotate.Service;
import qio.model.web.ResponseData;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ProspectService {

    @Inject
    AuthService authService;

    @Inject
    ProspectRepo prospectRepo;

    @Inject
    StatusRepo statusRepo;

    @Inject
    EffortRepo effortRepo;

    @Inject
    ActivityRepo activityRepo;

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


    private ProspectActivity checkCorrectPhonesFormat(String phones, ProspectActivity prospectActivity) {
        if(phones == null ||
                phones.equals("")){
            return prospectActivity;
        }
        String[] phoneParts = phones.split(",");
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for(String phone: phoneParts){
            phone = Polygon.getPhone(phone);
            sb.append(phone);
            if(count < phoneParts.length){
                sb.append(",");
            }
            count++;
        }
        prospectActivity.setPhones(sb.toString());
        return prospectActivity;
    }

    protected ProspectActivity setPhone(ProspectActivity activity){
        User user = authService.getUser();
        if(user.getPhone() != null &&
                !user.getPhone().equals("")){
            activity.setPhones(user.getPhone());
        }
        return activity;
    }

    public String data(ResponseData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }

        Map<String, Integer> activitiesAggregated = new HashMap<>();
        List<ProspectActivity> salesActivities = prospectRepo.getCompletedSalesActivities();
        System.out.println("sales activities " + salesActivities.size());
        for(ProspectActivity prospectActivity : salesActivities){
            try {
                if(prospectActivity.getCompleteDate() > 0){
                    SimpleDateFormat incoming = new SimpleDateFormat(Polygon.DATE_FORMAT);
                    Date datePre = incoming.parse(prospectActivity.getCompleteDate().toString());

                    SimpleDateFormat outgoing = new SimpleDateFormat("yyyy-MM-dd");
                    String dateKey = outgoing.format(datePre);

                    System.out.println("key " + dateKey);
                    if(!activitiesAggregated.containsKey(dateKey)){
                        activitiesAggregated.put(dateKey, 1);
                    }else{
                        int count = activitiesAggregated.get(dateKey);
                        count++;
                        activitiesAggregated.replace(dateKey, count);
                    }
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Date,# Sales\n");
        for(Map.Entry<String, Integer> entry: activitiesAggregated.entrySet()){
            sb.append(entry.getKey() + "," + entry.getValue()+ "\n");
        }

        return sb.toString();
    }

    public String snapshot(ResponseData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }

        /*
            Conversion Rate
            How many active Sales Activities
            How many completed Sales Activities
            How many prospects
            How many propsects by status
         */
        List<ProspectActivity> prospectActivities = prospectRepo.getActivities();
        prospectActivities.stream().forEach((prospectActivity -> setPretty(prospectActivity)));
        data.put("prospectActivities", prospectActivities);


        BigDecimal salesCount = new BigDecimal(prospectRepo.getCompletedSalesActivitiesCount());
        BigDecimal completedCount = new BigDecimal(prospectRepo.getCompletedActivitiesCount());

        BigDecimal conversionRate = new BigDecimal(0);
        try{
            conversionRate = salesCount.divide(completedCount, 3, RoundingMode.HALF_UP).movePointRight(2);
        }catch(Exception ex){}

        Long activeCount = prospectRepo.getActiveActivitiesCount();
        Long prospectsCount = prospectRepo.getCount();

        data.put("salesCount", salesCount);
        data.put("prospectsCount", prospectsCount);
        data.put("completedCount", completedCount);
        data.put("activeCount", activeCount);
        data.put("conversionRate", conversionRate);
        data.put("snapshotHref", "active");

        data.put("title", "Snapshot");
        data.put("page", "/pages/prospect/snapshot.jsp");
        return "/designs/auth.jsp";
    }

    public String searchScreen(ResponseData data, HttpServletRequest req) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }

        List<ProspectActivity> prospectActivities = prospectRepo.getActivities();
        prospectActivities.stream().forEach((prospectActivity -> setPretty(prospectActivity)));
        data.put("prospectActivities", prospectActivities);

        List<Prospect> prospects = prospectRepo.getList();

        data.put("searchHref", "active");
        data.put("prospects", prospects);
        data.put("title", "Search Prospects");
        data.put("page", "/pages/prospect/search.jsp");
        return "/designs/auth.jsp";
    }

    public String getProspects(ResponseData data, HttpServletRequest req) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }

        List<ProspectActivity> prospectActivities = prospectRepo.getActivities();
        prospectActivities.stream().forEach((prospectActivity -> setPretty(prospectActivity)));
        data.put("prospectActivities", prospectActivities);

        String query = req.getParameter("q");
        List<Prospect> prospects = prospectRepo.getResults(query);
        data.put("prospects", prospects);

        data.put("searchHref", "active");
        data.put("title", "Search Results");
        data.put("page", "/pages/prospect/results.jsp");
        return "/designs/auth.jsp";
    }

    public String index(Long id, ResponseData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }

        List<ProspectActivity> prospectActivities = prospectRepo.getActivities();
        prospectActivities.stream().forEach((prospectActivity -> setPretty(prospectActivity)));
        data.put("prospectActivities", prospectActivities);

        List<Status> statuses = statusRepo.getList();
        Prospect prospect = prospectRepo.get(id);

        List<ProspectActivity> upcomingActivities = prospectRepo.getIncompletedActivities(id);
        upcomingActivities.stream().forEach((prospectActivity) -> setPretty(prospectActivity));
        Effort effort = effortRepo.getProspectEffort(prospect.getId(), false);

        data.put("effort", effort);
        data.put("upcomingActivities", upcomingActivities);
        data.put("statuses", statuses);
        data.put("prospect", prospect);

        data.put("title", prospect.getName());
        data.put("page", "/pages/prospect/index.jsp");
        return "/designs/auth.jsp";
    }

    public String create(ResponseData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }

        List<ProspectActivity> prospectActivities = prospectRepo.getActivities();
        prospectActivities.stream().forEach((prospectActivity -> setPretty(prospectActivity)));
        data.put("prospectActivities", prospectActivities);

        List<Status> statuses = statusRepo.getList();
        data.put("statuses", statuses);

        data.set("createHref", "active");
        data.put("title", "Create Prospect");
        data.put("page", "/pages/prospect/create.jsp");
        return "/designs/auth.jsp";
    }

    public String save(ResponseData data, HttpServletRequest req) {

        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }

        String name = req.getParameter("name");
        String phone = Polygon.getPhone(req.getParameter("phone"));
        String email = Polygon.getSpaces(req.getParameter("email"));
        String contacts = req.getParameter("contacts");
        Long statusId = Long.parseLong(req.getParameter("status"));

        if(name.equals("")){
            data.put("message", "Please give your prospect a name...");
            return "[redirect]/prospects/create";
        }

        Prospect prospect = new Prospect();
        prospect.setName(name);
        prospect.setPhone(phone);
        prospect.setEmail(email);
        prospect.setContacts(contacts);
        prospect.setStatusId(statusId);

        Prospect savedProspect = prospectRepo.save(prospect);

        data.put("message", "Successfully saved " + prospect.getName() + "!");
        return "[redirect]/prospects/" + savedProspect.getId();
    }

    public String getEdit(Long id, ResponseData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }

        List<ProspectActivity> prospectActivities = prospectRepo.getActivities();
        prospectActivities.stream().forEach((prospectActivity -> setPretty(prospectActivity)));
        data.put("prospectActivities", prospectActivities);

        List<Status> statuses = statusRepo.getList();
        Prospect prospect = prospectRepo.get(id);
        Effort effort = effortRepo.getProspectEffort(prospect.getId(), false);

        data.put("effort", effort);
        data.put("statuses", statuses);
        data.put("prospect", prospect);

        data.put("title", "Edit Prospect");
        data.put("page", "/pages/prospect/edit.jsp");
        return "/designs/auth.jsp";
    }

    public String update(ResponseData data, HttpServletRequest req) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }

        Long id = Long.parseLong(req.getParameter("id"));
        String name = req.getParameter("name");
        String phone = req.getParameter("phone");
        String email = req.getParameter("email");
        String contacts = req.getParameter("contacts");

        if(name.equals("")){
            data.put("message", "Please give your prospect a name...");
            return "[redirect]/prospects/edit/" + id;
        }

        Prospect prospect = prospectRepo.get(id);
        prospect.setName(name);
        prospect.setPhone(phone);
        prospect.setEmail(email);
        prospect.setContacts(contacts);
        prospectRepo.update(prospect);

        List<Status> statuses = statusRepo.getList();

        data.put("statuses", statuses);
        data.put("message", "Successfully updated prospect");
        return "[redirect]/prospects/edit/" + prospect.getId();
    }


    public String delete(Long id, ResponseData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        if(!authService.isAdministrator()){
            return "[redirect]/unauthorized";
        }

        prospectRepo.deleteActivities(id);
        prospectRepo.delete(id);
        data.put("message", "Successfully deleted prospect.");

        return "[redirect]/prospects";
    }

    public String addActivity(Long id, ResponseData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }

        List<ProspectActivity> prospectActivities = prospectRepo.getActivities();
        prospectActivities.stream().forEach((prospectActivity -> setPretty(prospectActivity)));
        data.put("prospectActivities", prospectActivities);

        List<Activity> activities = activityRepo.getList();
        Prospect prospect = prospectRepo.get(id);
        data.put("timezone", TimeZone.getDefault().getDisplayName());
        data.put("activities", activities);
        data.put("prospect", prospect);

        data.put("title", "Prospect Activity");
        data.put("page", "/pages/prospect_activity/index.jsp");
        return "/designs/auth.jsp";
    }

    public String saveActivity(Long id, ResponseData data, HttpServletRequest req) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }

        try {

            Long activityId = Long.parseLong(req.getParameter("activity-id"));
            Activity activity = activityRepo.get(activityId);
            Prospect prospect = prospectRepo.get(id);
            Effort effort = effortRepo.getProspectEffort(prospect.getId(), false);

            String date = req.getParameter("activity-date");
            String hour = Polygon.pad(req.getParameter("activity-hour").trim(), 2, "0");
            String minute = Polygon.pad(req.getParameter("activity-minute").trim(), 2, "0");

            String dateStr = date.concat(" ")
                    .concat(hour)
                    .concat(":").concat(minute);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date datePreParsed = format.parse(dateStr);

            Calendar cal = Calendar.getInstance();
            cal.setTime(datePreParsed);

            String timezone = req.getParameter("timezone");

            Date dateMillis = new Date();
            dateMillis.setTime(cal.getTimeInMillis());
            SimpleDateFormat sdf = new SimpleDateFormat(Polygon.DATE_FORMAT);

            Long taskDate = Long.parseLong(sdf.format(dateMillis));

            ProspectActivity prospectActivity = new ProspectActivity();
            prospectActivity.setTaskDate(taskDate);
            prospectActivity.setTimeZone(timezone);
            prospectActivity.setActivityId(activityId);
            prospectActivity.setProspectId(id);

            if(effort != null){
                prospectActivity.setEffortId(effort.getId());
            }

            setPhone(prospectActivity);

            prospectRepo.saveActivity(prospectActivity);

        }catch(Exception ex){
            ex.printStackTrace();
            data.put("message", "You may have entered in an incorrect time. Please try again");
            return "[redirect]/prospects/activity/add/" + id;
        }

        ProspectActivity savedActivity = prospectRepo.getLastInsertedActivity();
        data.put("message", "Successfully added sales action");
        return "[redirect]/prospects/activity/edit/"  + savedActivity.getId();
    }

    public String completeActivity(Long id, ResponseData data, HttpServletRequest req) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        User user = authService.getUser();
        ProspectActivity prospectActivity = prospectRepo.getActivity(id);
        prospectActivity.setCompleteDate(Polygon.getDate());
        prospectActivity.setCompletedByUserId(user.getId());
        prospectRepo.setCompleted(prospectActivity);

        data.put("message", "Successfully completed activity.");
        return "[redirect]/prospects/history/" + prospectActivity.getProspectId();
    }

    public String deleteActivity(Long id, ResponseData data, HttpServletRequest req) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        ProspectActivity activity = prospectRepo.getActivity(id);
        prospectRepo.deleteActivity(id);
        data.put("message", "Successfully deleted activity");
        return "[redirect]/prospects/" + activity.getProspectId();
    }


    public String saveEffort(Long id, ResponseData data, HttpServletRequest req) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }

        Prospect prospect = prospectRepo.get(id);
        Effort effort = new Effort();
        effort.setProspectId(prospect.getId());
        effort.setStartDate(Polygon.getDate());
        effort.setStartingStatusId(prospect.getStatusId());
        effortRepo.save(effort);

        data.put("message", "Successfully started effort. All sales activities will be recorded and a clear view at your sales funnels will be visible.");
        return "[redirect]/prospects/" + id;
    }

    public String stopEffort(Long id, ResponseData data, HttpServletRequest req) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        Prospect prospect = prospectRepo.get(id);
        Effort effort = effortRepo.getProspectEffort(id, false);
        effort.setEndDate(Polygon.getDate());
        effort.setEndingStatusId(prospect.getStatusId());
        effort.setFinished(true);
        Status endingStatus = statusRepo.get(prospect.getStatusId());
        if(endingStatus.equals(Polygon.CUSTOMER_STATUS)){
            effort.setSuccess(true);
        }
        effortRepo.update(effort);
        data.put("message", "Successfully stopped effort.");
        return "[redirect]/prospects/" + id;
    }

    public String history(Long id, ResponseData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        List<ProspectActivity> prospectActivities = prospectRepo.getActivities();
        prospectActivities.stream().forEach((prospectActivity -> setPretty(prospectActivity)));

        Prospect prospect = prospectRepo.get(id);
        List<ProspectActivity> activities = prospectRepo.getCompletedActivities(id);
        activities.stream().forEach((prospectActivity -> setPretty(prospectActivity)));

        data.put("prospectActivities", prospectActivities);
        data.put("prospect", prospect);
        data.put("activities", activities);

        data.put("title", "Prospect History");
        data.put("page", "/pages/prospect/history.jsp");
        return "/designs/auth.jsp";
    }

    public String editActivity(Long id, ResponseData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        List<ProspectActivity> prospectActivities = prospectRepo.getActivities();
        prospectActivities.stream().forEach((prospectActivity -> setPretty(prospectActivity)));

        ProspectActivity activity = prospectRepo.getActivity(id);
        setPretty(activity);

        data.put("activity", activity);
        data.put("prospectActivities", prospectActivities);

        data.put("title", "Edit Activity");
        data.put("page", "/pages/prospect_activity/edit.jsp");
        return "/designs/auth.jsp";
    }

    public String updateActivity(Long id, ResponseData data, HttpServletRequest req) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }

        ProspectActivity prospectActivity = prospectRepo.getActivity(id);
        String phones = req.getParameter("phones");
        checkCorrectPhonesFormat(phones, prospectActivity);

        Boolean five = getBooleanNotification("five", req);
        Boolean fifteen = getBooleanNotification("fifteen", req);
        Boolean thirty = getBooleanNotification("thirty", req);

        prospectActivity.setCompleted(false);
        prospectActivity.setFiveReminder(five);
        prospectActivity.setFifteenReminder(fifteen);
        prospectActivity.setThirtyReminder(thirty);

        prospectRepo.updateActivity(prospectActivity);

        data.put("message", "Successfully updated sales activity");
        return "[redirect]/prospects/activity/edit/" + id;
    }

    public Boolean getBooleanNotification(String parameter, HttpServletRequest req){
        String on = req.getParameter(parameter);
        if(on != null &&
                on.equals("on")){
            return true;
        }
        return false;
    }


    public String editNotes(Long id, ResponseData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }

        Prospect prospect = prospectRepo.get(id);
        List<ProspectActivity> prospectActivities = prospectRepo.getActivities();
        prospectActivities.stream().forEach((prospectActivity -> setPretty(prospectActivity)));

        data.put("prospect", prospect);
        data.put("prospectActivities", prospectActivities);

        data.put("title", "Notes");
        data.put("page", "/pages/prospect/notes.jsp");
        return "/designs/auth.jsp";
    }

    public String updateNotes(Long id, ResponseData data, HttpServletRequest req) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }

        Prospect prospect = prospectRepo.get(id);
        String notes = req.getParameter("notes");
        prospect.setNotes(notes);
        prospectRepo.update(prospect);

        data.put("message", "Successfully updated notes!");
        return "[redirect]/prospects/notes/edit/" + id;
    }

    public String markSale(Long id, ResponseData data, HttpServletRequest req) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        Prospect prospect = prospectRepo.get(id);
        User authUser = authService.getUser();
        List<ProspectActivity> prospectActivities = prospectRepo.getIncompletedActivities(id);
        for(ProspectActivity prospectActivity : prospectActivities){
            Activity activity = activityRepo.get("Sale");
            prospectActivity.setCompleted(true);
            prospectActivity.setActivityId(activity.getId());
            prospectRepo.markAsSale(prospectActivity);

            prospectActivity.setCompleteDate(Polygon.getDate());
            prospectActivity.setCompletedByUserId(authUser.getId());
            prospectRepo.setCompleted(prospectActivity);
        }
        data.put("message", "Congratulations! Successfully updated all sales activities to sale!");
        return "[redirect]/prospects/edit/" + id;
    }

}
