package university.project.MailBackend.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.tomcat.util.http.fileupload.FileUtils;

import university.project.MailBackend.Interfaces.IStorage;
import university.project.MailBackend.Model.UserContact;
import university.project.MailBackend.Model.UserData;
import university.project.MailBackend.Model.UserInfo;

public class Storage implements IStorage {
    private FileService fileService;
    private String defaultPath;

    public Storage(FileService fileService){
        this.fileService = fileService;
        this.defaultPath = "Database/";
    }

    @Override
    public UserInfo getUserInfo(String user) {
        String path = defaultPath + user + "/Info.json";
        return (UserInfo) fileService.readJson(path, UserInfo.class, false);
    }

    @Override
    public UserData getUserData(String user) {
        String path = defaultPath + user + "/Data.json";
        return  (UserData) fileService.readJson(path, UserData.class, false);
    }

    @Override
    public UserContact getUserContact(String user) {
        String path = defaultPath + user + "/Contacts.json";
        return  (UserContact) fileService.readJson(path, UserContact.class, false);
    }

    @Override
    public void setUserInfo(UserInfo info) {
        String email = info.getEmail();
        String path = defaultPath + email + "/Info.json";
        fileService.saveAsJson(path, info);
    }

    @Override
    public void setUserData(UserData data, String user) {
        String path = defaultPath + user + "/Data.json";
        fileService.saveAsJson(path, data);
    }

    @Override
    public void setUserContact(UserContact contact, String user) {
        String path = defaultPath + user + "/Contacts.json";
        fileService.saveAsJson(path, contact);
    }

    // @Override
    // public void delete(String user, int id, String name) {
    //     String path = defaultPath + user + "/attachments/" + id + "/" + name;
    //     fileService.deleteFile(path);
        
    // }

    @Override
    public void delete(String user, int id) {
        String path = defaultPath + user + "/attachments/" + id;
        fileService.deleteFile(path);
    }

    @Override
    public void addAttachment(String user, int emaiID, File f) {
        String path = defaultPath + user + "/attachments/" + emaiID+ "/";
        try{
            byte[] b = Files.readAllBytes(Paths.get(f.getAbsolutePath()));
            fileService.writeFile(path + f.getName(), b);
            // Files.copy(Paths.get(f.getAbsolutePath()), Paths.get(path + f.getName()), StandardCopyOption.REPLACE_EXISTING);

        }catch(Exception e){
            int x = 0;
        }
        // return new File(path);
    }

    
}
