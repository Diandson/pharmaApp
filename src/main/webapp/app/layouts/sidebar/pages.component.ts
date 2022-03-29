import { Component } from '@angular/core';

import {Authority} from "../../config/authority.constants";
import {StructAdminMenu} from "./struct-admin-menu";

@Component({
  selector: 'jhi-pages',
  styleUrls: ['./pages.component.scss'],
  templateUrl: './pages.component.html',
})
export class PagesComponent {
  authority = Authority;

  // admin_menu = ADMIN_MENU_ITEMS;
  structAdminMenu = StructAdminMenu;

}
