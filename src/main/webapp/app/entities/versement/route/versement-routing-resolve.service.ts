import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVersement, Versement } from '../versement.model';
import { VersementService } from '../service/versement.service';

@Injectable({ providedIn: 'root' })
export class VersementRoutingResolveService implements Resolve<IVersement> {
  constructor(protected service: VersementService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVersement> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((versement: HttpResponse<Versement>) => {
          if (versement.body) {
            return of(versement.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Versement());
  }
}
