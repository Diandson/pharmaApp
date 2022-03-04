import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzAutocompleteModule } from 'ng-zorro-antd/auto-complete';
import {
  NbAccordionModule,
  NbAutocompleteModule,
  NbButtonModule,
  NbCardModule,
  NbFormFieldModule,
  NbIconModule,
  NbInputModule, NbListModule,
  NbOptionModule, NbUserModule,
} from '@nebular/theme';
import { NzGridModule } from 'ng-zorro-antd/grid';
import { NzSelectModule } from 'ng-zorro-antd/select';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatButtonModule } from '@angular/material/button';
import { NzListModule } from 'ng-zorro-antd/list';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { InputNumberModule } from 'primeng/inputnumber';
import {DateAgoPipe} from "../config/date-ago.pipe";
import {NzTypographyModule} from "ng-zorro-antd/typography";

@NgModule({
    imports: [
        SharedModule,
        RouterModule.forChild([HOME_ROUTE]),
        NzInputModule,
        NzButtonModule,
        NzIconModule,
        NzAutocompleteModule,
        NbInputModule,
        NbCardModule,
        NzGridModule,
        NzSelectModule,
        NbOptionModule,
        NbAutocompleteModule,
        MatCardModule,
        MatChipsModule,
        NbFormFieldModule,
        NbButtonModule,
        NbIconModule,
        MatButtonModule,
        NzListModule,
        MatFormFieldModule,
        MatIconModule,
        InputNumberModule,
        NbAccordionModule,
        NbListModule,
        NbUserModule,
        NzTypographyModule,
    ],
  declarations: [HomeComponent, DateAgoPipe],
})
export class HomeModule {}
