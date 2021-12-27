package university.project.MailBackend.Model.Filter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import university.project.MailBackend.Model.Mail;

import java.util.ArrayList;

public class PriorityCriteria implements Criteria{
    private int criteria;

    @JsonCreator
    public PriorityCriteria(@JsonProperty("criteria") int criteria) {
        this.criteria = criteria;
    }

    @Override
    public ArrayList<Mail> meetCriteria(ArrayList<Mail> mails) {
        ArrayList<Mail> criteriaMails = new ArrayList<>();
        for(Mail mail: mails){
            if(mail.getHeader().getPriority() == criteria){
                criteriaMails.add(mail);
            }
        }
        return criteriaMails;
    }
}
