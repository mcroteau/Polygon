package shape.service;

import qio.annotate.Service;

@Service
public class UxService {

    String businessName;
    String businessEmail;

    public String getBusinessName(){
        return this.businessName;
    }

    public String getBusinessEmail(){
        return this.businessEmail;
    }
}
