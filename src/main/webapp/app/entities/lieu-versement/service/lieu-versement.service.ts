import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILieuVersement, getLieuVersementIdentifier } from '../lieu-versement.model';

export type EntityResponseType = HttpResponse<ILieuVersement>;
export type EntityArrayResponseType = HttpResponse<ILieuVersement[]>;

@Injectable({ providedIn: 'root' })
export class LieuVersementService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/lieu-versements');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(lieuVersement: ILieuVersement): Observable<EntityResponseType> {
    return this.http.post<ILieuVersement>(this.resourceUrl, lieuVersement, { observe: 'response' });
  }

  update(lieuVersement: ILieuVersement): Observable<EntityResponseType> {
    return this.http.put<ILieuVersement>(`${this.resourceUrl}/${getLieuVersementIdentifier(lieuVersement) as number}`, lieuVersement, {
      observe: 'response',
    });
  }

  partialUpdate(lieuVersement: ILieuVersement): Observable<EntityResponseType> {
    return this.http.patch<ILieuVersement>(`${this.resourceUrl}/${getLieuVersementIdentifier(lieuVersement) as number}`, lieuVersement, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILieuVersement>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILieuVersement[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLieuVersementToCollectionIfMissing(
    lieuVersementCollection: ILieuVersement[],
    ...lieuVersementsToCheck: (ILieuVersement | null | undefined)[]
  ): ILieuVersement[] {
    const lieuVersements: ILieuVersement[] = lieuVersementsToCheck.filter(isPresent);
    if (lieuVersements.length > 0) {
      const lieuVersementCollectionIdentifiers = lieuVersementCollection.map(
        lieuVersementItem => getLieuVersementIdentifier(lieuVersementItem)!
      );
      const lieuVersementsToAdd = lieuVersements.filter(lieuVersementItem => {
        const lieuVersementIdentifier = getLieuVersementIdentifier(lieuVersementItem);
        if (lieuVersementIdentifier == null || lieuVersementCollectionIdentifiers.includes(lieuVersementIdentifier)) {
          return false;
        }
        lieuVersementCollectionIdentifiers.push(lieuVersementIdentifier);
        return true;
      });
      return [...lieuVersementsToAdd, ...lieuVersementCollection];
    }
    return lieuVersementCollection;
  }
}
