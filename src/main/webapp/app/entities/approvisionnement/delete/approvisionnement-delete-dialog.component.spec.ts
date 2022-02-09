jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ApprovisionnementService } from '../service/approvisionnement.service';

import { ApprovisionnementDeleteDialogComponent } from './approvisionnement-delete-dialog.component';

describe('Approvisionnement Management Delete Component', () => {
  let comp: ApprovisionnementDeleteDialogComponent;
  let fixture: ComponentFixture<ApprovisionnementDeleteDialogComponent>;
  let service: ApprovisionnementService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ApprovisionnementDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(ApprovisionnementDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ApprovisionnementDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ApprovisionnementService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
