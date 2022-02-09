import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPack, getPackIdentifier } from '../pack.model';

export type EntityResponseType = HttpResponse<IPack>;
export type EntityArrayResponseType = HttpResponse<IPack[]>;

@Injectable({ providedIn: 'root' })
export class PackService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/packs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pack: IPack): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pack);
    return this.http
      .post<IPack>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(pack: IPack): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pack);
    return this.http
      .put<IPack>(`${this.resourceUrl}/${getPackIdentifier(pack) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(pack: IPack): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pack);
    return this.http
      .patch<IPack>(`${this.resourceUrl}/${getPackIdentifier(pack) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPack>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPack[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPackToCollectionIfMissing(packCollection: IPack[], ...packsToCheck: (IPack | null | undefined)[]): IPack[] {
    const packs: IPack[] = packsToCheck.filter(isPresent);
    if (packs.length > 0) {
      const packCollectionIdentifiers = packCollection.map(packItem => getPackIdentifier(packItem)!);
      const packsToAdd = packs.filter(packItem => {
        const packIdentifier = getPackIdentifier(packItem);
        if (packIdentifier == null || packCollectionIdentifiers.includes(packIdentifier)) {
          return false;
        }
        packCollectionIdentifiers.push(packIdentifier);
        return true;
      });
      return [...packsToAdd, ...packCollection];
    }
    return packCollection;
  }

  protected convertDateFromClient(pack: IPack): IPack {
    return Object.assign({}, pack, {
      dateRenew: pack.dateRenew?.isValid() ? pack.dateRenew.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateRenew = res.body.dateRenew ? dayjs(res.body.dateRenew) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((pack: IPack) => {
        pack.dateRenew = pack.dateRenew ? dayjs(pack.dateRenew) : undefined;
      });
    }
    return res;
  }
}
