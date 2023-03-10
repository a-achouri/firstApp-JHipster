import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFiliere } from '../filiere.model';
import { FiliereService } from '../service/filiere.service';

@Injectable({ providedIn: 'root' })
export class FiliereRoutingResolveService implements Resolve<IFiliere | null> {
  constructor(protected service: FiliereService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFiliere | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((filiere: HttpResponse<IFiliere>) => {
          if (filiere.body) {
            return of(filiere.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
