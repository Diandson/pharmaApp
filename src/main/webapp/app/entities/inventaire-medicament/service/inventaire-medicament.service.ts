import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInventaireMedicament, getInventaireMedicamentIdentifier } from '../inventaire-medicament.model';

export type EntityResponseType = HttpResponse<IInventaireMedicament>;
export type EntityArrayResponseType = HttpResponse<IInventaireMedicament[]>;

@Injectable({ providedIn: 'root' })
export class InventaireMedicamentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/inventaire-medicaments');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(inventaireMedicament: IInventaireMedicament): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(inventaireMedicament);
    return this.http
      .post<IInventaireMedicament>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(inventaireMedicament: IInventaireMedicament): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(inventaireMedicament);
    return this.http
      .put<IInventaireMedicament>(`${this.resourceUrl}/${getInventaireMedicamentIdentifier(inventaireMedicament) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(inventaireMedicament: IInventaireMedicament): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(inventaireMedicament);
    return this.http
      .patch<IInventaireMedicament>(`${this.resourceUrl}/${getInventaireMedicamentIdentifier(inventaireMedicament) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IInventaireMedicament>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IInventaireMedicament[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addInventaireMedicamentToCollectionIfMissing(
    inventaireMedicamentCollection: IInventaireMedicament[],
    ...inventaireMedicamentsToCheck: (IInventaireMedicament | null | undefined)[]
  ): IInventaireMedicament[] {
    const inventaireMedicaments: IInventaireMedicament[] = inventaireMedicamentsToCheck.filter(isPresent);
    if (inventaireMedicaments.length > 0) {
      const inventaireMedicamentCollectionIdentifiers = inventaireMedicamentCollection.map(
        inventaireMedicamentItem => getInventaireMedicamentIdentifier(inventaireMedicamentItem)!
      );
      const inventaireMedicamentsToAdd = inventaireMedicaments.filter(inventaireMedicamentItem => {
        const inventaireMedicamentIdentifier = getInventaireMedicamentIdentifier(inventaireMedicamentItem);
        if (inventaireMedicamentIdentifier == null || inventaireMedicamentCollectionIdentifiers.includes(inventaireMedicamentIdentifier)) {
          return false;
        }
        inventaireMedicamentCollectionIdentifiers.push(inventaireMedicamentIdentifier);
        return true;
      });
      return [...inventaireMedicamentsToAdd, ...inventaireMedicamentCollection];
    }
    return inventaireMedicamentCollection;
  }

  protected convertDateFromClient(inventaireMedicament: IInventaireMedicament): IInventaireMedicament {
    return Object.assign({}, inventaireMedicament, {
      dateFabrication: inventaireMedicament.dateFabrication?.isValid()
        ? inventaireMedicament.dateFabrication.format(DATE_FORMAT)
        : undefined,
      dateExpiration: inventaireMedicament.dateExpiration?.isValid() ? inventaireMedicament.dateExpiration.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateFabrication = res.body.dateFabrication ? dayjs(res.body.dateFabrication) : undefined;
      res.body.dateExpiration = res.body.dateExpiration ? dayjs(res.body.dateExpiration) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((inventaireMedicament: IInventaireMedicament) => {
        inventaireMedicament.dateFabrication = inventaireMedicament.dateFabrication
          ? dayjs(inventaireMedicament.dateFabrication)
          : undefined;
        inventaireMedicament.dateExpiration = inventaireMedicament.dateExpiration ? dayjs(inventaireMedicament.dateExpiration) : undefined;
      });
    }
    return res;
  }
}
