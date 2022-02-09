import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IApprovisionnement, Approvisionnement } from '../approvisionnement.model';
import { ApprovisionnementService } from '../service/approvisionnement.service';

@Injectable({ providedIn: 'root' })
export class ApprovisionnementRoutingResolveService implements Resolve<IApprovisionnement> {
  constructor(protected service: ApprovisionnementService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IApprovisionnement> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((approvisionnement: HttpResponse<Approvisionnement>) => {
          if (approvisionnement.body) {
            return of(approvisionnement.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Approvisionnement());
  }
}
