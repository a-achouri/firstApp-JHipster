import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { FiliereService } from '../service/filiere.service';

import { FiliereComponent } from './filiere.component';

describe('Filiere Management Component', () => {
  let comp: FiliereComponent;
  let fixture: ComponentFixture<FiliereComponent>;
  let service: FiliereService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'filiere', component: FiliereComponent }]), HttpClientTestingModule],
      declarations: [FiliereComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(FiliereComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FiliereComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(FiliereService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.filieres?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to filiereService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getFiliereIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getFiliereIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
