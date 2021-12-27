package university.project.MailBackend.Model;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class UserData {
    public Map<String, Folder> folders;
    public Map<Integer, Email> emails;
    private int nextEmailID;

    public void addEmail(Email email, String type){
        if(email.id < 0){
            email.id = nextEmailID++;
        }
        emails.put(email.id, email);
        Folder folder;
        switch (type.toLowerCase()){
            case "draft":
                folder = folders.get(type.toLowerCase());
                folder.addEmail(email);
                break;
            case "sent":
                folder = folders.get("draft");
                folder.removeEmail(email);
                folder = folders.get("sent");
                folder.addEmail(email);
                break;
            case "received":
                folder = folders.get("inbox");
                folder.addEmail(email);
                for(Folder f: folders.values()){
                    f.filter(email);        // default folders doesn't have filter tokens(null object ?)
                }
                break;
        }
    }

    public void moveToTrash(Integer emailID){
        if(emails.containsKey(emailID)){
            Email email = emails.get(emailID);
            for(String folderName: email.folders){
                Folder folder = this.folders.get(folderName);
                folder.removeEmail(email);
            }
            Folder trash = folders.get("trash");
            email.deleteDate = new Date();
            trash.addEmail(email);
        }
    }
    public void deleteEmail(int emailID){
        if(emails.containsKey(emailID)){
            Folder trash = folders.get("trash");
            Email email = emails.remove(emailID);
            trash.removeEmail(email);
        }
    }

    public void autoDelete(){
        Folder trash = folders.get("trash");
        for(int emailID: trash.emails){
            Email email = emails.get(emailID);
            if(TimeUnit.DAYS.convert(new Date().getTime() - email.deleteDate.getTime(), TimeUnit.MILLISECONDS) > 30){
                emails.remove(email.id);
                trash.removeEmail(email);
            }
        }
    }

    public void addFolder(Folder folder){
        this.folders.put(folder.name, folder);
    }
    public void deleteFolder(String name){
        Folder folder = this.folders.remove(name);
        for(int emailID: folder.emails){
            folder.removeEmail(this.emails.get(emailID));
        }
    }

}
