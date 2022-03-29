import { NgModule } from '@angular/core';
import { NbMenuModule } from '@nebular/theme';
import { PagesComponent } from './pages.component';
import { RouterModule } from '@angular/router';
import { PagesRoutingModule } from './pages-routing.module';
import {SharedModule} from "../../shared/shared.module";

@NgModule({
  imports: [NbMenuModule.forRoot(), RouterModule, PagesRoutingModule, SharedModule],
  declarations: [PagesComponent],
  exports: [PagesComponent],
})
export class PagesModule {}
