import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITypePack, getTypePackIdentifier } from '../type-pack.model';

export type EntityResponseType = HttpResponse<ITypePack>;
export type EntityArrayResponseType = HttpResponse<ITypePack[]>;

@Injectable({ providedIn: 'root' })
export class TypePackService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/type-packs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(typePack: ITypePack): Observable<EntityResponseType> {
    return this.http.post<ITypePack>(this.resourceUrl, typePack, { observe: 'response' });
  }

  update(typePack: ITypePack): Observable<EntityResponseType> {
    return this.http.put<ITypePack>(`${this.resourceUrl}/${getTypePackIdentifier(typePack) as number}`, typePack, { observe: 'response' });
  }

  partialUpdate(typePack: ITypePack): Observable<EntityResponseType> {
    return this.http.patch<ITypePack>(`${this.resourceUrl}/${getTypePackIdentifier(typePack) as number}`, typePack, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypePack>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypePack[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTypePackToCollectionIfMissing(typePackCollection: ITypePack[], ...typePacksToCheck: (ITypePack | null | undefined)[]): ITypePack[] {
    const typePacks: ITypePack[] = typePacksToCheck.filter(isPresent);
    if (typePacks.length > 0) {
      const typePackCollectionIdentifiers = typePackCollection.map(typePackItem => getTypePackIdentifier(typePackItem)!);
      const typePacksToAdd = typePacks.filter(typePackItem => {
        const typePackIdentifier = getTypePackIdentifier(typePackItem);
        if (typePackIdentifier == null || typePackCollectionIdentifiers.includes(typePackIdentifier)) {
          return false;
        }
        typePackCollectionIdentifiers.push(typePackIdentifier);
        return true;
      });
      return [...typePacksToAdd, ...typePackCollection];
    }
    return typePackCollection;
  }
}
