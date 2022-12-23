import { IFiliere } from 'app/entities/filiere/filiere.model';

export interface IEtudiant {
  id: number;
  codeEtudiant?: number | null;
  nom?: string | null;
  prenom?: string | null;
  age?: number | null;
  email?: string | null;
  filiere?: Pick<IFiliere, 'id'> | null;
}

export type NewEtudiant = Omit<IEtudiant, 'id'> & { id: null };
