import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IApprovisionnementMedicament, ApprovisionnementMedicament } from '../approvisionnement-medicament.model';
import { ApprovisionnementMedicamentService } from '../service/approvisionnement-medicament.service';

@Injectable({ providedIn: 'root' })
export class ApprovisionnementMedicamentRoutingResolveService implements Resolve<IApprovisionnementMedicament> {
  constructor(protected service: ApprovisionnementMedicamentService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IApprovisionnementMedicament> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((approvisionnementMedicament: HttpResponse<ApprovisionnementMedicament>) => {
          if (approvisionnementMedicament.body) {
            return of(approvisionnementMedicament.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ApprovisionnementMedicament());
  }
}
