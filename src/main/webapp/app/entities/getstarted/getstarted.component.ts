import { Component, OnInit } from '@angular/core';
import {StructureService} from "../structure/service/structure.service";
import {NzModalService} from "ng-zorro-antd/modal";
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {ProgressDialogComponent} from "../../shared/progress-dialog/progress-dialog.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'jhi-getstarted',
  templateUrl: './getstarted.component.html',
  styleUrls: ['./getstarted.component.scss']
})
export class GetstartedComponent implements OnInit {
  clickl?: string;

  constructor(
    private structueService: StructureService,
    private router: Router,
    private dialog: MatDialog,
    private modalService: NgbModal,
    private modal: NzModalService
  ) {
  }

  ngOnInit(): void {
    // is init
    this.clickl = 'true';
  }

  getStarted(): void{
    const modalRef = this.modalService.open(ProgressDialogComponent,
      { backdrop: 'static', centered: true, windowClass: 'myCustomModalClass' });
    this.structueService.findOnly().subscribe(res => {
      if (res.body){
        this.warning('Veuillez rafraichir la page svp!');
        modalRef.close();
      }else {
        this.router.navigate(['structure/new']);
        modalRef.close();
      }
    }, () => {
      this.router.navigate(['structure/new']);
      modalRef.close();
    })
  }

  success(msg: string): void{
    this.modal.success({
      nzContent: msg,
      nzTitle: 'SUCCESS',
      nzOkText: 'OK'
    })
  }
  warning(msg: string): void{
    this.modal.warning({
      nzContent: msg,
      nzTitle: 'ATTENTION',
      nzOkText: 'OK'
    })
  }
  error(msg: string): void{
    this.modal.error({
      nzContent: msg,
      nzTitle: 'ERROR',
      nzOkText: 'OK'
    })
  }

}
