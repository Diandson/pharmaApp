import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVersement, getVersementIdentifier } from '../versement.model';

export type EntityResponseType = HttpResponse<IVersement>;
export type EntityArrayResponseType = HttpResponse<IVersement[]>;

@Injectable({ providedIn: 'root' })
export class VersementService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/versements');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(versement: IVersement): Observable<EntityResponseType> {
    return this.http.post<IVersement>(this.resourceUrl, versement, { observe: 'response' });
  }

  update(versement: IVersement): Observable<EntityResponseType> {
    return this.http.put<IVersement>(`${this.resourceUrl}/${getVersementIdentifier(versement) as number}`, versement, {
      observe: 'response',
    });
  }

  partialUpdate(versement: IVersement): Observable<EntityResponseType> {
    return this.http.patch<IVersement>(`${this.resourceUrl}/${getVersementIdentifier(versement) as number}`, versement, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVersement>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVersement[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addVersementToCollectionIfMissing(
    versementCollection: IVersement[],
    ...versementsToCheck: (IVersement | null | undefined)[]
  ): IVersement[] {
    const versements: IVersement[] = versementsToCheck.filter(isPresent);
    if (versements.length > 0) {
      const versementCollectionIdentifiers = versementCollection.map(versementItem => getVersementIdentifier(versementItem)!);
      const versementsToAdd = versements.filter(versementItem => {
        const versementIdentifier = getVersementIdentifier(versementItem);
        if (versementIdentifier == null || versementCollectionIdentifiers.includes(versementIdentifier)) {
          return false;
        }
        versementCollectionIdentifiers.push(versementIdentifier);
        return true;
      });
      return [...versementsToAdd, ...versementCollection];
    }
    return versementCollection;
  }
}
