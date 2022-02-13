import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStructure, getStructureIdentifier } from '../structure.model';

export type EntityResponseType = HttpResponse<IStructure>;
export type EntityArrayResponseType = HttpResponse<IStructure[]>;

@Injectable({ providedIn: 'root' })
export class StructureService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/structures');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(structure: IStructure): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(structure);
    return this.http
      .post<IStructure>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(structure: IStructure): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(structure);
    return this.http
      .put<IStructure>(`${this.resourceUrl}/${getStructureIdentifier(structure) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(structure: IStructure): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(structure);
    return this.http
      .patch<IStructure>(`${this.resourceUrl}/${getStructureIdentifier(structure) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IStructure>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }
  findOnly(): Observable<EntityResponseType> {
    return this.http
      .get<IStructure>(`${this.resourceUrl}/only`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }
  findOnlyAuth(): Observable<EntityResponseType> {
    return this.http
      .get<IStructure>(`${this.resourceUrl}/only/auth`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IStructure[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addStructureToCollectionIfMissing(
    structureCollection: IStructure[],
    ...structuresToCheck: (IStructure | null | undefined)[]
  ): IStructure[] {
    const structures: IStructure[] = structuresToCheck.filter(isPresent);
    if (structures.length > 0) {
      const structureCollectionIdentifiers = structureCollection.map(structureItem => getStructureIdentifier(structureItem)!);
      const structuresToAdd = structures.filter(structureItem => {
        const structureIdentifier = getStructureIdentifier(structureItem);
        if (structureIdentifier == null || structureCollectionIdentifiers.includes(structureIdentifier)) {
          return false;
        }
        structureCollectionIdentifiers.push(structureIdentifier);
        return true;
      });
      return [...structuresToAdd, ...structureCollection];
    }
    return structureCollection;
  }

  protected convertDateFromClient(structure: IStructure): IStructure {
    return Object.assign({}, structure, {
      dateConfig: structure.dateConfig?.isValid() ? structure.dateConfig.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateConfig = res.body.dateConfig ? dayjs(res.body.dateConfig) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((structure: IStructure) => {
        structure.dateConfig = structure.dateConfig ? dayjs(structure.dateConfig) : undefined;
      });
    }
    return res;
  }
}
