import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInventaire, Inventaire } from '../inventaire.model';
import { InventaireService } from '../service/inventaire.service';

@Injectable({ providedIn: 'root' })
export class InventaireRoutingResolveService implements Resolve<IInventaire> {
  constructor(protected service: InventaireService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInventaire> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((inventaire: HttpResponse<Inventaire>) => {
          if (inventaire.body) {
            return of(inventaire.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Inventaire());
  }
}
