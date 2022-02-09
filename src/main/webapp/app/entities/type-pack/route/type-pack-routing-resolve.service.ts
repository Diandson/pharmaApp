import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITypePack, TypePack } from '../type-pack.model';
import { TypePackService } from '../service/type-pack.service';

@Injectable({ providedIn: 'root' })
export class TypePackRoutingResolveService implements Resolve<ITypePack> {
  constructor(protected service: TypePackService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITypePack> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((typePack: HttpResponse<TypePack>) => {
          if (typePack.body) {
            return of(typePack.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TypePack());
  }
}
