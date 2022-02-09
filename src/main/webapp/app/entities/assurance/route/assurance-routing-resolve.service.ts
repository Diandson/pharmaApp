import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAssurance, Assurance } from '../assurance.model';
import { AssuranceService } from '../service/assurance.service';

@Injectable({ providedIn: 'root' })
export class AssuranceRoutingResolveService implements Resolve<IAssurance> {
  constructor(protected service: AssuranceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAssurance> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((assurance: HttpResponse<Assurance>) => {
          if (assurance.body) {
            return of(assurance.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Assurance());
  }
}
