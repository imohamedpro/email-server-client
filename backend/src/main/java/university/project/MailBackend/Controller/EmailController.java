package university.project.MailBackend.Controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import university.project.MailBackend.Model.*;
import university.project.MailBackend.Model.Requests.*;
import university.project.MailBackend.Service.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

@RestController
@CrossOrigin()
@RequestMapping("/api")
public class EmailController {
    private AccountManager accountManager;
    private ContactManager contactManager;
    private EmailManager emailManager;
    private FolderManager folderManager;
    private FileService fileService;

    public EmailController(FileService fileService) {
        this.fileService = new FileService();
        Storage storage = new Storage(fileService);
        StorageProxy storageProxy = new StorageProxy(storage);
        StorageAdapter storageAdapter = new StorageAdapter(storageProxy);
        this.accountManager = new AccountManager(storageAdapter);
        this.contactManager = new ContactManager(storageAdapter);
        this.emailManager = new EmailManager(storageAdapter);
        this.folderManager = new FolderManager(storageAdapter, new SortFactory());
    }

    @PostMapping("/signup")
    public boolean signUp(@RequestBody UserInfo userInfo){
        return accountManager.signUp(userInfo);
    }

    @PostMapping("/login")
    public boolean signIn(@RequestBody UserInfo userInfo){
        return accountManager.signIn(userInfo);
    }

    @GetMapping("/home-folders")
    public FoldersInfo getHomeFolders(@RequestParam("user") String user){
        return folderManager.getFoldersInfo(user);
    }

    @GetMapping("/contact/pages")
    public int getContactPages(
            @RequestParam("user") String user,
            @RequestParam("perPage") int perPage,
            @RequestParam("tokens") List<String> tokens)
    {
        return contactManager.getNumberOfPages(user, perPage, tokens);
    }

    @GetMapping("/contact/load")
    public List<Contact> loadContacts(
            @RequestParam("user") String user,
            @RequestParam("pageNumber") int pageNumber,
            @RequestParam("perPage") int perPage,
            @RequestParam("sorted") boolean sorted)
    {
        return contactManager.getContactsList(user, pageNumber, perPage, sorted);
    }

    @GetMapping("/contact/get")
    public Contact getContact(
            @RequestParam("user") String user,
            @RequestParam("id") int contactID)
    {
        return contactManager.getContact(user, contactID);
    }

    @GetMapping("/contact/search")
    public List<Contact> searchContact(@RequestParam("user") String user,
                                       @RequestParam("tokens") List<String> tokens,
                                       @RequestParam("pageNumber") int pageNumber,
                                       @RequestParam("perPage") int perPage,
                                       @RequestParam("sorted") boolean sorted)
    {
        return contactManager.searchContact(user, tokens, pageNumber, perPage, sorted);
    }

    @DeleteMapping("/contact/delete")
    public void deleteContact(
            @RequestParam("user") String user,
            @RequestParam("id") int contactID){
        contactManager.deleteContact(user, contactID);
    }

    @PostMapping("/contact/add")
    public void addContact(@RequestBody ContactAndUsername contactAndUsername){
        contactManager.addContact(contactAndUsername.user, contactAndUsername.contact);
    }

    @GetMapping("/email/create")
    public int createEmail(@RequestParam("user") String user){
        return emailManager.createEmail(user);
    }
    @PostMapping("/email/send")
    public void sendEmail(@RequestBody Email email){
        emailManager.sendEmail(email);
    }

    @PostMapping("/email/save-draft")
    public void saveDraft(@RequestBody Email email){
        emailManager.saveDraft(email);
    }

    @DeleteMapping("/email/trash")
    public void moveToTrash(@RequestParam("emailIDs") int[] emailIDs, @RequestParam("user") String user){
        emailManager.moveToTrash(emailIDs, user);
        System.out.println(emailIDs[0] + " is moved to trash");
    }

    @DeleteMapping("/email/delete")
    public void deleteEmails(@RequestParam("emailIDs") int[] emailIDs, @RequestParam("user") String user){
        emailManager.deleteEmails(emailIDs, user);
    }

    @GetMapping("/email/get")
    public Email getEmail(
            @RequestParam("id") int emailID,
            @RequestParam("user") String user)
    {
        return emailManager.readEmail(emailID, user);
    }

