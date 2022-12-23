import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'ecole',
        data: { pageTitle: 'firstAppJHipsterApp.ecole.home.title' },
        loadChildren: () => import('./ecole/ecole.module').then(m => m.EcoleModule),
      },
      {
        path: 'filiere',
        data: { pageTitle: 'firstAppJHipsterApp.filiere.home.title' },
        loadChildren: () => import('./filiere/filiere.module').then(m => m.FiliereModule),
      },
      {
        path: 'etudiant',
        data: { pageTitle: 'firstAppJHipsterApp.etudiant.home.title' },
        loadChildren: () => import('./etudiant/etudiant.module').then(m => m.EtudiantModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
