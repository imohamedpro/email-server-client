package university.project.MailBackend.Service;

import university.project.MailBackend.Model.Email;

public class EmailManager {
    private StorageAdapter storage;
    public EmailManager(StorageAdapter storageAdapter){
        this.storage = storageAdapter;
    }

    /*
         clonning needed??
    */ 
    public void sendEmail(Email email){ 
        String from = email.emailHeader.from;
        String[] recipients = email.emailHeader.to;
        storage.setEmail(from, email, "sent");
        for(String recipient: recipients){
            storage.setEmail(recipient, new Email(email), "received");
        }
    }


    /*
        Needs refactoring (template dp or smth)
    */
    public void saveDraft(Email email){
        String from = email.emailHeader.from;
        storage.setEmail(from, email, "draft");
    }

    public void moveToTrash(int[] emailIDs, String user){
        for(int id: emailIDs){
            storage.deleteEmail(user, id);
        }
        
    }

    public void deleteEmails(int[] emailIDs, String user){
        for(int id: emailIDs){
            storage.deleteEmail(user, id);
        }
    }

    public Email readEmail(int emailID, String user){
        return storage.readEmail(user, emailID);
    }
    
    public void markAsUnread(int[] emailIDs, String user){
        for(int id: emailIDs){
            storage.markAsUnread(id, user);
        }
    }
    public void markAsRead(int[] emailIDs, String user){
        for(int id: emailIDs){
            storage.markAsRead(id, user);
        }
    }
    public void moveEmails(int[] emailIDs, String destination, String user){
        for(int id: emailIDs){
            storage.moveEmail(user, id, destination);
        }
    }
}
