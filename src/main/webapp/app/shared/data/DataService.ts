import {Injectable} from "@angular/core";
import {IStructure, Structure} from "../../entities/structure/structure.model";
import {BehaviorSubject} from "rxjs";


@Injectable({ providedIn: 'root' })
export class DataService {
  structure: IStructure = new Structure();

  myStructure = new BehaviorSubject(this.structure);
  currentStructure = this.myStructure.asObservable();


  actializeStructure(structur: IStructure): any {
    this.myStructure.next(structur);
  }
}