    @PostMapping("/email/restore")
    public void restoreEmails(@RequestBody EmailUserClass email){
        emailManager.restoreEmails(email.emailIDs, email.user);
    }

    @PostMapping("/email/read")
    public void markAsRead(@RequestBody EmailUserClass email){
        emailManager.markAsRead(email.emailIDs, email.user);
    }

    @PostMapping("/email/unread")
    public void markAsUnread(@RequestBody EmailUserClass email){
        emailManager.markAsUnread(email.emailIDs, email.user);
    }

    @PostMapping("/email/move")
    public void moveEmails(@RequestBody MoveEmailClass moveEmailObject){
        emailManager.moveEmails(moveEmailObject.emailIDs, moveEmailObject.destinationID, moveEmailObject.user);
    }

    @PostMapping("/folder/set")
    public void setFolder(@RequestBody SetFolder setFolder){
        folderManager.setFolder(setFolder.folderID, setFolder.folderName, setFolder.filterTokens, setFolder.user);
    }

    @GetMapping("/folder/get")
    public Folder getFolder(@RequestParam("id") int folderID, @RequestParam("user") String user){
        return folderManager.getFolder(folderID, user);
    }
    @DeleteMapping("/folder/delete")
    public void deleteFolder(@RequestParam("user") String user, @RequestParam("id") int folderID){
        folderManager.deleteFolder(user, folderID);
    }
    @PutMapping("/folder/rename")
    public void renameFolder(@RequestBody SetFolder folder){
        folderManager.rename(folder.folderID, folder.folderName, folder.user);
    }
    @PutMapping("/folder/editFilterTokens")
    public void editFilterTokens(@RequestBody SetFolder folder){
        folderManager.editTokens(folder.folderID, List.of(folder.filterTokens), folder.user);
    }
    @GetMapping("/folder/getFilterTokens")
    public List<String> getFilterTokens(@RequestParam("id") int folderID, @RequestParam("user") String user){
        return folderManager.getFilterTokens(folderID, user);
    }
    @GetMapping("/folder/load")
    public List<HeaderResponse> loadFolder(@RequestParam("folderID") int folderID,
                                           @RequestParam("sortBy") String sortBy,
                                           @RequestParam("reverse") boolean reverse,
                                           @RequestParam("searchToken") String searchToken,
                                           @RequestParam("pageNumber") int pageNumber,
                                           @RequestParam("perPage") int perPage,
                                           @RequestParam("user") String user){
        return folderManager.loadFolder(folderID,sortBy,reverse,searchToken,pageNumber,perPage,user);
    }

    @GetMapping("/folder/pages")
    public int getFolderPages(
            @RequestParam("id") int folderID,
            @RequestParam("perPage") int perPage,
            @RequestParam("user") String user,
            @RequestParam(value = "searchToken") String token
    ){
        return folderManager.getNumberOfPages(folderID, perPage, user, token);
    }

    @GetMapping("/attachment/download")
    public ResponseEntity<Object> downloadAttachment(
            @RequestParam("user") String username,
            @RequestParam("emailID") String emailID,
            @RequestParam("fileName") String fileName)
    {
        String path = "Database/" + username + "/attachments/" + emailID + "/" + fileName;
        System.out.println(path);
        File file = new File(path);

        try {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");

            return ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(
                    MediaType.parseMediaType("application/txt")).body(resource);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body("Exception occurred for: " + fileName + "!");
        }
    }

    @PostMapping("/attachment/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("user") String username,
            @RequestParam("emailID") String emailID,
            @RequestParam("fileName") String fileName)
    {
        String path = "Database/" + username + "/attachments/" + emailID + "/" + fileName;
        try {
            fileService.writeFile(path, file.getBytes());
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Files uploaded successfully: " + file.getOriginalFilename());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body("Exception occurred for: " + file.getOriginalFilename() + "!");
        }
    }

    @DeleteMapping("/attachment/delete")
    public void deleteFile(
            @RequestParam("user") String username,
            @RequestParam("emailID") String emailID,
            @RequestParam("fileName") String fileName)
    {

        String path = "Database/" + username + "/attachments/" + emailID + "/" + fileName;
        System.out.println(path);

        fileService.deleteFile(path);
        //Delete file from email
    }

}
