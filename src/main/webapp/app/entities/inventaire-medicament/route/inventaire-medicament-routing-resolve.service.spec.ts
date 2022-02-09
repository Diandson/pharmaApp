import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IInventaireMedicament, InventaireMedicament } from '../inventaire-medicament.model';
import { InventaireMedicamentService } from '../service/inventaire-medicament.service';

import { InventaireMedicamentRoutingResolveService } from './inventaire-medicament-routing-resolve.service';

describe('InventaireMedicament routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: InventaireMedicamentRoutingResolveService;
  let service: InventaireMedicamentService;
  let resultInventaireMedicament: IInventaireMedicament | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(InventaireMedicamentRoutingResolveService);
    service = TestBed.inject(InventaireMedicamentService);
    resultInventaireMedicament = undefined;
  });

  describe('resolve', () => {
    it('should return IInventaireMedicament returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInventaireMedicament = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInventaireMedicament).toEqual({ id: 123 });
    });

    it('should return new IInventaireMedicament if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInventaireMedicament = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultInventaireMedicament).toEqual(new InventaireMedicament());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as InventaireMedicament })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInventaireMedicament = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInventaireMedicament).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
