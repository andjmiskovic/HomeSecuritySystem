import {Component, EventEmitter, Inject, Output, ViewChild} from '@angular/core';
import {TokenService} from "../../../../services/token.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {BlacklistTableComponent} from "../../components/blacklist-table/blacklist-table.component";

@Component({
  selector: 'app-tokens',
  templateUrl: './tokens-container.component.html',
  styleUrls: ['./tokens-container.component.css']
})
export class TokensContainerComponent {
  token: string = '';
  @ViewChild(BlacklistTableComponent) tableComponent!:BlacklistTableComponent;
  constructor(private tokenService: TokenService, @Inject(MatSnackBar) private _snackBar: MatSnackBar) {
  }

  addTokenToBlackList() {
    this.tokenService.addTokenToBlacklist(this.token).subscribe({
      next: () => {
        this.token = ""
        this.tableComponent.loadBlacklist();
      },
      error: (err) => console.error(err)
    })
  }
}
