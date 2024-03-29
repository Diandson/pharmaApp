import { NgModule, LOCALE_ID } from '@angular/core';
import { registerLocaleData } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import locale from '@angular/common/locales/fr';
import { BrowserModule, Title } from '@angular/platform-browser';
import { ServiceWorkerModule } from '@angular/service-worker';
import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { NgxWebstorageModule } from 'ngx-webstorage';
import dayjs from 'dayjs/esm';
import { NgbDateAdapter, NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import './config/dayjs';
import { SharedModule } from 'app/shared/shared.module';
import { TranslationModule } from 'app/shared/language/translation.module';
import { AppRoutingModule } from './app-routing.module';
import { HomeModule } from './home/home.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { NgbDateDayjsAdapter } from './config/datepicker-adapter';
import { fontAwesomeIcons } from './config/font-awesome-icons';
import { httpInterceptorProviders } from 'app/core/interceptor/index';
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';
import {
  NbActionsModule,
  NbButtonModule,
  NbContextMenuModule,
  NbIconModule,
  NbLayoutModule,
  NbMenuModule,
  NbSelectModule, NbSidebarModule,
  NbThemeModule,
  NbUserModule,
} from '@nebular/theme';
import { NzLayoutModule } from 'ng-zorro-antd/layout';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatToolbarModule } from '@angular/material/toolbar';
import { ExtendedModule, FlexModule } from '@angular/flex-layout';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatDividerModule } from '@angular/material/divider';
import { NbEvaIconsModule } from '@nebular/eva-icons';
import { GetstartedComponent } from './entities/getstarted/getstarted.component';
import { NzModalService } from 'ng-zorro-antd/modal';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { ProgressDialogComponent } from './shared/progress-dialog/progress-dialog.component';
import { MatDialogModule } from '@angular/material/dialog';
import { LoginModule } from './login/login.module';
import {MatBadgeModule} from "@angular/material/badge";
import {InputMaskModule} from "@ngneat/input-mask";
import {PagesModule} from "./layouts/sidebar/pages.module";

@NgModule({
  imports: [
    BrowserModule,
    SharedModule,
    HomeModule,
    NbThemeModule.forRoot(),
    NbMenuModule.forRoot(),
    NbSidebarModule.forRoot(),
    PagesModule,
    BrowserAnimationsModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    AppRoutingModule,
    // Set this to true to enable service worker (PWA)
    ServiceWorkerModule.register('ngsw-worker.js', {enabled: false}),
    HttpClientModule,
    NgxWebstorageModule.forRoot({prefix: 'jhi', separator: '-', caseSensitive: true}),
    TranslationModule,
    NbLayoutModule,
    NzLayoutModule,
    MatToolbarModule,
    ExtendedModule,
    MatButtonModule,
    NbButtonModule,
    FlexModule,
    MatIconModule,
    MatMenuModule,
    MatDividerModule,
    NbActionsModule,
    NbUserModule,
    NbSelectModule,
    NbEvaIconsModule,
    NbContextMenuModule,
    NbIconModule,
    NzSpinModule,
    MatDialogModule,
    LoginModule,
    MatBadgeModule,
    InputMaskModule.forRoot({inputSelector: 'input', isAsync: true}),
    PagesModule
  ],
    providers: [
        Title,
        NzModalService,
        {provide: LOCALE_ID, useValue: 'fr'},
        {provide: NgbDateAdapter, useClass: NgbDateDayjsAdapter},
        httpInterceptorProviders,
    ],
    declarations: [
        MainComponent,
        NavbarComponent,
        ErrorComponent,
        GetstartedComponent,
        ProgressDialogComponent,
        PageRibbonComponent,
        ActiveMenuDirective,
        FooterComponent,
    ],
    bootstrap: [MainComponent]
})
export class AppModule {
  constructor(applicationConfigService: ApplicationConfigService, iconLibrary: FaIconLibrary, dpConfig: NgbDatepickerConfig) {
    applicationConfigService.setEndpointPrefix(SERVER_API_URL);
    registerLocaleData(locale);
    iconLibrary.addIcons(...fontAwesomeIcons);
    dpConfig.minDate = { year: dayjs().subtract(100, 'year').year(), month: 1, day: 1 };
  }
}
