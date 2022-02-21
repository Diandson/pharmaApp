import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMotifListeDepense, getMotifListeDepenseIdentifier } from '../motif-liste-depense.model';

export type EntityResponseType = HttpResponse<IMotifListeDepense>;
export type EntityArrayResponseType = HttpResponse<IMotifListeDepense[]>;

@Injectable({ providedIn: 'root' })
export class MotifListeDepenseService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/motif-liste-depenses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(motifListeDepense: IMotifListeDepense): Observable<EntityResponseType> {
    return this.http.post<IMotifListeDepense>(this.resourceUrl, motifListeDepense, { observe: 'response' });
  }

  update(motifListeDepense: IMotifListeDepense): Observable<EntityResponseType> {
    return this.http.put<IMotifListeDepense>(
      `${this.resourceUrl}/${getMotifListeDepenseIdentifier(motifListeDepense) as number}`,
      motifListeDepense,
      { observe: 'response' }
    );
  }

  partialUpdate(motifListeDepense: IMotifListeDepense): Observable<EntityResponseType> {
    return this.http.patch<IMotifListeDepense>(
      `${this.resourceUrl}/${getMotifListeDepenseIdentifier(motifListeDepense) as number}`,
      motifListeDepense,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMotifListeDepense>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMotifListeDepense[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMotifListeDepenseToCollectionIfMissing(
    motifListeDepenseCollection: IMotifListeDepense[],
    ...motifListeDepensesToCheck: (IMotifListeDepense | null | undefined)[]
  ): IMotifListeDepense[] {
    const motifListeDepenses: IMotifListeDepense[] = motifListeDepensesToCheck.filter(isPresent);
    if (motifListeDepenses.length > 0) {
      const motifListeDepenseCollectionIdentifiers = motifListeDepenseCollection.map(
        motifListeDepenseItem => getMotifListeDepenseIdentifier(motifListeDepenseItem)!
      );
      const motifListeDepensesToAdd = motifListeDepenses.filter(motifListeDepenseItem => {
        const motifListeDepenseIdentifier = getMotifListeDepenseIdentifier(motifListeDepenseItem);
        if (motifListeDepenseIdentifier == null || motifListeDepenseCollectionIdentifiers.includes(motifListeDepenseIdentifier)) {
          return false;
        }
        motifListeDepenseCollectionIdentifiers.push(motifListeDepenseIdentifier);
        return true;
      });
      return [...motifListeDepensesToAdd, ...motifListeDepenseCollection];
    }
    return motifListeDepenseCollection;
  }
}
