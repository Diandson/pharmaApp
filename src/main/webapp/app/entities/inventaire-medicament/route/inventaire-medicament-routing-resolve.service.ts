import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInventaireMedicament, InventaireMedicament } from '../inventaire-medicament.model';
import { InventaireMedicamentService } from '../service/inventaire-medicament.service';

@Injectable({ providedIn: 'root' })
export class InventaireMedicamentRoutingResolveService implements Resolve<IInventaireMedicament> {
  constructor(protected service: InventaireMedicamentService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInventaireMedicament> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((inventaireMedicament: HttpResponse<InventaireMedicament>) => {
          if (inventaireMedicament.body) {
            return of(inventaireMedicament.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new InventaireMedicament());
  }
}
