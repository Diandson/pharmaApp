import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { SessionStorageService } from 'ngx-webstorage';

import { VERSION } from 'app/app.constants';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from 'app/login/login.service';
import { ProfileService } from 'app/layouts/profiles/profile.service';
import { EntityNavbarItems } from 'app/entities/entity-navbar-items';
import {NbMediaBreakpointsService, NbMenuItem, NbMenuService, NbThemeService} from "@nebular/theme";
import {filter, map, takeUntil} from "rxjs/operators";
import { Subject} from "rxjs";
import {DataService} from "../../shared/data/DataService";
import {IStructure, Structure} from "../../entities/structure/structure.model";
import {StructureService} from "../../entities/structure/service/structure.service";

@Component({
  selector: 'jhi-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit {
  isNavbarCollapsed = true;
  version = '';
  account: Account | null = null;
  entitiesNavbarItems: any[] = [];
  userPictureOnly?: boolean = false;
  user: any;
  itemsMenu: NbMenuItem[] = [
    {
      title: 'Gestion des médicaments',
      link: 'medicament',
      pathMatch: "full",
      icon: 'archive'
    },
    {
      title: 'Gestion des Utilisateurs',
      link: 'admin/user-management/all',
      pathMatch: "full",
      icon: 'people'
    },
    {
      title: 'Surveillance d\'activitées',
      link: 'admin/tracker',
      pathMatch: "full",
      icon: 'monitor'
    }
  ];
  userMenu: NbMenuItem[] = [
    {
      title: 'Profile',
      pathMatch: "full",
      link: 'account/settings',
      icon: 'people'
    },
    {
      title: 'Déconnexion',
      pathMatch: "full",
      icon: 'power'
    },
  ];

  currentTheme?: string = 'default';
  structure: IStructure = new Structure();

  private destroy$: Subject<void> = new Subject<void>();

  constructor(
    private loginService: LoginService,
    private translateService: TranslateService,
    private sessionStorageService: SessionStorageService,
    private accountService: AccountService,
    private profileService: ProfileService,
    private breakpointService: NbMediaBreakpointsService,
    private menuService: NbMenuService,
    private themeService: NbThemeService,
    private dataService: DataService,
    private structureService: StructureService,
    private router: Router
  ) {
    if (VERSION) {
      this.version = VERSION.toLowerCase().startsWith('v') ? VERSION : `v${VERSION}`;
    }
  }

  ngOnInit(): void {
    this.entitiesNavbarItems = EntityNavbarItems;

    this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
    });

    if (this.isAuthenticated()){
      this.structureService.findOnlyAuth().subscribe(res => {
        if (res.body){
          this.structure = res.body;
          this.dataService.actializeStructure(this.structure);
        }else {
          this.router.navigate(['login']);
        }
      }, () => {
        this.router.navigate(['login']);
      })
    }

    const { xl } = this.breakpointService.getBreakpointsMap();
    this.themeService.onMediaQueryChange()
      .pipe(
        map(([, currentBreakpoint]) => currentBreakpoint.width < xl),
        takeUntil(this.destroy$),
      )
      .subscribe((isLessThanXl: boolean) => this.userPictureOnly = isLessThanXl);

    this.themeService.onThemeChange()
      .pipe(
        map((value: string) => value),
        takeUntil(this.destroy$),
        )
      .subscribe(themeName => this.currentTheme = themeName);

    this.menuService.onItemClick()
      .pipe(
        filter(({ tag }) => tag === 'userTags'),
        map(({ item: { title } }) => title),
      )
      .subscribe(title => {
        if (title === 'Déconnexion'){
          this.logout();
        }
      });
  }

  changeLanguage(languageKey: string): void {
    this.sessionStorageService.store('locale', languageKey);
    this.translateService.use(languageKey);
  }
  changeTheme(themeName: string): void {
    this.themeService.changeTheme(themeName);
  }

  collapseNavbar(): void {
    this.isNavbarCollapsed = true;
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  logout(): void {
    this.collapseNavbar();
    this.loginService.logout();
    this.router.navigate(['']);
  }

  isAuthenticated(): boolean{
    return this.accountService.isAuthenticated();
  }

  toggleNavbar(): void {
    this.isNavbarCollapsed = !this.isNavbarCollapsed;
  }
}
