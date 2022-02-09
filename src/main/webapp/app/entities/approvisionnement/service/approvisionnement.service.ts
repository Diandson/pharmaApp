import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IApprovisionnement, getApprovisionnementIdentifier } from '../approvisionnement.model';

export type EntityResponseType = HttpResponse<IApprovisionnement>;
export type EntityArrayResponseType = HttpResponse<IApprovisionnement[]>;

@Injectable({ providedIn: 'root' })
export class ApprovisionnementService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/approvisionnements');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(approvisionnement: IApprovisionnement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(approvisionnement);
    return this.http
      .post<IApprovisionnement>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(approvisionnement: IApprovisionnement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(approvisionnement);
    return this.http
      .put<IApprovisionnement>(`${this.resourceUrl}/${getApprovisionnementIdentifier(approvisionnement) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(approvisionnement: IApprovisionnement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(approvisionnement);
    return this.http
      .patch<IApprovisionnement>(`${this.resourceUrl}/${getApprovisionnementIdentifier(approvisionnement) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IApprovisionnement>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IApprovisionnement[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addApprovisionnementToCollectionIfMissing(
    approvisionnementCollection: IApprovisionnement[],
    ...approvisionnementsToCheck: (IApprovisionnement | null | undefined)[]
  ): IApprovisionnement[] {
    const approvisionnements: IApprovisionnement[] = approvisionnementsToCheck.filter(isPresent);
    if (approvisionnements.length > 0) {
      const approvisionnementCollectionIdentifiers = approvisionnementCollection.map(
        approvisionnementItem => getApprovisionnementIdentifier(approvisionnementItem)!
      );
      const approvisionnementsToAdd = approvisionnements.filter(approvisionnementItem => {
        const approvisionnementIdentifier = getApprovisionnementIdentifier(approvisionnementItem);
        if (approvisionnementIdentifier == null || approvisionnementCollectionIdentifiers.includes(approvisionnementIdentifier)) {
          return false;
        }
        approvisionnementCollectionIdentifiers.push(approvisionnementIdentifier);
        return true;
      });
      return [...approvisionnementsToAdd, ...approvisionnementCollection];
    }
    return approvisionnementCollection;
  }

  protected convertDateFromClient(approvisionnement: IApprovisionnement): IApprovisionnement {
    return Object.assign({}, approvisionnement, {
      dateCommande: approvisionnement.dateCommande?.isValid() ? approvisionnement.dateCommande.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateCommande = res.body.dateCommande ? dayjs(res.body.dateCommande) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((approvisionnement: IApprovisionnement) => {
        approvisionnement.dateCommande = approvisionnement.dateCommande ? dayjs(approvisionnement.dateCommande) : undefined;
      });
    }
    return res;
  }
}
