import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { SessionStorageService } from 'ngx-webstorage';

import { VERSION } from 'app/app.constants';
import { LANGUAGES } from 'app/config/language.constants';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from 'app/login/login.service';
import { ProfileService } from 'app/layouts/profiles/profile.service';
import { EntityNavbarItems } from 'app/entities/entity-navbar-items';
import {NbMediaBreakpointsService, NbMenuItem, NbMenuService, NbThemeService} from "@nebular/theme";
import {map, takeUntil} from "rxjs/operators";
import { Subject} from "rxjs";

@Component({
  selector: 'jhi-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit {
  inProduction?: boolean;
  isNavbarCollapsed = true;
  languages = LANGUAGES;
  openAPIEnabled?: boolean;
  version = '';
  account: Account | null = null;
  entitiesNavbarItems: any[] = [];
  userPictureOnly?: boolean = false;
  user: any;
  itemsMenu: NbMenuItem[] = [
    {
      title: 'Gestion des médicaments',
      link: 'medicament',
    },
    {
      title: 'Gestion des Utilisateurs',
      link: 'admin/user-management/all',
    },
    {
      title: 'Surveillance d\'activitées',
      link: 'admin/tracker',
    }
  ];
  themes = [
    {
      value: 'default',
      name: 'Light',
    },
    {
      value: 'dark',
      name: 'Dark',
    },
    {
      value: 'cosmic',
      name: 'Cosmic',
    },
    {
      value: 'corporate',
      name: 'Corporate',
    },
  ];

  currentTheme?: string = 'default';

  userMenu = [ { title: 'Profile' }, { title: 'Déconnexion' } ];
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
