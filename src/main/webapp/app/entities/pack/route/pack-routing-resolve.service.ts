import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPack, Pack } from '../pack.model';
import { PackService } from '../service/pack.service';

@Injectable({ providedIn: 'root' })
export class PackRoutingResolveService implements Resolve<IPack> {
  constructor(protected service: PackService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPack> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pack: HttpResponse<Pack>) => {
          if (pack.body) {
            return of(pack.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Pack());
  }
}
