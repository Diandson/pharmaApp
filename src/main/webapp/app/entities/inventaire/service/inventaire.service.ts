import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInventaire, getInventaireIdentifier } from '../inventaire.model';

export type EntityResponseType = HttpResponse<IInventaire>;
export type EntityArrayResponseType = HttpResponse<IInventaire[]>;

@Injectable({ providedIn: 'root' })
export class InventaireService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/inventaires');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(inventaire: IInventaire): Observable<EntityResponseType> {
    return this.http.post<IInventaire>(this.resourceUrl, inventaire, { observe: 'response' });
  }

  update(inventaire: IInventaire): Observable<EntityResponseType> {
    return this.http.put<IInventaire>(`${this.resourceUrl}/${getInventaireIdentifier(inventaire) as number}`, inventaire, {
      observe: 'response',
    });
  }

  partialUpdate(inventaire: IInventaire): Observable<EntityResponseType> {
    return this.http.patch<IInventaire>(`${this.resourceUrl}/${getInventaireIdentifier(inventaire) as number}`, inventaire, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInventaire>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInventaire[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addInventaireToCollectionIfMissing(
    inventaireCollection: IInventaire[],
    ...inventairesToCheck: (IInventaire | null | undefined)[]
  ): IInventaire[] {
    const inventaires: IInventaire[] = inventairesToCheck.filter(isPresent);
    if (inventaires.length > 0) {
      const inventaireCollectionIdentifiers = inventaireCollection.map(inventaireItem => getInventaireIdentifier(inventaireItem)!);
      const inventairesToAdd = inventaires.filter(inventaireItem => {
        const inventaireIdentifier = getInventaireIdentifier(inventaireItem);
        if (inventaireIdentifier == null || inventaireCollectionIdentifiers.includes(inventaireIdentifier)) {
          return false;
        }
        inventaireCollectionIdentifiers.push(inventaireIdentifier);
        return true;
      });
      return [...inventairesToAdd, ...inventaireCollection];
    }
    return inventaireCollection;
  }
}
