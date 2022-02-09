import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IVenteMedicament, VenteMedicament } from '../vente-medicament.model';
import { VenteMedicamentService } from '../service/vente-medicament.service';

import { VenteMedicamentRoutingResolveService } from './vente-medicament-routing-resolve.service';

describe('VenteMedicament routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: VenteMedicamentRoutingResolveService;
  let service: VenteMedicamentService;
  let resultVenteMedicament: IVenteMedicament | undefined;

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
    routingResolveService = TestBed.inject(VenteMedicamentRoutingResolveService);
    service = TestBed.inject(VenteMedicamentService);
    resultVenteMedicament = undefined;
  });

  describe('resolve', () => {
    it('should return IVenteMedicament returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVenteMedicament = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultVenteMedicament).toEqual({ id: 123 });
    });

    it('should return new IVenteMedicament if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVenteMedicament = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultVenteMedicament).toEqual(new VenteMedicament());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as VenteMedicament })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVenteMedicament = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultVenteMedicament).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
