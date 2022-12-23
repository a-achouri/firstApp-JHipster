import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { FiliereFormService, FiliereFormGroup } from './filiere-form.service';
import { IFiliere } from '../filiere.model';
import { FiliereService } from '../service/filiere.service';
import { IEcole } from 'app/entities/ecole/ecole.model';
import { EcoleService } from 'app/entities/ecole/service/ecole.service';

@Component({
  selector: 'jhi-filiere-update',
  templateUrl: './filiere-update.component.html',
})
export class FiliereUpdateComponent implements OnInit {
  isSaving = false;
  filiere: IFiliere | null = null;

  ecolesSharedCollection: IEcole[] = [];

  editForm: FiliereFormGroup = this.filiereFormService.createFiliereFormGroup();

  constructor(
    protected filiereService: FiliereService,
    protected filiereFormService: FiliereFormService,
    protected ecoleService: EcoleService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEcole = (o1: IEcole | null, o2: IEcole | null): boolean => this.ecoleService.compareEcole(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ filiere }) => {
      this.filiere = filiere;
      if (filiere) {
        this.updateForm(filiere);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const filiere = this.filiereFormService.getFiliere(this.editForm);
    if (filiere.id !== null) {
      this.subscribeToSaveResponse(this.filiereService.update(filiere));
    } else {
      this.subscribeToSaveResponse(this.filiereService.create(filiere));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFiliere>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(filiere: IFiliere): void {
    this.filiere = filiere;
    this.filiereFormService.resetForm(this.editForm, filiere);

    this.ecolesSharedCollection = this.ecoleService.addEcoleToCollectionIfMissing<IEcole>(this.ecolesSharedCollection, filiere.ecole);
  }

  protected loadRelationshipsOptions(): void {
    this.ecoleService
      .query()
      .pipe(map((res: HttpResponse<IEcole[]>) => res.body ?? []))
      .pipe(map((ecoles: IEcole[]) => this.ecoleService.addEcoleToCollectionIfMissing<IEcole>(ecoles, this.filiere?.ecole)))
      .subscribe((ecoles: IEcole[]) => (this.ecolesSharedCollection = ecoles));
  }
}
