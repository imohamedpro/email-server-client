import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { contactsRequest } from 'src/app/classes/Requests/ContactsRequest';

@Component({
  selector: 'app-contacts-folder',
  templateUrl: './contacts-folder.component.html',
  styleUrls: ['./contacts-folder.component.css']
})
export class ContactsFolderComponent implements OnInit {
  contacts!: contactsRequest[];
  pageNumber!: number;
  totalPageNumber!: number;
  constructor(private router: Router, private r: ActivatedRoute) {
    this.r.params.subscribe(val => {
      //api to get InitContactsFolder
      this.contacts = [
        {
          contactName: "karim",
          emails: ["karim@gmail.com","karim2@gmail.com","karim3@gmail.com"],
          id: 0
        },
        {
          contactName: "Magdy",
          emails: ["magdy@gmail.com", "magdy2@gmail.com", "magdy3@gmail.com", "magdy4@gmail.com", "magdy5@gmail.com"],
          id: 1,
        },
        {
          contactName: "Youssef",
          emails: ["youssef@gmail.com", "youssef2@gmail.com", "youssef3@gmail.com"],
          id: 2,
        },
        {
          contactName: "Youhanna",
          emails: ["youhanna1@gmail.com", "youhanna2@gmail.com", "youhanna3@gmail.com"],
          id: 2,
        },
      ]
    });
    this.pageNumber = 1;
    this.totalPageNumber = 3;
  }

  ngOnInit(): void {
  }
  goToContact(index: number) {
    return this.router.navigate([this.contacts[index].id], { relativeTo: this.r });
  }
  delete(index: number) {
    this.contacts.splice(index, 1);
  }
  moveForward() {
    if (this.pageNumber < this.totalPageNumber) {
      ++this.pageNumber;
      //api to get the contacts in case of none -> --this.pageNumber;
    }
  }
  moveBackward() {
    if (this.pageNumber > 1) {
      --this.pageNumber;
      //api to get the contacts;
    }
  }

}
