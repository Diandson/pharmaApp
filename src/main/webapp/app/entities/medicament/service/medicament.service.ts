import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMedicament, getMedicamentIdentifier } from '../medicament.model';

export type EntityResponseType = HttpResponse<IMedicament>;
export type EntityArrayResponseType = HttpResponse<IMedicament[]>;

@Injectable({ providedIn: 'root' })
export class MedicamentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/medicaments');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(medicament: IMedicament): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(medicament);
    return this.http
      .post<IMedicament>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(medicament: IMedicament): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(medicament);
    return this.http
      .put<IMedicament>(`${this.resourceUrl}/${getMedicamentIdentifier(medicament) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(medicament: IMedicament): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(medicament);
    return this.http
      .patch<IMedicament>(`${this.resourceUrl}/${getMedicamentIdentifier(medicament) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  upload(file: any): Observable<EntityResponseType> {
    const formdata: FormData = new FormData();
    formdata.append('file', file);
    return this.http.post<IMedicament>(this.resourceUrl + '/upload', formdata, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMedicament>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMedicament[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }
  queryGenerer(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMedicament[]>(this.resourceUrl + '/generer', { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMedicamentToCollectionIfMissing(
    medicamentCollection: IMedicament[],
    ...medicamentsToCheck: (IMedicament | null | undefined)[]
  ): IMedicament[] {
    const medicaments: IMedicament[] = medicamentsToCheck.filter(isPresent);
    if (medicaments.length > 0) {
      const medicamentCollectionIdentifiers = medicamentCollection.map(medicamentItem => getMedicamentIdentifier(medicamentItem)!);
      const medicamentsToAdd = medicaments.filter(medicamentItem => {
        const medicamentIdentifier = getMedicamentIdentifier(medicamentItem);
        if (medicamentIdentifier == null || medicamentCollectionIdentifiers.includes(medicamentIdentifier)) {
          return false;
        }
        medicamentCollectionIdentifiers.push(medicamentIdentifier);
        return true;
      });
      return [...medicamentsToAdd, ...medicamentCollection];
    }
    return medicamentCollection;
  }

  protected convertDateFromClient(medicament: IMedicament): IMedicament {
    return Object.assign({}, medicament, {
      dateFabrication: medicament.dateFabrication?.isValid() ? medicament.dateFabrication.format(DATE_FORMAT) : undefined,
      dateExpiration: medicament.dateExpiration?.isValid() ? medicament.dateExpiration.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((medicament: IMedicament) => {
        medicament.dateFabrication = medicament.dateFabrication ? dayjs(medicament.dateFabrication) : undefined;
        medicament.dateExpiration = medicament.dateExpiration ? dayjs(medicament.dateExpiration) : undefined;
      });
    }
    return res;
  }
}
