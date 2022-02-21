import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILieuVersement, LieuVersement } from '../lieu-versement.model';
import { LieuVersementService } from '../service/lieu-versement.service';

@Injectable({ providedIn: 'root' })
export class LieuVersementRoutingResolveService implements Resolve<ILieuVersement> {
  constructor(protected service: LieuVersementService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILieuVersement> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((lieuVersement: HttpResponse<LieuVersement>) => {
          if (lieuVersement.body) {
            return of(lieuVersement.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LieuVersement());
  }
}
