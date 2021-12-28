import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { EmailHeaderResponse } from '../../classes/Responses/EmailHeaderResponse';

@Component({
  selector: 'app-folder-view',
  templateUrl: './folder-view.component.html',
  styleUrls: ['./folder-view.component.css']
})
export class FolderViewComponent implements OnInit {
  selectedRadio!: boolean;
  selectedSearching!: any; 
  selectedSorting: string = "date";
  settingsForm!: FormGroup;
  emails!: EmailHeaderResponse[];
  selected: Set<number>;
  pageNumber: number = 1;
  constructor(private router: Router, private r: ActivatedRoute) {
    r.params.subscribe(val =>{
      console.log(this.getPageName());
      //api to get emails
      this.emails = [
        {
          id: 0,
          from: '0',
          date: new Date("2019-10-12"),
          priority: 2,
          subject: 'eee',
          isRead: false,
        },
        {
          id: 1,
          from: '1',
          date: new Date("2020-06-05"),
          priority: 1,
          subject: 'fff',
          isRead: true,
        },
        {
          id: 2,
          from: '2',
          date: new Date("2020-06-05"),
          priority: 1,
          subject: 'fff',
          isRead: true,
        },
        {
          id: 3,
          from: '3',
          date: new Date("2020-06-05"),
          priority: 1,
          subject: 'fff',
          isRead: true,
        },
        {
          id: 4,
          from: '4',
          date: new Date("2020-06-05"),
          priority: 1,
          subject: 'fff',
          isRead: true,
        },
      ]
    });
    this.selected = new Set<number>();
   }

  ngOnInit(): void {
    
  }
  updateSorting(e: any){
    this.selectedSorting = e.target.value  ;
    console.log(this.selectedSorting);
  }
  updateSearching(){
    console.log(this.selectedSearching);
  }
  updateRadio(b: boolean){
    this.selectedRadio = b;
  }
 
  select(index: number){
    if(this.selected.has(index)){
      this.selected.delete(index);
      console.log(index + " is deselected");

    }else{
      this.selected.add(index);
      console.log(index + " is selected");

    }
  }
  getPageName(){
    return this.r.snapshot.paramMap.get("folder");
  }

  goToEmail(id: number){
    if(this.emails[id].isRead == false) this.emails[id].isRead = true;
    this.router.navigate([id],{relativeTo: this.r});
  }
  moveForward(){
    if(this.emails.length == 5){
      ++this.pageNumber;
      //api to get the emails in case of none -> --this.pageNumber;
    }
  }
  moveBackward(){
    if(this.pageNumber > 1){
      --this.pageNumber;
      //api to get the emails;
    }
  }

  delete(index: number, e: any){
    e.stopPropagation();
    console.log(index + " is deleted");
    //call api
    this.emails.splice(index, 1);
  }
  toggleRead(index: number, e: any){
    e.stopPropagation();
    this.emails[index].isRead = !this.emails[index].isRead;
    //call api
  }
}
