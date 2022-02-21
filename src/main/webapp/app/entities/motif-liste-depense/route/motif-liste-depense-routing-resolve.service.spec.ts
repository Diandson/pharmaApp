import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IMotifListeDepense, MotifListeDepense } from '../motif-liste-depense.model';
import { MotifListeDepenseService } from '../service/motif-liste-depense.service';

import { MotifListeDepenseRoutingResolveService } from './motif-liste-depense-routing-resolve.service';

describe('MotifListeDepense routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: MotifListeDepenseRoutingResolveService;
  let service: MotifListeDepenseService;
  let resultMotifListeDepense: IMotifListeDepense | undefined;

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
    routingResolveService = TestBed.inject(MotifListeDepenseRoutingResolveService);
    service = TestBed.inject(MotifListeDepenseService);
    resultMotifListeDepense = undefined;
  });

  describe('resolve', () => {
    it('should return IMotifListeDepense returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMotifListeDepense = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultMotifListeDepense).toEqual({ id: 123 });
    });

    it('should return new IMotifListeDepense if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMotifListeDepense = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultMotifListeDepense).toEqual(new MotifListeDepense());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as MotifListeDepense })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMotifListeDepense = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultMotifListeDepense).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
