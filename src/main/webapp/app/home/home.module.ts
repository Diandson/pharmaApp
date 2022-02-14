import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import {NzInputModule} from "ng-zorro-antd/input";
import {NzButtonModule} from "ng-zorro-antd/button";
import {NzIconModule} from "ng-zorro-antd/icon";
import {NzAutocompleteModule} from "ng-zorro-antd/auto-complete";
import {NbCardModule, NbInputModule} from "@nebular/theme";
import {NzGridModule} from "ng-zorro-antd/grid";

@NgModule({
  imports: [SharedModule, RouterModule.forChild([HOME_ROUTE]), NzInputModule, NzButtonModule, NzIconModule, NzAutocompleteModule, NbInputModule, NbCardModule, NzGridModule],
  declarations: [HomeComponent],
})
export class HomeModule {}
