import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IApprovisionnementMedicament, getApprovisionnementMedicamentIdentifier } from '../approvisionnement-medicament.model';

export type EntityResponseType = HttpResponse<IApprovisionnementMedicament>;
export type EntityArrayResponseType = HttpResponse<IApprovisionnementMedicament[]>;

@Injectable({ providedIn: 'root' })
export class ApprovisionnementMedicamentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/approvisionnement-medicaments');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(approvisionnementMedicament: IApprovisionnementMedicament): Observable<EntityResponseType> {
    return this.http.post<IApprovisionnementMedicament>(this.resourceUrl, approvisionnementMedicament, { observe: 'response' });
  }

  update(approvisionnementMedicament: IApprovisionnementMedicament): Observable<EntityResponseType> {
    return this.http.put<IApprovisionnementMedicament>(
      `${this.resourceUrl}/${getApprovisionnementMedicamentIdentifier(approvisionnementMedicament) as number}`,
      approvisionnementMedicament,
      { observe: 'response' }
    );
  }

  partialUpdate(approvisionnementMedicament: IApprovisionnementMedicament): Observable<EntityResponseType> {
    return this.http.patch<IApprovisionnementMedicament>(
      `${this.resourceUrl}/${getApprovisionnementMedicamentIdentifier(approvisionnementMedicament) as number}`,
      approvisionnementMedicament,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IApprovisionnementMedicament>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IApprovisionnementMedicament[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addApprovisionnementMedicamentToCollectionIfMissing(
    approvisionnementMedicamentCollection: IApprovisionnementMedicament[],
    ...approvisionnementMedicamentsToCheck: (IApprovisionnementMedicament | null | undefined)[]
  ): IApprovisionnementMedicament[] {
    const approvisionnementMedicaments: IApprovisionnementMedicament[] = approvisionnementMedicamentsToCheck.filter(isPresent);
    if (approvisionnementMedicaments.length > 0) {
      const approvisionnementMedicamentCollectionIdentifiers = approvisionnementMedicamentCollection.map(
        approvisionnementMedicamentItem => getApprovisionnementMedicamentIdentifier(approvisionnementMedicamentItem)!
      );
      const approvisionnementMedicamentsToAdd = approvisionnementMedicaments.filter(approvisionnementMedicamentItem => {
        const approvisionnementMedicamentIdentifier = getApprovisionnementMedicamentIdentifier(approvisionnementMedicamentItem);
        if (
          approvisionnementMedicamentIdentifier == null ||
          approvisionnementMedicamentCollectionIdentifiers.includes(approvisionnementMedicamentIdentifier)
        ) {
          return false;
        }
        approvisionnementMedicamentCollectionIdentifiers.push(approvisionnementMedicamentIdentifier);
        return true;
      });
      return [...approvisionnementMedicamentsToAdd, ...approvisionnementMedicamentCollection];
    }
    return approvisionnementMedicamentCollection;
  }
}
