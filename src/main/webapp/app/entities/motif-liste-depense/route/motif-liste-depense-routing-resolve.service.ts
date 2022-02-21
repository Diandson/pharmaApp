import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMotifListeDepense, MotifListeDepense } from '../motif-liste-depense.model';
import { MotifListeDepenseService } from '../service/motif-liste-depense.service';

@Injectable({ providedIn: 'root' })
export class MotifListeDepenseRoutingResolveService implements Resolve<IMotifListeDepense> {
  constructor(protected service: MotifListeDepenseService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMotifListeDepense> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((motifListeDepense: HttpResponse<MotifListeDepense>) => {
          if (motifListeDepense.body) {
            return of(motifListeDepense.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MotifListeDepense());
  }
}
