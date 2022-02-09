import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAssurance, getAssuranceIdentifier } from '../assurance.model';

export type EntityResponseType = HttpResponse<IAssurance>;
export type EntityArrayResponseType = HttpResponse<IAssurance[]>;

@Injectable({ providedIn: 'root' })
export class AssuranceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/assurances');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(assurance: IAssurance): Observable<EntityResponseType> {
    return this.http.post<IAssurance>(this.resourceUrl, assurance, { observe: 'response' });
  }

  update(assurance: IAssurance): Observable<EntityResponseType> {
    return this.http.put<IAssurance>(`${this.resourceUrl}/${getAssuranceIdentifier(assurance) as number}`, assurance, {
      observe: 'response',
    });
  }

  partialUpdate(assurance: IAssurance): Observable<EntityResponseType> {
    return this.http.patch<IAssurance>(`${this.resourceUrl}/${getAssuranceIdentifier(assurance) as number}`, assurance, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAssurance>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAssurance[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAssuranceToCollectionIfMissing(
    assuranceCollection: IAssurance[],
    ...assurancesToCheck: (IAssurance | null | undefined)[]
  ): IAssurance[] {
    const assurances: IAssurance[] = assurancesToCheck.filter(isPresent);
    if (assurances.length > 0) {
      const assuranceCollectionIdentifiers = assuranceCollection.map(assuranceItem => getAssuranceIdentifier(assuranceItem)!);
      const assurancesToAdd = assurances.filter(assuranceItem => {
        const assuranceIdentifier = getAssuranceIdentifier(assuranceItem);
        if (assuranceIdentifier == null || assuranceCollectionIdentifiers.includes(assuranceIdentifier)) {
          return false;
        }
        assuranceCollectionIdentifiers.push(assuranceIdentifier);
        return true;
      });
      return [...assurancesToAdd, ...assuranceCollection];
    }
    return assuranceCollection;
  }
}
