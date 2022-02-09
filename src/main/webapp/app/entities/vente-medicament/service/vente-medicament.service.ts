import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVenteMedicament, getVenteMedicamentIdentifier } from '../vente-medicament.model';

export type EntityResponseType = HttpResponse<IVenteMedicament>;
export type EntityArrayResponseType = HttpResponse<IVenteMedicament[]>;

@Injectable({ providedIn: 'root' })
export class VenteMedicamentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vente-medicaments');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(venteMedicament: IVenteMedicament): Observable<EntityResponseType> {
    return this.http.post<IVenteMedicament>(this.resourceUrl, venteMedicament, { observe: 'response' });
  }

  update(venteMedicament: IVenteMedicament): Observable<EntityResponseType> {
    return this.http.put<IVenteMedicament>(
      `${this.resourceUrl}/${getVenteMedicamentIdentifier(venteMedicament) as number}`,
      venteMedicament,
      { observe: 'response' }
    );
  }

  partialUpdate(venteMedicament: IVenteMedicament): Observable<EntityResponseType> {
    return this.http.patch<IVenteMedicament>(
      `${this.resourceUrl}/${getVenteMedicamentIdentifier(venteMedicament) as number}`,
      venteMedicament,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVenteMedicament>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVenteMedicament[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addVenteMedicamentToCollectionIfMissing(
    venteMedicamentCollection: IVenteMedicament[],
    ...venteMedicamentsToCheck: (IVenteMedicament | null | undefined)[]
  ): IVenteMedicament[] {
    const venteMedicaments: IVenteMedicament[] = venteMedicamentsToCheck.filter(isPresent);
    if (venteMedicaments.length > 0) {
      const venteMedicamentCollectionIdentifiers = venteMedicamentCollection.map(
        venteMedicamentItem => getVenteMedicamentIdentifier(venteMedicamentItem)!
      );
      const venteMedicamentsToAdd = venteMedicaments.filter(venteMedicamentItem => {
        const venteMedicamentIdentifier = getVenteMedicamentIdentifier(venteMedicamentItem);
        if (venteMedicamentIdentifier == null || venteMedicamentCollectionIdentifiers.includes(venteMedicamentIdentifier)) {
          return false;
        }
        venteMedicamentCollectionIdentifiers.push(venteMedicamentIdentifier);
        return true;
      });
      return [...venteMedicamentsToAdd, ...venteMedicamentCollection];
    }
    return venteMedicamentCollection;
  }
}
