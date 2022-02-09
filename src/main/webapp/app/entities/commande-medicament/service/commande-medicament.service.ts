import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICommandeMedicament, getCommandeMedicamentIdentifier } from '../commande-medicament.model';

export type EntityResponseType = HttpResponse<ICommandeMedicament>;
export type EntityArrayResponseType = HttpResponse<ICommandeMedicament[]>;

@Injectable({ providedIn: 'root' })
export class CommandeMedicamentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/commande-medicaments');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(commandeMedicament: ICommandeMedicament): Observable<EntityResponseType> {
    return this.http.post<ICommandeMedicament>(this.resourceUrl, commandeMedicament, { observe: 'response' });
  }

  update(commandeMedicament: ICommandeMedicament): Observable<EntityResponseType> {
    return this.http.put<ICommandeMedicament>(
      `${this.resourceUrl}/${getCommandeMedicamentIdentifier(commandeMedicament) as number}`,
      commandeMedicament,
      { observe: 'response' }
    );
  }

  partialUpdate(commandeMedicament: ICommandeMedicament): Observable<EntityResponseType> {
    return this.http.patch<ICommandeMedicament>(
      `${this.resourceUrl}/${getCommandeMedicamentIdentifier(commandeMedicament) as number}`,
      commandeMedicament,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICommandeMedicament>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICommandeMedicament[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCommandeMedicamentToCollectionIfMissing(
    commandeMedicamentCollection: ICommandeMedicament[],
    ...commandeMedicamentsToCheck: (ICommandeMedicament | null | undefined)[]
  ): ICommandeMedicament[] {
    const commandeMedicaments: ICommandeMedicament[] = commandeMedicamentsToCheck.filter(isPresent);
    if (commandeMedicaments.length > 0) {
      const commandeMedicamentCollectionIdentifiers = commandeMedicamentCollection.map(
        commandeMedicamentItem => getCommandeMedicamentIdentifier(commandeMedicamentItem)!
      );
      const commandeMedicamentsToAdd = commandeMedicaments.filter(commandeMedicamentItem => {
        const commandeMedicamentIdentifier = getCommandeMedicamentIdentifier(commandeMedicamentItem);
        if (commandeMedicamentIdentifier == null || commandeMedicamentCollectionIdentifiers.includes(commandeMedicamentIdentifier)) {
          return false;
        }
        commandeMedicamentCollectionIdentifiers.push(commandeMedicamentIdentifier);
        return true;
      });
      return [...commandeMedicamentsToAdd, ...commandeMedicamentCollection];
    }
    return commandeMedicamentCollection;
  }
}
