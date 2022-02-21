import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPaiement, getPaiementIdentifier } from '../paiement.model';

export type EntityResponseType = HttpResponse<IPaiement>;
export type EntityArrayResponseType = HttpResponse<IPaiement[]>;

@Injectable({ providedIn: 'root' })
export class PaiementService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/paiements');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(paiement: IPaiement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paiement);
    return this.http
      .post<IPaiement>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(paiement: IPaiement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paiement);
    return this.http
      .put<IPaiement>(`${this.resourceUrl}/${getPaiementIdentifier(paiement) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(paiement: IPaiement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paiement);
    return this.http
      .patch<IPaiement>(`${this.resourceUrl}/${getPaiementIdentifier(paiement) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPaiement>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPaiement[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPaiementToCollectionIfMissing(paiementCollection: IPaiement[], ...paiementsToCheck: (IPaiement | null | undefined)[]): IPaiement[] {
    const paiements: IPaiement[] = paiementsToCheck.filter(isPresent);
    if (paiements.length > 0) {
      const paiementCollectionIdentifiers = paiementCollection.map(paiementItem => getPaiementIdentifier(paiementItem)!);
      const paiementsToAdd = paiements.filter(paiementItem => {
        const paiementIdentifier = getPaiementIdentifier(paiementItem);
        if (paiementIdentifier == null || paiementCollectionIdentifiers.includes(paiementIdentifier)) {
          return false;
        }
        paiementCollectionIdentifiers.push(paiementIdentifier);
        return true;
      });
      return [...paiementsToAdd, ...paiementCollection];
    }
    return paiementCollection;
  }

  protected convertDateFromClient(paiement: IPaiement): IPaiement {
    return Object.assign({}, paiement, {
      datePaiement: paiement.datePaiement?.isValid() ? paiement.datePaiement.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.datePaiement = res.body.datePaiement ? dayjs(res.body.datePaiement) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((paiement: IPaiement) => {
        paiement.datePaiement = paiement.datePaiement ? dayjs(paiement.datePaiement) : undefined;
      });
    }
    return res;
  }
}
