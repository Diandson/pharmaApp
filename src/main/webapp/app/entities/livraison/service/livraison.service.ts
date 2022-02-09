import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILivraison, getLivraisonIdentifier } from '../livraison.model';

export type EntityResponseType = HttpResponse<ILivraison>;
export type EntityArrayResponseType = HttpResponse<ILivraison[]>;

@Injectable({ providedIn: 'root' })
export class LivraisonService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/livraisons');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(livraison: ILivraison): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(livraison);
    return this.http
      .post<ILivraison>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(livraison: ILivraison): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(livraison);
    return this.http
      .put<ILivraison>(`${this.resourceUrl}/${getLivraisonIdentifier(livraison) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(livraison: ILivraison): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(livraison);
    return this.http
      .patch<ILivraison>(`${this.resourceUrl}/${getLivraisonIdentifier(livraison) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILivraison>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILivraison[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLivraisonToCollectionIfMissing(
    livraisonCollection: ILivraison[],
    ...livraisonsToCheck: (ILivraison | null | undefined)[]
  ): ILivraison[] {
    const livraisons: ILivraison[] = livraisonsToCheck.filter(isPresent);
    if (livraisons.length > 0) {
      const livraisonCollectionIdentifiers = livraisonCollection.map(livraisonItem => getLivraisonIdentifier(livraisonItem)!);
      const livraisonsToAdd = livraisons.filter(livraisonItem => {
        const livraisonIdentifier = getLivraisonIdentifier(livraisonItem);
        if (livraisonIdentifier == null || livraisonCollectionIdentifiers.includes(livraisonIdentifier)) {
          return false;
        }
        livraisonCollectionIdentifiers.push(livraisonIdentifier);
        return true;
      });
      return [...livraisonsToAdd, ...livraisonCollection];
    }
    return livraisonCollection;
  }

  protected convertDateFromClient(livraison: ILivraison): ILivraison {
    return Object.assign({}, livraison, {
      dateLivraison: livraison.dateLivraison?.isValid() ? livraison.dateLivraison.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateLivraison = res.body.dateLivraison ? dayjs(res.body.dateLivraison) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((livraison: ILivraison) => {
        livraison.dateLivraison = livraison.dateLivraison ? dayjs(livraison.dateLivraison) : undefined;
      });
    }
    return res;
  }
}
