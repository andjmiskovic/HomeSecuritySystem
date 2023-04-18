import {Component, OnInit} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {MatDialog} from "@angular/material/dialog";
import {UserListItem} from "../../model/UserListItem";
import {User} from "../../../../model/User";
import {UserService} from "../../../../services/user.service";
import {RegisterNewUserDialogComponent} from "../register-new-user-dialog/register-new-user-dialog.component";
import {EditUserDialogComponent} from "../edit-user-dialog/edit-user-dialog.component";

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {
  displayedColumns: string[] = ['name', 'email', 'type', 'requestedChanges'];
  userList: MatTableDataSource<UserListItem> = new MatTableDataSource<UserListItem>();
  propertyOwners: User[] = [];
  filterUsersByRequests = false;

  constructor(private userService: UserService, private dialog: MatDialog) {
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.userList.filter = filterValue.trim().toLowerCase();
  }

  ngOnInit(): void {
    this.getUsers(false);
  }

  public getUsers(filterByRequests: boolean) {
    this.filterUsersByRequests = filterByRequests;
    this.userService.getPropertyOwners().subscribe((propertyOwners) => {
      this.propertyOwners = propertyOwners;
      this.userList = new MatTableDataSource<UserListItem>(this.usersToUserListItems(this.propertyOwners));
    });
  }

  registerNewUser() {
    const dialogRef = this.dialog.open(RegisterNewUserDialogComponent, {
      width: '600px',
      height: '600px'
    });
    dialogRef.afterClosed().subscribe(() => this.getUsers(false))
  }

  openUsersProfileDialog(element: UserListItem) {
    const dialogRef = this.dialog.open(EditUserDialogComponent, {panelClass: 'no-padding-card'});
    // dialogRef.componentInstance.userEmail = element.email;
    // dialogRef.componentInstance.userRole = 'ADMIN';
  }

  private usersToUserListItems(users: User[]): UserListItem[] {
    const userList: UserListItem[] = [];
    for (let i = 0; i < users.length; i++) {
      if (!this.filterUsersByRequests) {
        userList.push(new UserListItem(users[i].email, users[i].firstName + ' ' + users[i].lastName, "tenant"));
      }
    }
    return userList;
  }
}
