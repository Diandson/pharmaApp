import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IApprovisionnement, Approvisionnement } from '../approvisionnement.model';
import { ApprovisionnementService } from '../service/approvisionnement.service';

import { ApprovisionnementRoutingResolveService } from './approvisionnement-routing-resolve.service';

describe('Approvisionnement routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ApprovisionnementRoutingResolveService;
  let service: ApprovisionnementService;
  let resultApprovisionnement: IApprovisionnement | undefined;

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
    routingResolveService = TestBed.inject(ApprovisionnementRoutingResolveService);
    service = TestBed.inject(ApprovisionnementService);
    resultApprovisionnement = undefined;
  });

  describe('resolve', () => {
    it('should return IApprovisionnement returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultApprovisionnement = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultApprovisionnement).toEqual({ id: 123 });
    });

    it('should return new IApprovisionnement if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultApprovisionnement = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultApprovisionnement).toEqual(new Approvisionnement());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Approvisionnement })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultApprovisionnement = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultApprovisionnement).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
