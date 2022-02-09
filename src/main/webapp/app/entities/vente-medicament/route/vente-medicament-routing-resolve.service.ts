import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVenteMedicament, VenteMedicament } from '../vente-medicament.model';
import { VenteMedicamentService } from '../service/vente-medicament.service';

@Injectable({ providedIn: 'root' })
export class VenteMedicamentRoutingResolveService implements Resolve<IVenteMedicament> {
  constructor(protected service: VenteMedicamentService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVenteMedicament> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((venteMedicament: HttpResponse<VenteMedicament>) => {
          if (venteMedicament.body) {
            return of(venteMedicament.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new VenteMedicament());
  }
}
