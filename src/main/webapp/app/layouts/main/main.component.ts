import { Component, OnInit, RendererFactory2, Renderer2 } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router, ActivatedRouteSnapshot, NavigationEnd } from '@angular/router';
import {LangChangeEvent, TranslateService} from '@ngx-translate/core';

import { AccountService } from 'app/core/auth/account.service';
import { PrimeNGConfig } from 'primeng/api';
import {StructureService} from "../../entities/structure/service/structure.service";
import {IStructure} from "../../entities/structure/structure.model";
import dayjs from "dayjs";
import {DataService} from "../../shared/data/DataService";

@Component({
  selector: 'jhi-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.scss']
})
export class MainComponent implements OnInit {
  structure?: IStructure;
  mainSidebarOpen: any;
  private renderer: Renderer2;

  constructor(
    private accountService: AccountService,
    private titleService: Title,
    private router: Router,
    private translateService: TranslateService,
    private primengConfig: PrimeNGConfig,
    private structureService: StructureService,
    private dataService: DataService,
    rootRenderer: RendererFactory2
  ) {
    this.renderer = rootRenderer.createRenderer(document.querySelector('html'), null);
    this.accountService.identity().subscribe();
  }

  ngOnInit(): void {
    // try to log in automatically

    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.updateTitle();
      }
    });

    this.translateService.onLangChange.subscribe((langChangeEvent: LangChangeEvent) => {
      this.updateTitle();
      dayjs.locale(langChangeEvent.lang);
      this.renderer.setAttribute(document.querySelector('html'), 'lang', langChangeEvent.lang);
    });

    this.structureService.findOnly().subscribe(res => {
      if (res.body){
        this.structure = res.body;
      }else {
        this.router.navigate(['getstarted']);
      }
    }, () => {
      this.router.navigate(['getstarted']);
    });
  }

  isAuthenticated(): boolean{
    return this.accountService.isAuthenticated();
  }
  login(): void {
    this.router.navigate(['/login']);
  }

  hoverEffect($event: any): void {
    this.mainSidebarOpen = $event.type === 'mouseover' ? 'main-sidebar-open' : '';
  }

  private getPageTitle(routeSnapshot: ActivatedRouteSnapshot): string {
    const title: string = routeSnapshot.data['pageTitle'] ?? '';
    if (routeSnapshot.firstChild) {
      return this.getPageTitle(routeSnapshot.firstChild) || title;
    }
    return title;
  }

  private updateTitle(): void {
    let pageTitle = this.getPageTitle(this.router.routerState.snapshot.root);
    if (!pageTitle) {
      pageTitle = 'global.title';
    }
    this.translateService.get(pageTitle).subscribe(title => this.titleService.setTitle(title));
  }
}
